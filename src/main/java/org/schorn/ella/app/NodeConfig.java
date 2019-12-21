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
package org.schorn.ella.app;

import java.util.Properties;
import java.util.StringJoiner;
import org.schorn.ella.ComponentProperties;
import org.schorn.ella.extension.AppContextExt;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.transform.ActiveTransform.DSVLineParser;
import org.schorn.ella.transform.ActiveTransform.OpenNodeToActiveNode;
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
public enum NodeConfig implements BaseConfig {
    ACTIVE_SPEC(ActiveMain.class, "Active.Spec", DataGroup.URL, ",", null),
    ACTIVE_LANG(ActiveMain.class, "Active.Language", DataGroup.TEXT, null, null),
    ACTIVE_LABELS(ActiveMain.class, "Active.Labels", DataGroup.URL, ",", null),
    ACTIVE_CONTEXTS(ActiveMain.class, "Active.Contexts", DataGroup.TEXT, ",", null),
    ACTIVE_METAS(ActiveMain.class, "Active.Metas", DataGroup.TEXT, ",", null),
    ACTIVITY_DIR(ActiveMain.class, "ActivityDir", DataGroup.TEXT, null, "./activity"),
    ACTIVITY_FILE(ActiveMain.class, "ActivityFile", DataGroup.TEXT, null, "activity.{DATE}.{CONTEXT}.log"),
    //LINE_PARSER_CSV(DSVLineParser.class, "Parser.LineParser.CSV", "org.schorn.ella.node.transform.DSVLineParserImpl"),
    LINE_PARSER_CSV_PATTERN(DSVLineParser.class, "Parser_LineParser_CSV_Pattern", DataGroup.TEXT, null, "(?:(?<=\")([^\"]*)(?=\"))|(?<=,|^)([^,]*)(?=,|$)"),
    TABULAR_ALLOW_DYNAMIC_FIELDS(NodeConfig.class, "Tabular_allowDynamicFields_%s", DataGroup.TEXT, null, null),
    TABULAR_ALLOW_DYNAMIC_FIELDS_ALWAYS(NodeConfig.class, "Tabular_allowDynamicFields_*", DataGroup.TEXT, null, "0"),
    AUTO_DYNAMIC_TYPE(OpenNodeToActiveNode.class, "AutoDynamicType", DataGroup.TEXT, null, "0"),
    AUTO_VERSIONING(AppContextExt.class, "AutoVersioning", DataGroup.TEXT, null, "1"),;

    private static final Logger LGR = LoggerFactory.getLogger(NodeConfig.class);

    private final Class<?> propertyOwner;
    private final String propertyKey;
    private final String defaultValue;
    private final DataGroup dataGroup;
    private final String delimiter;

    NodeConfig(Class<?> propertyOwner, String propertyKey, DataGroup dataGroup, String delimiter, String defaultValue) {
        this.propertyOwner = propertyOwner;
        this.propertyKey = propertyKey;
        this.dataGroup = dataGroup;
        this.delimiter = delimiter;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isMultiValue() {
        return this.delimiter != null;
    }

    @Override
    public String delimiter() {
        return this.delimiter;
    }

    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

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
        return ComponentProperties.NODE.properties();
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * Special Configuration Method
     *
     * @param typeName
     * @return
     */
    static public String getTabularAllowDynamicFields(String typeName) {
        String propertyKey = String.format(TABULAR_ALLOW_DYNAMIC_FIELDS.propertyKey, typeName);
        for (NodeConfig config : NodeConfig.values()) {
            if (config.propertyKey.equals(propertyKey)) {
                return config.asString();
            }
        }
        return null;
    }

    static public String dump() {
        StringJoiner joiner = new StringJoiner("\n\t", "[\n\t", "\n]\n");
        for (NodeConfig config : NodeConfig.values()) {
            joiner.add(String.format("%-35s: %-40s %-60s", config.name(), config.propertyKey, config.asString()));
        }
        return joiner.toString();
    }

}


/*
HTML_FORM_CLASS(HtmlProviderImpl.class, "HtmlFormClass", "node-form"),
HTML_FORM_LABEL_CLASS(HtmlProviderImpl.class, "HtmlFormLabelClass", "node-form-label"),
HTML_INPUT_CLASS(HtmlProviderImpl.class, "HtmlInputClass", "node-input"),
HTML_INPUT_LABEL_CLASS(HtmlProviderImpl.class, "HtmlInputLabelClass", "node-input-label"),
HTML_SELECT_CLASS(HtmlProviderImpl.class, "HtmlSelectClass", "node-select"),
HTML_SELECT_LABEL_CLASS(HtmlProviderImpl.class, "HtmlSelectLabelClass", "node-select-label"),
HTML_TABLE_CLASS(HtmlProviderImpl.class, "HtmlTableClass", "node-table"),
HTML_TABLE_THEAD_CLASS(HtmlProviderImpl.class, "HtmlTableTHeadClass", "node-table-thead"),
HTML_TABLE_TFOOT_CLASS(HtmlProviderImpl.class, "HtmlTableTFootClass", "node-table-tfoot"),
HTML_TABLE_TBODY_CLASS(HtmlProviderImpl.class, "HtmlTableTBodyClass", "node-table-tbody"),
HTML_TABLE_TROW_CLASS(HtmlProviderImpl.class, "HtmlTableTRowClass", "node-table-trow"),
HTML_TABLE_TCOL_CLASS(HtmlProviderImpl.class, "HtmlTableTColClass", "node-table-tcol"),
HTML_TABLE_SMALL_CAPTION_CLASS(HtmlProviderImpl.class, "HtmlTableSmallCaptionClass", "active-table-caption-small"),
SQL_TEMPLATE_DIR(ActiveSQL.class, "SQLTemplateDir", "../lds-node/sql"),

 */
