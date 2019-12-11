/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.schorn.ella.impl.repo;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveOperator;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.sql.ActiveSQL;

/**
 *
 * @author schorn
 *
 */
class ActiveConditionImpl implements ActiveCondition {

    ObjectType parentType;
    ValueType valueType;
    Predicate<ValueData> predicate;
    String description;
    ActiveOperator operator = null;

    public ActiveConditionImpl(ObjectType parentType, ValueType valueType) {
        this.parentType = parentType;
        this.valueType = valueType;
    }

    @Override
    public String descriptive() {
        return this.description;
    }

    @Override
    public ObjectType objectType() {
        return this.parentType;
    }

    @Override
    public ValueType valueType() {
        return this.valueType;
    }

    @Override
    public ActiveOperator operator() {
        return this.operator;
    }

    @Override
    public void setCondition(ActiveOperator operator, Object[] values) {
        this.operator = operator.translate(ActiveSQL.SQLOperators.values());
        switch (this.operator.dimension()) {
            case UNIVAL:
                Object value = values[0];
                this.description = String.format("%s.%s.%s %s '%s'",
                        parentType.context().name(),
                        parentType.name(),
                        valueType.name(),
                        this.operator.opstr(),
                        value.toString());
                break;
            case BIVAL:
                Object loValue = values[0];
                Object hiValue = values[1];
                this.description = String.format("%s.%s.%s %s '%s' %s '%s'",
                        parentType.context().name(),
                        parentType.name(),
                        valueType.name(),
                        this.operator.opstr(),
                        loValue.toString(),
                        this.operator.valsep(),
                        hiValue.toString());
                break;
            case MULTIVAL:
                List<Object> valueList = Arrays.asList(values);
                StringJoiner joiner = new StringJoiner(String.format("'%s'", this.operator.valsep(), "('", "')"));
                valueList.stream().forEach(v -> joiner.add(v.toString()));
                this.description = String.format("%s.%s.%s %s %s",
                        parentType.context().name(),
                        parentType.name(),
                        valueType.name(),
                        this.operator.opstr(),
                        joiner.toString());
                break;
        }

        switch (this.operator.mnemonic()) {
            case EQUALS:
                this.predicate = n -> n.compareValue(values[0]) == 0;
                break;
            case NOT_EQUALS:
                this.predicate = n -> n.compareValue(values[0]) != 0;
                break;
            case LESS_THAN:
                this.predicate = n -> n.compareValue(values[0]) == -1;
                break;
            case GREATER_THAN:
                this.predicate = n -> n.compareValue(values[0]) == 1;
                break;
            case LESS_THAN_OR_EQUALS:
                this.predicate = n -> n.compareValue(values[0]) <= 0;
                break;
            case GREATER_THAN_OR_EQUALS:
                this.predicate = n -> n.compareValue(values[0]) >= 0;
                break;
            case BETWEEN_OR_EQUALS:
                this.predicate = n -> n.compareValue(values[0]) >= 0 && n.compareValue(values[1]) <= 0;
                break;
            case EQUALS_IN:
                this.predicate = n -> (Arrays.asList(values).stream().filter(v -> n.match(v))).findAny().isPresent();
                break;
            case NOT_EQUALS_IN:
                this.predicate = n -> (Arrays.asList(values).stream().filter(v -> n.match(v))).findAny().isPresent();
                this.predicate = this.predicate.negate();
                break;
            default:
                break;
        }

    }

    /**
     * Method is for ObjectData looking for a particular member ValueType's
     * ValueData and running the condition(s) in this.predicate.test(ValueData)
     */
    @Override
    public boolean test(ActiveData activeData) {
        if (activeData.role() == Role.Object) {
            ObjectData objectData = (ObjectData) activeData;
            if (objectData.objectType().isMember(this.valueType)) {
                ValueData valueData = (ValueData) objectData.get(this.valueType);
                if (valueData != null) {
                    return this.predicate.test(valueData);
                }
            } else {
                List<ValueData> valueDatas = objectData.find(this.valueType);
                return (valueDatas.stream().filter(this.predicate).count() > 0);
            }
        }
        return false;
    }
}
