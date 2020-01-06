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
package org.schorn.ella.html;

import org.schorn.ella.Provider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabeler;
import org.schorn.ella.html.ActiveHtml.HtmlPageElement;
import org.schorn.ella.html.ActiveHtml.TableData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface HtmlProvider extends Provider {

    /**
     * Returns the configured implementation of HtmlProvider
     *
     * @return
     */
    static HtmlProvider provider() {
        return Provider.Providers.HTML.getInstance(HtmlProvider.class);
    }

    /**
     * Returns a HtmlLabeler
     *
     * @return
     */
    public HtmlLabeler html_labeler(AppContext context) throws Exception;

    /**
     *
     * @param htmlHead
     * @param htmlBody
     * @return
     * @throws Exception
     */
    public HtmlPageElement html_page(AppContext context) throws Exception;

    /**
     * Creates the User Interface element block containing a form <form>
     * base off the schema of a ObjectType. The contents will be one or more
     * <input> blocks created by call the html_input() on each member of the
     * ObjectType.
     *
     *
     * @param objectType
     * @return
     * @throws Exception
     */
    public HtmlElement html_form(ObjectType objectType) throws Exception;

    /**
     * * Not Implemented *
     *
     * @param objectType
     * @return
     * @throws Exception
     */
    public HtmlElement html_menu(ObjectType objectType) throws Exception;

    /**
     * Creates the User Interface element block containing an input <input>
     * based off the constraints of a ValueType. If the value is provided it
     * will be included in the <input>.
     *
     * @param objectType
     * @param valueType
     * @param value
     * @return
     * @throws Exception
     */
    public HtmlElement html_input(ObjectType objectType, ValueType valueType, Object value) throws Exception;

    /**
     * Creates the User Interface element block containing a drop-down list
     * <select>
     * based off the constraints of an enum ValueType.
     *
     * @param objectType
     * @param valueType
     * @param value
     * @return
     * @throws Exception
     */
    public HtmlElement html_list(ObjectType objectType, ValueType valueType, Object value) throws Exception;

    /**
     * Creates the User Interface element block containing a drop-down list
     * <select>
     * based off the data in the ArrayData.
     *
     * @param String valueName
     * @param String labelName
     * @return HtmlElement
     * @throws Exception
     */
    public HtmlElement html_select(ArrayData arrayData, String valueName, String labelName) throws Exception;

    /**
     * Creates the User Interface element block containing a table <table>
     * based off the contents of an ArrayData.
     *
     * @param structData
     * @return
     * @throws Exception
     */
    public HtmlElement html_table(TableData tableData) throws Exception;

}
