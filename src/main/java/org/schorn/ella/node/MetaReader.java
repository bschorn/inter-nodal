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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.DomainType;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectLevel;
import org.schorn.ella.node.ActiveNode.ObjectRole;
import org.schorn.ella.node.ActiveNode.ObjectSubRole;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.DataType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MetaReader
 * <p>
 * Reads meta-data (from JSON or directly from a file containing JSON) and
 * provides the ActiveNode types based on the meta-data.
 *
 * <code>
 * {
 *  "meta": {
 *      "context": "--your-context--",
 *      "field_types": [
 *      ],
 *      "value_types": [
 *      ],
 *      "array_types": [
 *      ],
 *      "object_types": [
 *      ]
 * }
 * </code>
 *
 * @author schorn
 *
 */
public class MetaReader extends AbstractContextual {

    static private final Logger LGR = LoggerFactory.getLogger(MetaReader.class);

    static final String META_TAG = "meta";
    static Map<AppContext, MetaReader> READERS = new HashMap<>();

    static public MetaReader get(AppContext context) {
        return READERS.get(context);
    }

    private final ObjectData metaData;
    private final ArrayData fieldTypes;
    private final ArrayData valueTypes;
    private final ArrayData objectTypes;
    private final ArrayData arrayTypes;

    private final List<String> fieldTypeNames;
    private final List<String> valueTypeNames;
    private final List<String> objectTypeNames;
    private final List<String> arrayTypeNames;

    protected MetaReader(AppContext context, ObjectData metaData) {
        super(context);
        READERS.put(context, this);
        this.metaData = metaData;
        this.arrayTypes = this.metaData.findFirst(ArrayType.get(AppContext.Common, ArrayType.TYPE_TAG));
        this.objectTypes = this.metaData.findFirst(ArrayType.get(AppContext.Common, ObjectType.TYPE_TAG));
        this.valueTypes = this.metaData.findFirst(ArrayType.get(AppContext.Common, ValueType.TYPE_TAG));
        this.fieldTypes = this.metaData.findFirst(ArrayType.get(AppContext.Common, FieldType.TYPE_TAG));

        this.fieldTypeNames = new ArrayList<>(100);
        this.valueTypeNames = new ArrayList<>(100);
        this.objectTypeNames = new ArrayList<>(15);
        this.arrayTypeNames = new ArrayList<>(20);
    }

    /**
     *
     */
    public interface MetaSupplier extends Supplier<String> {

        public String get();
    }

    /**
     *
     */
    static public class FileMetaSupplier implements MetaSupplier {

        private Path filePath;

        public FileMetaSupplier(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public String get() {
            try {
                String jsonString = new String(Files.readAllBytes(this.filePath));
                LGR.info(String.format("%s.createFromFile(\"%s\")",
                        MetaReader.class.getSimpleName(), this.filePath.toString()));
                return jsonString;

            } catch (Exception e) {
                LGR.error(Functions.getStackTraceAsString(e));
            }
            return null;
        }

    }
    /**
     *
     */
    static public class StringMetaSupplier implements MetaSupplier {

        private final String jsonString;

        public StringMetaSupplier(String jsonString) {
            this.jsonString = jsonString;
        }

        @Override
        public String get() {
            return jsonString;
        }

    }

    /**
     *
     * @param filePath
     * @return
     */
    static public MetaReader createFromFile(Path filePath) {
        try {
            FileMetaSupplier metaSupplier = new FileMetaSupplier(filePath);
            LGR.info(String.format("%s.createFromFile(\"%s\")",
                    MetaReader.class.getSimpleName(), filePath.toString()));
            return MetaReader.create(metaSupplier);

        } catch (Exception e) {
            LGR.error(Functions.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     *
     * @param jsonMetaData
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    static public MetaReader create(MetaSupplier metaSupplier) throws Exception {
        //Transform transformer2 = TransformProvider.provider().getTransform(AppContext.Common, Format.JSON, Format.OpenNode);
        //OpenNode openNode = (OpenNode) transformer2.apply(jsonMetaData);
        Transform transformer = TransformProvider.provider().getTransform(AppContext.Common, Format.JSON, Format.ActiveNode);
        if (transformer == null) {
            throw new Exception(String.format("%s.load() - there was no JSON to ActiveNode transformer found.",
                    MetaReader.class.getSimpleName()));
        }
        @SuppressWarnings("unchecked")
        StructData structData = (StructData) transformer.apply(metaSupplier.get());
        if (structData == null) {
            transformer.throwException();
            throw new Exception(String.format("%s.load() - json converted to null",
                    MetaReader.class.getSimpleName()));
        }
        ObjectType metaType = ObjectType.get(AppContext.Common, META_TAG);
        if (metaType == null) {
            throw new Exception(String.format("%s.load() - the object_type '" + META_TAG + "' does not exist.",
                    MetaReader.class.getSimpleName()));
        }
        ObjectData metaObj = structData.findFirst(metaType);
        if (metaObj == null) {
            throw new Exception(String.format("%s.load() - the node with object_type '" + META_TAG + "' was not found in the data object.",
                    MetaReader.class.getSimpleName()));
        }
        ActiveData activeData = metaObj.get(ActiveNode.ValueType.get(metaObj.context(), "context"));
        String strContext = activeData.activeValue().toString();
        if (strContext == null) {
            throw new Exception(String.format("%s.load() - the node with value_type 'context' was not found in the data object.",
                    MetaReader.class.getSimpleName()));
        }
        AppContext context = AppContext.create(strContext);
        //LGR.debug("{}.load() - context_name: {}, metaObj.toString():\n{}", MetaReader.class.getSimpleName(), context.getName(), metaObj.toString());
        //LGR.debug("{}.load() - context_name: {}, metaObj.asJsonString():\n{}", MetaReader.class.getSimpleName(), context.getName(), metaObj.asJsonString());
        //System.out.println(metaObj.asJsonString());
        return new MetaReader(context, metaObj);
    }

    /**
     *
     */
    public void register() {

        /*
	 * Get List of FieldType (names)
         */
        for (ActiveData activeData : this.fieldTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            if (objectData != null) {
                ValueData valueData = objectData.get(ValueType.get(objectData.context(), "name"));
                if (valueData != null && valueData.activeValue() != null) {
                    String name = valueData.activeValue().toString();
                    if (!this.fieldTypeNames.contains(name)) {
                        this.fieldTypeNames.add(name);
                    }
                }
            }
        }
        /*
	 * Get List of ValueType (names)
         */
        for (ActiveData activeData : this.valueTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            if (objectData != null) {
                ValueData valueData = objectData.get(ValueType.get(objectData.context(), "name"));
                if (valueData != null && valueData.activeValue() != null) {
                    String name = valueData.activeValue().toString();
                    if (!this.valueTypeNames.contains(name)) {
                        this.valueTypeNames.add(name);
                    }
                }
            }
        }
        /*
	 * Get List of ObjectType (names)
         */
        for (ActiveData activeData : this.objectTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            if (objectData != null) {
                ValueData valueData = objectData.get(ValueType.get(objectData.context(), "name"));
                if (valueData != null && valueData.activeValue() != null) {
                    String name = valueData.activeValue().toString();
                    if (!this.objectTypeNames.contains(name)) {
                        this.objectTypeNames.add(name);
                    }
                }
            }
        }
        /*
	 * Get List of ArrayType (names)
         */
        for (ActiveData activeData : this.arrayTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            if (objectData != null) {
                ValueData valueData = objectData.get(ValueType.get(objectData.context(), "name"));
                if (valueData != null && valueData.activeValue() != null) {
                    String name = valueData.activeValue().toString();
                    if (!this.arrayTypeNames.contains(name)) {
                        this.arrayTypeNames.add(name);
                    }
                }
            }
        }

        /*
	 * Create FieldTypes
         */
        Map<String, Exception> badFieldTypes = new HashMap<>();
        for (String fieldTypeName : this.fieldTypeNames) {
            try {
                this.findOrCreateType(FieldType.class, fieldTypeName);
            } catch (Exception ex) {
                badFieldTypes.put(fieldTypeName, ex);
            }
        }
        /*
	 * Create ValueTypes
         */
        Map<String, Exception> badValueTypes = new HashMap<>();
        for (String valueTypeName : this.valueTypeNames) {
            try {
                this.findOrCreateType(ValueType.class, valueTypeName);
            } catch (Exception ex) {
                badValueTypes.put(valueTypeName, ex);
            }
        }
        /*
	 * Create ObjectTypes
         */
        Map<String, Exception> badObjectTypes = new HashMap<>();
        for (String objectTypeName : this.objectTypeNames) {
            try {
                this.findOrCreateType(ObjectType.class, objectTypeName);
            } catch (Exception ex) {
                badObjectTypes.put(objectTypeName, ex);
            }
        }
        Map<String, Exception> badArrayTypes = new HashMap<>();
        for (String arrayTypeName : this.arrayTypeNames) {
            try {
                this.findOrCreateType(ArrayType.class, arrayTypeName);
            } catch (Exception ex) {
                badObjectTypes.put(arrayTypeName, ex);
            }
        }
        for (String name : badFieldTypes.keySet()) {
            Exception ex = badFieldTypes.get(name);
            LGR.error(String.format("%s.register() - %s -> FieldType: %s - failed: %s",
                    this.getClass().getSimpleName(),
                    this.context().name(),
                    name,
                    ex.getMessage()));
        }
        for (String name : badValueTypes.keySet()) {
            Exception ex = badValueTypes.get(name);
            LGR.error(String.format("%s.register() - %s -> ValueType: %s - failed: %s",
                    this.getClass().getSimpleName(),
                    this.context().name(),
                    name,
                    ex.getMessage()));
        }
        for (String name : badObjectTypes.keySet()) {
            Exception ex = badObjectTypes.get(name);
            LGR.error(String.format("%s.register() - %s -> ObjectType: %s - failed: %s",
                    this.getClass().getSimpleName(),
                    this.context().name(),
                    name,
                    ex.getMessage()));
        }
        for (String name : badArrayTypes.keySet()) {
            Exception ex = badArrayTypes.get(name);
            LGR.error(String.format("%s.register() - %s -> ArrayType: %s - failed: %s",
                    this.getClass().getSimpleName(),
                    this.context().name(),
                    name,
                    ex.getMessage()));
        }
    }

    /**
     *
     * @param <T>
     * @param classForT
     * @param name
     * @return
     * @throws java.lang.Exception
     */
    @SuppressWarnings("unchecked")
    public <T extends ActiveNode> T findOrCreateType(Class<T> classForT, String name) throws Exception {
        if (name == null) {
            throw new Exception(String.format("%s.getType() - call for %s provided a null value for name",
                    this.getClass().getSimpleName(), classForT.getSimpleName()));
        }
        if (classForT.equals(ArrayType.class)) {
            return (T) getOrCreateArrayType(name);
        } else if (classForT.equals(ObjectType.class)) {
            return (T) getOrCreateObjectType(name);
        } else if (classForT.equals(ValueType.class)) {
            return (T) getOrCreateValueType(name);
        } else if (classForT.equals(FieldType.class)) {
            return (T) getOrCreateFieldType(name);
        } else if (classForT.equals(DataType.class)) {
            return (T) DataType.get(this.context(), name);
        }
        throw new Exception(String.format("%s.getType() - type missing or misconfigured (%s) '%s'",
                this.getClass().getSimpleName(),
                classForT.getSimpleName(),
                name));
    }

    /**
     *
     * @param name
     * @return
     */
    private ArrayType getOrCreateArrayType(String name) throws Exception {
        ArrayType arrayType = ArrayType.get(this.context(), name);
        if (arrayType == null) {
            arrayType = createArrayType(name);
        }
        return arrayType;
    }

    /**
     *
     * @param name
     * @return
     */
    private ObjectType getOrCreateObjectType(String name) throws Exception {
        ObjectType objectType = ObjectType.get(this.context(), name);
        if (objectType == null) {
            objectType = createObjectType0(name);
        }
        return objectType;
    }

    /**
     *
     * @param name
     * @return
     */
    private ValueType getOrCreateValueType(String name) throws Exception {
        ValueType valueType = ValueType.get(this.context(), name);
        if (valueType == null) {
            valueType = createValueType0(name);
        }
        return valueType;
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    private FieldType getOrCreateFieldType(String name) throws Exception {
        FieldType fieldType = FieldType.get(this.context(), name);
        if (fieldType == null) {
            fieldType = createFieldType(name);
        }
        return fieldType;
    }

    /**
     * {
     *  "array_types": [
     *      {
     *          "name": "--name-of-type--",
     *          "value_type": "--from_value_types--",
     *          "bond_type": "--from_bond_types--"
     *      }
     *  ]
     * }
     *
     * 
     * 
     * @param name
     * @return
     */
    private ArrayType createArrayType(String name) throws Exception {
        if (!this.arrayTypeNames.contains(name)) {
            return null;
        }
        for (ActiveData activeData : this.arrayTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            ValueData valueData = objectData.get(ValueType.get(AppContext.Common, "name"));
            if (valueData.activeValue().toString().equals(name)) {
                ValueData vdName = (ValueData) objectData.get(MetaTypes.ValueTypes.name.valueType());
                ArrayType arrayType = null;
                ArrayData adMemberTypes = (ArrayData) objectData.get(MetaTypes.Arrays.member_types.arrayType());
                for (ActiveData adata : adMemberTypes.nodes()) {
                    if (activeData instanceof ObjectData) {
                        ObjectData odMemberType = (ObjectData) adata;
                        Role memberRole = MetaSupport.getRoleFromMemberTypes(odMemberType);
                        String memberName = MetaSupport.getMemberTypeName(odMemberType);
                        BondType bondType = MetaSupport.getMemberTypeBondType(odMemberType);
                        AppContext memberContext = MetaSupport.getMemberContext(odMemberType, this.context());
                        ActiveType memberType = MetaSupport.findType(memberName, memberRole, memberContext);
                        if (arrayType != null) {
                            throw new Exception(String.format(
                                    "%s.createArrayType() - Context: %s ArrayType: %s has more than one member_types entry.",
                                    this.getClass().getSimpleName(),
                                    this.context().name(),
                                    name));
                        }
                        arrayType = ArrayType.create(this.context(), vdName.activeValue().toString(), memberType, bondType);
                    }
                }
                //if (arrayType != null) {
                //LGR.info("Added {}: {}", arrayType.getClass().getSimpleName(), arrayType.toString());
                //}
                return arrayType;
            }
        }
        throw new Exception(String.format("%s.createArrayType() - there was no ArrayType: %s in Context: %s",
                this.getClass().getSimpleName(),
                name,
                this.context().name()));

    }

    /**
     *
     * @param object_type
     * @return
     */
    private ObjectType createObjectType0(String object_type) throws Exception {
        if (!this.objectTypeNames.contains(object_type)) {
            return null;
        }
        for (ActiveData activeData : this.objectTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            ValueData valueData = objectData.get(ValueType.get(AppContext.Common, "name"));
            if (valueData.activeValue().toString().equals(object_type)) {
                return createObjectType(objectData);

            }
        }
        throw new Exception(String.format("%s.createObjectType() - there was no ObjectType: %s in Context: %s",
                this.getClass().getSimpleName(),
                object_type, this.context()));
    }

    /**
     * <code>
     * {
     *  "object_types": [
     *      {
     *          "name": "--name-of-type--",
     *          "domain_type": "ActiveNode.DomainType",
     *          "value_type|object_type|array_type": "--from_the_other_types--",
     *          "bond_type": "BondType",
     *      }
     *  ]
     * }
     * </code>
     *
     * 
     * 
     * @param objectData
     * @return
     * @throws Exception
     */
    public ObjectType createObjectType(ObjectData objectData) throws Exception {
        String memberTypeName = "!error";
        ValueData vdName = (ValueData) objectData.get(MetaTypes.ValueTypes.name.valueType());
        if (vdName != null && vdName.activeValue() != null) {
            memberTypeName = vdName.activeValue().toString();
        }
        DomainType memberDomainType = DomainType.Unknown;
        ValueData vdDomainType = (ValueData) objectData.get(MetaTypes.ValueTypes.domain_type.valueType());
        if (vdDomainType != null && vdDomainType.activeValue() != null) {
            String domain_type = vdDomainType.activeValue().toString();
            memberDomainType = DomainType.valueFromTag(domain_type);
        }
        ObjectRole memberObjectRole = ObjectRole.Unknown;
        ValueData vdObjectRole = (ValueData) objectData.get(MetaTypes.ValueTypes.object_role.valueType());
        if (vdObjectRole != null && vdObjectRole.activeValue() != null) {
            String object_role = vdObjectRole.activeValue().toString();
            memberObjectRole = ObjectRole.valueFromTag(object_role);
        }
        ObjectLevel memberObjectLevel = ObjectLevel.Unknown;
        ValueData vdObjectLevel = (ValueData) objectData.get(MetaTypes.ValueTypes.object_role.valueType());
        if (vdObjectLevel != null && vdObjectLevel.activeValue() != null) {
            String object_level = vdObjectLevel.activeValue().toString();
            memberObjectLevel = ObjectLevel.valueFromTag(object_level);
        }
        ObjectSubRole memberObjectSubRole = ObjectSubRole.Unknown;
        ValueData vdObjectSubRole = (ValueData) objectData.get(MetaTypes.ValueTypes.object_role.valueType());
        if (vdObjectSubRole != null && vdObjectSubRole.activeValue() != null) {
            String object_level = vdObjectSubRole.activeValue().toString();
            memberObjectSubRole = ObjectSubRole.valueFromTag(object_level);
        }
        ObjectType.Builder builder = ActiveNode.ObjectType.builder(this.context(), memberTypeName, memberDomainType, memberObjectRole, memberObjectLevel, memberObjectSubRole);
        ArrayData adMemberTypes = (ArrayData) objectData.get(MetaTypes.Arrays.member_types.arrayType());
        for (ActiveData adata : adMemberTypes.nodes()) {
            if (adata instanceof ObjectData) {
                ObjectData odMemberType = (ObjectData) adata;
                Role memberRole = MetaSupport.getRoleFromMemberTypes(odMemberType);
                String memberName = MetaSupport.getMemberTypeName(odMemberType);
                BondType bondType = MetaSupport.getMemberTypeBondType(odMemberType);
                AppContext memberContext = MetaSupport.getMemberContext(odMemberType, this.context());
                ActiveType memberType = MetaSupport.findType(memberName, memberRole, memberContext);
                builder.add(memberType, bondType);
            }
        }
        /*
        * Auto Types
         */
        for (ValueType autoType : this.context().autoTypes()) {
            builder.add(autoType, BondType.AUTOMATIC);
        }
        ObjectType objectType = builder.build();
        LGR.info("Added {}: {}", objectType.getClass().getSimpleName(), objectType.toString());
        ArrayType arrayType = ArrayType.create(this.context(), objectType.name(), objectType, BondType.OPTIONAL);
        LGR.info("Added {}: {}", ArrayType.class.getSimpleName(), arrayType.toString());
        return objectType;
    }

    /**
     *
     * @param name
     * @return
     */
    private ValueType createValueType0(String name) throws Exception {
        for (ActiveData activeData : this.valueTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            ValueData valueData = objectData.get(ValueType.get(AppContext.Common, "name"));
            if (valueData.activeValue().toString().equals(name)) {
                return createValueType(objectData);
            }
        }
        throw new Exception(String.format("%s.createValueType() - there was no ValueType: %s in Context: %s",
                this.getClass().getSimpleName(),
                name, this.context()));
    }

    /**
     * <code>
     * {
     *  "value_types": [
     *      { "name": "--name-of-type--", "field_type": "--from_field_types_section--" }
     *  ]
     * }
     * </code>
     * 
     * @param objectData
     * @return
     * @throws Exception 
     */
    public ValueType createValueType(ObjectData objectData) throws Exception {
        ValueData valueData = objectData.get(ValueType.get(AppContext.Common, "name"));
        FieldType fieldType;
        ValueData vdFieldType = (ValueData) objectData.get(MetaTypes.ValueTypes.field_type.valueType());
        if (vdFieldType != null && vdFieldType.activeValue() != null && vdFieldType.activeValue().toString() != null) {
            String field_type = vdFieldType.activeValue().toString();
            fieldType = MetaReader.get(this.context()).findOrCreateType(FieldType.class, field_type);
            if (fieldType == null) {
                throw new Exception(String.format("%s.ctor() - field type: %s was not found.",
                        vdFieldType.activeValue().toString()));
            }
        } else {
            throw new Exception(String.format("%s.ctor() - \"field_type\" parameters is missing.",
                    this.getClass().getSimpleName()));
        }
        ValueType valueType = ValueType.create(this.context(), valueData.activeValue().toString(), fieldType);
        //LGR.info("Added {}: {}", valueType.getClass().getSimpleName(), valueType.toString());
        return valueType;
    }

    /**
     * <code>
     * {
     *  "field_types": [
     *      {
     *          "name": "--name-of-type--",
     *          "data_type": "--from_defined_data_types--",
     *          "constraints": {
     *              "--constraint-type--": "--constraint-value--"
     *          }
     *      }
     *  ]
     * }
     * </code>
     *
     * 
     * @param name
     * @return
     */
    private FieldType createFieldType(String name) throws Exception {
        for (ActiveData activeData : this.fieldTypes.nodes()) {
            ObjectData objectData = (ObjectData) activeData;
            ValueData valueData = objectData.get(ValueType.get(AppContext.Common, "name"));
            if (valueData.activeValue().toString().equals(name)) {
                ValueData vdDataType = (ValueData) objectData.get(MetaTypes.ValueTypes.data_type.valueType());
                DataType dataType = DataType.get(this.context(), vdDataType.activeValue().toString());
                if (dataType == null) {
                    throw new Exception(String.format("%s.createFieldType() - data type: %s was not found.",
                            this.getClass().getSimpleName(),
                            vdDataType.activeValue().toString()));
                }
                LGR.debug(objectData.toString());
                ActiveNode.Constraints.Builder builder = ActiveNode.Constraints.builder(dataType.primitiveType().dataGroup());
                ObjectData odConstraint = (ObjectData) objectData.get(MetaTypes.ObjectTypes.constraints.objectType());
                Integer maxWidth = null;
                if (odConstraint != null) {
                    for (ConstraintType<?> constraintType : dataType.constraintTypes()) {
                        if (constraintType == null) {
                            continue;
                        }
                        ValueType vtConstraintType = ActiveNode.ValueType.get(AppContext.Common, constraintType.name());
                        ArrayType atConstraintType = null;
                        atConstraintType = ActiveNode.ArrayType.get(AppContext.Common, constraintType.name());
                        if (atConstraintType != null) {
                            ArrayData adConstraintArray = odConstraint.findFirst(atConstraintType);
                            if (adConstraintArray != null) {
                                for (Object activeValue : adConstraintArray.activeValues()) {
                                    LGR.debug("{} - {}", activeValue.getClass().getSimpleName(), activeValue.toString());
                                }
                                builder.add(constraintType, adConstraintArray.activeValues());
                            }
                        } else {
                            ValueData vdConstraintValue = odConstraint.findFirst(vtConstraintType);
                            if (vdConstraintValue != null) {
                                builder.add(constraintType, vdConstraintValue.activeValues());
                                try {
                                    /*
                                    * This is a temporary hack to parse the pattern looking for the max_length.
                                    * Need to add a max_length constraint for TEXT.
                                    * This is so the value can be truncated before insertion into a RDBMS, otherwise
                                    * it will be rejected by the database.
                                     */
                                    if (constraintType.name().equals("pattern")) {
                                        if (!vdConstraintValue.activeValues().isEmpty()) {
                                            String pattern = (String) vdConstraintValue.activeValues().get(0);
                                            if (pattern.equals("\".*\"")) {
                                                maxWidth = null;
                                            } else {
                                                //    ",34}"
                                                int startIdx = pattern.indexOf(',');
                                                int endIdx = pattern.indexOf('}');
                                                String maxLengthStr = pattern.substring(startIdx + 1, endIdx);
                                                try {
                                                    maxWidth = Integer.valueOf(maxLengthStr);
                                                } catch (Exception ex) {
                                                    maxWidth = null;
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    //LGR.warn("{}.createFieldType() - {}", this.getClass().getSimpleName(),
                                    //	vtConstraintType.toString());
                                }
                            }
                        }
                    }
                }
                ActiveNode.Constraints constraints = builder.build();
                //if (constraints != null) {
                //LGR.info("Created: {}: {}", constraints.getClass().getSimpleName(), constraints.toString());
                //}
                FieldType fieldType = FieldType.create(this.context(), name, dataType, constraints, maxWidth);
                //LGR.info("Added {}: {}", fieldType.getClass().getSimpleName(), fieldType.toString());
                return fieldType;
            }
        }
        throw new Exception(String.format("%s.createFieldType() - there was no FieldType: %s in Context: %s",
                this.getClass().getSimpleName(),
                name, this.context()));
    }

}
