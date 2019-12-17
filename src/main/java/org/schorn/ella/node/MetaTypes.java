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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.DataType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the meta-data's meta-data. This bootstraps the node types used for
 * reading the meta files.
 *
 *
 * @author schorn
 *
 */
public class MetaTypes {

    static final Logger LGR = LoggerFactory.getLogger(MetaTypes.class);

    //static final OData BOND_TYPE_CONSTRAINTS;
    /**
     * Initialize
     */
    static public void initialize() {

        /*
		AData list = ArrayType.create(NodeContext.Common,"list").create();
		BOND_TYPE_CONSTRAINTS.add(list);
		for (NodeBond bondType : NodeBond.values()) {
			list.add(NodeValue.create(NodeContext.Common,"list", bondType.name()));
		}
         */
        FieldTypes.values();
        ValueTypes.values();
        for (MetaType type : ObjectTypes.values()) {
            try {
                type.register();
            } catch (Exception ex) {
                LGR.error(type.toString() + ": " + Functions.getStackTraceAsString(ex));
            }
        }
        for (MetaType type : Arrays.values()) {
            try {
                type.register();
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
            }
        }
    }

    /**
     * Role
     *
     */
    enum MetaRole {
        Primitive,
        Field,
        Value,
        Object,
        Array,;
    }

    /**
     *
     * MetaType
     *
     */
    public interface MetaType {

        MetaRole role();

        String name();

        default void register() throws Exception {
        }
    }

