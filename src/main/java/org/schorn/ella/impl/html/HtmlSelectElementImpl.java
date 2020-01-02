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
import org.schorn.ella.html.ActiveHtml.HtmlAttribute;
import org.schorn.ella.html.ActiveHtml.HtmlOptionElement;
import org.schorn.ella.html.ActiveHtml.HtmlSelectElement;
import org.schorn.ella.node.ActiveNode.ObjectData;

/**
 *
 * @author schorn
 *
 */
public class HtmlSelectElementImpl extends HtmlElementImpl implements HtmlSelectElement {

    /**
     * ValueLabel is a one entry <option> in a <select>
     * Constructors: 1) Integer value, String label 2) String value, String
     * label 3) ObjectData (with field name of 'value' and 'label') 4)
     * ObjectData, ValueType value, ValueType label
     *
     */
    static public class ValueLabelImpl implements HtmlSelectElement.ValueLabel {

        String value;
        String label;

        public ValueLabelImpl(Integer value, String label) {
            this.value = value.toString();
            this.label = label;
        }

        public ValueLabelImpl(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public ValueLabelImpl(ObjectData objectData) {
            this.value = objectData.getValueString("value");
            if (this.value == null) {
                this.value = objectData.name();
            }
            this.label = objectData.getValueString("label");
            if (this.label == null) {
                this.label = this.value;
            }
        }

        public ValueLabelImpl(ObjectData objectData, String value_type, String label_type) {
            this.value = objectData.getValueString(value_type);
            if (this.value == null) {
                this.value = objectData.name();
            }
            this.label = objectData.getValueString(label_type);
            if (this.label == null) {
                this.label = this.value;
            }
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getLabel() {
            return this.label;
        }
    }

    List<HtmlOptionElement> options = new ArrayList<>();

    HtmlSelectElementImpl() {
        super("select");
    }

    HtmlSelectElementImpl(HtmlTag html) throws Exception {
        super(html);
    }

    public void setName(String value) throws Exception {
        if (value != null) {
            this.addAttribute(HtmlAttribute.create("name", value));
        }
    }

    public void addSelectedOption(String value, String label) throws Exception {
        addOption(value, label);
        this.options.forEach(o -> o.unselect());
        this.options.get(this.options.size() - 1).select();
    }

    public void addOption(String value, String label) throws Exception {
        HtmlOptionElement optionElement = HtmlOptionElement.create();
        optionElement.setValue(value);
        optionElement.setTextContent(label);
        this.options.add(optionElement);
        this.append(optionElement);
    }

    public void addOption(ValueLabel valueLabel) throws Exception {
        this.addOption(valueLabel.getValue(), valueLabel.getLabel());
    }
}
