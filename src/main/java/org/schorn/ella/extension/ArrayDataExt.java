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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface ArrayDataExt {

    /**
     * Set<ValueType> ArrayData.memberValueTypesWithData()
     *
     * Scans through an array of objects looking for the value fields with data
     * (ValueData.isNull() == false) and returns the unique set of value types
     * that have data in at least one of the objects contained in the array.
     *
     * ArrayData contains one or more ObjectData (or ValueData) but they will be
     * all of the same ObjectType (or ValueType)
     *
     * @return
     */
    default Set<ValueType> memberValueTypesWithData() {
        Set<ValueType> valueTypeSet = new HashSet<>();
        if (this instanceof ArrayData) {
            ArrayData arrayData = (ArrayData) this;
            ActiveType activeType = arrayData.memberType();
            if (activeType.role().equals(Role.Object)) {
                ObjectType objectType = (ObjectType) activeType;
                List<ValueType> valueTypes = objectType.valueTypes();
                //valueTypeSet.addAll(valueTypes);
                List<ActiveData> rows = arrayData.nodes();

                for (ActiveData rowData : rows) {
                    ObjectData rowObj = (ObjectData) rowData;
                    rowObj.nodes().stream()
                            .filter(ad -> ad.role().equals(Role.Value))
                            .map(ad -> ValueData.class.cast(ad))
                            .filter(vd -> !vd.isNull())
                            .map(vd -> vd.valueType())
                            .forEach(vt -> valueTypeSet.add(vt));
                    /*
					 * Check if we can quit early...
                     */
                    if (valueTypeSet.size() == valueTypes.size()) {
                        return valueTypeSet;
                    }
                }
                /*
				 * Return the unique set of ValueTypes that have non-null values
                 */
                return valueTypeSet;
            } else if (activeType.role().equals(Role.Value)) {
                if (arrayData.nodes().stream()
                        .filter(ad -> (ad != null && !ad.isNull()))
                        .findAny()
                        .isPresent()) {

                    valueTypeSet.add((ValueType) arrayData.memberType());
                    return valueTypeSet;
                }
            }
        }
        return valueTypeSet;
    }

    static public Predicate<ActiveData> onlyGoodData() {
        return activeData
                -> activeData != null
                && !activeData.isNull()
                && !activeData.isBlank();
    }

    default List<ActiveData> memberData() {
        if (this instanceof ArrayData) {
            ArrayData arrayData = (ArrayData) this;
            return arrayData.activeValues(ActiveData.class, onlyGoodData());
        }
        return new ArrayList<>(0);
    }

}
