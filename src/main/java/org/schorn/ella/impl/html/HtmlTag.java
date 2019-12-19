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
package org.schorn.ella.impl.html;

import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.schorn.ella.html.ActiveHtml.HtmlSelectElement;

/**
 *
 * @author bschorn
 */
public interface HtmlTag {

    public String tag();

    public String id();

    public TagAttribute[] attributes();

    public String text();

    public void text(String text);

    public Value value();

    public void value(Value value);

    public enum ValueType {
        BOOL(Boolean.class),
        TEXT(String.class),
        INTEGER(Integer.class),
        DOUBLE(Double.class),
        TEMPORAL(Temporal.class),
        LIST(List.class);
        ;
        
        Class<?> typeClass;

        ValueType(Class<?> typeClass) {
            this.typeClass = typeClass;
        }

        public Class<?> getTypeClass() {
            return this.typeClass;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    static public class Value {

        ValueType valueType;
        Object value;

        public Value(Boolean value) {
            this.valueType = ValueType.BOOL;
            this.value = value;
        }

        public Value(String value) {
            this.valueType = ValueType.TEXT;
            this.value = value;
        }

        public Value(Integer value) {
            this.valueType = ValueType.INTEGER;
            this.value = value;
        }

        public Value(Double value) {
            this.valueType = ValueType.DOUBLE;
            this.value = value;
        }

        public Value(Temporal value) {
            this.valueType = ValueType.TEMPORAL;
            this.value = value;
        }

        public Boolean asBoolean() {
            return (Boolean) value;
        }

        public String asString() {
            return (String) value;
        }

        public Integer asInteger() {
            return (Integer) value;
        }

        public Double asDouble() {
            return (Double) value;
        }

        public Temporal asTemporal() {
            return (Temporal) value;
        }
    }

    /**
     *
     */
    interface TagAttribute {

        public String attributeName();

        public ValueType attributeValueType();

        public Object defaultValue();
    }

    public enum GlobalAttribute implements TagAttribute {
        ID("id", ValueType.TEXT, ""),
        CLASS("class", ValueType.TEXT, ""),
        HIDDEN("hidden", ValueType.BOOL, false);

        String attributeName;
        ValueType attributeValueType;
        Class<?> dataClass;
        Object defaultValue;

        GlobalAttribute(String attributeName, ValueType attributeValueType, Object defaultValue) {
            this.attributeName = attributeName;
            this.attributeValueType = attributeValueType;
            this.defaultValue = defaultValue;
        }

        @Override
        public String attributeName() {
            return this.attributeName;
        }

        @Override
        public ValueType attributeValueType() {
            return this.attributeValueType;
        }

        @Override
        public Object defaultValue() {
            return this.defaultValue;
        }
    }

    /**
     * Interface for an Input object to represent the recommended HTML
     * {@code <input>} attributes for a given field
     */
    interface Tag extends HtmlTag {

        Boolean getFlagAttribute(TagAttribute attribute);

        String getTextAttribute(TagAttribute attribute);

        Integer getIntegerAttribute(TagAttribute attribute);

        Double getDoubleAttribute(TagAttribute attribute);

        List<Integer> getIntegerAttributeList(TagAttribute attribute);

        List<String> getStringAttributeList(TagAttribute attribute);

        List<HtmlSelectElement.ValueLabel> getValueLabelList(TagAttribute attribute);

        /**
         * Builder class (protected)
         *
         * @param <T>
         */
        static abstract class Builder<T> {

            Map<TagAttribute, Boolean> flagMap = new HashMap<>();
            Map<TagAttribute, String> textMap = new HashMap<>();
            Map<TagAttribute, Integer> integerMap = new HashMap<>();
            Map<TagAttribute, Double> doubleMap = new HashMap<>();
            Map<TagAttribute, List<Integer>> integerList = new HashMap<>();
            Map<TagAttribute, List<String>> stringList = new HashMap<>();
            Map<TagAttribute, List<HtmlSelectElement.ValueLabel>> valueLabels = new HashMap<>();

            public Builder<T> add(TagAttribute attribute, Integer value) {
                this.integerMap.put(attribute, value);
                return this;
            }

            public Builder<T> add(TagAttribute attribute, Double value) {
                this.doubleMap.put(attribute, value);
                return this;
            }

            public Builder<T> add(TagAttribute attribute, String value) {
                this.textMap.put(attribute, value);
                return this;
            }

            public Builder<T> add(TagAttribute attribute, Boolean value) {
                this.flagMap.put(attribute, value);
                return this;
            }

            public Builder<T> add(TagAttribute attribute, Integer[] list) {
                this.integerList.put(attribute, Arrays.asList(list));
                return this;
            }

            public Builder<T> add(TagAttribute attribute, String[] list) {
                this.stringList.put(attribute, Arrays.asList(list));
                return this;
            }

            public Builder<T> add(TagAttribute attribute, List<HtmlSelectElement.ValueLabel> valueLabels) {
                this.valueLabels.put(attribute, valueLabels);
                return this;
            }

            abstract public T build();
        }

        static abstract class Impl implements Tag {

            String tag;
            Set<TagAttribute> attributes = new HashSet<>();
            String text = null;
            Value value = null;
            Map<TagAttribute, Boolean> flagMap;
            Map<TagAttribute, String> textMap;
            Map<TagAttribute, Integer> integerMap;
            Map<TagAttribute, Double> doubleMap;
            Map<TagAttribute, List<Integer>> integerList;
            Map<TagAttribute, List<String>> stringList;
            Map<TagAttribute, List<HtmlSelectElement.ValueLabel>> valueLabels;

            Impl(String tag,
                    Map<TagAttribute, Boolean> flagMap,
                    Map<TagAttribute, String> textMap,
                    Map<TagAttribute, Integer> integerMap,
                    Map<TagAttribute, Double> doubleMap,
                    Map<TagAttribute, List<Integer>> integerList,
                    Map<TagAttribute, List<String>> stringList,
                    Map<TagAttribute, List<HtmlSelectElement.ValueLabel>> valueLabels) {
                this.tag = tag;
                this.flagMap = flagMap == null ? new HashMap<>() : flagMap;
                this.textMap = textMap == null ? new HashMap<>() : textMap;
                this.integerMap = integerMap == null ? new HashMap<>() : integerMap;
                this.doubleMap = doubleMap == null ? new HashMap<>() : doubleMap;
                this.integerList = integerList == null ? new HashMap<>() : integerList;
                this.stringList = stringList == null ? new HashMap<>() : stringList;
                this.valueLabels = valueLabels == null ? new HashMap<>() : valueLabels;

                attributes.addAll(this.flagMap.keySet());
                attributes.addAll(this.textMap.keySet());
                attributes.addAll(this.integerMap.keySet());
                attributes.addAll(this.doubleMap.keySet());
                attributes.addAll(this.integerList.keySet());
                attributes.addAll(this.stringList.keySet());
                attributes.addAll(this.valueLabels.keySet());
            }

            @Override
            public String tag() {
                return this.tag;
            }

            @Override
            public TagAttribute[] attributes() {
                TagAttribute[] aa = new TagAttribute[attributes.size()];
                return attributes.toArray(aa);
            }

            @Override
            public String text() {
                return this.text;
            }

            @Override
            public void text(String text) {
                this.text = text;
            }

            @Override
            public Value value() {
                return this.value;
            }

            @Override
            public void value(Value value) {
                this.value = value;
            }

            @Override
            public Boolean getFlagAttribute(TagAttribute attribute) {
                return this.flagMap.get(attribute);
            }

            @Override
            public String getTextAttribute(TagAttribute attribute) {
                return this.textMap.get(attribute);
            }

            @Override
            public Integer getIntegerAttribute(TagAttribute attribute) {
                return this.integerMap.get(attribute);
            }

            @Override
            public Double getDoubleAttribute(TagAttribute attribute) {
                return this.doubleMap.get(attribute);
            }

            @Override
            public List<Integer> getIntegerAttributeList(TagAttribute attribute) {
                return this.integerList.get(attribute);
            }

            @Override
            public List<String> getStringAttributeList(TagAttribute attribute) {
                return this.stringList.get(attribute);
            }

            @Override
            public List<HtmlSelectElement.ValueLabel> getValueLabelList(TagAttribute attribute) {
                return this.valueLabels.get(attribute);
            }

        }
    }

    public enum FormAttribute implements TagAttribute {
        ID("id", ValueType.TEXT, null),
        NAME("name", ValueType.TEXT, null),
        ACTION("action", ValueType.TEXT, ""),
        AUTOCOMPLETE("autocomplete", ValueType.BOOL, Boolean.FALSE),
        METHOD("method", ValueType.TEXT, null),
        TARGET("target", ValueType.TEXT, null),;

        String attributeName;
        ValueType attributeValueType;
        Object defaultValue;

        FormAttribute(String attributeName, ValueType attributeValueType, Object defaultValue) {
            this.attributeName = attributeName;
            this.attributeValueType = attributeValueType;
            this.defaultValue = defaultValue;
        }

        @Override
        public String attributeName() {
            return this.attributeName;
        }

        @Override
        public Object defaultValue() {
            return this.defaultValue;
        }

        @Override
        public ValueType attributeValueType() {
            return this.attributeValueType;
        }

    }

    /**
     * Interface for an Input object to represent the recommended HTML
     * {@code <input>} attributes for a given field
     */
    public interface FormTag extends Tag {

        static public Builder builder() {
            return new Builder();
        }

        public String label();

        /**
         * Builder class (protected)
         */
        static class Builder extends Tag.Builder<FormTag> {

            String label;

            Builder() {
                super.add(FormAttribute.AUTOCOMPLETE, Boolean.FALSE);
                if (FormAttribute.ACTION.defaultValue() != null) {
                    super.add(FormAttribute.ACTION, (String) FormAttribute.ACTION.defaultValue());
                }
                if (FormAttribute.METHOD.defaultValue() != null) {
                    super.add(FormAttribute.METHOD, (String) FormAttribute.METHOD.defaultValue());
                }
                if (FormAttribute.TARGET.defaultValue() != null) {
                    super.add(FormAttribute.TARGET, (String) FormAttribute.TARGET.defaultValue());
                }
            }

            public Builder label(String label) {
                this.label = label != null ? label : this.label;
                return this;
            }

            @Override
            public FormTag build() {
                return (FormTag) new Impl(label, flagMap, textMap, integerMap, doubleMap);
            }
        }

        static class Impl extends Tag.Impl implements FormTag {

            String label;

            Impl(String label,
                    Map<HtmlTag.TagAttribute, Boolean> flagMap,
                    Map<HtmlTag.TagAttribute, String> textMap,
                    Map<HtmlTag.TagAttribute, Integer> integerMap,
                    Map<HtmlTag.TagAttribute, Double> doubleMap) {
                super("form", flagMap, textMap, integerMap, doubleMap, null, null, null);
                this.label = label;
            }

            @Override
            public String label() {
                return this.label;
            }

            @Override
            public String id() {
                return this.textMap.get(FormAttribute.ID);
            }
        }
    }

    public enum InputType {
        BUTTON("button"),
        CHECKBOX("checkbox"),
        COLOR("color"),
        DATE("date"),
        DATETIME("datetime-local"),
        EMAIL("email"),
        FILE("file"),
        HIDDEN("hidden"),
        IMAGE("image"),
        LIST("list"),
        MONTH("month"),
        NUMBER("number"),
        PASSWORD("password"),
        RADIO("radio"),
        RANGE("range"),
        RESET("reset"),
        SEARCH("search"),
        SUBMIT("submit"),
        TEL("tel"),
        TEXT("text"),
        TIME("time"),
        URL("url"),
        WEEK("week"),
        UNKNOWN("");

        String inputType;

        InputType(String inputType) {
            this.inputType = inputType;
        }

        public String inputType() {
            return this.inputType;
        }

        static public InputType parse(String type) {
            if (type != null) {
                for (InputType inputType : values()) {
                    if (inputType.inputType == null) {
                    }
                    if (inputType.inputType.equalsIgnoreCase(type)) {
                        return inputType;
                    }
                }
            }
            return InputType.TEXT;
        }
    }

    /**
     * Enumeration of the valid HTML {@code <input --attributes-->}
     */
    public enum InputAttribute implements TagAttribute {
        ID("id", ValueType.TEXT, (Object) InputType.TEXT),
        TYPE("type", ValueType.TEXT, (Object) InputType.TEXT),
        CHECKED("checked", ValueType.BOOL, (Object) Boolean.FALSE),
        DISABLED("disabled", ValueType.BOOL, (Object) Boolean.FALSE),
        UID("uid", ValueType.INTEGER, null),

        /**
         *
         */
        LIST("list", ValueType.LIST, null),
        MAX("max", ValueType.TEXT, null),
        MAXLENGTH("maxlength", ValueType.INTEGER, null),
        MIN("min", ValueType.TEXT, null),
        MINLENGTH("minlength", ValueType.INTEGER, null),
        MULTIPLE("multiple", ValueType.BOOL, (Object) Boolean.FALSE),
        NAME("name", ValueType.TEXT, null),
        PATTERN("pattern", ValueType.TEXT, null),
        PLACEHOLDER("placeholder", ValueType.TEXT, null),
        READONLY("readonly", ValueType.BOOL, (Object) Boolean.FALSE),
        REQUIRED("required", ValueType.BOOL, (Object) Boolean.FALSE),
        SIZE("size", ValueType.INTEGER, null),
        STEP("step", ValueType.DOUBLE, null),
        VALUE("value", ValueType.TEXT, null);

        String attributeName;
        ValueType attributeValueType;
        Class<?> dataClass;
        Object defaultValue;

        InputAttribute(String attributeName, ValueType attributeValueType,
                Object defaultValue) {
            this.attributeName = attributeName;
            this.attributeValueType = attributeValueType;
            this.defaultValue = defaultValue;
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner("] [", "InputAttribute: [", "]");
            if (this.attributeName != null) {
                joiner.add(String.format("%s: %s", "attributeName", this.attributeName));
            }
            if (this.attributeValueType != null) {
                joiner.add(String.format("%s: %s", "attributeValueType", this.attributeValueType.toString()));
            }
            if (this.dataClass != null) {
                joiner.add(String.format("%s: %s", "dataClass", this.dataClass.getSimpleName()));
            }
            if (this.defaultValue != null) {
                joiner.add(String.format("%s: %s", "defaultValue", this.defaultValue.toString()));
            }
            return joiner.toString();
        }

        @Override
        public String attributeName() {
            return this.attributeName;
        }

        @Override
        public ValueType attributeValueType() {
            return this.attributeValueType;
        }

        @Override
        public Object defaultValue() {
            return this.defaultValue;
        }

        /**
         * Special Conditions Check
         *
         * @param inputType
         * @return
         */
        public Boolean isValid(InputType inputType) {
            switch (this) {
                case MULTIPLE:
                    switch (inputType) {
                        case EMAIL:
                        case FILE:
                            return true;
                        default:
                            break;
                    }
                    break;
                case PATTERN:
                    switch (inputType) {
                        case EMAIL:
                        case TEXT:
                        case SEARCH:
                        case TEL:
                        case URL:
                        case PASSWORD:
                            return true;
                        default:
                            break;
                    }
                default:
                    break;
            }
            return true;
        }

    }

    /**
     * Interface for an Input object to represent the recommended HTML
     * {@code <input>} attributes for a given field
     */
    public interface InputTag extends Tag {

        public InputType type();

        public String label();

        public String name();

        /**
         * Gets a new Builder instance
         *
         * @param inputType
         * @return
         */
        static public Builder builder(HtmlTag.InputType inputType) {
            Builder builder = new Builder();
            builder.type(inputType);
            return builder;
        }

        /**
         * Builder class (protected)
         */
        static class Builder extends Tag.Builder<InputTag> {

            String label;

            @Override
            public InputTag build() {
                return (InputTag) new InputTag.Impl(label, flagMap, textMap, integerMap, doubleMap, integerList, stringList, valueLabels);
            }

            Builder() {
                super.add(InputAttribute.CHECKED, (Boolean) InputAttribute.CHECKED.defaultValue());
                super.add(InputAttribute.DISABLED, (Boolean) InputAttribute.DISABLED.defaultValue());
                super.add(InputAttribute.MULTIPLE, (Boolean) InputAttribute.MULTIPLE.defaultValue());
                super.add(InputAttribute.READONLY, (Boolean) InputAttribute.READONLY.defaultValue());
                super.add(InputAttribute.REQUIRED, (Boolean) InputAttribute.REQUIRED.defaultValue());
            }

            public void type(InputType inputType) {
                this.add(InputAttribute.TYPE, inputType.inputType);

            }

            public void label(String label) {
                this.label = label;
            }
        }

        static class Impl extends Tag.Impl implements InputTag, HtmlTag {

            String label;

            Impl(String label,
                    Map<HtmlTag.TagAttribute, Boolean> flagMap,
                    Map<HtmlTag.TagAttribute, String> textMap,
                    Map<HtmlTag.TagAttribute, Integer> integerMap,
                    Map<HtmlTag.TagAttribute, Double> doubleMap,
                    Map<TagAttribute, List<Integer>> integerList,
                    Map<TagAttribute, List<String>> stringList,
                    Map<TagAttribute, List<HtmlSelectElement.ValueLabel>> valueLabels
            ) {
                super("input", flagMap, textMap, integerMap, doubleMap, integerList, stringList, valueLabels);
                this.label = label;
            }

            @Override
            public InputType type() {
                String type = this.textMap.get(InputAttribute.TYPE);
                if (type != null) {
                    return InputType.parse(type);
                }
                return InputType.TEXT;
            }

            @Override
            public String label() {
                return this.label;
            }

            @Override
            public String id() {
                return this.textMap.get(InputAttribute.ID);
            }

            @Override
            public String name() {
                return this.textMap.get(InputAttribute.NAME);
            }

        }
    }

    /**
     * Interface for an Input object to represent the recommended HTML
     * {@code <input>} attributes for a given field
     */
    public interface ListTag extends Tag {

        static public Builder builder() {
            return new Builder();
        }

        public String label();

        /**
         * Builder class (protected)
         */
        static class Builder extends Tag.Builder<ListTag> {

            String label;

            Builder() {
            }

            public Builder label(String label) {
                this.label = label;
                return this;
            }

            /**
             *
             * @return
             */
            @Override
            public ListTag build() {
                return (ListTag) new InputTagImpl(label, flagMap, textMap, integerMap, doubleMap);
            }
        }

        static class InputTagImpl extends Tag.Impl implements ListTag {

            String label;

            InputTagImpl(String label,
                    Map<HtmlTag.TagAttribute, Boolean> flagMap,
                    Map<HtmlTag.TagAttribute, String> textMap,
                    Map<HtmlTag.TagAttribute, Integer> integerMap,
                    Map<HtmlTag.TagAttribute, Double> doubleMap) {
                super("form", flagMap, textMap, integerMap, doubleMap, null, null, null);
                this.label = label;
            }

            /**
             *
             * @return
             */
            @Override
            public String label() {
                return this.label;
            }

            @Override
            public String id() {
                return this.textMap.get(GlobalAttribute.ID);
            }
        }

    }
}
