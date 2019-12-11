/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
package org.schorn.ella.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType;
import org.schorn.ella.node.BondType;

/**
 *
 * @author bschorn
 */
public interface ActiveModel {

    static List<ModelDataType> dataTypes(AppContext context) {
        return ModelProvider.provider().dataTypes(context);
    }

    static List<ModelFieldType> fieldTypes(AppContext context) {
        return ModelProvider.provider().fieldTypes(context);
    }

    static List<ModelValueType> valueTypes(AppContext context) {
        return ModelProvider.provider().valueTypes(context);
    }

    static List<ModelObjectType> objectTypes(AppContext context) {
        return ModelProvider.provider().objectTypes(context);
    }

    static List<ModelArrayType> arrayTypes(AppContext context) {
        return ModelProvider.provider().arrayTypes(context);
    }

    public enum ModelTypeCategory {
        STATIC,
        META,
        CODE,
        VALUE,
        OBJECT,
        ARRAY,
        REQUEST,
        RESPONSE,
    }

    public enum ModelRole implements Predicate<ModelType> {
        PrimitiveType(false),
        DataType(false),
        ConstraintType(false),
        FieldType(false),
        ValueType(true),
        ObjectType(true),
        ArrayType(true),
        CompoundValueType(true),
        Member(false);

        private final boolean container;

        ModelRole(boolean container) {
            this.container = container;
        }

        public boolean isContainer() {
            return this.container;
        }

        @Override
        public boolean test(ModelType model) {
            return this.equals(model.modelRole());
        }
    }

    public enum DataCategory {
        MASTER,
        TRANSACTIONAL,
        REPORTING;
    }

    public enum DataPurpose {
        ENTITY(DataCategory.MASTER), // Party, Account, Product
        ASSOCIATION(DataCategory.MASTER), // PartyGroup(Customers,Employees)
        REFERENCE(DataCategory.MASTER), // Side(Buy,Short,Cover,Sell), MarketStrategy(Long,Short)
        REQUEST(DataCategory.TRANSACTIONAL), // Order,
        EXECUTION(DataCategory.TRANSACTIONAL), // Fill, Journal
        REALTIME(DataCategory.REPORTING), // RTBalance, RTPosition
        SNAPSHOT(DataCategory.REPORTING); // SODBalance, EODPosition, Ledger

        DataCategory category;

        DataPurpose(DataCategory category) {
            this.category = category;
        }

        public DataCategory category() {
            return this.category;
        }
    }

    public class UnresolveModeldDependency extends Exception {

        private final ModelRole role;
        private final String name;
        public UnresolveModeldDependency(String message, ModelRole role, String name) {
            super(message);
            this.role = role;
            this.name = name;
        }

        public ModelRole modelRole() {
            return this.role;
        }

        public String modelTypeName() {
            return this.name;
        }
    }

    interface ModelType {

        String name();

        //ModelQualifiedName qualifiedName();

        ModelRole modelRole();

    }

    interface ModelResolvable {

        void resolve() throws UnresolveModeldDependency;

        boolean isResolved();
    }

    interface ModelQualifiedName {

        String typeStr();

        String nameStr();

        default String name() {
            return String.format("%s.%s", this.typeStr(), this.nameStr());
        }

        static ModelQualifiedName create(String typeStr, String nameStr) {
            return ModelQualifiedName.Impl.create(typeStr, nameStr);
        }

        static class Impl implements ModelQualifiedName {
            static ModelQualifiedName create(String typeStr, String nameStr) {
                ModelQualifiedName modelQualifiedName = MQN.get(String.format("%s.%s", typeStr, nameStr));
                if (modelQualifiedName != null) {
                    return modelQualifiedName;
                }
                modelQualifiedName = new Impl(typeStr, nameStr);
                MQN.put(modelQualifiedName.name(), modelQualifiedName);
                return modelQualifiedName;
            }

            static private final Map<String, ModelQualifiedName> MQN = new HashMap<>();
            private final String typeStr;
            private final String nameStr;

