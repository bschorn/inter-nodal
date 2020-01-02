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
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.FormBuilder;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlFormElement;

/**
 *
 * @author schorn
 *
 */
public class FormBuilderImpl implements FormBuilder {

    private String name = null;
    private String id = null;
    private List<HtmlElement> inputs = new ArrayList<>();

    FormBuilderImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void addInput(HtmlElement htmlElement) {
        this.inputs.add(htmlElement);
    }

    @Override
    public HtmlFormElement build() throws Exception {
        HtmlFormElement formElement = ActiveHtml.HtmlFormElement.create();
        formElement.setName(this.name);
        formElement.setId(this.id);
        this.inputs.stream().forEach(e -> formElement.append(e));
        return formElement;
    }

}