    /**
     *
     *
     *
     */
    public enum DataTypes implements MetaType {
        TEXT(Primitive.Ptext.class, ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.pattern.name())),
        ENUM(Primitive.Ptext.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.list.name())),
        NUMBER(Primitive.Pinteger.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.min_integer.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.max_integer.name())),
        DECIMAL(Primitive.Pdecimal.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.min_decimal.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.max_decimal.name())),
        DATE(Primitive.Pdate.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.min_date.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.max_date.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.day_of_week.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.holidays.name())),
        TIMESTAMP(Primitive.Ptimestamp.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.min_datetime.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.max_datetime.name())),
        TIME(Primitive.Ptime.class,
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.min_time.name()),
                ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.max_time.name())),
        BOOL(Primitive.Pbool.class),
        UUID(Primitive.Puuid.class, ActiveNode.Constraints.ConstraintType.get(ActiveNode.Constraints.ConstraintType.StandardTypes.pattern.name())),
        DAY_OF_WEEK(Primitive.Pdayofweek.class),
        UNKNOWN(null);

        @SuppressWarnings("rawtypes")
        Class<? extends PrimitiveType> ptypeClass;
        DataType dtype;

        @SuppressWarnings("rawtypes")
        DataTypes(Class<? extends PrimitiveType> ptypeClass, ActiveNode.Constraints.ConstraintType... constraintTypes) {
            this.ptypeClass = ptypeClass;
            if (this.ptypeClass == null) {
                return;
            }
            try {
                this.dtype = DataType.create(AppContext.Common, this.name(), this.ptypeClass.newInstance(), constraintTypes);
                if (dtype != null) {
                    //LGR.info(String.format("DataType: %s", dtype.toString()));
                }
            } catch (Exception e) {
                LGR.error(String.format("%s.ctor() - %s: Exception: %s", DataTypes.class.getSimpleName(),
                        this.ptypeClass.getSimpleName(), Functions.getStackTraceAsString(e)));
            }
        }

        @Override
        public MetaRole role() {
            return MetaRole.Value;
        }

        public DataType dataType() {
            return this.dtype;
        }

        static public DataTypes valueOf(DataType dataType) {
            for (DataTypes mdt : DataTypes.values()) {
                if (mdt.dataType().equals(dataType)) {
                    return mdt;
                }
            }
            return DataTypes.UNKNOWN;
        }
    }

    /**
     *
     * Fields
     *
     */
    public enum FieldTypes implements MetaType {
        TEXT(DataTypes.TEXT),
        ENUM(DataTypes.ENUM),
        NUMBER(DataTypes.NUMBER),
        DECIMAL(DataTypes.DECIMAL),
        DATE(DataTypes.DATE),
        DAY_OF_WEEK(DataTypes.DAY_OF_WEEK),
        IDENTITY_TYPE(DataTypes.ENUM),
        TIMESTAMP(DataTypes.TIMESTAMP),
        TIME(DataTypes.TIME),
        BOND_TYPE(DataTypes.TEXT), //BOND_TYPE_CONSTRAINTS)
        UUID(DataTypes.UUID),
        IDATA_USER(DataTypes.TEXT),

        /**
         *
         */
        IDATA_ID(DataTypes.NUMBER),
        IDATA_UUID(DataTypes.UUID),
        IDATA_TS(DataTypes.TIMESTAMP),
        IDATA_KEY(DataTypes.TEXT),
        IDATA_VER(DataTypes.NUMBER),;

        FieldType fieldType;

        FieldTypes(DataTypes metaPrimitives) {
            try {
                ActiveNode.Constraints.Builder builder = ActiveNode.Constraints.builder(metaPrimitives.dataType().primitiveType().dataGroup());

                ActiveNode.Constraints constraints = builder.build();
                this.fieldType = FieldType.create(AppContext.Common, this.name(), metaPrimitives.dataType(), constraints, null);
                //LGR.info("{}.ctor() - {} FieldType: {}", FieldTypes.class.getSimpleName(), this.name(), this.fieldType.toString());
            } catch (Exception e) {
                LGR.error("{}.ctor() - {}: Exception: {}", FieldTypes.class.getSimpleName(),
                        this.name(), Functions.getStackTraceAsString(e));
            }
        }

        public FieldType fieldType() {
            return this.fieldType;
        }

        @Override
        public MetaRole role() {
            return MetaRole.Field;
        }

    }

    /**
     *
     * Values
     *
     */
    public enum ValueTypes implements MetaType {
        context(FieldTypes.TEXT),
        name(FieldTypes.TEXT),
        domain_type(FieldTypes.ENUM),
        object_category(FieldTypes.ENUM),
        object_level(FieldTypes.ENUM),
        object_purpose(FieldTypes.ENUM),
        value_type(FieldTypes.TEXT),
        object_type(FieldTypes.TEXT),
        array_type(FieldTypes.TEXT),
        base_types(FieldTypes.TEXT),
        skey(FieldTypes.TEXT),
        fkey(FieldTypes.TEXT),
        key(FieldTypes.TEXT),
        one(FieldTypes.TEXT),
        many(FieldTypes.TEXT),
        meta_owner(FieldTypes.TEXT),
        immutable(FieldTypes.TEXT),
        mutable(FieldTypes.TEXT),
        optional(FieldTypes.TEXT),
        data_type(FieldTypes.TEXT),
        field_type(FieldTypes.TEXT),
        list(FieldTypes.ENUM),
        bond_type(FieldTypes.BOND_TYPE),
        min_integer(FieldTypes.NUMBER),
        max_integer(FieldTypes.NUMBER),
        min_decimal(FieldTypes.DECIMAL),
        max_decimal(FieldTypes.DECIMAL),
        pattern(FieldTypes.TEXT),
        min_date(FieldTypes.DATE),
        max_date(FieldTypes.DATE),
        min_datetime(FieldTypes.TIMESTAMP),
        max_datetime(FieldTypes.TIMESTAMP),
        min_time(FieldTypes.TIME),
        max_time(FieldTypes.TIME),
        precision(FieldTypes.DECIMAL),
        day_of_week(FieldTypes.DAY_OF_WEEK),
        holidays(FieldTypes.TIMESTAMP),
        data_type_name(FieldTypes.TEXT),
        data_group(FieldTypes.TEXT),
        data_class(FieldTypes.TEXT),
        exception_message(FieldTypes.TEXT),
        exception_stack_trace(FieldTypes.TEXT),
        parent_class(FieldTypes.TEXT),
        type_class(FieldTypes.TEXT),
        value_class(FieldTypes.TEXT),
        idata_idtype(FieldTypes.IDENTITY_TYPE),
        idata_user(FieldTypes.IDATA_USER),
        idata_id(FieldTypes.IDATA_ID),
        idata_uuid(FieldTypes.IDATA_UUID),
        idata_cts(FieldTypes.IDATA_TS),
        idata_mts(FieldTypes.IDATA_TS),
        idata_ats(FieldTypes.IDATA_TS),
        idata_ts(FieldTypes.IDATA_TS),
        idata_ver(FieldTypes.IDATA_VER),
        package_name(FieldTypes.TEXT),
        class_name(FieldTypes.TEXT),
        method_name(FieldTypes.TEXT),
        active_role(FieldTypes.TEXT),
        active_type(FieldTypes.TEXT),
        active_data(FieldTypes.TEXT),
        error_ts(FieldTypes.TIMESTAMP),
        error_message(FieldTypes.TEXT),
        parent_data(FieldTypes.TEXT),
        child_data(FieldTypes.TEXT),
        count(FieldTypes.NUMBER),
        bytes(FieldTypes.NUMBER),
        null_bytes(FieldTypes.NUMBER),;

        ValueType valueType;

        ValueTypes(FieldTypes metaField) {
            try {
                FieldType ftype = metaField.fieldType();
                valueType = ValueType.get(AppContext.Common, this.name());
                if (valueType == null) {
                    valueType = ValueType.create(AppContext.Common, this.name(), ftype);
                }
            } catch (Exception e) {
                LGR.error(Functions.getStackTraceAsString(e));
            }
        }

        @Override
        public MetaRole role() {
            return MetaRole.Value;
        }

        public ValueType valueType() {
            return this.valueType;
        }
    }

    /**
     *
     * AutoTypes are ValueTypes that are automatically added to every ObjectType
     * definition for the purpose of maintaining ObjectData integrity.
     *
     */
    public enum AutoTypes implements MetaType {

        okey(FieldTypes.IDATA_KEY),
        over(FieldTypes.IDATA_VER),
        octs(FieldTypes.IDATA_TS),;

        ValueType valueType;

        AutoTypes(FieldTypes metaField) {
            try {
                FieldType ftype = metaField.fieldType();
                valueType = ValueType.get(AppContext.Common, this.name());
                if (valueType == null) {
                    valueType = ValueType.create(AppContext.Common, this.name(), ftype);
                }
            } catch (Exception e) {
                LGR.error(Functions.getStackTraceAsString(e));
            }
        }

        @Override
        public MetaRole role() {
            return MetaRole.Value;
        }

        public ValueType valueType() {
            return this.valueType;
        }

        /**
         * Check if a ValueType is an AutoType
         *
         * @param valueType
         * @return
         */
        static public boolean isAutoType(ValueType valueType) {
            for (AutoTypes autoType : AutoTypes.values()) {
                if (valueType != null && valueType.equals(autoType.valueType)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *
     * Value Arrays
     *
     */
    public enum ArrayValueTypes implements MetaType {
        day_of_week(ValueTypes.day_of_week),
        holidays(ValueTypes.holidays),
        list(ValueTypes.list),
        base_types(ValueTypes.base_types);

        MetaType metaType;
        ArrayType arrayType;

        ArrayValueTypes(MetaType metaType) {
            this.metaType = metaType;
            if (this.metaType.role() == MetaRole.Value) {
                this.arrayType = ArrayType.get(AppContext.Common, this.name());
                if (this.arrayType == null) {
                    ValueType vtype = ValueType.get(AppContext.Common, this.metaType.name());
                    if (vtype == null) {
                        LGR.error(String.format("MetaArrays() - there is no ValueType of '%s' defined", this.name()));
                    } else {
                        try {
                            this.arrayType = ArrayType.create(AppContext.Common, vtype.name(), vtype, BondType.OPTIONAL);
                            if (this.arrayType != null) {
                                //LGR.info(String.format("ArrayType:\n%s", this.arrayType));
                            }
                        } catch (Exception e) {
                            LGR.error(Functions.getStackTraceAsString(e));
                        }
                    }
                }
            }

        }

        /**
         *
         * @return
         */
        @Override
        public MetaRole role() {
            return MetaRole.Array;
        }

        public ArrayType arrayType() {
            return this.arrayType;
        }
    }

    /**
     *
     * Objects
     *
     */
    public enum ObjectTypes implements MetaType {
        //IData(MetaValues.idata_uuid,MetaValues.idata_cts,MetaValues.idata_mts,MetaValues.idata_ats),
        attributes(ValueTypes.domain_type, ValueTypes.object_category, ValueTypes.object_purpose, ValueTypes.object_level),
        member_types(ValueTypes.value_type, ValueTypes.object_type, ValueTypes.array_type, ValueTypes.bond_type, ValueTypes.meta_owner),
        constraints(ValueTypes.min_integer, ValueTypes.max_integer, ValueTypes.min_decimal, ValueTypes.max_decimal, ValueTypes.min_date, ValueTypes.max_date, ValueTypes.min_datetime, ValueTypes.max_datetime,
                ValueTypes.min_time, ValueTypes.max_time, ValueTypes.pattern, ArrayValueTypes.day_of_week, ArrayValueTypes.holidays, ArrayValueTypes.list),
        data_types(ValueTypes.data_type_name, ValueTypes.data_group, ValueTypes.data_class, ValueTypes.parent_class, ValueTypes.type_class, ValueTypes.value_class),
        field_types(ValueTypes.name, ValueTypes.data_type, ObjectTypes.constraints, ValueTypes.meta_owner),
        value_types(ValueTypes.name, ValueTypes.field_type, ValueTypes.meta_owner),
        object_types(ValueTypes.name, ObjectTypes.attributes, ArrayValueTypes.base_types, Arrays.member_types),
        array_types(ValueTypes.name, Arrays.member_types),
        meta(ValueTypes.context, Arrays.data_types, Arrays.field_types, Arrays.value_types, Arrays.array_types, Arrays.object_types),
        error_data(ValueTypes.context, ValueTypes.active_role, ValueTypes.active_type, ValueTypes.error_ts, ValueTypes.class_name, ValueTypes.method_name, ValueTypes.error_message, ValueTypes.parent_data, ValueTypes.child_data),
        error_report(ValueTypes.package_name, ValueTypes.class_name, ValueTypes.method_name, ValueTypes.error_message, ValueTypes.error_ts, ValueTypes.exception_message, ValueTypes.exception_stack_trace, ValueTypes.context, ValueTypes.active_type, ValueTypes.active_data),
        repo_stats(ValueTypes.object_type, ValueTypes.count, ValueTypes.bytes, ValueTypes.null_bytes, ValueTypes.min_datetime, ValueTypes.max_datetime),
        identity_type(ValueTypes.idata_idtype, ValueTypes.idata_user, ValueTypes.idata_id, ValueTypes.idata_uuid, ValueTypes.idata_cts);

        ObjectType objectType;
        ArrayType arrayType;
        MetaType[] metaTypes;

        ObjectTypes(MetaType... metaTypes) {
            this.metaTypes = metaTypes;
        }

        /**
         *
         * @return
         */
        @Override
        public MetaRole role() {
            return MetaRole.Object;
        }

        @Override
        public void register() throws Exception {
            if (this.metaTypes != null) {
                ObjectType.Builder builder = ObjectType.builder(AppContext.Common, this.name(),
                        java.util.Arrays.asList(new ActiveNode.TypeAttribute[]{TypeAttributes.DomainType.Meta}));
                for (MetaType metaType : this.metaTypes) {
                    ActiveType activeType;
                    switch (metaType.role()) {
                        case Value:
                            activeType = ValueType.get(AppContext.Common, metaType.name());
                            break;
                        case Object:
                            activeType = ObjectType.get(AppContext.Common, metaType.name());
                            break;
                        case Array:
                            activeType = ArrayType.get(AppContext.Common, metaType.name());
                            break;
                        default:
                            throw new Exception(String.format("%s.register() - memberName '%s' was not found.",
                                    ObjectTypes.class.getSimpleName(), metaType.name()));
                    }
                    if (activeType == null) {
                        LGR.error("{}.register() - {}.{} type was not found in type library",
                                this.getClass().getSimpleName(), metaType.role().name(),
                                metaType.name());
                    } else {
                        builder.add(activeType, BondType.OPTIONAL);
                    }
                }
                this.objectType = builder.build();
                //LGR.info(String.format("ObjectType:\n%s", this.objectType.toString()));
                this.arrayType = ArrayType.create(this.objectType.context(), this.objectType.name(), this.objectType, BondType.OPTIONAL);
                //LGR.info(String.format("ArrayType:\n%s", this.arrayType.toString()));
            }
        }

        public ObjectType objectType() {
            return this.objectType;
        }
    }

    /**
     *
     * Arrays
     *
     */
    public enum Arrays implements MetaType {
        data_types(ObjectTypes.data_types),
        field_types(ObjectTypes.field_types),
        value_types(ObjectTypes.value_types),
        object_types(ObjectTypes.object_types),
        array_types(ObjectTypes.array_types),
        member_types(ObjectTypes.member_types),
        error_data(ObjectTypes.error_data),
        repo_stats(ObjectTypes.repo_stats),;

        MetaType metaType;
        ArrayType arrayType;

        Arrays(MetaType metaType) {
            this.metaType = metaType;
        }

        /**
         *
         * @return
         */
        @Override
        public MetaRole role() {
            return MetaRole.Array;
        }

        @Override
        public void register() throws Exception {
            this.arrayType = ArrayType.get(AppContext.Common, this.name());
            if (this.arrayType == null) {
                if (this.metaType != null) {
                    switch (this.metaType.role()) {
                        case Value:
                            ValueType vtype = ValueType.get(AppContext.Common, this.metaType.name());
                            if (vtype == null) {
                                throw new Exception(String.format("MetaArrays() - there is no ValueType of '%s' defined", this.name()));
                            }
                            this.arrayType = ArrayType.create(AppContext.Common, vtype.name(), vtype, BondType.OPTIONAL);
                            break;
                        case Object:
                            ObjectType otype = ObjectType.get(AppContext.Common, this.metaType.name());
                            if (otype == null) {
                                throw new Exception(String.format("MetaArrays() - there is no ValueType of '%s' defined", this.name()));
                            }
                            this.arrayType = ArrayType.create(AppContext.Common, otype.name(), otype, BondType.OPTIONAL);
                            break;
                        case Array:
                            ArrayType atype = ArrayType.get(AppContext.Common, this.metaType.name());
                            if (atype == null) {
                                throw new Exception(String.format("MetaArrays() - there is no ValueType of '%s' defined", this.name()));
                            }
                            this.arrayType = ArrayType.create(AppContext.Common, atype.name(), atype, BondType.OPTIONAL);
                            break;
                        default:
                            break;
                    }
                }
                //LGR.info(String.format("ArrayType:\n%s", this.arrayType));
            }
        }

        public ArrayType arrayType() {
            return this.arrayType;
        }
    }
}
