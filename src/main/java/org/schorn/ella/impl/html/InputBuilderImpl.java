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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlInputElement;
import org.schorn.ella.html.ActiveHtml.InputBuilder;
import org.schorn.ella.impl.html.HtmlTag.TagAttribute;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
class InputBuilderImpl implements InputBuilder {

    static final Logger LGR = LoggerFactory.getLogger(InputBuilderImpl.class);

    ObjectType objectType;
    ValueType valueType;
    HtmlInputElement.Type inputType;
    String name;
    String id;
    boolean required = true;
    boolean readonly = false;
    Map<TagAttribute, Object> attributes = new HashMap<>();

    InputBuilderImpl() {
    }

    InputBuilderImpl(ObjectType compositeType, ValueType valueType) {
        this.objectType = compositeType;
        this.valueType = valueType;
        if (compositeType.isUniqueKey(valueType) && !compositeType.isNaturalKey(valueType)) {
            this.inputType = HtmlInputElement.Type.HIDDEN;
        } else {
            this.inputType = InputBuilder.getInputType(valueType);
        }
        this.required = !this.objectType.isOptional(this.valueType);
        this.readonly = this.objectType.isUniqueKey(this.valueType);
        this.attributes();
    }

    public HtmlInputElement.Type getInputType() {
        return this.inputType;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void attributes() {
        attributes.put(HtmlTag.InputAttribute.TYPE, this.inputType.value());
        attributes.put(HtmlTag.InputAttribute.READONLY, this.readonly);
        attributes.put(HtmlTag.InputAttribute.REQUIRED, this.required);
        List<ConstraintType<?>> constraintTypes = valueType.fieldType().constraints().constraintTypes();
        constraintTypes.stream().forEach(constraintType -> {
            ConstraintData constraintData = valueType.fieldType().constraints().constraint(constraintType);
            List<Object> values = constraintData.constraintValues();
            if (values != null && !values.isEmpty()) {
                String constraintName = constraintType.name();
                ConstraintType.StandardTypes standardType = ConstraintType.StandardTypes.valueOf(constraintName);
                DataGroup dataGroup = constraintData.constraintType().dataGroup();
                switch (dataGroup) {
                    case BOOL:
                        break;
                    case DATE:
                        switch (standardType) {
                            case min_date:
                                attributes.put(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) values.get(0)));
                                break;
                            case max_date:
                                attributes.put(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_DATE.format((LocalDate) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case TIME:
                        switch (standardType) {
                            case min_time:
                                attributes.put(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_TIME.format((LocalTime) values.get(0)));
                                break;
                            case max_time:
                                attributes.put(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_TIME.format((LocalTime) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case TIMESTAMP:
                        switch (standardType) {
                            case min_datetime:
                                attributes.put(HtmlTag.InputAttribute.MIN,
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) values.get(0)));
                                break;
                            case max_datetime:
                                attributes.put(HtmlTag.InputAttribute.MAX,
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format((LocalDateTime) values.get(0)));
                                break;
                            default:
                                break;
                        }
                        break;
                    case NUMBER:
                        switch (standardType) {
                            case min_integer:
                                attributes.put(HtmlTag.InputAttribute.MIN, values.get(0).toString());
                                break;
                            case max_integer:
                                attributes.put(HtmlTag.InputAttribute.MAX, values.get(0).toString());
                                break;
                            case inc_integer:
                                attributes.put(HtmlTag.InputAttribute.STEP, values.get(0).toString());
                                break;
                        }
                        break;
                    case DECIMAL:
                        switch (standardType) {
                            case min_decimal:
                                attributes.put(HtmlTag.InputAttribute.MIN, values.get(0).toString());
                                break;
                            case max_decimal:
                                attributes.put(HtmlTag.InputAttribute.MAX, values.get(0).toString());
                                break;
                            default:
                                break;
                        }
                        break;
                    case TEXT:
                        switch (standardType) {
                            case pattern:
                                attributes.put(HtmlTag.InputAttribute.PATTERN, values.get(0).toString());
                                break;
                            default:
                                break;
                        }
                        break;
                    case ENUM:
                        switch (standardType) {
                            case list:
                                List<ActiveHtml.HtmlSelectElement.ValueLabel> valueLabels = new ArrayList<>();
                                values.forEach(item -> {
                                    try {
                                        valueLabels
                                                .add(ActiveHtml.HtmlSelectElement.ValueLabel.create(item.toString(), item.toString()));
                                    } catch (Exception ex) {
                                        LGR.error(Functions.getStackTraceAsString(ex));
                                    }
                                });
                                attributes.put(HtmlTag.InputAttribute.LIST, valueLabels);
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

    }

    @Override
    public HtmlInputElement build() throws Exception {
        HtmlInputElement inputElement = ActiveHtml.HtmlInputElement.create();
        inputElement.setName(this.name);
        inputElement.setId(this.id);
        for (Map.Entry<HtmlTag.TagAttribute, Object> entry : this.attributes.entrySet()) {
            inputElement.addAttribute(ActiveHtml.HtmlAttribute.create(entry.getKey().attributeName(), entry.getValue()));
        }
        return inputElement;
    }

}
