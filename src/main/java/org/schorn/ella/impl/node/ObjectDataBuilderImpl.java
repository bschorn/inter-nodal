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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.schorn.ella.error.ActiveError;
import org.schorn.ella.error.ActiveError.DataErrorManager;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectData.Builder;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.DefaultValue;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public class ObjectDataBuilderImpl implements Builder {

    /*
	 * ActiveType -> ActiveData
     */
    private final Map<ActiveType, ActiveData> data;
    private ObjectType objectType;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", "", "");
        for (ActiveType activeType : this.data.keySet()) {
            ActiveData activeData = this.data.get(activeType);
            if (activeData == null) {
                joiner.add(String.format("%s: null", activeType.name()));
            } else {
                switch (activeType.role()) {
                    case Value:
                        if (activeData.isNull()) {
                            joiner.add(String.format("%s.%s: null", this.objectType.name(), activeType.name()));
                        } else {
                            joiner.add(String.format("%s.%s: %s", this.objectType.name(), activeType.name(), activeData.activeValue().toString()));
                        }
                        break;
                    case Object:
                        joiner.add(activeData.toString());
                        break;
                    case Array:
                        joiner.add(activeData.toString());
                        break;
                    default:
                        break;
                }
            }
        }
        return joiner.toString();
    }

    /*
	 * Constructor
     */
    public ObjectDataBuilderImpl(ObjectType objectType) {
        this.objectType = objectType;
        this.data = new TreeMap<>();
    }

    @Override
    public ObjectType objectType() {
        return this.objectType;
    }

    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    /**
     * Add one or more self-contained member entries
     *
     */
    @Override
    public void add(ActiveData... entries) {
        if (entries == null || entries.length == 0) {
            return;
        }
        for (int i = 0; i < entries.length; i += 1) {
            if (entries[i] != null) {
                this.add(entries[i].activeType(), entries[i]);
            }
        }
    }

    protected void add0(ActiveData activeData) {
        MemberDef memberDef = this.objectType.schema().get(activeData.activeType());
        if (memberDef != null) {
            this.data.put(activeData.activeType(), activeData);
            return;
        }
        switch (activeData.role()) {
            case Object: {
                /*
			 * Incoming ActiveData is an ObjectData but it was not directly a member
			 * of this object. But it may be a member of a member.
			 *
                 */
                ObjectData objectData = (ObjectData) activeData;
                ArrayType arrayType = ArrayType.get(objectData.context(), objectData.name());
                if (arrayType != null) {
                    memberDef = this.objectType.schema().get(arrayType);
                    if (memberDef != null) {
                        ArrayData arrayData = (ArrayData) this.data.get(arrayType);
                        if (arrayData == null) {
                            arrayData = arrayType.create();
                            this.data.put(arrayType, arrayData);
                        }
                        arrayData.add(activeData);
                    }
                }
                break;
            }
            case Array:
                break;
            case Value:
                break;
            default:
                break;
        }
    }

    /**
     * Add a single entry with type provided independently
     *
     */
    @Override
    public void add(ActiveType activeType, ActiveData activeData) {
        if (activeData == null) {
            switch (activeType.role()) {
                case Value:
                    ValueType valueType = (ValueType) activeType;
                    activeData = valueType.create(null);
                    break;
                case Array:
                    ArrayType arrayType = (ArrayType) activeType;
                    activeData = arrayType.create();
                    break;
                case Object:
                    return;
                default:
                    break;
            }
        }
        if (activeData == null) {
            return;
        }
        if (this.objectType.isDynamic()) {
            /*
			 * Dynamic instance just adds the entries without checking.
             */
            this.data.put(activeType, activeData);
            if (this.objectType instanceof ObjectType.Builder) {
                ObjectType.Builder builder = (ObjectType.Builder) this.objectType;
                builder.add(activeType, BondType.OPTIONAL);
            }
        }
        /*
		 * Static instance confirms the type is part of the schema.
         */
        ObjectSchema schema = this.objectType.schema();
        MemberDef memberType = schema.get(activeType);
        if (memberType != null) {
            this.data.put(activeType, activeData);
        }
    }

    /*
	 *  
     */
    @Override
    public ObjectData build() throws Exception {
        return (ObjectData) build0(false);
    }

    /*
	 * 
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ActiveType> getMissingTypes() throws Exception {
        return (List<ActiveType>) build0(true);
    }

    /*
	 * 
     */
    private Object build0(boolean buildCheckOnly) throws Exception {
        if (this.objectType.isDynamic()) {
            /*
			 * Dynamic instance will build/create a new object type
             */
            if (this.objectType instanceof ObjectType.Builder) {
                ObjectType.Builder builder = (ObjectType.Builder) this.objectType;
                ObjectType objectType = builder.build();
                if (objectType == null) {
                    return null;
                }
                //ActiveProvider.provider().createObjectType(context, object_type, objectMembers)
                this.objectType = objectType;
            }
        }
        ObjectSchema schema = objectType.schema();
        List<ActiveData> members = Functions.createPresizedArrayList(ActiveData.class, schema.memberDefs().size(), null);
        List<ActiveType> missingTypes = new ArrayList<>();
        /*
		 * Go through each defined member and see if the data was added
         */
        for (MemberDef memberType : schema.memberDefs()) {
            // get the data for this type
            ActiveData memberData = this.data.get(memberType.activeType());
            if (memberType.bondType().equals(BondType.AUTOMATIC)) {
                ValueType valueType = (ValueType) memberType.activeType();
                Object value = NodeProvider.provider().getDefaultValue(valueType);
                // check if value is a DefaultValue callback which is a functional (Function<T,R>)
                if (value != null && value instanceof DefaultValue) {
                    // cast and apply
                    DefaultValue defaultValue = (DefaultValue) value;
                    List<ActiveData> memberValues = this.data.values().stream()
                            //.filter(ad -> ad.role().equals(Role.Value))
                            //.map(ad -> ValueData.class.cast(ad))
                            .collect(Collectors.toList());
                    value = defaultValue.getValue(this.objectType, valueType, memberValues);
                    if (value != null) {
                        // great! let's create data object and add to this container
                        this.add(valueType, valueType.create(value));
                        // check if it made it and get the MemberData wrapper
                        memberData = this.data.get(memberType.activeType());
                        // double check
                        if (memberData != null) {
                            // data is there now, set it and continue
                            members.set(memberType.index(), memberData);
                            continue;
                        }
                    }
                } else {
                    this.add(valueType, valueType.create(null));
                }
            }
            switch (memberType.activeType().role()) {
                case Value:
                    if (memberData != null && !memberData.isBlank()) {
                        // data is there, set it and continue
                        members.set(memberType.index(), memberData);
                        continue;
                    }
                    // as of now, only value types will have default values
                    ValueType valueType = (ValueType) memberType.activeType();
                    Object value = NodeProvider.provider().getDefaultValue(valueType);
                    // check if value is a DefaultValue callback which is a functional (Function<T,R>)
                    if (value != null && value instanceof DefaultValue) {
                        // cast and apply
                        DefaultValue defaultValue = (DefaultValue) value;
                        List<ActiveData> memberValues = this.data.values().stream()
                                //.filter(ad -> ad.role().equals(Role.Value))
                                //.map(ad -> ValueData.class.cast(ad))
                                .collect(Collectors.toList());
                        value = defaultValue.getValue(this.objectType, valueType, memberValues);
                    }
                    // check if we have a non-null or if it's an optional field (then we create a null)
                    if (value != null || memberType.bondType().equals(BondType.OPTIONAL) || memberType.bondType().equals(BondType.AUTOMATIC)) {
                        // great! let's create data object and add to this container
                        this.add(valueType, valueType.create(value));
                        // check if it made it and get the MemberData wrapper
                        memberData = this.data.get(memberType.activeType());
                        // double check
                        if (memberData != null) {
                            // data is there now, set it and continue
                            members.set(memberType.index(), memberData);
                            continue;
                        }
                    }
                    break;
                case Array:
                    if (memberData == null) {
                        ArrayType arrayType = (ArrayType) memberType.activeType();
                        members.set(memberType.index(), arrayType.create());
                        continue;
                    }
                default:
                    if (memberData != null) {
                        // data is there, set it and continue
                        members.set(memberType.index(), memberData);
                        continue;
                    }
                    switch (memberType.bondType()) {
                        case OPTIONAL:
                        case FOREIGN_KEY:
                            continue;
                        default:
                            break;
                    }
                    break;
            }
            /*
			 * Must be missing, add to the missing list 
             */
            missingTypes.add(memberType.activeType());
        }
        if (buildCheckOnly) {
            return missingTypes;
        }
        /*
		 * If we have no missing entries then let's create the Key and construct the instance
         */
        if (missingTypes.isEmpty()) {
            //SeriesKey objectKey = null;
            //List<ValueData> keyValues = getKeyValues(members);
            //if (keyValues.size() > 0) {
            //objectKey = SeriesKey.create(this.objectType().context(), getKeyValues(members));
            //}
            //return new ObjectDataImpl(this.objectType,members, objectKey);
            return new ObjectDataImpl(this.objectType, members);
        } else {
            //SeriesKey objectKey = SeriesKey.create(this.objectType().context(), getKeyValues(members));
            //return new ObjectDataImpl(this.objectType,members, objectKey, missingTypes);
            return new ObjectDataImpl(this.objectType, members, missingTypes);
        }
    }

    /*
	 * Key Values for Key creation
     */
    @Deprecated
    private List<ValueData> getKeyValues(List<ActiveData> members) {
        if (this.objectType.schema().hasKeys()) {
            List<MemberDef> keys = this.objectType.schema().keys();
            List<ValueData> keyValues = new ArrayList<>(keys.size());
            for (MemberDef memberDef : keys) {
                keyValues.add((ValueData) members.get(memberDef.index()));
            }
            return keyValues;
        } else {
            return new ArrayList<>(0);
        }
    }

    /*
	 * 
     */
    @SuppressWarnings("unused")
    @Deprecated
    private void throwMissingError(List<ActiveType> missingTypes) throws Exception {
        String requires;
        if (missingTypes.size() > 1) {
            StringJoiner joiner = new StringJoiner(", ", "member", "");
            missingTypes.forEach(activeType -> joiner.add(String.format("%s%s", activeType.role().symbol(), activeType.name())));
            requires = joiner.toString();
        } else {
            StringJoiner joiner = new StringJoiner(", ", "members [ ", " ]");
            missingTypes.forEach(activeType -> joiner.add(String.format("%s%s", activeType.role().symbol(), activeType.name())));
            requires = joiner.toString();
        }
        ErrorData errorData = ErrorData.create(this.objectType, ObjectDataBuilderImpl.class,
                new Object() {
                }.getClass().getEnclosingMethod());
        errorData.setDetail(String.format("ObjectType '%s' requires %s at creation.",
                this.objectType.name(), requires));
        DataErrorManager.get(this.objectType.context()).fire(this.objectType.context(), ActiveError.ErrorFlag.OBJECT_DATA_BUILDER_ERROR, errorData.asActiveData());
        throw new Exception(errorData.toString());
    }

}
