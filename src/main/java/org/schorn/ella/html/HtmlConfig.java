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

import java.util.Properties;
import java.util.StringJoiner;
import org.schorn.ella.ActiveConfig;
import org.schorn.ella.Component;
import org.schorn.ella.node.DataGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * CONFIG_ENUM(PropertyOwner, PropertyKey, DefaultValue)
 *
 * PropertyOwner - the class from which the property will be accessed/used
 * (error will be logged if accessed by non-owner class) PropertyKey - the KEY
 * in System.getProperty(KEY,default); DefaultValue - the DEFAULT in
 * System.getProperty(key,DEFAULT);
 *
 * @author schorn
 *
 */
public enum HtmlConfig implements ActiveConfig {
    HTML_LABEL(HtmlProvider.class, "Html.Label", DataGroup.TEXT, null, null),
    HTML_FORM_CLASS(HtmlProvider.class, "HtmlClass.Form", DataGroup.TEXT, null, "node-form"),
    HTML_FORM_LABEL_CLASS(HtmlProvider.class, "HtmlClass.FormLabel", DataGroup.TEXT, null, "node-form-label"),
    HTML_INPUT_CLASS(HtmlProvider.class, "HtmlClass.Input", DataGroup.TEXT, null, "node-input"),
    HTML_INPUT_LABEL_CLASS(HtmlProvider.class, "HtmlClass.InputLabel", DataGroup.TEXT, null, "node-input-label"),
    HTML_SELECT_CLASS(HtmlProvider.class, "HtmlClass.Select", DataGroup.TEXT, null, "node-select"),
    HTML_SELECT_LABEL_CLASS(HtmlProvider.class, "HtmlClass.SelectLabel", DataGroup.TEXT, null, "node-select-label"),
    HTML_TABLE_CLASS(HtmlProvider.class, "HtmlClass.Table", DataGroup.TEXT, null, "node-table"),
    HTML_TABLE_THEAD_CLASS(HtmlProvider.class, "HtmlClass.TableTHead", DataGroup.TEXT, null, "node-table-thead"),
    HTML_TABLE_TFOOT_CLASS(HtmlProvider.class, "HtmlClass.TableTFoot", DataGroup.TEXT, null, "node-table-tfoot"),
    HTML_TABLE_TBODY_CLASS(HtmlProvider.class, "HtmlClass.TableTBody", DataGroup.TEXT, null, "node-table-tbody"),
    HTML_TABLE_TROW_CLASS(HtmlProvider.class, "HtmlClass.TableTRow", DataGroup.TEXT, null, "node-table-trow"),
    HTML_TABLE_TCOL_CLASS(HtmlProvider.class, "HtmlClass.TableTCol", DataGroup.TEXT, null, "node-table-tcol"),
    HTML_TABLE_SMALL_CAPTION_CLASS(HtmlProvider.class, "HtmlClass.TableSmallCaption", DataGroup.TEXT, null, "active-table-caption-small"),;

    private static final Logger LGR = LoggerFactory.getLogger(HtmlConfig.class);

    private final Class<?> propertyOwner;
    private final String propertyKey;
    private final String defaultValue;
    private final DataGroup dataGroup;
    private final String delimiter;


    HtmlConfig(Class<?> propertyOwner, String propertyKey, DataGroup dataGroup, String delimiter, String defaultValue) {
        this.propertyOwner = propertyOwner;
        this.propertyKey = propertyKey;
        this.dataGroup = dataGroup;
        this.delimiter = delimiter;
        this.defaultValue = defaultValue;
    }

    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    /*
    @Override
    public boolean isMultiValue() {
        return this.delimiter != null;
    }
     */

    @Override
    public String delimiter() {
        return this.delimiter;
    }

    /*
    @Override
    public String propertyName() {
        return this.name();
    }
     */

    @Override
    public Class<?> propertyOwner() {
        return this.propertyOwner;
    }

    @Override
    public String propertyKey() {
        return this.propertyKey;
    }

    @Override
    public String defaultValue() {
        return this.defaultValue;
    }

    @Override
    public Logger logger() {
        return LGR;
    }

    @Override
    public Properties properties() {
        return Component.HTML.properties();
    }

    static public String dump() {
        StringJoiner joiner = new StringJoiner("\n\t", "[\n\t", "\n]\n");
        for (HtmlConfig config : HtmlConfig.values()) {
            joiner.add(String.format("%-35s: %-40 -> %-60", config.name(), config.propertyKey, config.asString()));
        }
        return joiner.toString();
    }

}
