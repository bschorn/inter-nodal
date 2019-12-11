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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.extension.ActiveConstraint;
import org.schorn.ella.extension.ActiveFamily;
import org.schorn.ella.extension.ActiveFind;
import org.schorn.ella.extension.ActiveJson;
import org.schorn.ella.extension.ActiveLabel;
import org.schorn.ella.extension.ActiveLink;
import org.schorn.ella.extension.ActiveMatch;
import org.schorn.ella.extension.ActiveMeta;
import org.schorn.ella.extension.ActiveOut;
import org.schorn.ella.extension.ActiveStream;
import org.schorn.ella.extension.ArrayDataExt;
import org.schorn.ella.extension.ArrayHtml;
import org.schorn.ella.extension.ObjectDataExt;
import org.schorn.ella.extension.ObjectHtml;
import org.schorn.ella.extension.ObjectTypeExt;
import org.schorn.ella.extension.ValueTypeExt;
import org.schorn.ella.node.MetaTypes.FieldTypes;

/**
 * Active Nodes Interface
 *
 * Implementation: ActiveProvider.provider()
 *
 * @author schorn
 *
 */
public interface ActiveNode {

    String name();

    /**
     *
     *
     *
     */
    public enum Role {
        Field('^', ValueType.FieldType.class, null),
        Value('$', ValueType.class, ValueData.class),
        Object('#', ObjectType.class, ObjectData.class),
        Array('@', ArrayType.class, ArrayData.class),
        Unknown('?', Object.class, Object.class);

        Character symbol;
        Class<?> typeClass;
        Class<?> dataClass;

        Role(Character symbol, Class<?> typeClass, Class<?> dataClass) {
            this.symbol = symbol;
            this.typeClass = typeClass;
            this.dataClass = dataClass;
        }

        public String symbol() {
            return symbol.toString();
        }

        static public Role roleFor(Class<?> classOf) {
            for (Role role : Role.values()) {
                if (role.typeClass.equals(classOf) || (role.dataClass != null && role.dataClass.equals(classOf))) {
                    return role;
                }
            }
            return Role.Unknown;
        }

        static public Role roleForSymbol(Character symbol) {
            for (Role role : Role.values()) {
                if (role.symbol == symbol) {
                    return role;
                }
            }
            return Role.Unknown;
        }
    }

    /**
     *
     *
     *
     */
    public enum DomainType {
        Attribute("attribute"),
        ValueObject("value_object"),
        Entity("entity"),
        Aggregate("aggregate"),
        Unknown("unknown"),
        Dynamic("dynamic"),
        Meta("meta"),
        Mapping("mapping");

        String tagName;

        DomainType(String tagName) {
            this.tagName = tagName;
        }

        /**
         *
         * @return
         */
        public String tagName() {
            return this.tagName;
        }

        static public DomainType valueFromTag(String tagName) {
            for (DomainType domainType : DomainType.values()) {
                if (domainType.tagName().equals(tagName)) {
                    return domainType;
                }
            }
            return DomainType.Unknown;
        }
    }

    /**
     *
     *
     *
     */
    public enum ObjectRole {
        Entity,
        Activity,
        Aggregate,
        Meta,
        Unknown;

        String tagName;

        ObjectRole() {
            this.tagName = this.name().toLowerCase();
        }

        /**
         *
         * @return
         */
        public String tagName() {
            return this.tagName;
        }

