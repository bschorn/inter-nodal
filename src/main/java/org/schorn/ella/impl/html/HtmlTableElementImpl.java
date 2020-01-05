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
import org.schorn.ella.html.ActiveHtml.HtmlTableElement;

/**
 *
 * @author schorn
 *
 */
public class HtmlTableElementImpl extends HtmlElementImpl implements HtmlTableElement {

    public HtmlTableElementImpl() {
        super("table");
    }

    static public class HtmlCaptionElementImpl extends HtmlElementImpl implements HtmlCaptionElement {

        public HtmlCaptionElementImpl() {
            super("caption");
        }
    }

    /*
	 * 
     */
    static public class HtmlTHeadElementImpl extends HtmlElementImpl implements HtmlTHeadElement {

        public HtmlTHeadElementImpl() {
            super("thead");
        }
    }

    /*
	 * 
     */
    static public class HtmlTFootElementImpl extends HtmlElementImpl implements HtmlTFootElement {

        public HtmlTFootElementImpl() {
            super("tfoot");
        }
    }

    /*
     * 
     */
    static public class HtmlTBodyElementImpl extends HtmlElementImpl implements HtmlTBodyElement {

        public HtmlTBodyElementImpl() {
            super("tbody");
        }
    }

    /*
     * 
     */
    static public class HtmlTrElementImpl extends HtmlElementImpl implements HtmlTrElement {

        public HtmlTrElementImpl() {
            super("tr");
        }
    }

    /*
     * 
     */
    static public class HtmlThElementImpl extends HtmlElementImpl implements HtmlThElement {

        public enum Scope {
            row,
            col,
            rowgroup,
            colgroup,
            auto,;
        }
        int colspan;

        public HtmlThElementImpl() {
            super("th");
            this.colspan = 1;
        }

        public void setColspan(int colspan) throws Exception {
            this.colspan = colspan;
            this.addAttribute0(ActiveHtml.HtmlAttribute.create("colspan", colspan));
        }

        public int getColspan() {
            return this.colspan;
        }
    }

    /*
     * 
     */
    static public class HtmlTdElementImpl extends HtmlElementImpl implements HtmlTdElement {

        int colspan;

        public HtmlTdElementImpl() {
            super("td");
            this.colspan = 1;
        }

        public void setColspan(int colspan) throws Exception {
            this.colspan = colspan;
            this.addAttribute0(ActiveHtml.HtmlAttribute.create("colspan", colspan));
        }

        public int getColspan() {
            return this.colspan;
        }
    }

}