            private Impl(String typeStr, String nameStr) {
                this.typeStr = typeStr;
                this.nameStr = nameStr;
            }

            @Override
            public String typeStr() {
                return this.typeStr;
            }

            @Override
            public String nameStr() {
                return this.nameStr;
            }
        }
    }

    interface ModelDataType extends ModelType {

        PrimitiveType primitiveType();

        @Override
        default ModelRole modelRole() {
            return ModelRole.DataType;
        }

    }

    interface ModelConstraintType extends ModelType {
        @Override
        default ModelRole modelRole() {
            return ModelRole.ConstraintType;
        }

    }

    interface ModelConstraint<T, V> {

        T constraintType();

        V constraintValue();
    }

    interface ModelFieldType extends ModelType, ModelResolvable {

        ModelDataType dataType();
        @Override
        default ModelRole modelRole() {
            return ModelRole.FieldType;
        }
        static ModelFieldType create(String name, ModelDataType dataType) throws Exception {
            return ModelProvider.provider().createInstance(ModelFieldType.class, name, dataType);
        }
    }

    interface ModelMember extends ModelType, ModelResolvable {
        BondType bondType();

        ModelMemberType memberType();

        ModelContainerType containerType();

        long valueTypeFlags();

        boolean isFlag(ValueTypeFlag valueTypeFlag);

        @Override
        default ModelRole modelRole() {
            return ModelRole.Member;
        }

    }

    /**
     * This interface is strictly for restricting the interfaces that can be
     * used as members of a container type. For example, ModelDataType,
     * ModelFieldType should not be members as they are not used to describe an
     * attribute but rather storage details and restrictions of a ValueType
     */
    interface ModelMemberType extends ModelType, ModelResolvable {
    }
    interface ModelContainerType extends ModelType {

        void addMember(ModelMember member);
        List<ModelMember> members();

    }

    public enum ObjectTypeFlag {
        UNDEFINED(1 << 0),
        PII(1 << 1),
        NOFLAG(1 << 31);

        private final long flag;

        ObjectTypeFlag(long flag) {
            this.flag = flag;
        }

        public long flag() {
            return this.flag;
        }

        /**
         * Convert a single object into a set of flags.
         *
         * @param object
         * @return
         */
        static public EnumSet<ObjectTypeFlag> getFlagsForObject(long object) {
            EnumSet objectTypeFlags = EnumSet.noneOf(ObjectTypeFlag.class);
            for (ObjectTypeFlag objectTypeFlag : ObjectTypeFlag.values()) {
                if ((objectTypeFlag.flag & object) == objectTypeFlag.flag) {
                    objectTypeFlags.add(object);
                }
            }
            return objectTypeFlags;
        }

        /**
         * Convert a set of flags into a single object.
         *
         * @param flags
         * @return
         */
        static public long getValueForFlags(Set<ObjectTypeFlag> flags) {
            long object = 0;
            for (ObjectTypeFlag objectTypeFlag : ObjectTypeFlag.values()) {
                object |= objectTypeFlag.flag;
            }
            return object;
        }

        static public ObjectTypeFlag fromString(String object_type_flag) {
            if (object_type_flag != null) {
                for (ObjectTypeFlag objectTypeFlag : ObjectTypeFlag.values()) {
                    if (objectTypeFlag.name().compareToIgnoreCase(object_type_flag) == 0) {
                        return objectTypeFlag;
                    }
                }
            }
            return ObjectTypeFlag.UNDEFINED;
        }

        static public long getValueForFlagsFromString(String... object_type_flags) {
            Set<ObjectTypeFlag> flags = new HashSet<>();
            for (String object_type_flag : object_type_flags) {
                ObjectTypeFlag objectTypeFlag = fromString(object_type_flag);
                if (objectTypeFlag != ObjectTypeFlag.UNDEFINED) {
                    flags.add(objectTypeFlag);
                }
            }
            return getValueForFlags(flags);
        }
    }

