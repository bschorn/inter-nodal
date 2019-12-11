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

import java.util.HashMap;
import java.util.Map;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml.HtmlLabeler;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectType;

/**
 *
 * @author schorn
 *
 */
class HtmlLabelerImpl implements HtmlLabeler {

    Map<ActiveType, String> typeMap = new HashMap<>();
    Map<ActiveType, Map<ActiveType, String>> objectMap = new HashMap<>();

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
        String label = null;
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

}
