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
package org.schorn.ella.extension;

import java.util.List;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface ActiveQuick {

    /**
     * Quickly creates a ValueData for this context using name + value. The
     * value is assessed to determine the underlying FieldType.
     *
     * @param name
     * @param value
     * @return
     */
    default ValueData createValue(String name, Object value) {
        if (this instanceof AppContext) {
            try {
                return ValueType.dynamic((AppContext) this, name, value).create(value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Quickly creates a ObjectData for this context use name + activeData(s).
     *
     *
     * @param name
     * @param activeDatas
     * @return
     */
    default ObjectData createObject(String name, ActiveData... activeDatas) {
        if (this instanceof AppContext) {
            try {
                return ObjectType.dynamic((AppContext) this, name).create(activeDatas);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;

    }

    /**
     * Quickly creates an ArrayData for this context using name + items.
     *
     * The first entry in the items is assessed to determine the contents of the
     * items list.
     *
     * If its an ActiveData then it further discovers which one
     * (Value,Object,Array). -or- If its a plain data value (String,LocalDate)
     *
     * @param name
     * @param items
     * @return
     */
    default ArrayData createArray(String name, List<Object> items) {
        if (this instanceof AppContext) {
            try {
                if (items.isEmpty()) {
                    return ArrayData.empty(name);
                }
                AppContext context = (AppContext) this;
                Object sample = items.get(0);
                if (sample instanceof ActiveData) {
                    ActiveData activeData = (ActiveData) sample;
                    switch (activeData.role()) {
                        case Value: {
                            ValueData valueData = (ValueData) activeData;
                            ValueType valueType = valueData.valueType();
                            final ArrayData resultData = ArrayType.dynamic(context, valueType.name()).create();
                            items.stream()
                                    .filter(i -> (i instanceof ValueData && ((ValueData) i).valueType().equals(valueType)))
                                    .map(i -> (ValueData) i)
                                    .forEach(vd -> resultData.add(vd));
                            return resultData;
                        }
                        case Object: {
                            ObjectData objectData = (ObjectData) activeData;
                            ObjectType objectType = objectData.objectType();
                            final ArrayData resultData = ArrayType.dynamic(context, objectType.name()).create();
                            items.stream()
                                    .filter(i -> (i instanceof ObjectData && ((ObjectData) i).objectType().equals(objectType)))
                                    .map(i -> (ObjectData) i)
                                    .forEach(od -> resultData.add(od));
                            return resultData;
                        }
                        case Array: {
                            ArrayData arrayData = (ArrayData) activeData;
                            ArrayType arrayType = arrayData.arrayType();
                            final ArrayData resultData = ArrayType.dynamic(context, arrayType.name()).create();
                            items.stream()
                                    .filter(i -> (i instanceof ArrayData && ((ArrayData) i).arrayType().equals(arrayType)))
                                    .map(i -> (ArrayData) i)
                                    .forEach(ad -> resultData.add(ad));
                            return resultData;
                        }
                        default:
                            break;
                    }
                } else {
                    ValueType valueType = ValueType.dynamic(context, name, sample);
                    final ArrayData resultData = ArrayType.dynamic(context, valueType.name()).create();
                    items.stream()
                            .filter(i -> i.getClass().equals(sample.getClass()))
                            .map(i -> valueType.create(i))
                            .forEach(vd -> resultData.add(vd));
                    return resultData;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
