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

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabelElement;
import org.schorn.ella.html.ActiveHtml.HtmlSelectElement;
import org.schorn.ella.html.ActiveHtml.SelectBuilder;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 *
 * <div class="addClassDiv()">
 * <label for="setId()" class="addClassLabel()">setTextContentLabel()</label>
 * <select name="setName()" id="setId()" class="addSelectClass()">
 * <option value="addOption(value)">addOption(content)</option>
 * </select>
 * </div>
 *
 * @author schorn
 *
 */
public class SelectBuilderImpl implements SelectBuilder {

    private final List<String> listClassDiv = new ArrayList<>(1);
    private final List<String> listClassLabel = new ArrayList<>(1);
    private final List<String> listClassSelect = new ArrayList<>(1);
    private String id = null;
    private String name = null;
    private String label = null;
    private final List<String> options = new ArrayList<>(10);

    /**
     *
     */
    SelectBuilderImpl() {
        // For inheritance
    }

    /**
     *
     * @param arrayData
     * @param valueName
     * @param labelName
     */
    public SelectBuilderImpl(ArrayData arrayData, String valueName, String labelName) {
        ActiveType memberType = arrayData.memberType();
        switch (memberType.role()) {
            case Value:
                arrayData.nodes().stream()
                        .map(ad -> ValueData.class.cast(ad))
                        .forEach(vd -> this.addOption(vd.activeValue().toString(), vd.activeValue().toString()));
                break;
            case Object:
                ObjectType objectType = (ObjectType) memberType;
                ObjectSchema schema = objectType.schema();
                ValueType valueType = ValueType.get(arrayData.context(), valueName);
                ValueType labelType = ValueType.get(arrayData.context(), labelName);
                if (valueType != null) {
                    MemberDef valueDef = schema.get(valueType);
                    MemberDef labelDef = schema.get(labelType);
                    if (valueDef == null) {
                        break;
                    }
                    if (labelDef == null) {
                        labelType = valueType;
                    }
                    for (ActiveData activeData : arrayData.nodes()) {
                        ObjectData objectData = (ObjectData) activeData;
                        String valueStr = objectData.get(valueType).activeValue().toString();
                        String labelStr = objectData.get(labelType).activeValue().toString();
                        this.addOption(valueStr, labelStr);
                    }
                } else {
                    List<MemberDef> keys = schema.keys();
                    if (keys.size() == 1) {
                        MemberDef keyDef = keys.get(0);
                        valueName = keyDef.activeType().name();
                        this.setId(String.format("%s-%s", arrayData.name(), valueName));
                        this.setName(valueName);
                        List<MemberDef> naturalKeys = schema.naturalKeys();
                        StringJoiner joinerLabel = new StringJoiner(" ", "", "");
                        naturalKeys.forEach(md -> joinerLabel.add(md.activeType().name()));
                        labelName = joinerLabel.toString();
                        String label = HtmlProvider.provider().labeler().get(arrayData.memberType(), valueType);
                        if (label == null) {
                            label = HtmlProvider.provider().labeler().get(valueType);
                        }
                        if (label == null) {
                            this.setLabel(labelName);
                        } else {
                            this.setLabel(label);
                        }
                        for (ActiveData activeData : arrayData.nodes()) {
                            ObjectData objectData = (ObjectData) activeData;
                            List<ValueData> keyValues = objectData.getKeyValues();
                            List<ValueData> naturalKeyValues = objectData.getNaturalKeyValues();
                            StringJoiner joiner = new StringJoiner(" ", "", "");
                            naturalKeyValues.forEach(vd -> joiner.add(vd.activeValue().toString()));
                            this.addOption(keyValues.get(0).toString(), joiner.toString());
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void addClassDiv(String... divClasses) {
        for (String className : divClasses) {
            this.listClassDiv.add(className);
        }
    }

    @Override
    public void addClassLabel(String... labelClasses) {
        for (String className : labelClasses) {
            this.listClassLabel.add(className);
        }
    }

    @Override
    public void addClassSelect(String... selectClasses) {
        for (String className : selectClasses) {
            this.listClassSelect.add(className);
        }
    }

    @Override
    public void addOption(String value, String content) {
        this.options.add(String.format("%s~%s", value, content == null ? value : content));
    }

    @Override
    public HtmlElement build() throws Exception {
        if (this.id == null) {
            throw new Exception(String.format("%s.build() - id was not set", this.getClass().getSimpleName()));
        }
        if (this.name == null) {
            throw new Exception(String.format("%s.build() - name was not set", this.getClass().getSimpleName()));
        }
        if (this.label == null) {
            this.label = this.name;
        }
        HtmlElement divElement = ActiveHtml.HtmlDivElement.create();
        this.listClassDiv.forEach(className -> divElement.addClass(className));

        HtmlLabelElement labelElement = ActiveHtml.HtmlLabelElement.create();
        this.listClassLabel.forEach(className -> labelElement.addClass(className));
        labelElement.setFor(this.id);
        labelElement.setTextContent(this.label);
        divElement.append(labelElement);

        HtmlSelectElement selectElement = ActiveHtml.HtmlSelectElement.create();
        this.listClassSelect.forEach(className -> selectElement.addClass(className));
        selectElement.setId(this.id);
        selectElement.setName(this.name);
        for (String option : this.options) {
            String[] valueLabel = option.split("~");
            selectElement.addOption(valueLabel[0], valueLabel[1]);
        }
        divElement.append(selectElement);
        return divElement;
    }

}
