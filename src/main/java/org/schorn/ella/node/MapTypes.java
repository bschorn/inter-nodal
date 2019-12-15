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

import java.util.Arrays;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.node.MetaTypes.FieldTypes;
import org.schorn.ella.node.MetaTypes.MetaRole;
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
@Deprecated
public class MapTypes {

    static final Logger LGR = LoggerFactory.getLogger(MapTypes.class);

    /**
     * Initialize
     */
    static public void initialize() {

        ValueTypes.values();
        for (MetaType type : ObjectTypes.values()) {
            try {
                type.register();
            } catch (Exception ex) {
                LGR.error(type.toString() + ": " + Functions.getStackTraceAsString(ex));
            }
        }
        for (MetaType type : Arrayz.values()) {
            try {
                type.register();
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
            }
        }
    }

    /**
     *
     * MetaType
     *
     */
    public interface MetaType {

        MetaTypes.MetaRole role();

        String name();

        default void register() throws Exception {
        }
    }

    /**
     *
     * Values
     *
     */
    public enum ValueTypes implements MetaType {
        source_meta(FieldTypes.TEXT),
        target_meta(FieldTypes.TEXT),
        map_type(FieldTypes.ENUM),
        map_value(FieldTypes.TEXT),
        map_values(FieldTypes.TEXT),
        map_target(FieldTypes.TEXT),
        map_lookup(FieldTypes.TEXT),
        map_rules(FieldTypes.TEXT),;

        ValueType valueType;

        ValueTypes(FieldTypes metaField) {
            try {
                FieldType ftype = metaField.fieldType();
                valueType = ValueType.get(AppContext.Common, this.name());
                if (valueType == null) {
                    valueType = ValueType.create(AppContext.Common, this.name(), ftype);
                    //LGR.info(String.format("ValueType: %s", valueType));
                }
            } catch (Exception e) {
                LGR.error(Functions.getStackTraceAsString(e));
            }
        }

        /**
         *
         * @return
         */
        @Override
        public MetaRole role() {
            return MetaRole.Value;
        }

        /**
         *
         * @return
         */
        public ValueType valueType() {
            return this.valueType;
        }
    }

    /**
     *
     * Value Arrays
     *
     */
    public enum ArrayValueTypes implements MetaType {
        map_values(ValueTypes.map_values),
        map_lookup(ValueTypes.map_lookup),
        map_rules(ValueTypes.map_rules),;

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
        field_map(ValueTypes.map_type, ValueTypes.map_value, ArrayValueTypes.map_values, ValueTypes.map_target, ArrayValueTypes.map_rules, ArrayValueTypes.map_lookup),
        meta_link(ValueTypes.source_meta, ValueTypes.target_meta, Arrayz.field_map);

        ObjectType objectType;
        ArrayType arrayType;
        MetaType[] metaTypes;

        ObjectTypes(MetaType... metaTypes) {
            this.metaTypes = metaTypes;
        }

        @Override
        public MetaRole role() {
            return MetaRole.Object;
        }

        @Override
        public void register() throws Exception {
            if (this.metaTypes != null) {
                ObjectType.Builder builder = ObjectType.builder(AppContext.Common, this.name(),
                        Arrays.asList(new ActiveNode.TypeAttribute[]{TypeAttributes.DomainType.Mapping}));
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
    public enum Arrayz implements MetaType {
        field_map(ObjectTypes.field_map),;

        MetaType metaType;
        ArrayType arrayType;

        Arrayz(MetaType metaType) {
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
