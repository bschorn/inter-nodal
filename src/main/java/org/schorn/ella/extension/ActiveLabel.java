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

import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.node.ActiveNode.ActiveType;

/**
 * ActiveLabel provides a way to utilize a more appealing label for the HTML
 * components. By default the ActiveType.name() is used unless a label is
 * provided.
 *
 * Example: ActiveType.name(): "street_address" ActiveType.label(): "Street
 * Address"
 *
 * @author schorn
 *
 */
public interface ActiveLabel {

    /**
     * Returns the label for an ActiveType (ValueType, ObjectType, ArrayType)
     *
     * @return String
     */
    default String label() {
        if (this instanceof ActiveType) {
            ActiveType activeType = (ActiveType) this;
            try {
                //return ActiveHtml.HtmlLabeler.get().get(activeType);
                return HtmlProvider.provider().labeler().get(activeType);
            } catch (Exception e) {
                return activeType.name();
            }
        }
        return null;
    }

}
