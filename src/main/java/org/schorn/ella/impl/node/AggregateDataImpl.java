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
package org.schorn.ella.impl.node;

import java.util.List;
import java.util.function.Predicate;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.AggregateData;
import org.schorn.ella.node.ActiveNode.ObjectType;

/**
 *
 * @author schorn
 *
 */
class AggregateDataImpl extends AbstractContextual implements AggregateData {

    private final ObjectType objectType;
    private final ActiveData[] data;

    AggregateDataImpl(ObjectType objectType) {
        super(objectType.context());
        this.objectType = objectType;
        this.data = new ActiveData[this.objectType.activeTypes().size()];
    }

    @Override
    public AggregateData createObject(ActiveType activeType) {
        MemberDef memberDef = this.objectType.schema().get(activeType);
        if (memberDef == null) {
            return null;
        }
        switch (memberDef.activeType().role()) {
            case Array:
                ArrayType arrayType = (ArrayType) memberDef.activeType();
                if (arrayType.memberType().role().equals(Role.Object)) {
                    if (this.data[memberDef.index()] == null) {
                        this.data[memberDef.index()] = arrayType.create();
                    }
                    ObjectType objectType = (ObjectType) arrayType.memberType();
                    AggregateData objectData = new AggregateDataImpl(objectType);
                    for (ValueType valueType : objectType.valueTypes()) {
                        ValueData valueData = this.get(valueType);
                        if (valueData != null) {
                            objectData.set(valueData);
                        }
                    }
                }
                break;
            case Object:
                break;
        }
        return null;
    }

    public ValueData set(ValueData valueData) {
        MemberDef memberDef = this.objectType.schema().get(valueData.activeType());
        ValueData oldValueData = null;
        if (memberDef != null) {
            oldValueData = (ValueData) this.data[memberDef.index()];
            this.data[memberDef.index()] = valueData;
        }
        return oldValueData;
    }

    @Override
    public List<ActiveData> nodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ActiveData get(int index) {
        if (index >= 0 && index < this.data.length) {
            return this.data[index];
        }
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ActiveType activeType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object activeValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> activeValues() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AppContext context() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int bytes() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int compareTo(ActiveData o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Integer activeId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBlank() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isIncomplete() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Builder replicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean appendChild(ObjectData childData) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addSibling(ObjectData siblingData) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addChild(ObjectData childData) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<ActiveType> missingTypes() {
        // TODO Auto-generated method stub
        return null;
    }

}
