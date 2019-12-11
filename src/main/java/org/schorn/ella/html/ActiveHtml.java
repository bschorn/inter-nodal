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

import java.util.List;

import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface ActiveHtml {

    /**
     *
     */
    interface Render {

        String render();
    }

    /**
     *
     */
    interface HtmlAttribute extends Render {

        static public HtmlAttribute create(String name, Object value) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlAttribute.class, name, value);
        }

        String name();

        Object value();

        void setValue(Object value);

        void addValue(Object value);
    }

    /**
     *
     */
    interface HtmlElement extends Render {

        static public HtmlElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlElement.class, params);
        }

        void setTextContent(String content);

        void append(HtmlElement element);

        /**
         *
         * @param className
         */
        void addClass(String className);

        void addAttribute(HtmlAttribute attribute) throws Exception;

        void setId(String value) throws Exception;

        String getId();
    }

    /**
     *
     */
    interface HtmlDivElement extends HtmlElement {

        static public HtmlDivElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlDivElement.class, params);
        }
    }

    /**
     *
     */
    interface HtmlFieldsetElement extends HtmlElement {

        static public HtmlFieldsetElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlFieldsetElement.class, params);
        }

        void setLegend(String value);

        void addInput(String id, String name, String label) throws Exception;
    }

    /**
     *
     */
    interface HtmlFormElement extends HtmlElement {

        static public HtmlFormElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlFormElement.class, params);
        }

        /**
         *
         * @param name
         * @throws Exception
         */
        void setName(String name) throws Exception;
    }

    /**
     *
     */
    interface HtmlSingleElement extends HtmlElement {

        static public HtmlSingleElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlSingleElement.class, params);
        }

    }

    /**
     *
     */
    interface HtmlInputElement extends HtmlSingleElement {

        static public HtmlInputElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlInputElement.class, params);
        }

        public enum Type {
            CHECKBOX, DATE, DATETIME("datetime-local"),
            HIDDEN, LIST, MONTH, NUMBER,

            /**
             *
             */
            TEXT, TIME;
            String value;

            Type(String value) {
                this.value = value;
            }

            Type() {
                this.value = name().toLowerCase();
            }

            public String value() {
                return this.value;
            }
        }

        void setName(String value) throws Exception;

        void setType(HtmlInputElement.Type inputType) throws Exception;
    }

    /**
     *
     */
    interface HtmlLabelElement extends HtmlElement {

        static public HtmlLabelElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlLabelElement.class, params);
        }

        void setFor(String value) throws Exception;
    }

    /**
     *
     */
    interface HtmlOptionElement extends HtmlElement {

        static public HtmlOptionElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlOptionElement.class, params);
        }

        void setValue(String value) throws Exception;

        void select();

        void unselect();
    }

    /**
     *
     */
    interface HtmlSelectElement extends HtmlElement {

        static public HtmlSelectElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlSelectElement.class, params);
        }

        interface ValueLabel {

            static public ValueLabel create(String value, String label) throws Exception {
                return HtmlProvider.provider().createInstance(ValueLabel.class, value, label);
            }

            static public ValueLabel create(ActiveNode.ObjectData objectData) throws Exception {
                return HtmlProvider.provider().createInstance(ValueLabel.class, objectData);
            }

            String getValue();

            String getLabel();
        }

        void setName(String value) throws Exception;

        /**
         *
         * @param value
         * @param label
         * @throws Exception
         */
        void addSelectedOption(String value, String label) throws Exception;

        void addOption(String value, String label) throws Exception;

        /**
         *
         * @param valueLabel
         * @throws Exception
         */
        void addOption(ValueLabel valueLabel) throws Exception;
    }

    /**
     *
     */
    interface HtmlSpanElement extends HtmlElement {

        static public HtmlSpanElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlSpanElement.class, params);
        }

    }

    /**
     *
     */
    interface HtmlTableElement extends HtmlElement {

        static public HtmlTableElement create(Object... params) throws Exception {
            return HtmlProvider.provider().createInstance(HtmlTableElement.class, params);
        }

        interface HtmlCaptionElement extends HtmlElement {

            static public HtmlCaptionElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlCaptionElement.class, params);
            }

        }

        interface HtmlTHeadElement extends HtmlElement {

            static public HtmlTHeadElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlTHeadElement.class, params);
            }

        }

        interface HtmlTFootElement extends HtmlElement {

            static public HtmlTFootElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlTFootElement.class, params);
            }

        }

        interface HtmlTBodyElement extends HtmlElement {

            static public HtmlTBodyElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlTBodyElement.class, params);
            }

        }

        interface HtmlTrElement extends HtmlElement {

            static public HtmlTrElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlTrElement.class, params);
            }

        }

        interface HtmlThElement extends HtmlElement {

            static public HtmlThElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlThElement.class, params);
            }

            public enum Scope {
                row,
                col,

                /**
                 *
                 */
                rowgroup,
                colgroup,
                auto,;
            }

            void setColspan(int colspan) throws Exception;

            int getColspan();
        }

        interface HtmlTdElement extends HtmlElement {

            static public HtmlTdElement create(Object... params) throws Exception {
                return HtmlProvider.provider().createInstance(HtmlTdElement.class, params);
            }

            void setColspan(int colspan) throws Exception;

            int getColspan();
        }
    }

    /**
     *
     */
    interface Builder<T extends HtmlElement> {

        T build() throws Exception;
    }

    /**
     *
     */
    interface InputBuilder extends Builder<HtmlInputElement> {

        static public InputBuilder builder(ObjectType objectType, ValueType valueType) throws Exception {
            return HtmlProvider.provider().createInstance(InputBuilder.class, objectType, valueType);
        }

        static public HtmlInputElement.Type getInputType(ValueType valueType) {
            MetaTypes.DataTypes metaDataTypes = MetaTypes.DataTypes.valueOf(valueType.fieldType().dataType());
            switch (metaDataTypes) {
                case BOOL:
                    return HtmlInputElement.Type.CHECKBOX;
                case DATE:
                    return HtmlInputElement.Type.DATE;
                case ENUM:
                    return HtmlInputElement.Type.LIST;
                case DECIMAL:
                case NUMBER:
                    return HtmlInputElement.Type.NUMBER;
                case TIME:
                    return HtmlInputElement.Type.TIME;
                case TIMESTAMP:
                    return HtmlInputElement.Type.DATETIME;
                case TEXT:
                    return HtmlInputElement.Type.TEXT;
                default:
                    return HtmlInputElement.Type.TEXT;
            }
        }

        public void setName(String name);

        public void setId(String id);

        public HtmlInputElement.Type getInputType();

        public HtmlInputElement build() throws Exception;
    }

    /**
     *
     */
    interface SelectBuilder extends Builder<HtmlElement> {

        static public SelectBuilder builder() throws Exception {
            return HtmlProvider.provider().createInstance(SelectBuilder.class);
        }

        static public SelectBuilder builder(ArrayData arrayData, String valueName, String labelName) throws Exception {
            return HtmlProvider.provider().createInstance(SelectBuilder.class, arrayData, valueName, labelName);
        }

        void addClassDiv(String... divClass);

        void addClassLabel(String... labelClass);

        void addClassSelect(String... selectClass);

        void addOption(String value, String content);

        void setId(String id);

        void setName(String name);

        void setLabel(String label);

        /**
         *
         * @return
         * @throws Exception
         */
        HtmlElement build() throws Exception;
    }

    /**
     *
     */
    interface FormBuilder extends Builder<HtmlFormElement> {

        static public FormBuilder builder() throws Exception {
            return HtmlProvider.provider().createInstance(FormBuilder.class);
        }

        static public FormBuilder builder(String id, String name) throws Exception {
            return HtmlProvider.provider().createInstance(FormBuilder.class, id, name);
        }

        void setName(String name);

        void setId(String id);

        /**
         *
         * @param htmlElement
         */
        void addInput(HtmlElement htmlElement);

        /**
         *
         * @return
         * @throws Exception
         */
        public HtmlFormElement build() throws Exception;
    }

    /**
     *
     */
    interface TableBuilder extends Builder<HtmlTableElement> {

        static public TableBuilder builder() throws Exception {
            return HtmlProvider.provider().createInstance(TableBuilder.class);
        }

        /**
         *
         */
        public enum FieldDataType {
            Numeric("active_node_numeric"),
            //Temporal("active_node_temporal"),
            Temporal,
            //AlphaNumeric("active_node_alpha"),
            AlphaNumeric,
            //Enumeration("active_node_enum");
            Enumeration;
            String htmlClassName;

            FieldDataType() {
                this.htmlClassName = null;
            }

            FieldDataType(String htmlClassName) {
                this.htmlClassName = htmlClassName;
            }

            public String getClassName() {
                return htmlClassName;
            }

            static public FieldDataType convert(ValueType valueType) {
                DataGroup dataGroup = valueType.fieldType().dataType().primitiveType().dataGroup();
                if (dataGroup.isNumeric()) {
                    return Numeric;
                }
                if (dataGroup.isTemporal()) {
                    return Temporal;
                }
                if (dataGroup.isEnumerable()) {
                    return Enumeration;
                }
                return AlphaNumeric;
            }
        }

        /**
         *
         * @param caption
         */
        void setCaption(String caption);

        void setCaption(String caption, String cssClass);

        void addHeaderRow(List<ActiveType> nodeTypes) throws Exception;

        /**
         *
         * @param nodeTypes
         * @throws Exception
         */
        void addFooterRow(List<ActiveType> nodeTypes) throws Exception;

        void addBodyRow(List<ActiveData> node) throws Exception;

        HtmlTableElement build() throws Exception;
    }

    /**
     *
     */
    interface HtmlLabeler {

        static public HtmlLabeler get() throws Exception {
            return HtmlProvider.provider().createInstance(HtmlLabeler.class);
        }

        String get(AppContext context, String label_type);

        String get(ActiveType labelType);

        String get(AppContext context, String parent_type, String label_type);

        String get(ActiveType parentType, ActiveType labelType);

        HtmlLabeler set(ActiveType parentType, ActiveType labelType, String label);

        HtmlLabeler set(ActiveType labelType, String label);
    }

    /**
     *
     *
     */
    interface TableData extends ArrayData, Renewable<TableData> {

        static public TableData create(ArrayData arrayData, String caption) {
            TableData tableData = HtmlProvider.provider().getRenewable(TableData.class);
            return tableData.renew(arrayData, caption);
        }

        String getCaption();
    }
}
