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
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.TableData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.repo.RepoSupport.QueryData;

/**
 * This extension is a convenience method into the HTML library.
 *
 * @author schorn
 *
 */
public interface ArrayHtml {

    default HtmlElement htmlTable() {
        if (this instanceof TableData) {
            try {
                return HtmlProvider.provider().html_table((TableData) this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this instanceof QueryData) {
            try {
                QueryData queryData = (QueryData) this;
                TableData tableData = TableData.create(queryData.arrayData(), queryData.title());
                return HtmlProvider.provider().html_table(tableData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this instanceof ArrayData) {
            try {
                ArrayData arrayData = (ArrayData) this;
                TableData tableData = TableData.create(arrayData, arrayData.name());
                return HtmlProvider.provider().html_table(tableData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    default HtmlElement htmlSelect(String valueName, String labelName) {
        if (this instanceof ArrayData) {
            try {
                //String HtmlProvider.provider()
                return HtmlProvider.provider().html_select((ArrayData) this, valueName, labelName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
