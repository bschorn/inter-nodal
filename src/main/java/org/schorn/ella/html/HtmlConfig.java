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
import org.schorn.ella.ComponentProperties;
import org.schorn.ella.app.BaseConfig;
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
public enum HtmlConfig implements BaseConfig {
    HTML_FORM_CLASS(HtmlProvider.class, "HtmlFormClass", "node-form"),
    HTML_FORM_LABEL_CLASS(HtmlProvider.class, "HtmlFormLabelClass", "node-form-label"),
    HTML_INPUT_CLASS(HtmlProvider.class, "HtmlInputClass", "node-input"),
    HTML_INPUT_LABEL_CLASS(HtmlProvider.class, "HtmlInputLabelClass", "node-input-label"),
    HTML_SELECT_CLASS(HtmlProvider.class, "HtmlSelectClass", "node-select"),
    HTML_SELECT_LABEL_CLASS(HtmlProvider.class, "HtmlSelectLabelClass", "node-select-label"),

    /**
     *
     */
    HTML_TABLE_CLASS(HtmlProvider.class, "HtmlTableClass", "node-table"),
    HTML_TABLE_THEAD_CLASS(HtmlProvider.class, "HtmlTableTHeadClass", "node-table-thead"),
    HTML_TABLE_TFOOT_CLASS(HtmlProvider.class, "HtmlTableTFootClass", "node-table-tfoot"),
    HTML_TABLE_TBODY_CLASS(HtmlProvider.class, "HtmlTableTBodyClass", "node-table-tbody"),

    /**
     *
     */
    HTML_TABLE_TROW_CLASS(HtmlProvider.class, "HtmlTableTRowClass", "node-table-trow"),
    HTML_TABLE_TCOL_CLASS(HtmlProvider.class, "HtmlTableTColClass", "node-table-tcol"),
    HTML_TABLE_SMALL_CAPTION_CLASS(HtmlProvider.class, "HtmlTableSmallCaptionClass", "active-table-caption-small"),;

    private static final Logger LGR = LoggerFactory.getLogger(HtmlConfig.class);

    Class<?> propertyOwner;
    String propertyKey;
    String defaultValue;

    HtmlConfig(Class<?> propertyOwner, String propertyKey, String defaultValue) {
        this.propertyOwner = propertyOwner;
        this.propertyKey = propertyKey;
        this.defaultValue = defaultValue;
    }

    /**
     *
     * @return
     */
    @Override
    public String propertyName() {
        return this.name();
    }

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
        return ComponentProperties.HTML.properties();
    }

    static public String dump() {
        StringJoiner joiner = new StringJoiner("\n\t", "[\n\t", "\n]\n");
        for (HtmlConfig config : HtmlConfig.values()) {
            joiner.add(String.format("%-35s: %-40 -> %-60", config.name(), config.propertyKey, config.value()));
        }
        return joiner.toString();
    }

}
