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

import java.util.List;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlDivElement;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlFormElement;
import org.schorn.ella.html.ActiveHtml.HtmlInputElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabelElement;
import org.schorn.ella.html.ActiveHtml.HtmlSelectElement;
import org.schorn.ella.impl.html.HtmlTag.InputType;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The original implementation of HTML.
 *
 * There may still be some dependencies.
 *
 *
 * @author schorn
 *
 */
public class HtmlTagRenderer {

    private static final Logger LGR = LoggerFactory.getLogger(HtmlTagRenderer.class);

    /**
     *
     * @param formTag
     * @param inputs
     * @param instanceUUID
     * @return
     * @throws Exception
     */
    static public HtmlElement form(HtmlTag.FormTag formTag, List<HtmlTag.InputTag> inputs, String instanceUUID) throws Exception {
        FormId formId = FormId.create(formTag.id(), instanceUUID);

        HtmlDivElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.addClass(formTag.id());
        divElement.addClass(instanceUUID);

        HtmlFormElement formElement = ActiveHtml.HtmlFormElement.create();
        formElement.setId(formId.toString());
        divElement.append(formElement);
        String formIdStr = formTag.id();
        inputs.stream().forEach(t -> {
            try {
                formElement.append(input(t, formIdStr, instanceUUID, null));
            } catch (Exception ex) {

            }
        });
        // set level which will cascade down through the children
        ((HtmlDivElementImpl) divElement).setLevel(0);
        return divElement;
    }

    /**
     *
     * @param inputTag
     * @param parentId
     * @param instanceID
     * @param value
     * @return
     * @throws Exception
     */
    static public HtmlElement select(HtmlTag.InputTag inputTag, String parentId, String instanceID, String value) throws Exception {
        InputId inputId = InputId.create(parentId, inputTag.id(), instanceID);

        HtmlElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.addClass(parentId);
        divElement.addClass(inputTag.id());

        HtmlLabelElement labelElement = ActiveHtml.HtmlLabelElement.create();
        labelElement.setFor(inputId.toString());
        labelElement.setTextContent(inputTag.label());
        divElement.append(labelElement);

        HtmlSelectElement selectElement = ActiveHtml.HtmlSelectElement.create();
        selectElement.setName(inputTag.name());
        selectElement.setId(inputId.toString());
        divElement.append(selectElement);

        List<HtmlSelectElement.ValueLabel> valueLabels = inputTag.getValueLabelList(HtmlTag.InputAttribute.LIST);
        if (valueLabels != null && !valueLabels.isEmpty()) {
            valueLabels.forEach(valueLabel -> {
                try {
                    selectElement.addOption(valueLabel);
                } catch (Exception ex) {
                    LGR.error(Functions.getStackTraceAsString(ex));
                }
            });
        }

        HtmlElement spanElement = ActiveHtml.HtmlSpanElement.create();
        spanElement.addClass("validity");
        //divElement.append(spanElement);

        return divElement;

    }

    /**
     *
     * @param inputTag
     * @param parentId
     * @param instanceID
     * @param value
     * @return
     * @throws Exception
     */
    static public HtmlElement input(HtmlTag.InputTag inputTag, String parentId, String instanceID, String value) throws Exception {
        if (inputTag.type().equals(HtmlTag.InputType.LIST)) {
            return select(inputTag, parentId, instanceID, value);
        }
        InputId inputId = InputId.create(parentId, inputTag.id(), instanceID);

        HtmlElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.addClass(parentId);
        divElement.addClass(inputTag.id());

        HtmlLabelElement labelElement = ActiveHtml.HtmlLabelElement.create();
        labelElement.setFor(inputId.toString());
        labelElement.setTextContent(inputTag.label());
        divElement.append(labelElement);

        HtmlInputElement inputElement = ActiveHtml.HtmlInputElement.create();
        inputElement.setId(inputId.toString());
        divElement.append(inputElement);
        if (inputTag.type().equals(InputType.HIDDEN)) {
            divElement.addAttribute(ActiveHtml.HtmlAttribute.create("hidden", Boolean.TRUE));
        }

        HtmlElement spanElement = ActiveHtml.HtmlSpanElement.create();
        spanElement.addClass("validity");
        //divElement.append(spanElement);

        return divElement;

    }

    /**
     * Utility FormId
     *
     */
    static public class FormId {

        String id;
        String instanceUUID;

        static public FormId create(String id, String instanceUUID) {
            return new FormId(id, instanceUUID);
        }

        static public FormId parse(String inputId) {
            return new FormId(inputId);
        }

        public FormId(String id, String instanceUUID) {
            this.id = id;
            this.instanceUUID = instanceUUID;
        }

        public FormId(String inputId) {
            String[] token = inputId.split("_");
            if (token.length == 2) {
                this.id = token[0];
                this.instanceUUID = token[1];
            }
        }

        @Override
        public String toString() {
            return String.format("%s_%s", id, instanceUUID);
        }
    }

    /**
     * Utility InputId
     *
     */
    static public class InputId {

        String parentId;
        String id;
        String instanceUUID;

        static public InputId create(String parentId, String id, String instanceUUID) {
            return new InputId(parentId, id, instanceUUID);
        }

        static public InputId parse(String inputId) {
            return new InputId(inputId);
        }

        public InputId(String parentId, String id, String instanceUUID) {
            this.parentId = parentId;
            this.id = id;
            this.instanceUUID = instanceUUID;
        }

        public InputId(String inputId) {
            String[] token = inputId.split("_");
            if (token.length == 3) {
                this.parentId = token[0];
                this.id = token[1];
                this.instanceUUID = token[2];
            }
        }

        @Override
        public String toString() {
            return String.format("%s_%s_%s", parentId, id, instanceUUID);
        }
    }
}
