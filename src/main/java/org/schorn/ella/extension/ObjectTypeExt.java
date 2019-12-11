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

import org.schorn.ella.node.BondType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ActiveRef.References;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;

/**
 * ObjectType Extensions
 *
 * This are helper methods and can be implemented purely from the API (no inner
 * workings knowledge needed).
 *
 * @author schorn
 *
 */
public interface ObjectTypeExt {

    default ValueType specialKey() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            MemberDef memberDef = objectType.schema().specialKey();
            if (memberDef != null && memberDef.activeType().role().equals(Role.Value)) {
                return (ValueType) memberDef.activeType();
            }
        }
        return null;
    }

    default References references() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.context().getActiveRef().get(objectType);
        }
        return null;
    }

    default BondType getBondType(ActiveType activeType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            Optional<MemberDef> optMemberDef = objectType.schema().memberDefs().stream()
                    .filter(md -> md.activeType().equals(activeType))
                    .findAny();
            if (optMemberDef.isPresent()) {
                return optMemberDef.get().bondType();
            }
        }
        return BondType.NOBOND;
    }

    /**
     * Gets the ArrayType for this ObjectType. If one can not be found, then a
     * transient one is created and returned.
     *
     * @return
     * @throws Exception
     */
    default ArrayType arrayType() throws Exception {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            ArrayType arrayType = ArrayType.get(objectType.context(), objectType.name());
            if (arrayType != null) {
                if (!arrayType.memberType().equals(objectType)) {
                    arrayType = null;
                }
            }
            if (arrayType == null) {
                arrayType = NodeProvider.provider().createTransientArrayType(objectType.context(), objectType.name(), objectType, BondType.OPTIONAL);
            }
            return arrayType;
        }
        return null;
    }

    /**
     * Members of this ObjectType (shortcut directly from ObjectType)
     * @return 
     */
    default <T> List<T> memberTypes(Class<T> classOfT) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return (List<T>) objectType.schema().memberDefs().stream()
                    .filter(md -> classOfT.isAssignableFrom(md.activeType().getClass()))
                    .map(md -> md.activeType())
                    .map(mt -> classOfT.cast(mt))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     *
     * @return
     */
    default List<ValueType> valueTypes() {
        if (this instanceof ObjectType) {
            return this.memberTypes(ValueType.class);
        }
        return new ArrayList<>(0);
    }

    /**
     *
     * @return
     */
    default List<ValueTypeMember> valueTypeMembers() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            List<ValueTypeMember> list = new ArrayList<>();
            for (ValueType valueType : objectType.valueTypes()) {
                try {
                    list.add(ValueTypeMember.get(objectType, valueType));
                } catch (Exception e) {
                }
            }
            return list;
        }
        return new ArrayList<>(0);
    }

    /**
     *
     * @return
     */
    default List<ObjectType> objectTypes() {
        if (this instanceof ObjectType) {
            return this.memberTypes(ObjectType.class);
        }
        return new ArrayList<>(0);
    }

    /**
     *
     * @return
     */
    default List<ArrayType> arrayTypes() {
        if (this instanceof ObjectType) {
            return this.memberTypes(ArrayType.class);
        }
        return new ArrayList<>(0);
    }

    /**
     *
     * @return
     */
    default List<ActiveType> activeTypes() {
        if (this instanceof ObjectType) {
            return this.memberTypes(ActiveType.class);
        }
        return new ArrayList<>(0);
    }

    /**
     *
     */
    default boolean isMember(ActiveType activeType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().memberDefs().stream()
                    .filter(md -> md.activeType().equals(activeType))
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Flag indicates whether ValueType is a member of this ObjectType and is a
     * key field.
     *
     * bond_type: "skey" or "key"
     *
     * @param valueType
     * @return
     */
    default boolean isKey(ValueType valueType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().keys().stream()
                    .filter(mt -> mt.activeType().equals(valueType))
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Flag indicates whether ValueType is a member of this ObjectType and is a
     * unique key field.
     *
     * bond_type: "skey"
     *
     * @param valueType
     * @return
     */
    default boolean isUniqueKey(ValueType valueType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().uniqueKeys().stream()
                    .filter(mt -> mt.activeType().equals(valueType))
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Flag indicates whether ValueType is a member of this ObjectType and is a
     * unique key field.
     *
     * bond_type: "key"
     *
     * @param valueType
     * @return
     */
    default boolean isNaturalKey(ValueType valueType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().naturalKeys().stream()
                    .filter(mt -> mt.activeType().equals(valueType))
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Flag indicates whether ValueType is a member of this ObjectType and is a
     * foreign key field.
     *
     * bond_type: "fkey"
     *
     * @param valueType
     * @return
     */
    default boolean isForeignKey(ValueType valueType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().foreignKeys().stream()
                    .filter(mt -> mt.activeType().equals(valueType))
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Flag indicates whether ValueType is a member of this ObjectType and is an
     * optional field.
     *
     * bond_type: "optional"
     *
     * @param valueType
     * @return
     */
    default boolean isOptional(ValueType valueType) {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().memberDefs().stream()
                    .filter(mt -> mt.activeType().equals(valueType) && mt.bondType() == BondType.OPTIONAL)
                    .findAny()
                    .isPresent();
        }
        return false;
    }

    /**
     * Returns all "key" fields that are of type Value (which they should all
     * be)
     *
     * @return
     */
    default List<ValueType> valueTypeKeys() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            return objectType.schema().keys().stream()
                    .map(md -> md.activeType())
                    .filter(at -> at.role().equals(Role.Value))
                    .map(at -> ValueType.class.cast(at))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    /**
     * Utilizing reference (relationship) information within ActiveRef to get
     * the ObjectTypes's Parent ObjectType.
     *
     * @return
     */
    default ObjectType parentType() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            ActiveRef activeRef = objectType.context().getActiveRef();
            if (activeRef != null) {
                References references = activeRef.get(objectType);
                if (references != null) {
                    ObjectType parentType = references.getParentType();
                    if (parentType != null && !parentType.equals(objectType)) {
                        return parentType;
                    }
                }
            }
            return objectType;
        }
        return null;
    }

    /**
     *
     *
     *
     * @return
     */
    default boolean isParentAnAttribute() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            ActiveRef activeRef = objectType.context().getActiveRef();
            if (activeRef != null) {
                References references = activeRef.get(objectType);
                if (references != null) {
                    return references.isParentAsAttribute();
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Returns all "key" fields for the parent type
     *
     * @return
     */
    default List<ValueType> parentValueTypeKeys() {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            ObjectType parentType = objectType.parentType();
            if (parentType != null && !parentType.equals(objectType)) {
                return parentType.valueTypeKeys();
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * Given a list of ValueType returns a map of ValueType -> Index of its
     * position in this ObjectType. If a ValueType provided is not a member of
     * this object, it will not be in the map.
     *
     * @param valueTypes
     * @return
     */
    default Map<ValueType, Integer> getIndexMap(List<ValueType> valueTypes) {
        Map<ValueType, Integer> map = new HashMap<>(valueTypes.size());
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            for (ValueType valueType : valueTypes) {
                Integer index = objectType.schema().getIndex(valueType);
                if (index != null) {
                    map.put(valueType, index);
                }
            }
        }
        return map;
    }

    /**
     * Create ObjectData
     *
     * Returns a {@link org.schorn.ella.node.ActiveNode.ObjectData ObjectData}
     * instance of
     * {@link com.ms.banking.active.node.ActiveNode.ObjectType ObjectType} using
     * the provided JSON string.
     *
     * Exceptions are thrown when: 1) resulting object has an unexpected type 2)
     * the JSON does not result in an object
     *
     * All other scenarios a null is returned.
     *
     * @param json
     * @return {@link org.schorn.ella.node.ActiveNode.ObjectData ObjectData}
     */
    @SuppressWarnings("rawtypes")
    default ObjectData create(String json) throws Exception {
        if (this instanceof ObjectType) {
            ObjectType objectType = (ObjectType) this;
            Transform transform = TransformProvider.provider().getTransform(objectType.context(), Format.JSON, Format.ActiveNode);
            @SuppressWarnings("unchecked")
            StructData structData = (StructData) transform.apply((String) json);
            if (structData != null) {
                if (structData.activeType().equals(objectType)) {
                    // perfect!
                    return (ObjectData) structData;
                }
                ObjectData objectData = structData.findFirst(objectType);
                if (objectData != null) {
                    // had to search for it.
                    return objectData;
                }
                // oh no, wrong JSON!
                String msg = String.format("%s.create() - unable to create a '%s.%s' object with this JSON: %s",
                        ObjectType.class.getSimpleName(), objectType.context().name(), objectType.name(), json);
                throw new Exception(msg);
            }
            // bad JSON!!
            String msg = String.format("%s.create() - unable to parse JSON to create an object: %s",
                    ObjectType.class.getSimpleName(), json);
            throw new Exception(msg);
        }
        return null;
    }
}
