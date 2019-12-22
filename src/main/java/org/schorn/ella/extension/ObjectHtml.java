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
package org.schorn.ella.extension;

import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This extension is a convenience method into the HTML library.
 *
 * @author schorn
 *
 */
public interface ObjectHtml {

    static final Logger LGR = LoggerFactory.getLogger(ObjectHtml.class);

    default HtmlElement htmlForm() {
        if (this instanceof ObjectType) {
            try {
                return HtmlProvider.provider().html_form((ObjectType) this);
            } catch (Exception e) {
                LGR.error("{}.htmlForm() - Caught Exception: {}",
                        this.getClass().getSimpleName(),
                        Functions.stackTraceToString(e));
            }
        }
        if (this instanceof ObjectData) {
            try {
                ObjectData objectData = (ObjectData) this;
                return HtmlProvider.provider().html_form(objectData.objectType());
            } catch (Exception e) {
                LGR.error("{}.htmlForm() - Caught Exception: {}",
                        this.getClass().getSimpleName(),
                        Functions.stackTraceToString(e));
            }
        }
        return null;
    }
}