        static public ObjectRole valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectRole.Unknown;
            }
            for (ObjectRole dataCategory : ObjectRole.values()) {
                if (dataCategory.tagName.equals(tagName)) {
                    return dataCategory;
                }
            }
            return ObjectRole.Unknown;
        }
    }

    public enum ObjectLevel {
        Universal,
        Industry,
        Enterprise,
        Division,
        Department,
        Application,
        Meta,
        Unknown;

        String tagName;

        ObjectLevel() {
            this.tagName = this.name().toLowerCase();
        }

        public String tagName() {
            return this.tagName;
        }

        static public ObjectLevel valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectLevel.Unknown;
            }
            for (ObjectLevel level : ObjectLevel.values()) {
                if (level.tagName.equalsIgnoreCase(tagName)) {
                    return level;
                }
            }
            return ObjectLevel.Unknown;

        }
    }

    public enum ObjectSubRole {
        Reference(ObjectRole.Entity),
        Meta(ObjectRole.Meta),
        Unknown(ObjectRole.Unknown);

        String tagName;
        ObjectRole objectRole;

        ObjectSubRole(ObjectRole objectRole) {
            this.tagName = this.name().toLowerCase();
            this.objectRole = objectRole;
        }

        public ObjectRole parentRole() {
            return this.objectRole;
        }

        public String tagName() {
            return this.tagName;
        }

        static public ObjectSubRole valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectSubRole.Unknown;
            }
            for (ObjectSubRole subRole : ObjectSubRole.values()) {
                if (subRole.tagName.equalsIgnoreCase(tagName)) {
                    return subRole;
                }
            }
            return ObjectSubRole.Unknown;

        }
    }

    /**
     *
     *
     *
     */
    public enum Format {
        JSON, // (String.class),
        JsonRecord, // (String.class),
        JsonObject, // (JsonObject.class),
        JsonReader, // (Reader.class),
        OpenNode, // (OpenNode.class),
        ActiveNode, // (StructData.class),
        DSV, // (ActiveIO.Tabular.class),
        Line, // (String.class),
        Tokens,// (List.class),
        YAML, // (String.class),
        ;
    }

    /**
     * Implementor must provide storage size in bytes
     */
    interface Bytes {

        int bytes();
    }

    /**
     * Implementor must provide unique integer for each instance
     */
    interface ActiveId {

        Integer activeId();
    }

    /**
     * Implementor must provide logic for determining if the content is null or
     * is blank
     */
    interface ContentState {

        boolean isNull();

        boolean isBlank();
    }

    /**
     * Implementor must provide a role for each instance.
     *
     * Default implementation is provided.
     */
    interface ActiveRole {

        default Role role() {
            if (this instanceof ValueType.FieldType) {
                return Role.Field;
            }
            if (this instanceof ValueType || this instanceof ValueData) {
                return Role.Value;
            }
            if (this instanceof ObjectType || this instanceof ObjectData) {
                return Role.Object;
            }
            if (this instanceof ArrayType || this instanceof ArrayData) {
                return Role.Array;
            }
            if (this instanceof MemberDef) {
                return ((MemberDef) this).activeType().role();
            }
            return Role.Unknown;
        }
    }

    /**
     * Active Types that have no permanence are transient. They can *not* be
     * placed into the Repo (nor are they meant to be).
     */
    interface Transient {

        /**
         * Is this type or instance a transient?
         *
         * A transient type is not from a META file and its not kept in the Type
         * library. A transient type creates transient data instances. A
         * transient data instance will not participate in the repo.
         *
         * Transient objects could be 1) working objects 2) adhoc responses 3)
         * storage constructs
         *
         * @return boolean
         */
        default boolean isTransient() {
            return false;
        }
    }

    /**
     * ActiveType
     *
     * Base class for ValueType, ObjectType, ArrayType
     */
    interface ActiveType extends ActiveNode, Contextual, ActiveRole, Bytes, ActiveId, ActiveLabel,
            Comparable<ActiveType>, Transient, ActiveMeta {

        static ActiveType get(AppContext context, String active_type) {
            ActiveType activeType = ValueType.get(context, active_type);
            if (activeType != null) {
                return activeType;
            }
            return ObjectType.get(context, active_type);
        }

        int activeIdx();
    }

    /**
     * Identity of the party responsible for an action or owner
     */
    interface Identity extends ActiveNode, Contextual, Comparable<Identity> {

        public enum IdentityType {
            Person, Service, Component, Admin, Unknown,;
        }

        UUID uuid();

        Integer identityId();

        String uniqueId();

        String userId();

        IdentityType type();

        LocalDateTime createTS();

        static Identity create(AppContext context, IdentityType identityType, String userId) throws Exception {
            return NodeProvider.provider().createIdentity(context, identityType, userId);
        }
    }

    /**
     * INode
     *
     * Registered Data Object's Tracking Current Node Status (not business
     * status) and Location (host-port)
     */
    interface INode extends ActiveNode, Comparable<INode> {

        static INode create(ActiveData activeData, Identity identity) {
            return NodeProvider.provider().createINode(activeData, identity);
        }

        UUID uuid();

        ActiveData data();

        LocalDateTime createTS();

        /**
         *
         * @return
         */
        Identity createId();

        LocalDateTime modifyTS();

        Identity modifyId();

        LocalDateTime accessTS();

        Identity accessId();

        void modify(Identity identity);

        void access(Identity identity);

        static INode get(UUID uuid) {
            return NodeProvider.provider().retrieveINode(uuid);
        }
    }

    /**
     * Constraints - Constraints Types (legs) - Constraints Data
     */
    interface Constraints {

        DataGroup dataGroup();

        // constraint legs
        List<ConstraintType<?>> constraintTypes();

        ConstraintData constraint(ConstraintType<?> constraintType);

        interface ConstraintTest<T> extends Predicate<T> {
        }

        /**
         * Constraint Type
         *
         * one or more can be attached to a DataGroup and should facilitate the
         * constraining of an instance of the data type family
         */
        interface ConstraintType<T> {

            public enum StandardSets {
                Range,
                Pattern,
                List;

                public List<StandardTypes> getStandardTypes(Class<?> dataClass) {
                    List<StandardTypes> list = new ArrayList<>();
                    for (StandardSets set : StandardSets.values()) {
                        for (StandardTypes type : StandardTypes.values()) {
                            if (type.standardSet().equals(set)) {
                                if (type.valueClass().equals(dataClass)) {
                                    list.add(type);
                                }
                            }
                        }
                    }
                    return list;
                }
            }

            public enum StandardTypes {
                min_integer(Integer.class, StandardSets.Range),
                max_integer(Integer.class, StandardSets.Range),
                inc_integer(Integer.class, StandardSets.Range),
                min_decimal(BigDecimal.class, StandardSets.Range),
                max_decimal(BigDecimal.class, StandardSets.Range),
                inc_decimal(BigDecimal.class, StandardSets.Range),
                min_date(LocalDate.class, StandardSets.Range),
                max_date(LocalDate.class, StandardSets.Range),
                inc_date(LocalDate.class, StandardSets.Range),
                min_time(LocalTime.class, StandardSets.Range),
                max_time(LocalTime.class, StandardSets.Range),
                inc_time(LocalTime.class, StandardSets.Range),
                min_datetime(LocalDateTime.class, StandardSets.Range),
                max_datetime(LocalDateTime.class, StandardSets.Range),
                inc_datetime(LocalDateTime.class, StandardSets.Range),
                holidays(LocalDate.class, StandardSets.List),
                day_of_week(DayOfWeek.class, StandardSets.List),
                pattern(String.class, StandardSets.Pattern),
                list(List.class, StandardSets.List);

                Class<?> valueClass;
                StandardSets set;

                StandardTypes(Class<?> valueClass, StandardSets set) {
                    this.valueClass = valueClass;
                    this.set = set;
                }

                public Class<?> valueClass() {
                    return this.valueClass;
                }

                public StandardSets standardSet() {
                    return this.set;
                }
            }

            String name();

            DataGroup dataGroup();

            Class<T> dataClass();

            boolean test(List<Object> constraintValues, Object value);

            static ConstraintType<?> get(String name) {
                return NodeProvider.provider().getConstraintType(name);
            }
        }

        /**
         * Constraint Data
         */
        interface ConstraintData {

            ConstraintType<?> constraintType();

            List<Object> constraintValues();

            boolean test(Object value);
        }

        /**
         * Constraint Builder
         *
         * To create the instance associated with a custom FieldType
         */
        interface Builder {

            List<ConstraintType<?>> constraintTypes();

            Builder add(ConstraintType<?> constraintType, List<Object> value);

            Constraints build();
        }

        /**
         * Constraint Builder Factory
         *
         * Creates a builder instance and initializes with a list of
         * ContstraintType(s) for the DataGroup. Add discrete values to the
         * ConstraintType and build the Constraints instance to be used to
         * create a custom FieldType.
         *
         */
        static Builder builder(DataGroup dataGroup) throws Exception {
            return NodeProvider.provider().createInstance(ActiveNode.Constraints.Builder.class, dataGroup);
        }

    }

    /**
     * MemberDef
     *
     * Details the relationship between a parent-child where parent is a
     * StructData and child is ActiveData
     */
    interface MemberDef {

        ActiveType activeType();

        BondType bondType();

        int index();

        static MemberDef create(ActiveType activeType, BondType bondType, int index) throws Exception {
            return NodeProvider.provider().createMemberDef(activeType, bondType, index);
        }
    }

    /**
     * MemberType
     *
     * Details the relationship between a parent-child where parent is a
     * StructData and child is ActiveData
     * @param <T>
     */
    interface MemberType<T> {

        ObjectType memberOfType();

        T memberType();

        int index();
    }

    /**
     * ValueTypeMember
     *
     */
    interface ValueTypeMember extends MemberType<ValueType> {

        ValueType memberType();

        String asValueTypeMemberStr();

        static public ValueTypeMember get(ObjectType objectType, ValueType valueType) throws Exception {
            if (objectType != null && valueType != null) {
                MemberDef memberDef = objectType.schema().get(valueType);
                if (memberDef != null) {
                    return NodeProvider.provider().createValueMemberType(objectType, memberDef);
                }
            }
            throw new Exception(String.format("%s.get() - ObjectType: %s, ValueType: %s - unable to get membership.",
                    ValueTypeMember.class.getSimpleName(), objectType != null ? objectType.name() : "null",
                    valueType != null ? valueType.name() : "null"));
        }

        static public ValueTypeMember parse(AppContext context, String valueTypeMemberStr) throws Exception {
            return NodeProvider.provider().createValueMemberType(context, valueTypeMemberStr);
        }

        /**
         *
         * @param context_str
         * @param valueTypeMemberStr
         * @return
         * @throws Exception
         */
        static public ValueTypeMember parse(String context_str, String valueTypeMemberStr) throws Exception {
            Optional<AppContext> optContext = AppContext.valueOf(context_str);
            if (optContext.isPresent()) {
                return NodeProvider.provider().createValueMemberType(optContext.get(), valueTypeMemberStr);
            }
            throw new Exception(String.format("%s.parse() - AppContext: '%s' is not valid.",
                    ValueTypeMember.class.getSimpleName(), context_str));
        }

        static public ValueTypeMember[] parse(AppContext context, String[] valueTypeMembersStr) throws Exception {
            ValueTypeMember[] valueTypeMembers = new ValueTypeMember[valueTypeMembersStr.length];
            for (int i = 0; i < valueTypeMembersStr.length; i += 1) {
                valueTypeMembers[i] = NodeProvider.provider().createValueMemberType(context, valueTypeMembersStr[i]);
            }
            return valueTypeMembers;
        }

    }

    /**
     * ValueType - an identifiable (named) data point container.
     *
     * The name, definition, category, constraints of a container for a data
     * point.
     *
     * Note: NULL is a valid data point but a null ValueData is not. To capture
     * a null value for a particular ValueType the ValueData must exist in order
     * to identify the ValueType (ValueData.valueType()) and to communicate that
     * the data point is correctly null.
     */
    interface ValueType extends ActiveType, ValueTypeExt {

        static String TYPE_TAG = "value_types";

        /**
         * Creates a ValueData instance as defined by the ValueType instance
         * that created it, using the specified 'value' to seed the value. The
         * 'value' should be an instance of Object that corresponds with the
         * {@link org.schorn.ella.node.api.meta.DataGroup DataGroup} listed by
         * the null null         {@link org.schorn.ella.node.ActiveNode.ValueType.DataType
		 * ValueType.DataType} which can be found through the null null         {@link org.schorn.ella.node.ActiveNode.ValueType.FieldType
		 * ValueType.FieldType}
         *
         * If the value parameter is null (isNull() == true) then the ValueData
         * instance will be the null equivalent of the underlying primitive
         * type.
         *
         * The purpose of the NULL is to signify the non-existence of value for
         * an attribute, not the non-existence of an attribute.
         *
         * @param value
         * @return
         * @throws Exception
         */
        ValueData create(Object value);

        /**
         * Creates an undefined (isBlank() == true) ValueData instance as
         * defined by the ValueType instance that created it.
         *
         * The purpose of BLANK is to signify the value of attribute is missing
         * or unknown at this time but should be populated possibly through
         * automation (events).
         *
         * @param value
         * @return
         * @throws Exception
         */
        ValueData create() throws Exception;

        /**
         * Returns the FieldType for this ValueType.
         *
         * {@link org.schorn.ella.node.ActiveNode.ValueType
         * ValueType}->{@link org.schorn.ella.node.ActiveNode.ValueType.FieldType
         * FieldType}->{@link org.schorn.ella.node.ActiveNode.ValueType.FieldType.DataFamily
         * DataType}->{@link org.schorn.ella.node.ActiveNode.ValueType.FieldType.DataFamily.PrimitiveType
         * PrimitiveType}->{@link org.schorn.ella.node.ActiveNode.ValueType.FieldType.DataFamily.PrimitiveType.DataGroup
         * DataGroup}
         *
         * @return
         */
        FieldType fieldType();

        /**
         * PrimitiveType - the cargo type within a data point container
         */
        interface PrimitiveType<T> extends ActiveNode, Bytes {

            DataGroup dataGroup();

            Class<T> dataClass();

            ValueData.PrimitiveData<T> newInstance(Object value) throws Exception;

            ValueData.PrimitiveData<T> newInstance() throws Exception;

            ValueData.PrimitiveData<T> nullInstance() throws Exception;

            static PrimitiveType<?> getDefault(AppContext context, DataGroup dataGroup) {
                return NodeProvider.provider().getType(context, PrimitiveType.class, dataGroup.name());
            }
        }

        /**
         * DataType - a data point container's abilities
         */
        interface DataType extends ActiveNode, Contextual {

            static String TYPE_TAG = "data_types";

            /**
             *
             * @return {@link org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType
             *         PrimitiveType}
             */
            PrimitiveType<?> primitiveType();

            List<Constraints.ConstraintType<?>> constraintTypes();

            /**
             * STATIC
             *
             * DataType creation (definition)
             *
             * @param context NodeContext
             * @param data_type String
             * @param primitiveType PrimitiveType<?>
             * @param constraintTypes
             * @return {@link org.schorn.ella.node.ActiveNode.ValueType.DataType
             *         DataType}
             * @throws Exception
             */
            static DataType create(AppContext context, String data_type, PrimitiveType<?> primitiveType,
                    Constraints.ConstraintType<?>[] constraintTypes) throws Exception {
                return NodeProvider.provider().createDataType(context, data_type, primitiveType, constraintTypes);
            }

            static DataType get(AppContext context, String data_type) {
                DataType dataType = NodeProvider.provider().getType(context, DataType.class, data_type);
                if (dataType == null) {
                    dataType = NodeProvider.provider().getType(AppContext.Common, DataType.class, data_type);
                }
                return dataType;
            }
        }

        /**
         * FieldType - a data point container's configuration
         *
         */
        interface FieldType extends ActiveType {

            static String TYPE_TAG = "field_types";

            DataType dataType();

            Constraints constraints();

            static FieldType create(AppContext context, String field_type, DataType dataType, Constraints constraints,
                    Integer maxWidth) throws Exception {
                return NodeProvider.provider().createFieldType(context, field_type, dataType, constraints, maxWidth);
            }

            static FieldType get(AppContext context, String field_type) {
                return NodeProvider.provider().getType(context, FieldType.class, field_type);
            }

            static FieldType forClass(Class<? extends Object> classOf) {
                if (java.lang.Number.class.isAssignableFrom(classOf)) {
                    return FieldTypes.DECIMAL.fieldType();
                } else if (Temporal.class.isAssignableFrom(classOf)) {
                    return FieldTypes.TIMESTAMP.fieldType();
                } else {
                    return FieldTypes.TEXT.fieldType();
                }
            }

            Integer maxWidth();
        }

        /**
         * If a default value is dynamic, then implement this interface
         */
        interface DefaultValue {

            Object getValue(ObjectType objectType, ValueType valueType, List<ActiveData> memberData);
        }

        /**
         * Creates a new ValueType definition with name specified by
         * 'value_type' in the specified NodeContext using the specified
         * FieldType. The FieldType does NOT have to have the same NodeContext,
         * but the resulting ValueType will belong to the given NodeContext.
         *
         * NodeContext.ABC, "StreetAddress", XYZ.FieldType.address_street ->
         * ABC.StreetAddress
         *
         * @param context NodeContext the created ValueType will belong.
         * @param value_type String name of the ValueType. Uniqueness:
         * NodeContext + NodeRole + Name
         * @param fieldType null null         {@link org.schorn.ella.node.ActiveNode.ValueType.FieldType
		 *            ValueType.FieldType}
         *
         * @throws java.lang.Exception
         *
         * @return The created ValueType instance
         *
         */
        static ValueType create(AppContext context, String value_type, FieldType fieldType) throws Exception {
            return NodeProvider.provider().createValueType(context, value_type, fieldType);
        }

        /**
         * Creates a temporary ValueType with name specified by 'value_type' and
         * default FieldType based on data type of 'value' Object. It will be
         * associated with the NodeContext passed in the parameter list.
         *
         * The type is not cataloged into the type library but can be reused as
         * long as the client has a reference to the type object.
         *
         * @param context NodeContext the created ValueType will be associated.
         * @param value_type String name of the ValueType. Uniqueness:
         * NodeContext + NodeRole + Name
         * @param value Data that is representative of the data this type will
         * represent.
         *
         * @throws java.lang.Exception
         *
         * @return The temporary ValueType instance
         *
         */
        static ValueType dynamic(AppContext context, String value_type, Object value) throws Exception {
            return NodeProvider.provider().createDynamicValueType(context, value_type, value);
        }

        /**
         * Retrieves a previously defined ValueType with the name specified by
         * 'value_type' in the specified NodeContext.
         *
         * @param context NodeContext the created ValueType will belong.
         * @param value_type String name of the ValueType. Uniqueness:
         * NodeContext + NodeRole + Name
         *
         *
         * @return The request ValueType instance
         *
         */
        static ValueType get(AppContext context, String value_type) {
            return NodeProvider.provider().getValueType(context, value_type);
        }

        static ValueType[] parse(AppContext context, String[] value_types) {
            ValueType[] valueTypes = new ValueType[value_types.length];
            for (int i = 0; i < value_types.length; i += 1) {
                valueTypes[i] = ValueType.get(context, value_types[i]);
            }
            return valueTypes;
        }

    }

    /**
     * ObjectType - an identifiable (named) data object
     *
     * The name, attributes, schema.
     */
    interface ObjectType extends ActiveType, ObjectTypeExt, ObjectHtml {

        static String TYPE_TAG = "object_types";

        boolean isDynamic();

        DomainType domainType();

        ObjectRole objectRole();

        ObjectLevel objectLevel();

        ObjectSubRole objectSubRole();

        /**
         * Since ObjectData objects are immutable we use the Builder Pattern to
         * create them.
         *
         * @return
         */
        ObjectData.Builder builder();

        /**
         * Skip the builder and immediately create ObjecData objects by
         * providing all the members in the create method.
         *
         * @param activeData
         * @return
         * @throws Exception
         */
        ObjectData create(ActiveData... activeData) throws Exception;

        ObjectData create(Object... values) throws Exception;

        ObjectSchema schema();

        interface Builder {

            Builder add(ActiveType activeType, BondType bondType);

            Builder add(ActiveType activeType);

            /**
             * Create a registered immutable ObjectType.
             *
             * @return ObjectType
             */
            ObjectType build();

            /**
             * Create an unregistered immutable ObjectType.
             *
             * @return ObjectType
             */
            ObjectType lease();

            /**
             * Create an unregistered mutable ObjectType.
             *
             * @return
             */
            ObjectType dynamic();
        }

        /**
         *
         *
         *
         */
        interface ObjectSchema extends Bytes, ActiveNode {

            /**
             * All Members (all roles) in the order they were specified.
             *
             * @return
             */
            List<MemberDef> memberDefs();

            /**
             * Whether or not the object has key fields
             *
             * @return
             */
            boolean hasKeys();

            /**
             * All Members that are role of Value
             *
             * @return
             */
            List<MemberDef> valueTypes();

            /**
             * The index or position where the data should reside
             *
             * @return
             */
            List<Integer> valueIndexes();

            /**
             * Special Key if one is present
             */
            MemberDef specialKey();

            /**
             * Surrogate Key(s) if they are present
             */
            List<MemberDef> uniqueKeys();

            /**
             * Natural Key(s) if they are present
             */
            List<MemberDef> naturalKeys();

            /**
             * Surrogate Key(s) if present -else- Natural Key(s) if present
             */
            List<MemberDef> keys();

            List<Integer> keyIndexes();

            /**
             * Foreign Key(s) if present
             *
             * The surrogate (or natural) key of another object.
             *
             * @return
             */
            List<MemberDef> foreignKeys();

            /**
             *
             * @param activeType
             * @return
             */
            MemberDef get(ActiveType activeType);

            MemberDef get(Integer activeId);

            /**
             * Given an ActiveType returns which position 0..N the data should
             * reside internally. Returns null or -1 if the type is not valid in
             * this object
             *
             * @param activeType
             * @return Integer or null
             */
            Integer getIndex(ActiveType activeType);
        }

        static Builder builder(AppContext context, String object_type, DomainType domainType) throws Exception {
            return NodeProvider.provider().createInstance(ObjectType.Builder.class, context, object_type, domainType, ObjectRole.Unknown, ObjectLevel.Unknown, ObjectSubRole.Unknown);
        }

        static Builder builder(AppContext context, String object_type, DomainType domainType, ObjectRole objectRole, ObjectLevel objectLevel, ObjectSubRole objectSubRole) throws Exception {
            return NodeProvider.provider().createInstance(ObjectType.Builder.class, context, object_type, domainType, objectRole, objectLevel, objectSubRole);
        }

        /*
		 * - use builder static ObjectType create(NodeContext context, String
		 * object_type) throws Exception { return null; }
         */
        /**
         * Method dynamic() returns a ObjectType.Builder disguised as a static
         * ObjectType. The trick is that when the ObjectData.Builder.build() is
         * called it builds an ObjectType (and registers it as static) using the
         * members added while building the ObjectData.
         *
         *
         * @param context
         * @param object_type
         * @return
         * @throws Exception
         */
        static ObjectType dynamic(AppContext context, String object_type) throws Exception {
            Builder builder = NodeProvider.provider().createInstance(ObjectType.Builder.class, context, object_type,
                    DomainType.Dynamic);
            return builder.dynamic();
        }

        static ObjectType get(AppContext context, String object_type) {
            return NodeProvider.provider().getObjectType(context, object_type);
        }

        static ObjectType[] parse(AppContext context, String[] object_types) {
            ObjectType[] objectTypes = new ObjectType[object_types.length];
            for (int i = 0; i < object_types.length; i += 1) {
                objectTypes[i] = ObjectType.get(context, object_types[i]);
            }
            return objectTypes;
        }
    }

    /*
	 * ARRAY TYPE
     */
    interface ArrayType extends ActiveType {

        static String TYPE_TAG = "array_types";

        static ArrayType create(AppContext context, String array_type, ActiveType activeType, BondType bondType)
                throws Exception {
            return NodeProvider.provider().createArrayType(context, array_type, activeType, bondType);
        }

        boolean isDynamic();

        /**
         * Dynamic Array Type
         *
         * The first ActiveData object added to a dynamic ArrayType becomes the
         * prototype. Any other add() calls will be expected to have an
         * ActiveData with an ActiveType of the first one added.
         *
         * @param context
         * @param array_type
         * @return
         * @throws Exception
         */
        static ArrayType dynamic(AppContext context, String array_type) throws Exception {
            return NodeProvider.provider().createArrayType(context, array_type, null, null);
        }

        /**
         *
         * @param context
         * @param array_type
         * @return
         */
        static ArrayType get(AppContext context, String array_type) {
            return NodeProvider.provider().getArrayType(context, array_type);
        }

        ActiveType memberType();

        MemberDef memberDef();

        ArrayData create();
    }

    /*
	 * LOG TYPE
     */
    /**
     * ActiveData
     *
     * Base class for ValueData and StructData (which is base for ObjectData,
     * ArrayData)
     */
    interface ActiveData extends ActiveNode, Contextual, Bytes, Comparable<ActiveData>, ActiveId, ActiveRole,
            ContentState, ActiveFind, ActiveMatch, ActiveStream, ActiveOut, ActiveFamily, ActiveConstraint {

        int size();

        ActiveType activeType();

        Object activeValue();

        List<Object> activeValues();

        <T extends ActiveData> List<T> activeValues(Class<T> classForT);

        <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter);

        List<Object> activeValues(int startIdx, int length);

        <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length);

    }

    /**
     * ValueData
     *
     * A data object that represents a single data-point.
     */
    interface ValueData extends ActiveData, ActiveLink {

        default ValueType valueType() {
            return (ValueType) this.activeType();
        }

        /**
         *
         * @param out
         * @throws IOException
         */
        void write(ObjectOutput out) throws IOException;

        void read(ObjectInput in) throws IOException, ClassNotFoundException;

        <T> T as(Class<T> classOfT) throws Exception;

        /*
		 * PRIMITIVE DATA
         */
        interface PrimitiveData<T> extends ActiveNode, Externalizable, ContentState {

            boolean initialized();

            Class<T> dataClass();

            T asLocal();

            // byte[] asBytes();

            /**
             *
             * @param other
             * @return
             */
            int compareToOther(PrimitiveData<?> other);
        }
    }

    /**
     * StructData
     *
     * Shared interface of container interfaces of: - ObjectData (heterogeneous
     * members - no two members of the same ActiveType) - ArrayData (homogeneous
     * members - one or more members all the same ActiveType)
     *
     */
    interface StructData extends ActiveData, ActiveJson, ArrayHtml {

        List<ActiveData> nodes();

        ActiveData get(int index);
        // byte[] getBytes();
    }

    /**
     * ObjectData
     *
     * The entity container, collection of attributes, fields or members. A row
     * in a table.
     */
    interface ObjectData extends StructData, ActiveLink, ObjectDataExt, ObjectHtml {

        default ObjectType objectType() {
            return (ObjectType) this.activeType();
        }

        boolean isIncomplete();

        /**
         * The immutability of ObjectData requires replicating this instance
         * into new builder instance which can be updated and then new version
         * can be created by calling build()
         *
         * @return
         */
        ObjectData.Builder replicate() throws Exception;

        boolean appendChild(ObjectData childData);

        /**
         * The object data should be a member_type: 'object_type' with
         * bond_type: 'optional'
         */
        boolean addSibling(ObjectData siblingData);

        /**
         * The object data should be a member_type: 'array_type' with bond_type:
         * 'optional'
         * @return 
         */
        boolean addChild(ObjectData childData);

        /**
         *
         * @return
         */
        List<ActiveType> missingTypes();

        /**
         *
         * ObjectData's Key Generator
         *
         */
        interface KeyGenerator extends Mingleton {

            Integer generateKey(AppContext context, ValueData[] values);

            Integer generateKey(ObjectType targetType, ObjectData sourceData, ValueData[] values) throws Exception;

            Integer generateKey(ObjectData objectData);

            String keyAsString(Integer key);

            /**
             *
             * @param context
             * @return
             */
            static public KeyGenerator get(AppContext context) {
                return NodeProvider.provider().getMingleton(KeyGenerator.class, context);
            }
        }

        /**
         *
         * ObjectData's Builder
         *
         */
        interface Builder {

            ObjectType objectType();

            void add(ActiveData... fields);

            void add(ActiveType activeType, ActiveData activeData);

            ObjectData build() throws Exception;

            List<ActiveType> getMissingTypes() throws Exception;

            boolean isEmpty();
        }
    }

    interface AggregateData extends ObjectData {

        /**
         *
         * @param valueData
         * @return the value that @param valueData replaced
         */
        ValueData set(ValueData valueData);

        /**
         *
         * @param activeType
         * @return
         */
        AggregateData createObject(ActiveType activeType);
    }

    /**
     * ArrayData
     *
     * The entities container, different instances of the same entity type
     * (ObjectType). A table in a database.
     *
     * Used for current state tables.
     */
    interface ArrayData extends StructData, ActiveLink, ArrayHtml, ArrayDataExt {

        default ArrayType arrayType() {
            return (ArrayType) this.activeType();
        }

        ActiveType memberType();

        boolean add(ActiveData activeData);

        static ArrayData empty(String name) {
            try {
                return ArrayType.dynamic(AppContext.Common, name).create();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * This will contain all references across ObjectTypes for an entire
     * ActiveContext.
     *
     *
     */
    interface ActiveRef extends Contextual, Mingleton {

        public enum ReferenceType {
            PARENT(Role.Object, "Many-to-One -> Child should have members that match parent's key"),
            PARENT_AS_ATTRIBUTE(Role.Object, "One-to-One -> Parent should be a child of Child"),
            DESCENDANT(Role.Array, "One-to-Many -> Parent's keys are members of Child"),
            SIBLING(Role.Object, "One-to-One -> ShareKeys"),
            ATTRIBUTE(Role.Object, "One-to-One > Child's keys are members of Parent"),;

            Role memberRole;
            String description;

            ReferenceType(Role memberRole, String description) {
                this.memberRole = memberRole;
                this.description = description;
            }

            public String toString() {
                return String.format("%s -> %s", this.name(), this.description);
            }
        }

        /**
         *
         * For an ObjectType, this contains the defined references to the other
         * ObjectType
         *
         */
        interface References {

            ObjectType objectType();

            /**
             *
             * @return
             */
            ObjectType getParentType();

            boolean isParentAsAttribute();

            /**
             *
             * @return
             */
            ObjectType getAttributeOf();

            /**
             *
             * @param relationshipType
             * @return
             */
            List<ObjectType> get(ReferenceType relationshipType);

            /**
             *
             * @param objectType
             * @return
             */
            List<ReferenceType> get(ObjectType objectType);

            /**
             * Add a reference
             *
             * @param refType
             * @param objectType
             * @return boolean to inform caller whether or not this changed the
             * state of the object
             */
            boolean add(ReferenceType refType, ObjectType objectType);

            /**
             * Delete a reference
             *
             * @param refType
             * @param objectType
             * @return boolean to inform caller whether or not this changed the
             * state of the object
             */
            boolean del(ReferenceType refType, ObjectType objectType);
        }

        /**
         * Convenience Method
         *
         * @param objectType
         * @param relationshipType
         * @param memberType
         */
        void createReference(ObjectType objectType, ReferenceType relationshipType, ObjectType memberType);

        /**
         * Not fully tested
         */
        void autoCreateReferences();

        /**
         * Get the References for provided ObjectType
         *
         * @param objectType
         * @return
         */
        References get(ObjectType objectType);

    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * Conversions Class<T> -> Class<R>
	 * 
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    /**
     * Type Conversions
     *
     * Example: String to LocalDate
     *
     * @param <T>
     * @param <R>
     */
    interface TypeConversion<T, R> {

        String description();

        Function<T, R> function();

        Class<T> sourceClass();

        Class<R> targetClass();

        R apply(Object valueFrom);
    }

    /**
     * Enumeration of operators used for condition evaluation and predicates.
     * Includes the dimension of the right-hand side value.
     * <ol>
     * <li>UNIVAL - only one value will be on the right hand side of the
     * expression</li>
     * <li>BIVAL - there will always be two values on the right hand side of the
     * expression (low -> high)</li>
     * <li>MULTVAL - the values will be a list (array)</li>
     * </ol>
     *
     * These are the categories not the definitions. This list is not exhaustive
     * or closed.
     */
    public enum Operators {
        EQUALS(ActiveOperator.Dimension.UNIVAL),

        /**
         *
         */
        NOT_EQUALS(ActiveOperator.Dimension.UNIVAL), LESS_THAN(
                ActiveOperator.Dimension.UNIVAL), GREATER_THAN(ActiveOperator.Dimension.UNIVAL), LESS_THAN_OR_EQUALS(
                ActiveOperator.Dimension.UNIVAL), GREATER_THAN_OR_EQUALS(
                ActiveOperator.Dimension.UNIVAL), BETWEEN_NOT_EQUALS(
                ActiveOperator.Dimension.BIVAL), BETWEEN_OR_EQUALS(
                ActiveOperator.Dimension.BIVAL), EQUALS_IN(
                ActiveOperator.Dimension.MULTIVAL), NOT_EQUALS_IN(
                ActiveOperator.Dimension.MULTIVAL),;

        ActiveOperator.Dimension valdim;

        Operators(ActiveOperator.Dimension valdim) {
            this.valdim = valdim;
        }

        public ActiveOperator.Dimension dimension() {
            return this.valdim;
        }
    }

    /**
     * This is the interface or shell for the operators. Implementations should
     * be done in sets (enum). Not all operators have to be implemented, but
     * they should not have duplicated implementations within their set (enum).
     */
    interface ActiveOperator {

        public enum Dimension {
            UNIVAL, BIVAL, MULTIVAL;
        }

        Operators mnemonic();

        default String name() {
            return mnemonic().name();
        }

        Dimension dimension();

        /**
         * Operator Expression ("==", "!=", ">=", ...)
         */
        String opstr();

        /**
         * Value Separator (",", " ", ";", ...)
         */
        String valsep();

        /**
         * Value Ends - '(', ')'
         *
         * @return
         */
        String[] valends();

        /**
         * There may be multiple sets of ActiveOperator collections. This
         * provides a way to translate between them.
         *
         * @param others
         * @return
         */
        default ActiveOperator translate(ActiveOperator[] others) {
            for (ActiveOperator other : others) {
                if (this.mnemonic().equals(other.mnemonic())) {
                    return other;
                }
            }
            return null;
        }
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * Filters
	 * 
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    /**
     * Good Data Filter
     */
    static public final Predicate<ActiveData> GOOD_DATA_FILTER = ad -> ad != null && !ad.isNull() && !ad.isBlank();

    static public final Predicate<ValueData> SKIP_AUTO_TYPES = vd -> vd == null || !MetaTypes.AutoTypes.isAutoType(vd.valueType());

    /**
     * Node Traversal Example:
     *
     * <pre>
     *
     *  // declare before lambda definition
     *  List collector = new ArrayList();
     *
     *  // lambda definition
     *  Consumer<ActiveData> consumer = activeData -> {
     *  	ValueData valueData = (ValueData) activeData;
     *  	// Insert Your Code Here
     *  	collector.add(valueData);
     *  };
     *
     *  // call with starting node and lambda
     *  nodeTraversal(startNode, consumer);
     *
     * </pre>
     *
     *
     *
     * @param activeData
     * @param consumer
     */
    static public void nodeTraversal(ActiveData activeData, Consumer<ValueData> valueDataCallback, Consumer<ObjectData> objectDataCallback) {
        switch (activeData.role()) {
            case Value:
                if (valueDataCallback != null) {
                    valueDataCallback.accept((ValueData) activeData);
                }
                break;
            case Object:
                for (ActiveData childNode : ((ObjectData) activeData).nodes()) {
                    if (childNode.role().equals(Role.Object)) {
                        if (objectDataCallback != null) {
                            objectDataCallback.accept((ObjectData) childNode);
                        }
                    }
                    nodeTraversal(childNode, valueDataCallback, objectDataCallback);
                }
                break;
            case Array:
                for (ActiveData childNode : ((ArrayData) activeData).nodes()) {
                    nodeTraversal(childNode, valueDataCallback, objectDataCallback);
                }
                break;
            default:
                break;
        }
    }
}