    public enum ValueTypeFlag {
        UNDEFINED(1 << 0),
        ID(1 << 1), // field is an identifier
        SEQ(1 << 2), // field is in a sequence from oldest to newest
        ATTR(1 << 3), // field is an attribute or characteristic
        FLAG(1 << 4), // field is flag
        PII_LINKED(1 << 5), // field contains Personally Identifiable Information (Linked)
        PII_LINKABLE(1 << 6), // field contains Personally Identifiable Information (Linkable)
        NOFLAG(1 << 31);

        private final long flag;

        ValueTypeFlag(long flag) {
            this.flag = flag;
        }

        public long flag() {
            return this.flag;
        }

        /**
         * Convert a single value into a set of flags.
         *
         * @param value
         * @return
         */
        static public EnumSet<ValueTypeFlag> getFlagsForValue(long value) {
            EnumSet valueTypeFlags = EnumSet.noneOf(ValueTypeFlag.class);
            for (ValueTypeFlag valueTypeFlag : ValueTypeFlag.values()) {
                if ((valueTypeFlag.flag & value) == valueTypeFlag.flag) {
                    valueTypeFlags.add(value);
                }
            }
            return valueTypeFlags;
        }

        /**
         * Convert a set of flags into a single value.
         *
         * @param flags
         * @return
         */
        static public long getValueForFlags(Set<ValueTypeFlag> flags) {
            long value = 0;
            for (ValueTypeFlag valueTypeFlag : ValueTypeFlag.values()) {
                value |= valueTypeFlag.flag;
            }
            return value;
        }

        static public ValueTypeFlag fromString(String value_type_flag) {
            if (value_type_flag != null) {
                for (ValueTypeFlag valueTypeFlag : ValueTypeFlag.values()) {
                    if (valueTypeFlag.name().compareToIgnoreCase(value_type_flag) == 0) {
                        return valueTypeFlag;
                    }
                }
            }
            return ValueTypeFlag.UNDEFINED;
        }

        static public long getValueForFlagsFromString(String... value_type_flags) {
            Set<ValueTypeFlag> flags = new HashSet<>();
            for (String value_type_flag : value_type_flags) {
                ValueTypeFlag valueTypeFlag = fromString(value_type_flag);
                if (valueTypeFlag != ValueTypeFlag.UNDEFINED) {
                    flags.add(valueTypeFlag);
                }
            }
            return getValueForFlags(flags);
        }

        //static public final EnumSet KEYS = EnumSet.of(ValueTypeFlag.XYZ, ValueTypeFlag.ABC);
    }

    /**
     * Scalar
     */
    interface ModelValueType extends ModelMemberType {
        ModelFieldType fieldType();

        long valueTypeFlags();

        boolean isFlag(ValueTypeFlag valueTypeFlag);

        @Override
        default ModelRole modelRole() {
            return ModelRole.ValueType;
        }
        static ModelValueType create(String name, ModelFieldType fieldType) throws Exception {
            return ModelProvider.provider().createInstance(ModelValueType.class, name, fieldType);
        }
    }

    /**
     * Map
     */
    interface ModelObjectType extends ModelContainerType, ModelMemberType {
        DataCategory category();

        DataPurpose purpose();

        long objectTypeFlags();

        @Override
        default ModelRole modelRole() {
            return ModelRole.ObjectType;
        }

        static ModelObjectType create(String name, DataCategory category, DataPurpose purpose) throws Exception {
            return ModelProvider.provider().createInstance(ModelObjectType.class, name, category, purpose);
        }

    }

    /**
     * Series
     */
    interface ModelArrayType extends ModelContainerType, ModelMemberType {
        @Override
        default ModelRole modelRole() {
            return ModelRole.ArrayType;
        }

    }

    interface ModelCompoundValueType extends ModelObjectType, ModelValueType {

        @Override
        default ModelRole modelRole() {
            return ModelRole.CompoundValueType;
        }
    }

}
