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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveOperator;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.repo.RepoSupport.ConditionStatementParser;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
class ConditionStatementParserImpl extends AbstractContextual implements ConditionStatementParser {

    /**
     * There are variations of these (URLQueryString, SQL, ...)
     */
    private final ActiveOperator[] activeOperators;

    protected ConditionStatementParserImpl(AppContext context, ActiveOperator[] activeOperators) {
        super(context);
        this.activeOperators = activeOperators;
    }

    /**
     * The intent of this method is to convert a set of strings each with a
     * format of "<left-side> <operator> <right-side>" - left-side should be a
     * ValueTypeMember (which is ObjectType.ValueType) - operator should
     * translate via operator.opstr() into one of the ActiveNode.Operators types
     * - right-side should be one or more values depdening on the dimension of
     * the operator
     *
     * dimension is an enum: ActiveNode.ActiveOperator.Dimension - UNIVAL the
     * right-side is a single value - BIVAL the right-side is two values
     * delimited by operator.valsep() - MULTIVAL the right-side is a list
     * delimited by operator.valsep()
     *
     */
    @Override
    public ActiveCondition apply(String statement) {
        ActiveCondition condition = null;
        try {
            for (ActiveOperator operator : this.activeOperators) {
                String[] left_right = Functions.split_trim(operator.opstr(), statement);
                // looking for the correct expression - if it split into more than one, it's the expression
                if (left_right.length > 1) {
                    List<Object> values = new ArrayList<>();
                    switch (operator.dimension()) {
                        case UNIVAL:
                            values.add(left_right[1]);
                            break;
                        case BIVAL:
                        case MULTIVAL:
                            String[] valends = operator.valends();
                            String valstr = left_right[1];
                            valstr = valstr.replace(valends[0], "");
                            valstr = valstr.replace(valends[1], "");
                            String[] vals = Functions.split_trim(operator.valsep(), valstr);
                            values.addAll(Arrays.asList(vals));
                            break;
                    }
                    for (int i = 0; i < values.size(); i += 1) {
                        String val = (String) values.get(i);
                        char[] chars = val.toCharArray();
                        if ((chars[0] == '\'' && chars[chars.length - 1] == '\'')
                                || (chars[0] == '\"' && chars[chars.length - 1] == '\"')) {
                            values.set(i, val.substring(1, val.length() - 1));
                        }
                    }
                    ValueTypeMember valueTypeMember = ValueTypeMember.parse(this.context(), left_right[0]);
                    condition = ActiveCondition.create(operator, valueTypeMember, values);
                    // we have accepted that this was the correct expression - no need to continue looking
                    break;
                }
            }
        } catch (Exception ex) {
            this.setException(ex);
        }
        return condition;
    }

}
