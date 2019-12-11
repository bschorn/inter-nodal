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
package org.schorn.ella.node;

import java.util.Optional;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 *
 * @author schorn
 *
 */
public class MetaSupport {

    /**
     *
     * @param objectData
     * @return
     * @throws Exception
     */
    static public Role getRoleFromMemberTypes(ObjectData objectData) throws Exception {
        if (objectData != null && objectData.name().equals(MetaTypes.ObjectTypes.member_types.objectType().name())) {
            ValueData valueData = objectData.get(MetaTypes.ValueTypes.value_type.valueType());
            if (valueData != null && !valueData.isNull()) {
                return Role.Value;
            }
            valueData = objectData.get(MetaTypes.ValueTypes.object_type.valueType());
            if (valueData != null && !valueData.isNull()) {
                return Role.Object;
            }
            valueData = objectData.get(MetaTypes.ValueTypes.array_type.valueType());
            if (valueData != null && !valueData.isNull()) {
                return Role.Array;
            }
            return Role.Unknown;
        }
        throw new Exception(String.format("%s.getRoleFromMemberTypes() - method passed null or invalid parameter.",
                MetaSupport.class.getSimpleName()));
    }

    /**
     *
     * @param objectData
     * @return
     */
    static public String getMemberTypeName(ObjectData objectData) throws Exception {
        if (objectData != null && objectData.name().equals(MetaTypes.ObjectTypes.member_types.objectType().name())) {
            ValueData valueData = null;
            switch (getRoleFromMemberTypes(objectData)) {
                case Value:
                    valueData = (ValueData) objectData.get(MetaTypes.ValueTypes.value_type.valueType());
                    break;
                case Object:
                    valueData = (ValueData) objectData.get(MetaTypes.ValueTypes.object_type.valueType());
                    break;
                case Array:
                    valueData = (ValueData) objectData.get(MetaTypes.ValueTypes.array_type.valueType());
                    break;
                default:
                    break;
            }
            if (valueData != null && valueData.activeValue() != null) {
                return valueData.activeValue().toString();
            }
        }
        return null;
    }

    /**
     *
     * @param objectData
     * @return
     */
    static public BondType getMemberTypeBondType(ObjectData objectData) {
        BondType bondType = BondType.OPTIONAL;
        if (objectData != null && objectData.name().equals(MetaTypes.ObjectTypes.member_types.objectType().name())) {
            ValueData valueData = (ValueData) objectData.get(MetaTypes.ValueTypes.bond_type.valueType());
            if (valueData != null && valueData.activeValue() != null) {
                bondType = BondType.fromString(valueData.activeValue().toString());
            }
        }
        return bondType;
    }

    /**
     *
     * @param objectData
     * @param defaultContext
     * @return
     */
    static public AppContext getMemberContext(ObjectData objectData, AppContext defaultContext) {
        ValueData valueData = (ValueData) objectData.get(MetaTypes.ValueTypes.meta_owner.valueType());
        if (valueData != null && valueData.activeValue() != null) {
            Optional<AppContext> optContext = AppContext.valueOf(valueData.activeValue().toString());
            if (optContext.isPresent()) {
                return optContext.get();
            }
        }
        return defaultContext;
    }

    /**
     * The type we are looking for may or may not already been created. This
     * method will create any type that doesn't already exist, granted it does
     * exist in the meta file.
     *
     * This removes the need to declare meta-data in the order of dependency.
     *
     *
     * @param typeName
     * @param typeRole
     * @param typeContext
     * @return
     * @throws Exception
     */
    static public ActiveType findType(String typeName, Role typeRole, AppContext typeContext) throws Exception {
        switch (typeRole) {
            case Value: {
                ValueType valueType = ValueType.get(typeContext, typeName);
                if (valueType != null) {
                    return valueType;
                }
                MetaReader reader = MetaReader.get(typeContext);
                if (reader != null) {
                    valueType = reader.findOrCreateType(ValueType.class, typeName);
                    if (valueType != null) {
                        return valueType;
                    }
                }
            }
            break;
            case Object: {
                ObjectType objectType = ObjectType.get(typeContext, typeName);
                if (objectType != null) {
                    return objectType;
                }
                MetaReader reader = MetaReader.get(typeContext);
                if (reader != null) {
                    objectType = reader.findOrCreateType(ObjectType.class, typeName);
                    if (objectType != null) {
                        return objectType;
                    }
                }
            }
            break;
            case Array: {
                try {
                    findType(typeName, Role.Object, typeContext);
                } catch (Exception e0) {
                    try {
                        findType(typeName, Role.Value, typeContext);
                    } catch (Exception ex) {
                        throw new Exception(String.format("%s.findType() - Unable to find ValueType/ObjectType: %s in Context: %s for ArrayType: %s",
                                MetaSupport.class.getSimpleName(),
                                typeName,
                                typeContext.name(),
                                typeName));
                    }
                }
                ArrayType arrayType = ArrayType.get(typeContext, typeName);
                if (arrayType != null) {
                    return arrayType;
                }
                MetaReader reader = MetaReader.get(typeContext);
                if (reader != null) {
                    arrayType = reader.findOrCreateType(ArrayType.class, typeName);
                    if (arrayType != null) {
                        return arrayType;
                    }
                }
            }
            break;
            default:
                break;

        }
        throw new Exception(String.format("%s.findType() - Unable to find %sType: %s in Context: %s.",
                MetaSupport.class.getSimpleName(),
                typeRole.name(),
                typeName,
                typeContext.name()));
    }
}
