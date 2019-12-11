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
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface ObjectDataExt {

    /**
     *
     * @param activeType
     * @return
     */
    default ActiveData get(ActiveType activeType) {
        if (this instanceof ObjectData && activeType != null) {
            ObjectData objectData = (ObjectData) this;
            Optional<ActiveData> optData = objectData.nodes().stream()
                    .filter(ad -> ad != null)
                    .filter(ad -> activeType.equals(ad.activeType()))
                    .findFirst();
            if (optData.isPresent()) {
                return optData.get();
            }
        }
        return null;
    }

    default ValueData get(ValueType valueType) {
        if (this instanceof ObjectData && valueType != null) {
            ObjectData objectData = (ObjectData) this;
            Optional<ActiveData> optData = objectData.nodes().stream()
                    .filter(ad -> valueType.equals(ad.activeType()))
                    .findFirst();
            if (optData.isPresent()) {
                return (ValueData) optData.get();
            }
        }
        return null;
    }

    default ArrayData get(ArrayType arrayType) {
        if (this instanceof ObjectData && arrayType != null) {
            ObjectData objectData = (ObjectData) this;
            Optional<ActiveData> optData = objectData.nodes().stream()
                    .filter(ad -> arrayType.equals(ad.activeType()))
                    .findFirst();
            if (optData.isPresent()) {
                return (ArrayData) optData.get();
            }
        }
        return null;
    }

    default ValueData getValueData(String value_type) {
        if (this instanceof ObjectData && value_type != null) {
            ObjectData objectData = (ObjectData) this;
            ValueType valueType = ValueType.get(objectData.context(), value_type);
            if (valueType != null) {
                return (ValueData) objectData.get(valueType);
            }
        }
        return null;
    }

    default Object getValue(String value_type) {
        if (this instanceof ObjectData && value_type != null) {
            ObjectData objectData = (ObjectData) this;
            ValueType valueType = ValueType.get(objectData.context(), value_type);
            if (valueType != null) {
                return ((ValueData) objectData.get(valueType)).activeValue();
            }
        }
        return null;
    }

    default String getValueString(String value_type) {
        if (this instanceof ObjectData && value_type != null) {
            ObjectData objectData = (ObjectData) this;
            ValueType valueType = ValueType.get(objectData.context(), value_type);
            if (valueType != null) {
                return ((ValueData) objectData.get(valueType)).activeValue().toString();
            }
        }
        return null;
    }

    default List<ValueData> getKeyValues() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            ObjectSchema schema = objectData.objectType().schema();
            List<MemberDef> keys = schema.keys();
            return keys.stream()
                    .map(md -> objectData.get(md.activeType()))
                    .map(ad -> ValueData.class.cast(ad))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    default List<ValueData> getNaturalKeyValues() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            ObjectSchema schema = objectData.objectType().schema();
            List<MemberDef> keys = schema.naturalKeys();
            return keys.stream()
                    .map(md -> objectData.get(md.activeType()))
                    .map(ad -> ValueData.class.cast(ad))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    default List<ValueData> getParentKeyValues() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            List<ValueType> valueTypeKeys = objectData.objectType().parentValueTypeKeys();
            if (valueTypeKeys != null) {
                return valueTypeKeys.stream()
                        .map(vt -> objectData.get(vt))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>(0);
    }

    default Integer getSeriesKey() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            return ObjectData.KeyGenerator.get(objectData.context()).generateKey(objectData);
        }
        return null;
    }

    /**
     * ObjectData.getParentSeriesKey()
     * <br>
     * Get Series Key for parent by using this objects values for the foreign
     * key fields.
     * <ol>
     * <li>get parent type of this object</li>
     * <li>get key types for parent type</li>
     * <li>get values for keys from this object</li>
     * <li>use key generator to get unique id for key</li>
     * <li>return unique key id</li>
     * </ol>
     *
     * @return
     */
    default Integer getParentSeriesKey() throws Exception {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            ObjectType parentType = objectData.objectType().parentType();
            if (parentType != null && objectData.objectType().equals(parentType)) {
                return null;
            }
            if (parentType == null) {
                throw new Exception(String.format("%s.parentType() - returned null for %s",
                        ObjectTypeExt.class.getSimpleName(),
                        objectData.name()
                ));
            }
            List<ValueType> valueTypes = objectData.objectType().parentValueTypeKeys();
            if (valueTypes != null) {
                List<ValueData> keyValues = objectData.getParentKeyValues();
                if (keyValues != null && keyValues.size() == valueTypes.size()) {
                    return ObjectData.KeyGenerator.get(objectData.context()).generateKey(parentType, objectData, keyValues.toArray(new ValueData[0]));
                } else {
                    if (keyValues == null) {
                        throw new Exception(String.format("%s.getParentKeyValues() - returned null for %s",
                                ObjectDataExt.class.getSimpleName(),
                                objectData.name()
                        ));
                    }
                }
            } else {
                throw new Exception(String.format("%s.parentValueTypeKeys() - returned null for %s",
                        ObjectDataExt.class.getSimpleName(),
                        objectData.name()
                ));
            }
        }
        return null;
    }

    /**
     *
     * @return static public Predicate<ActiveData> onlyGoodData() { return
     * activeData -> activeData != null && !activeData.isNull() &&
     * !activeData.isBlank(); }
     */
    /**
     * Returns all the member nodes that have contents.
     *
     * @return
     */
    default List<ActiveData> memberData(Predicate<ActiveData> filter) {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            return objectData.activeValues(ActiveData.class, filter);
        }
        return new ArrayList<>(0);
    }

    /**
     * Providing a list of ActiveTypes the method will stream the member data
     * and return the member's that match the list.
     *
     * @param selectedTypes
     * @return
     */
    default Set<ActiveData> take(Set<ActiveType> selectedTypes) {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            return objectData.nodes().stream()
                    .filter(ad -> selectedTypes.contains(ad.activeType()))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>(0);
    }

    /**
     * This will be more performant than the take(Set<ActiveType>) Also will
     * return a list in the order selected.
     *
     * @param selectedIndexes
     * @return
     */
    default List<ActiveData> take(List<Integer> selectedIndexes) {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            selectedIndexes.stream()
                    .map(i -> objectData.get(i))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * Getting the Value Data members from Object at specific positions.
     *
     * @param valueIndexes
     * @return
     */
    default ValueData[] getValueDataForIndexes(int[] valueIndexes) {
        ValueData[] values = new ValueData[valueIndexes.length];
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            for (int i = 0; i < valueIndexes.length; i += 1) {
                values[i] = (ValueData) objectData.get(valueIndexes[i]);
            }
        }
        return values;
    }

    /**
     * Returns the version number for this ObjectData instance.
     *
     * @return
     */
    default Integer getVersion() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            ValueData valueData = objectData.get(MetaTypes.AutoTypes.over.valueType());
            if (valueData != null) {
                return (Integer) valueData.activeValue();
            }
        }
        return null;
    }

    /**
     * Returns the series' unique key.
     *
     *
     * @return
     */
    default String getKey() {
        if (this instanceof ObjectData) {
            ObjectData objectData = (ObjectData) this;
            ValueData valueData = objectData.get(MetaTypes.AutoTypes.okey.valueType());
            if (valueData != null) {
                return (String) valueData.activeValue();
            }
        }
        return null;
    }

}
