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
import org.schorn.ella.html.ActiveHtml.HtmlBodyElement;
import org.schorn.ella.html.ActiveHtml.HtmlHeadElement;
import org.schorn.ella.html.ActiveHtml.HtmlPageElement;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class HtmlPageElementImpl extends HtmlElementImpl implements HtmlPageElement {

    private static final Logger LGR = LoggerFactory.getLogger(HtmlPageElementImpl.class);

    private final HtmlHeadElement htmlHead;
    private final HtmlBodyElement htmlBody;

    public HtmlPageElementImpl() {
        super("html");
        HtmlHeadElement htmlHead0 = null;
        HtmlBodyElement htmlBody0 = null;
        try {
            htmlHead0 = ActiveHtml.HtmlHeadElement.create();
        } catch (Exception ex) {
            LGR.error("{}.ctor() - HtmlHeadElement.create() - Caught Exception: {}",
                    this.getClass().getSimpleName(),
                    Functions.stackTraceToString(ex));
        }
        try {
            htmlBody0 = ActiveHtml.HtmlBodyElement.create();
        } catch (Exception ex) {
            LGR.error("{}.ctor() - HtmlBodyElement.create() - Caught Exception: {}",
                    this.getClass().getSimpleName(),
                    Functions.stackTraceToString(ex));
        }
        this.htmlHead = htmlHead0;
        this.htmlBody = htmlBody0;
        this.append(this.htmlHead);
        this.append(this.htmlBody);
    }

    @Override
    public ActiveHtml.HtmlHeadElement htmlHead() {
        return this.htmlHead;
    }

    @Override
    public ActiveHtml.HtmlBodyElement htmlBody() {
        return this.htmlBody;
    }

}
