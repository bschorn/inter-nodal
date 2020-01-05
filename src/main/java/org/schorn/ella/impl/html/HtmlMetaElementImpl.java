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

import java.nio.charset.Charset;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlMetaElement;
import org.schorn.ella.html.MetaRules;

/**
 *
 * @author schorn
 *
 */
public class HtmlMetaElementImpl extends HtmlSingleElementImpl implements HtmlMetaElement {

    protected final MetaRules.Attribute attribute;
    protected String value = null;


    public HtmlMetaElementImpl(MetaRules.Attribute attribute) {
        super("meta");
        this.attribute = attribute;
    }

    @Override
    public MetaRules.Attribute attribute() {
        return this.attribute;
    }


    @Override
    public void setValue(String attributeValue) throws Exception {
        this.value = attributeValue;
        ActiveHtml.HtmlAttribute htmlAttribute = ActiveHtml.HtmlAttribute.create(this.attribute.name(), this.value);
        this.addAttribute(htmlAttribute);

    }

    @Override
    public void setValue(Object attributeObject) throws Exception {
        switch (attribute) {
            case charset:
                if (attributeObject instanceof Charset) {
                    setValue(((Charset) attributeObject).name());
                } else {
                    setValue(attributeObject.toString());
                }
                break;
            case http_equiv:
                break;
            case name:
                break;

        }
    }

}
