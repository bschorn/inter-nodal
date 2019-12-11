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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement;
import org.schorn.ella.html.ActiveHtml.TableBuilder;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlCaptionElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlTBodyElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlTFootElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlTHeadElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlTdElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlThElement;
import org.schorn.ella.html.ActiveHtml.HtmlTableElement.HtmlTrElement;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
class TableBuilderImpl implements TableBuilder {

    static final Logger LGR = LoggerFactory.getLogger(TableBuilderImpl.class);

    String caption;
    String cssClassCaption;
    List<HtmlTrElement> headerRows = null;
    List<HtmlTrElement> footerRows = null;
    List<HtmlTrElement> bodyRows = null;

    public void setCaption(String caption) {
        this.caption = caption;
        this.cssClassCaption = null;
    }

    @Override
    public void setCaption(String caption, String cssClassCaption) {
        this.caption = caption;
        this.cssClassCaption = cssClassCaption;
    }

    public void addHeaderRow(List<ActiveType> nodeTypes) throws Exception {
        if (this.headerRows == null) {
            this.headerRows = new ArrayList<>();
        }
        addRow(nodeTypes, this.headerRows);
    }

    public void addFooterRow(List<ActiveType> nodeTypes) throws Exception {
        if (this.footerRows == null) {
            this.footerRows = new ArrayList<>();
        }
        addRow(nodeTypes, this.footerRows);
    }

    private void addRow(List<ActiveType> nodeTypes, List<HtmlTrElement> rows) throws Exception {
        List<HtmlThElement> thElements = new ArrayList<>();
        for (ActiveType nodeType : nodeTypes) {
            HtmlThElement thElement;
            if (nodeType == null) {
                thElement = thElements.get(thElements.size() - 1);
                thElement.setColspan(thElement.getColspan() + 1);
            } else {
                thElement = ActiveHtml.HtmlTableElement.HtmlThElement.create();
                thElement.setTextContent(nodeType.name());
                if (nodeType instanceof ValueType) {
                    FieldDataType fdType = FieldDataType.convert((ValueType) nodeType);
                    String className = fdType.getClassName();
                    if (className != null) {
                        thElement.addClass(className);
                    }
                }
                thElements.add(thElement);
            }
        }
        HtmlTrElement trElement = ActiveHtml.HtmlTableElement.HtmlTrElement.create();
        thElements.forEach(e -> trElement.append(e));
        rows.add(trElement);
    }

    public void addBodyRow(List<ActiveData> nodes) throws Exception {
        if (this.bodyRows == null) {
            this.bodyRows = new ArrayList<>();
        }
        List<HtmlTdElement> tdElements = new ArrayList<>();
        for (ActiveData node : nodes) {
            HtmlTdElement tdElement;
            if (node == null) {
                tdElement = tdElements.get(tdElements.size() - 1);
                tdElement.setColspan(tdElement.getColspan() + 1);
            } else {
                tdElement = ActiveHtml.HtmlTableElement.HtmlTdElement.create();
                switch (node.role()) {
                    case Value:
                        ValueData nodeValue = (ValueData) node;
                        tdElement.setTextContent(ActiveFormatter.format(nodeValue).toString());
                        FieldDataType fdType = FieldDataType.convert((ValueType) nodeValue.valueType());
                        tdElement.addClass(fdType.getClassName());
                        break;
                    case Array:
                        tdElement.setTextContent("[]");
                        break;
                    case Object:
                        tdElement.setTextContent("{}");
                        break;
                    default:
                        break;
                }
                tdElements.add(tdElement);
            }
        }
        HtmlTrElement trElement = ActiveHtml.HtmlTableElement.HtmlTrElement.create();
        tdElements.forEach(e -> trElement.append(e));
        this.bodyRows.add(trElement);
    }

    @Override
    public HtmlTableElement build() throws Exception {
        HtmlTableElement tableElement = ActiveHtml.HtmlTableElement.create();
        if (this.caption != null) {
            HtmlCaptionElement captionElement = ActiveHtml.HtmlTableElement.HtmlCaptionElement.create();
            captionElement.setTextContent(this.caption);
            if (this.cssClassCaption != null) {
                captionElement.addClass(this.cssClassCaption);
            }
            tableElement.append(captionElement);
        }
        if (this.headerRows != null && this.headerRows.size() > 0) {
            HtmlTHeadElement theadElement = ActiveHtml.HtmlTableElement.HtmlTHeadElement.create();
            headerRows.forEach(e -> theadElement.append(e));
            tableElement.append(theadElement);
        }
        if (this.bodyRows != null && this.bodyRows.size() > 0) {
            HtmlTBodyElement tbodyElement = ActiveHtml.HtmlTableElement.HtmlTBodyElement.create();
            bodyRows.forEach(e -> tbodyElement.append(e));
            tableElement.append(tbodyElement);
        }
        if (this.footerRows != null && this.footerRows.size() > 0) {
            HtmlTFootElement tfootElement = ActiveHtml.HtmlTableElement.HtmlTFootElement.create();
            footerRows.forEach(e -> tfootElement.append(e));
            tableElement.append(tfootElement);
        }
        return tableElement;
    }

}
