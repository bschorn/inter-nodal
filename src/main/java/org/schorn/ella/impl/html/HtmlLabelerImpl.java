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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlLabeler;
import org.schorn.ella.io.ResourceReader;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class HtmlLabelerImpl extends AbstractContextual implements HtmlLabeler, Consumer<String> {

    private static final Logger LGR = LoggerFactory.getLogger(HtmlLabelerImpl.class);

    Map<ActiveType, String> typeMap = new HashMap<>();
    Map<ActiveType, Map<ActiveType, String>> objectMap = new HashMap<>();

    public HtmlLabelerImpl(AppContext context) throws Exception {
        super(context);
        URI labelsURI = ActiveHtml.Config.get(context).htmlLabels();
        if (labelsURI != null) {
            ResourceReader.readLines(labelsURI.toURL(), this);
        }
    }

    @Override
    public String get(AppContext context, String label_type) {
        ActiveType labelType = ActiveType.get(context, label_type);
        return get(labelType);
    }

    @Override
    public String get(ActiveType labelType) {
        return get(null, labelType);
    }

    @Override
    public String get(AppContext context, String parent_type, String label_type) {
        ActiveType parentType = ObjectType.get(context, parent_type);
        ActiveType labelType = ActiveType.get(context, label_type);
        return get(parentType, labelType);
    }

    @Override
    public String get(ActiveType parentType,
            ActiveType labelType) {
        String label;
        if (parentType == null && labelType == null) {
            return null;
        } else if (parentType == null && labelType != null) {
            label = this.typeMap.get(labelType);
            if (label == null) {
                return labelType.name();
            }
            return label;
        } else if (parentType != null && labelType == null) {
            label = this.typeMap.get(parentType);
            if (label == null) {
                return parentType.name();
            }
            return label;
        } else {
            Map<ActiveType, String> childLabels
                    = this.objectMap.get(parentType);
            if (childLabels == null) {
                label = this.typeMap.get(labelType);
                if (label == null) {
                    return labelType.name();
                } else {
                    return label;
                }
            } else {
                label = childLabels.get(labelType);
                if (label == null) {
                    return labelType.name();
                } else {
                    return label;
                }
            }
        }
    }

    @Override
    public HtmlLabeler set(ActiveType ActiveType, String label) {
        this.typeMap.put(ActiveType, label);
        return (HtmlLabeler) this;
    }

    @Override
    public HtmlLabeler set(ActiveType parentType,
            ActiveType labelType, String label) {
        if (parentType == null && labelType == null) {
            return this;
        } else if (parentType == null && labelType != null) {
            if (label == null) {
                this.typeMap.remove(labelType);
            } else {
                this.typeMap.put(labelType, label);
            }
        } else if (parentType != null && labelType == null) {
            if (label == null) {
                this.typeMap.remove(parentType);
            } else {
                this.typeMap.put(parentType, label);
            }
        } else {
            Map<ActiveType, String> childLabels
                    = this.objectMap.get(parentType);
            if (childLabels == null) {
                childLabels = new HashMap<>();
                this.objectMap.put(parentType, childLabels);
            }
            if (label == null) {
                childLabels.remove(labelType);
            } else {
                childLabels.put(labelType, label);
            }
        }
        return this;
    }

    @Override
    public void accept(String line) {
        try {
            String[] fields = line.split("\\|");
            if (fields.length == 2) {
                String label = fields[1];
                String[] typeParts = fields[0].split("\\.");
                if (typeParts.length == 2) {
                    String firstPart = typeParts[0];
                    String secondPart = typeParts[1];
                    ActiveNode.ActiveType labeledType;
                    ActiveNode.ActiveType parentType = null;
                    switch (firstPart) {
                        case "ValueType":
                            labeledType = ActiveNode.ValueType.get(this.context(), secondPart);
                            break;
                        case "ObjectType":
                            labeledType = ActiveNode.ObjectType.get(this.context(), secondPart);
                            break;
                        default:
                            parentType = ActiveNode.ObjectType.get(this.context(), firstPart);
                            labeledType = ActiveNode.ValueType.get(this.context(), secondPart);
                            break;
                    }
                    try {
                        if (parentType != null && labeledType != null && label != null) {
                            this.set(parentType, labeledType, label);
                        } else if (labeledType != null && label != null) {
                            this.set(labeledType, label);
                        }
                    } catch (Exception ex) {
                        LGR.error("{}.loadLabels({}) - Caught Exception: {}",
                                Functions.stackTraceToString(ex));
                    }
                }
            }
        } catch (Exception ex) {
            LGR.error("{}.initLabels({}) -  Caught Exception: {}",
                    this.context().name(),
                    Functions.stackTraceToString(ex));
        }
    }

}
