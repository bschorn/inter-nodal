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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;

/**
 *
 * @author bschorn
 */
public class HtmlTagCreator {

    /**
     * Creates the <form> tag using the given ObjectType
     *
     * @param objectType
     * @return
     */
    static HtmlTag.FormTag createFormTag(ObjectType objectType) {
        HtmlTag.FormTag.Builder builder = HtmlTag.FormTag.builder();
        builder.add(HtmlTag.FormAttribute.ID, objectType.name());
        builder.add(HtmlTag.FormAttribute.NAME, objectType.name());
        builder.label(objectType.name());
        builder.label(HtmlProvider.provider().labeler().get(objectType, null));
        return (HtmlTag.FormTag) builder.build();
    }

    /**
     * Creates a <input> tag using the ObjectType + ValueType
     *
     * @param objectType
     * @param valueType
     * @return
     */
    static HtmlTag.InputTag createInputTag(ObjectType objectType, ValueType valueType) {
        HtmlTag.InputTag.Builder builder = builder(objectType, valueType);
        builder.label(HtmlProvider.provider().labeler().get(objectType, valueType));
        // builder.label(objectType.name());
        return (HtmlTag.InputTag) builder.build();
    }

    /**
     * Creates a <input> tag with a value using the ObjectType + ValueType +
     * defaultValue
     *
     * @param objectType
     * @param valueType
     * @param element
     * @param defaultValue
     * @return
     */
    static HtmlTag.InputTag createInputTag(ObjectType objectType, ValueType valueType, Object defaultValue) {
        HtmlTag.InputTag.Builder builder = builder(objectType, valueType);
        builder.label(HtmlProvider.provider().labeler().get(objectType, valueType));
        builder.label(valueType.name());
        addValueToBuilder(builder, valueType, defaultValue);
        return (HtmlTag.InputTag) builder.build();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 						  				PRIVATE
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * InputTag Builder
     */
    static private HtmlTag.InputTag.Builder builder(ObjectType objectType, ValueType valueType) {
        HtmlTag.InputType inputType = convert(valueType);
        HtmlTag.InputTag.Builder builder = HtmlTag.InputTag.builder(inputType);
        builder.add(HtmlTag.InputAttribute.ID, valueType.name());
        builder.add(HtmlTag.InputAttribute.NAME, valueType.name());
        builder.label(valueType.name());

        builder.add(HtmlTag.InputAttribute.REQUIRED, Boolean.TRUE);

        if (objectType.isKey(valueType)) {
            builder.add(HtmlTag.InputAttribute.READONLY, Boolean.TRUE);
        }
        if (objectType.isForeignKey(valueType)) {
            ObjectType foreignKeyOwner = valueType.keyOf();
            builder.add(HtmlTag.GlobalAttribute.CLASS,
                    String.format("ForeignKey %s.%s", foreignKeyOwner.context().name(), foreignKeyOwner.name()));
        }
        if (objectType.isOptional(valueType)) {
            builder.add(HtmlTag.InputAttribute.REQUIRED, Boolean.FALSE);
        }
        List<ConstraintType<?>> constraintTypes = valueType.fieldType().constraints().constraintTypes();

        constraintTypes.stream().forEach(constraintType -> {
            ConstraintData constraintData = valueType.fieldType().constraints().constraint(constraintType);
            List<Object> values = constraintData.constraintValues();
            if (values != null && !values.isEmpty()) {
                String constraintName = constraintType.name();
                DataGroup dataGroup = constraintData.constraintType().dataGroup();
                switch (dataGroup) {
                    case BOOL:
                        break;
                    case DATE:
                        switch (constraintName) {
                            case "min_date":
                                builder.add(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) values.get(0)));
                                break;
                            case "max_date":
                                builder.add(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case TIME:
                        switch (constraintName) {
                            case "min_time":
                                builder.add(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_TIME.format((LocalDate) values.get(0)));
                                break;
                            case "max_time":
                                builder.add(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_TIME.format((LocalDate) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case TIMESTAMP:
                        switch (constraintName) {
                            case "min_datetime":
                                builder.add(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDate) values.get(0)));
                                break;
                            case "max_datetime":
                                builder.add(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDate) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case NUMBER:
                        switch (constraintName) {
                            case "min_integer":
                                builder.add(HtmlTag.InputAttribute.MIN, values.get(0).toString());
                                break;
                            case "max_integer":
                                builder.add(HtmlTag.InputAttribute.MAX, values.get(0).toString());
                                break;
                            case "increment":
                                builder.add(HtmlTag.InputAttribute.STEP, values.get(0).toString());
                                break;
                        }
                        break;
                    case DECIMAL:
                        switch (constraintName) {
                            case "min_decimal":
                                builder.add(HtmlTag.InputAttribute.MIN, values.get(0).toString());
                                break;
                            case "max_decimal":
                                builder.add(HtmlTag.InputAttribute.MAX, values.get(0).toString());
                                break;
                            default:
                                break;
                        }
                        break;
                    case TEXT:
                        switch (constraintName) {
                            case "pattern":
                                builder.add(HtmlTag.InputAttribute.PATTERN, values.get(0).toString());
                                break;
                            default:
                                break;
                        }
                        break;
                    case ENUM:
                        switch (constraintName) {
                            case "list":
                                List<ActiveHtml.HtmlSelectElement.ValueLabel> valueLabels = new ArrayList<>();
                                values.forEach(item -> {
                                    try {
                                        valueLabels.add(
                                                ActiveHtml.HtmlSelectElement.ValueLabel.create(item.toString(), item.toString()));
                                    } catch (Exception ex) {

                                    }
                                });
                                builder.add(HtmlTag.InputAttribute.LIST, valueLabels);
                                break;
                            default:
                                break;
                        }
                        break;
                    case UUID:
                        break;
                    case CUSTOM:
                        break;
                    default:
                        break;
                }
            }
        });
        return builder;
    }

    /**
     * Initializes an InputTag.Builder using ValueType
     *
     *
     * @param valueType
     * @return
     */
    @SuppressWarnings({"unused"})
    static private HtmlTag.InputTag.Builder builder(ValueType valueType) {
        HtmlTag.InputType inputType = convert(valueType);
        HtmlTag.InputTag.Builder builder = HtmlTag.InputTag.builder(inputType);
        builder.add(HtmlTag.InputAttribute.ID, valueType.name());
        builder.add(HtmlTag.InputAttribute.NAME, valueType.name());
        builder.label(valueType.name());
        builder.add(HtmlTag.InputAttribute.REQUIRED, Boolean.TRUE);
        return builder;
    }

    /**
     * Converts ValueType into InputType
     *
     */
    static private HtmlTag.InputType convert(ValueType valueType) {
        DataGroup dataGroup = valueType.fieldType().dataType().primitiveType().dataGroup();
        if (dataGroup.isBinary()) {
            return HtmlTag.InputType.CHECKBOX;
        } else if (dataGroup.isNumeric()) {
            return HtmlTag.InputType.NUMBER;
        } else if (dataGroup.isTemporal()) {
            if (valueType.fieldType().dataType().primitiveType().dataClass().equals(LocalDate.class)) {
                return HtmlTag.InputType.DATE;
            } else if (valueType.fieldType().dataType().primitiveType().dataClass().equals(LocalTime.class)) {
                return HtmlTag.InputType.TIME;
            } else {
                return HtmlTag.InputType.DATETIME;
            }
        } else if (dataGroup.isEnumerable()) {
            return HtmlTag.InputType.LIST;
        } else {
            return HtmlTag.InputType.TEXT;
        }
    }

    /**
     * Puts 'value' in the form for the ValueType
     *
     */
    static private void addValueToBuilder(HtmlTag.InputTag.Builder builder, ValueType valueType, Object value) {
        if (value == null) {
            return;
        }
        DataGroup dataGroup = valueType.fieldType().dataType().primitiveType().dataGroup();
        if (dataGroup.isBinary()) {
            builder.add(HtmlTag.InputAttribute.CHECKED, (Boolean) value);
        } else if (dataGroup.isTemporal()) {
            if (valueType.fieldType().dataType().primitiveType().dataClass().equals(LocalDate.class)) {
                builder.add(HtmlTag.InputAttribute.VALUE, DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) value));
            } else if (valueType.fieldType().dataType().primitiveType().dataClass().equals(LocalTime.class)) {
                builder.add(HtmlTag.InputAttribute.VALUE, DateTimeFormatter.ISO_LOCAL_TIME.format((LocalTime) value));
            } else {
                builder.add(HtmlTag.InputAttribute.VALUE,
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) value));
            }
        } else if (dataGroup.isDecimal()) {
            builder.add(HtmlTag.InputAttribute.VALUE, ((Number) value).doubleValue());
        } else if (dataGroup.isNumeric()) {
            builder.add(HtmlTag.InputAttribute.VALUE, ((Number) value).intValue());
        } else if (dataGroup.isEnumerable()) {
            builder.add(HtmlTag.InputAttribute.VALUE, value.toString());
        } else {
            builder.add(HtmlTag.InputAttribute.VALUE, value.toString());
        }
    }
}
