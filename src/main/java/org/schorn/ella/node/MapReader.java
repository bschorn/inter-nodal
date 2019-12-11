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
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;

/**
 *
 * @author schorn
 *
 */
public class MapReader {

    static private final Logger LGR = LoggerFactory.getLogger(MapReader.class);

    static Map<AppContext, MapReader> READERS = new HashMap<>();

    static public MapReader get(AppContext context) {
        return READERS.get(context);
    }

    private final AppContext sourceCxt;
    private final AppContext targetCxt;
    private final ObjectData mapData;
    private final List<FieldMap> fieldMaps;
    private final Map<ObjectType, List<FieldMap>> parentTypesToFieldMaps;

    MapReader(AppContext sourceCxt, AppContext targetCxt, ObjectData mapData) {
        READERS.put(targetCxt, this);
        this.sourceCxt = sourceCxt;
        this.targetCxt = targetCxt;
        this.mapData = mapData;
        this.fieldMaps = new ArrayList<>();
        this.parentTypesToFieldMaps = new HashMap<>();
    }

    /**
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    static public MapReader createFromFile(Path filePath) throws Exception {
        String jsonString = new String(Files.readAllBytes(filePath));
        LGR.info(String.format("%s.createFromFile(\"%s\")",
                MapReader.class.getSimpleName(), filePath.toString()));

        Transform transformer = TransformProvider.provider().getTransform(AppContext.Common, Format.JSON, Format.ActiveNode);
        if (transformer == null) {
            throw new Exception(String.format("%s.createFromFile() - there was no JSON to ActiveNode transformer found.",
                    MapReader.class.getSimpleName()));
        }
        @SuppressWarnings("unchecked")
        ObjectData objectData = (ObjectData) transformer.apply(jsonString);
        if (objectData == null) {
            throw new Exception(String.format("%s.createFromFile() - json converted to null",
                    MapReader.class.getSimpleName()));
        }
        ValueData sourceMetaData = objectData.findFirst(MapTypes.ValueTypes.source_meta.valueType());
        if (sourceMetaData == null) {
            throw new Exception(String.format("%s.createFromFile() - the node with object_type '"
                    + MapTypes.ValueTypes.source_meta.name()
                    + "' was not found in the data object.",
                    MapReader.class.getSimpleName()));
        }
        ValueData targetMetaData = objectData.findFirst(MapTypes.ValueTypes.target_meta.valueType());
        if (targetMetaData == null) {
            throw new Exception(String.format("%s.createFromFile() - the node with object_type '"
                    + MapTypes.ValueTypes.target_meta.name()
                    + "' was not found in the data object.",
                    MapReader.class.getSimpleName()));
        }
        if (sourceMetaData != null) {
            Optional<AppContext> optSourceCxt = AppContext.valueOf(sourceMetaData.activeValue().toString());
            if (optSourceCxt.isPresent()) {
                AppContext sourceCxt = optSourceCxt.get();
                if (targetMetaData != null) {
                    Optional<AppContext> optTargetCxt = AppContext.valueOf(targetMetaData.activeValue().toString());
                    if (optTargetCxt.isPresent()) {
                        AppContext targetCxt = optTargetCxt.get();
                        return new MapReader(sourceCxt, targetCxt, objectData);
                    }
                }
            }
        }
        return null;
    }

    public void register() throws Exception {
        ArrayData fieldMapArray = this.mapData.findFirst(MapTypes.Arrays.field_map.arrayType());
        if (fieldMapArray == null || fieldMapArray.size() == 0) {
            return;
        }
        List<ObjectData> fieldMapObjects = fieldMapArray.find(MapTypes.ObjectTypes.field_map.objectType());
        for (ObjectData fieldMapObj : fieldMapObjects) {
            ValueData vdMapType = fieldMapObj.get(MapTypes.ValueTypes.map_type.valueType());
            ValueData vdMapValue = fieldMapObj.get(MapTypes.ValueTypes.map_value.valueType());
            ArrayData adMapValues = fieldMapObj.get(MapTypes.ArrayValueTypes.map_values.arrayType());
            ValueData vdMapTarget = fieldMapObj.get(MapTypes.ValueTypes.map_target.valueType());
            if (vdMapType == null) {
                throw new Exception("map_type is missing");
            }
            if (vdMapTarget == null) {
                throw new Exception("map_target is missing");
            }
            List<String> mapValues = new ArrayList<>();
            MapType mapType = MapType.valueOf(vdMapType.activeValue().toString());
            if (vdMapValue != null && !vdMapValue.isNull()) {
                if (vdMapValue.activeValue() == null) {
                    LGR.error(fieldMapObj.toString());
                }
                mapValues.add(vdMapValue.activeValue().toString());
            } else if (adMapValues != null) {
                adMapValues.nodes().stream().forEach(ad -> mapValues.add(ad.activeValue().toString()));
            }
            FieldMap fieldMap = createFieldMap(this, mapType, vdMapTarget.activeValue().toString(), mapValues);
            ObjectType parentType = fieldMap.getTargetPath().parentType();
            List<FieldMap> parentFields = this.parentTypesToFieldMaps.get(parentType);
            if (parentFields == null) {
                parentFields = new ArrayList<>();
                this.parentTypesToFieldMaps.put(parentType, parentFields);
            }
            parentFields.add(fieldMap);

            ArrayData adMapRules = fieldMapObj.get(MapTypes.ArrayValueTypes.map_rules.arrayType());
            if (adMapRules != null) {
                List<String> rules = new ArrayList<>();
                adMapRules.nodes().stream().forEach(ad -> rules.add(ad.activeValue().toString()));
                fieldMap.setRules(rules);
            }
            ArrayData adMapLookup = fieldMapObj.get(MapTypes.ArrayValueTypes.map_lookup.arrayType());
            if (adMapLookup != null) {
                List<String> lookup = new ArrayList<>();
                adMapLookup.nodes().stream().forEach(ad -> lookup.add(ad.activeValue().toString()));
                fieldMap.setLookup(lookup);
            }
            this.fieldMaps.add(fieldMap);
        }

    }

    public List<FieldMap> getFieldMaps(ObjectType objectType) {
        return this.parentTypesToFieldMaps.get(objectType);
    }

    public FieldMap getFieldMap(ObjectType objectType, ValueType valueType) {
        Optional<FieldMap> optFieldMap = this.parentTypesToFieldMaps.get(objectType).stream()
                .filter(fm -> fm.getTargetPath().leafType().equals(valueType))
                .findFirst();
        if (optFieldMap.isPresent()) {
            return optFieldMap.get();
        }
        return null;
    }

    public AppContext sourceContext() {
        return this.sourceCxt;
    }

    public AppContext targetContext() {
        return this.targetCxt;
    }

    public List<FieldMap> getFieldMaps() {
        return this.fieldMaps;
    }

    public enum MapType {
        FIELD,

        /**
         *
         */
        VARIABLE,
        STATIC;
    }

    static private FieldMap createFieldMap(MapReader mapReader, MapType mapType, String mapTarget, List<String> mapValues) throws Exception {
        return new FieldMap(mapReader.targetCxt, mapReader.sourceCxt, mapType, mapTarget, mapValues);
    }

    /**
     *
     *
     *
     */
    static public class FieldMap {

        private final MapType mapType;
        private final FieldPath targetPath;

        private String value = null;
        private List<FieldPath> sourcePaths = null;
        private Map<Object, Object> lookupMap = null;
        private List<String> rulesList = null;

        /**
         *
         * @param mapType
         * @param mapValue
         * @throws Exception
         */
        public FieldMap(AppContext targetCxt, AppContext sourceCxt, MapType mapType, String mapTarget, List<String> mapValues) throws Exception {
            this.mapType = mapType;
            this.targetPath = new FieldPath(targetCxt, mapTarget);
            if (this.targetPath == null) {
                throw new Exception("FieldMap() - target path is null");
            }
            switch (mapType) {
                case STATIC:
                    if (mapValues == null) {
                        throw new Exception("FieldMap() - STATIC FieldMap entry for " + mapTarget + " has no static value");
                    }
                    this.value = mapValues.get(0);
                    break;
                case VARIABLE:
                    this.value = System.getProperty(mapValues.get(0));
                    if (this.value == null) {
                        this.value = System.getenv(mapValues.get(0));
                        if (this.value == null) {
                            throw new Exception("FieldMap() - VARIABLE FieldMap entry for " + mapTarget + " has no Properties or $ENV value");
                        }
                    }
                    break;
                case FIELD:
                    this.sourcePaths = new ArrayList<>();
                    if (mapValues == null) {
                        throw new Exception("FieldMap() - FIELD FieldMap entry for " + mapTarget + " has no map_value(s) set");
                    }
                    for (String mapValue : mapValues) {
                        this.sourcePaths.add(new FieldPath(sourceCxt, mapValue));
                    }
                    break;
            }
        }

        void setRules(List<String> rulesList) {
            this.rulesList = rulesList;
        }

        void setLookup(List<String> lookupList) {
            this.lookupMap = new HashMap<>();
            for (String lookup : lookupList) {
                String[] parts = lookup.split("|");
                if (parts.length == 2) {
                    this.lookupMap.put(parts[0], parts[1]);
                }
            }
        }

        public MapType getMapType() {
            return this.mapType;
        }

        public String getValue() {
            return this.value;
        }

        public Object doLookup(Object sourceValue) {
            if (this.lookupMap != null) {
                Object targetValue = this.lookupMap.get(sourceValue);
                if (targetValue != null) {
                    return targetValue;
                }
            }
            return sourceValue;
        }

        public List<String> getRulesList() {
            if (this.rulesList != null) {
                return this.rulesList;
            }
            return new ArrayList<>();
        }

        public FieldPath getTargetPath() {
            return this.targetPath;
        }

        public List<FieldPath> getSourcePaths() {
            return this.sourcePaths;
        }

        @Override
        public String toString() {
            switch (this.mapType) {
                case STATIC:
                case VARIABLE:
                    return String.format("%s.%s <- (%s) '%s' \nlookup: %s\nrules: %s",
                            this.targetPath.rootType.name(),
                            this.targetPath.leafType.name(),
                            mapType.name(),
                            value,
                            lookupMap == null ? "" : lookupMap.toString(),
                            rulesList == null ? "" : rulesList.toString());
                case FIELD:
                    StringJoiner joiner1 = new StringJoiner(",\n\t", "", "");
                    for (FieldPath fieldPath : this.sourcePaths) {
                        joiner1.add(fieldPath.toString());
                    }
                    return String.format("%s.%s <- (%s) sourcePath: [\n\t%s\n]\nlookup: %s\nrules: %s",
                            this.targetPath.rootType.name(),
                            this.targetPath.leafType.name(),
                            mapType.name(),
                            joiner1.toString(),
                            lookupMap == null ? "" : lookupMap.toString(),
                            rulesList == null ? "" : rulesList.toString());
                default:
                    break;
            }
            return super.toString();

        }
    }

    /**
     *
     *
     *
     */
    static public class FieldPath extends AbstractContextual {

        static public FieldPath create(AppContext context, String path) throws Exception {
            return new FieldPath(context, path);
        }

        private final String path;
        private final ObjectType rootType;
        private ObjectType parentType;
        private final ValueType leafType;
        private final List<ArrayType> arrayTypes;
        private final List<ObjectType> objectTypes;

        FieldPath(AppContext context, String path) throws Exception {
            super(context);
            this.path = path;
            this.arrayTypes = new ArrayList<>();
            this.objectTypes = new ArrayList<>();
            List<String> pathList = Arrays.asList(path.split("\\."));
            if (pathList.size() < 2) {
                throw new Exception("Bad path: " + pathList.toString());
            }
            String rootTypeName = pathList.get(0);
            this.rootType = ObjectType.get(this.context(), rootTypeName);
            if (this.rootType == null) {
                throw new Exception(String.format("%s.ctor() - The rootType '%s' was not found",
                        this.getClass().getSimpleName(),
                        rootTypeName));
            }
            String leafTypeName = pathList.get(pathList.size() - 1);
            ValueType vt = ValueType.get(this.context(), leafTypeName);
            if (vt == null) {
                vt = ValueType.get(this.context(), leafTypeName.toLowerCase());
                if (vt == null) {
                    throw new Exception(String.format("%s.ctor() - The leafType '%s' was not found",
                            this.getClass().getSimpleName(),
                            leafTypeName));
                }
            }
            this.leafType = vt;
            for (int i = 1; i < pathList.size() - 1; i += 1) {
                ArrayType arrayType = ArrayType.get(this.context(), pathList.get(i));
                if (arrayType == null) {
                    LGR.warn("{}.ctor() - The arrayType '{}' was not found",
                            this.getClass().getSimpleName(), pathList.get(i));
                } else {
                    this.arrayTypes.add(arrayType);
                }
                ObjectType objectType = ObjectType.get(this.context(), pathList.get(i));
                if (objectType == null) {
                    LGR.warn("{}.ctor() - The objectType '{}' was not found",
                            this.getClass().getSimpleName(), pathList.get(i));
                } else {
                    this.objectTypes.add(objectType);
                }
                if (arrayType == null && objectType == null) {
                    throw new Exception(String.format("%s.ctor() - The activeType '%s' was not found",
                            this.getClass().getSimpleName(),
                            pathList.get(i)));
                }
                this.parentType = objectType;
            }
            if (this.parentType == null) {
                this.parentType = this.rootType;
            }
        }

        /**
         *
         * @return
         */
        public ObjectType rootType() {
            return this.rootType;
        }

        public ObjectType parentType() {
            return this.parentType;
        }

        public ValueType leafType() {
            return this.leafType;
        }

        public int subLevels() {
            return this.arrayTypes.size();
        }

        public ArrayType getArraySubType(int subLevel) {
            return this.arrayTypes.get(subLevel);
        }

        public ObjectType getObjectSubType(int subLevel) {
            return this.objectTypes.get(subLevel);
        }

        @Override
        public String toString() {
            return this.path;
        }
    }

    /**
     *
     *
     *
     */
    public enum Rules {
        JOIN("delimiter", "prefix", "suffix"), MAX,;

        List<String> parameters;

        Rules(String... parameters) {
            this.parameters = Arrays.asList(parameters);
        }

        public Object apply(List<Object> parameterValues, List<Object> tokens) {
            switch (this) {
                case JOIN: {
                    Object delimiterValue = "";
                    Object prefixValue = "";
                    Object suffixValue = "";
                    for (int i = 0; i < parameters.size(); i += 1) {
                        switch (parameters.get(i)) {
                            case "delimiter":
                                if (parameterValues.size() > i) {
                                    delimiterValue = parameterValues.get(i);
                                }
                                break;
                            case "prefix":
                                if (parameterValues.size() > i) {
                                    prefixValue = parameterValues.get(i);
                                }
                                break;
                            case "suffix":
                                if (parameterValues.size() > i) {
                                    suffixValue = parameterValues.get(i);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    StringJoiner joiner = new StringJoiner(delimiterValue.toString(), prefixValue.toString(),
                            suffixValue.toString());
                    for (Object token : tokens) {
                        if (token != null) {
                            joiner.add(token.toString());
                        } else {
                            joiner.add("null");
                        }
                    }
                    return joiner.toString();
                }
                case MAX:
                    Object maxValue = null;
                    for (Object token : tokens) {
                        if (maxValue == null) {
                            maxValue = token;
                        } else if (token instanceof Number && maxValue instanceof Number) {
                            Number maxValueNumber = (Number) maxValue;
                            Number tokenNumber = (Number) token;
                            maxValue = Double.valueOf(Math.max(maxValueNumber.doubleValue(), tokenNumber.doubleValue()));
                        } else if (token instanceof Temporal && maxValue instanceof Temporal) {
                            if (token instanceof LocalDate && maxValue instanceof LocalDate) {
                                LocalDate tokenDate = (LocalDate) token;
                                LocalDate maxValueDate = (LocalDate) maxValue;
                                if (tokenDate.compareTo(maxValueDate) > 0) {
                                    maxValue = token;
                                }
                            }
                        } else if (token instanceof String && maxValue instanceof String) {
                            String tokenString = (String) token;
                            String maxValueString = (String) maxValue;
                            if (tokenString.compareToIgnoreCase(maxValueString) >= 0) {
                                maxValue = token;
                            }
                        }
                    }
                    return maxValue;
            }
            return null;
        }
    }

}
