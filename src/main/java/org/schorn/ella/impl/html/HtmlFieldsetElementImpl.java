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

import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlDivElement;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlFieldsetElement;
import org.schorn.ella.html.ActiveHtml.HtmlInputElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabelElement;

/**
 *
 * @author schorn
 *
 */
public class HtmlFieldsetElementImpl extends HtmlElementImpl implements HtmlFieldsetElement {

    HtmlTag.InputType inputType;
    HtmlDivElement divElement;
    HtmlElement legendElement;

    public HtmlFieldsetElementImpl(HtmlTag.InputType inputType) throws Exception {
        super("fieldset");
        this.inputType = inputType;
        this.legendElement = ActiveHtml.HtmlElement.create("legend");
        this.divElement = ActiveHtml.HtmlDivElement.create();
        this.divElement.append(this.legendElement);
        this.append(this.divElement);
    }

    public void setLegend(String value) {
        this.legendElement.setTextContent(value);
    }

    public void addInput(String id, String name, String label) throws Exception {
        HtmlInputElement inputElement = HtmlInputElement.create(); //this.inputType);
        HtmlLabelElement labelElement = HtmlLabelElement.create();
        labelElement.setFor(inputElement.getId());
        switch (this.inputType) {
            case RADIO:
            case CHECKBOX:
                this.divElement.append(inputElement);
                this.divElement.append(labelElement);
                this.divElement.append(ActiveHtml.HtmlSingleElement.create("br"));
                break;
            default:
                HtmlDivElement inputDivElement = HtmlDivElement.create();
                inputDivElement.append(labelElement);
                inputDivElement.append(inputElement);
                this.divElement.append(inputDivElement);
                break;
        }
    }
}
