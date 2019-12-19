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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml;
import org.schorn.ella.html.ActiveHtml.HtmlAttribute;
import org.schorn.ella.html.ActiveHtml.HtmlDivElement;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.ActiveHtml.HtmlFormElement;
import org.schorn.ella.html.ActiveHtml.HtmlInputElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabelElement;
import org.schorn.ella.html.ActiveHtml.HtmlLabeler;
import org.schorn.ella.html.ActiveHtml.InputBuilder;
import org.schorn.ella.html.ActiveHtml.SelectBuilder;
import org.schorn.ella.html.ActiveHtml.TableBuilder;
import org.schorn.ella.html.ActiveHtml.TableData;
import org.schorn.ella.html.HtmlConfig;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.impl.html.HtmlSelectElementImpl.ValueLabelImpl;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class HtmlProviderImpl extends AbstractProvider implements HtmlProvider {

    private static final Logger LGR = LoggerFactory.getLogger(HtmlProviderImpl.class);
    static private final String _UUID_ = "_UUID_";

    final Map<ActiveNode.ObjectType, HtmlTag.ListTag> lists = new HashMap<>();
    final Map<ActiveNode.ObjectType, List<HtmlTag.ListTag>> listTags = new HashMap<>();
    final Map<ActiveNode.ObjectType, HtmlTag.FormTag> forms = new HashMap<>();
    final Map<ActiveNode.ObjectType, List<HtmlTag.InputTag>> formTags = new HashMap<>();

    private HtmlLabeler labeler;
    private List<Class<? extends Mingleton>> mingletons = new ArrayList<>();
    private List<Class<? extends Renewable<?>>> renewables = new ArrayList<>();

    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(ActiveHtml.HtmlAttribute.class, HtmlAttributeImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlDivElement.class, HtmlDivElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlElement.class, HtmlElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlFieldsetElement.class, HtmlFieldsetElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlFormElement.class, HtmlFormElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlInputElement.class, HtmlInputElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlLabelElement.class, HtmlLabelElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlOptionElement.class, HtmlOptionElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlSelectElement.class, HtmlSelectElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlSelectElement.ValueLabel.class, ValueLabelImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlSingleElement.class, HtmlSingleElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlSpanElement.class, HtmlSpanElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.class, HtmlTableElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlThElement.class, HtmlTableElementImpl.HtmlThElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlCaptionElement.class, HtmlTableElementImpl.HtmlCaptionElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlTBodyElement.class, HtmlTableElementImpl.HtmlTBodyElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlTdElement.class, HtmlTableElementImpl.HtmlTdElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlTFootElement.class, HtmlTableElementImpl.HtmlTFootElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlTHeadElement.class, HtmlTableElementImpl.HtmlTHeadElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlTableElement.HtmlTrElement.class, HtmlTableElementImpl.HtmlTrElementImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.HtmlLabeler.class, HtmlLabelerImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.SelectBuilder.class, SelectBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.InputBuilder.class, InputBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.FormBuilder.class, FormBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.TableBuilder.class, TableBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveHtml.TableData.class, TableDataImpl.class);

        this.labeler = this.createReusable(HtmlLabeler.class);

        this.renewables.add(ActiveHtml.TableData.class);

    }

    @Override
    public void registerContext(AppContext context) throws Exception {
        if (context.equals(AppContext.Common)) {
            for (Class<?> classFor : this.mingletons) {
                this.createReusable(classFor);
                LGR.info(String.format("%s.registerContext('%s') - create Mingleton: %s",
                        this.getClass().getSimpleName(),
                        context.name(),
                        classFor.getSimpleName()
                ));
            }
            for (Class<?> classFor : this.renewables) {
                this.createReusable(classFor);
                LGR.info(String.format("%s.registerContext('%s') - create Renewable: %s",
                        this.getClass().getSimpleName(),
                        context.name(),
                        classFor.getSimpleName()
                ));
            }
        }
    }

    @Override
    public HtmlLabeler labeler() {
        return labeler;
    }

    @Override
    public HtmlElement html_form(ActiveNode.ObjectType objectType) throws Exception {
        String form_id = objectType.name();
        String form_name = objectType.name();
        String form_label = HtmlProvider.provider().labeler().get(objectType, null);
        if (form_label == null) {
            form_label = objectType.name();
        }

        HtmlDivElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.addClass(objectType.name());

        HtmlLabelElement labelElement = ActiveHtml.HtmlLabelElement.create();
        labelElement.setFor(form_id);
        labelElement.setTextContent(form_label);
        labelElement.addClass(HtmlConfig.HTML_FORM_LABEL_CLASS.value());
        divElement.append(labelElement);

        ActiveHtml.FormBuilder formBuilder = ActiveHtml.FormBuilder.builder(form_id, form_name);

        for (ActiveNode.MemberDef memberType : objectType.schema().memberDefs()) {
            ActiveType activeType = memberType.activeType();
            switch (activeType.role()) {
                case Value:
                    if (activeType instanceof ActiveNode.ValueType) {
                        if (ActiveHtml.InputBuilder.getInputType((ValueType) activeType) == HtmlInputElement.Type.LIST) {
                            formBuilder.addInput(html_list(objectType, (ValueType) activeType, null));
                        } else {
                            formBuilder.addInput(html_input(objectType, (ValueType) activeType, null));
                        }
                    }
                    break;
                case Object:
                    if (activeType instanceof ActiveNode.ObjectType) {
                        ObjectType fkObjectType = (ObjectType) activeType;
                        List<MemberDef> uniqueKeys = fkObjectType.schema().uniqueKeys();
                        List<MemberDef> naturalKeys = fkObjectType.schema().naturalKeys();
                        MemberDef uniqueKeyMember = null;
                        if (uniqueKeys.size() > 0) {
                            uniqueKeyMember = uniqueKeys.get(0);
                        }
                        MemberDef naturalKeyMember = null;
                        if (naturalKeys.size() > 0) {
                            naturalKeyMember = naturalKeys.get(0);
                        }
                        if (uniqueKeyMember == null) {
                            uniqueKeyMember = naturalKeyMember;
                        } else if (naturalKeyMember == null) {
                            naturalKeyMember = uniqueKeyMember;
                        }
                        if (uniqueKeyMember != null && naturalKeyMember != null) {
                            ValueType uniqueKeyType = (ValueType) uniqueKeyMember.activeType();
                            ValueType naturalKeyType = (ValueType) naturalKeyMember.activeType();
                            ArrayData fkSelectArray = activeType.context().runList(fkObjectType.name(), naturalKeyType.name());
                            //HtmlElement selectElement = html_select(fkSelectArray, uniqueKeyType.name(), naturalKeyType.label());
                            //selectElement.addClass(HTMLConfig.HTML_SELECT_CLASS.value());
                            //formBuilder.addInput(selectElement);
                        }
                    }
                    break;
                case Array:
                    if (activeType instanceof ActiveNode.ArrayType) {

                    }
                    break;
                default:
                    break;
            }
        }

        HtmlFormElement formElement = formBuilder.build();
        formElement.addClass(HtmlConfig.HTML_FORM_CLASS.value());
        divElement.append(formElement);

        return divElement;

    }

    @Override
    public HtmlElement html_menu(ObjectType objectType) throws Exception {
        HtmlTag.FormTag formTag = getForm(objectType);
        List<HtmlTag.InputTag> htmlInputs = getFormTags(objectType);
        HtmlElement rootElement = HtmlTagRenderer.form(formTag, htmlInputs, _UUID_);
        objectType.schema().memberDefs().stream()
                .map(mt -> mt.activeType())
                .filter(n -> !n.role().equals(Role.Value))
                .forEach(t -> {
                    try {
                        rootElement.append(html_form((ObjectType) t));
                    } catch (Exception ex) {
                        LGR.error(Functions.getStackTraceAsString(ex));
                    }
                });

        return rootElement;
    }

    /**
     * Creates the User Interface element block containing a drop-down list
     * <select>
     * based off the data in the ArrayData.
     *
     * @param ArrayData arrayData
     * @param String valueName
     * @param String labelName
     * @return HtmlElement
     * @throws Exception
     */
    @Override
    public HtmlElement html_select(ArrayData arrayData, String valueName, String labelName) throws Exception {
        /*
		<div class="addClassDiv()">
	  		<label for="setId()" class="addClassLabel()">setLabel()</label>
	  		<select name="setName()" id="setId()" class="addClassSelect()">
	  			<option value=""></option>
	  		</select>
	  	</div>     
         */
        ActiveHtml.SelectBuilder builder = ActiveHtml.SelectBuilder.builder(arrayData, valueName, labelName);
        builder.setId(String.format("%s-%s", arrayData.name(), valueName));
        builder.setName(valueName);
        builder.setLabel(labelName);
        builder.addClassDiv(arrayData.name(), valueName);
        builder.addClassLabel(HtmlConfig.HTML_SELECT_LABEL_CLASS.value());
        builder.addClassSelect(HtmlConfig.HTML_SELECT_CLASS.value());
        return builder.build();
    }

    /**
     * Creates the User Interface element block containing a drop-down list
     * <select>
     * based off the constraints of an enum ValueType.
     *
     * @return <div>
     * @throws Exception
     */
    @Override
    public HtmlElement html_list(ObjectType objectType, ValueType valueType, Object value) throws Exception {
        SelectBuilder enumListBuilder = EnumListBuilderImpl.enum_builder(valueType);
        enumListBuilder.setId(String.format("%s-%s", objectType.name(), valueType.name()));
        enumListBuilder.setName(valueType.name());
        //enumListBuilder.setLabel(String.format("%s.%s", objectType.label(), valueType.label()));
        enumListBuilder.setLabel(String.format("%s", valueType.label()));
        enumListBuilder.addClassDiv(objectType.name(), valueType.name());
        enumListBuilder.addClassLabel(HtmlConfig.HTML_SELECT_LABEL_CLASS.value());
        enumListBuilder.addClassSelect(HtmlConfig.HTML_SELECT_CLASS.value());
        return enumListBuilder.build();
    }

    /**
     * Creates the User Interface element block containing an input <input>
     * based off the constraints of a ValueType. If the value is provided it
     * will be included in the <input>.
     *
     * @param ObjectType
     */
    @Override
    public HtmlElement html_input(ObjectType objectType, ValueType valueType, Object value) throws Exception {

        /*
	    <div class='object_type value_type'>
	    	<label for='object_type_value_type__UUID_'>value_type</label>
	    	<input id='object_type_value_type__UUID_' type='input_type' name='value_type' required readonly>
	    </div>            
         */
        String input_id = String.format("%s-%s", objectType.name(), valueType.name());
        String input_name = valueType.name();
        //String input_label = String.format("%s.%s", objectType.label(), valueType.label());
        String input_label = String.format("%s", valueType.label());

        HtmlElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.addClass(objectType.name());
        divElement.addClass(input_name);

        HtmlLabelElement labelElement = ActiveHtml.HtmlLabelElement.create();
        labelElement.setFor(input_id);
        labelElement.setTextContent(input_label);
        labelElement.addClass(HtmlConfig.HTML_INPUT_LABEL_CLASS.value());
        divElement.append(labelElement);

        InputBuilder inputBuilder = InputBuilder.builder(objectType, valueType);
        inputBuilder.setName(input_name);
        inputBuilder.setId(input_id);

        HtmlInputElement inputElement = inputBuilder.build();
        inputElement.addClass(HtmlConfig.HTML_INPUT_CLASS.value());
        divElement.append(inputElement);
        if (inputBuilder.getInputType() == HtmlInputElement.Type.HIDDEN) {
            divElement.addAttribute(HtmlAttribute.create("hidden", Boolean.TRUE));
        }

        HtmlElement spanElement = ActiveHtml.HtmlSpanElement.create();
        spanElement.addClass("validity");
        //divElement.append(spanElement);

        return divElement;

    }

    /**
     * Table
     *
     * @throws Exception
     */
    @Override
    public HtmlElement html_table(TableData tableData) throws Exception {
        TableBuilder tableBuilder = TableBuilder.builder();
        if (tableData.getCaption().length() > 30) {
            tableBuilder.setCaption(tableData.getCaption(), HtmlConfig.HTML_TABLE_SMALL_CAPTION_CLASS.value());
        } else {
            tableBuilder.setCaption(tableData.getCaption());
        }
        List<Object> rows = tableData.activeValues(0, 1);
        if (!rows.isEmpty()) {
            ActiveData firstRow = (ActiveData) rows.get(0);
            if (firstRow instanceof StructData) {
                tableBuilder.addHeaderRow(
                        ((StructData) firstRow).nodes().stream()
                                .map(n -> n.activeType())
                                .collect(Collectors.toList())
                );
            } else {
                tableBuilder.addHeaderRow(
                        Arrays.asList(new ActiveType[]{firstRow.activeType()})
                );
            }
            for (ActiveData row : tableData.activeValues(ActiveData.class)) {
                tableBuilder.addBodyRow(row.activeValues(ActiveData.class));
            }
        }
        HtmlDivElement divElement = ActiveHtml.HtmlDivElement.create();
        divElement.append(tableBuilder.build());
        return divElement;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     * 
     * 										PRIVATE
     * 
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private HtmlTag.FormTag getForm(ObjectType objectType) {
        HtmlTag.FormTag formTag = this.forms.get(objectType);
        if (formTag == null) {
            synchronized (this.forms) {
                formTag = this.forms.get(objectType);
                if (formTag == null) {
                    formTag = HtmlTagCreator.createFormTag(objectType);
                    this.forms.put(objectType, formTag);
                }
            }
        }
        return formTag;
    }

    @SuppressWarnings("unused")
    private HtmlTag.FormTag getMenu(ObjectType compositeType) {
        HtmlTag.FormTag formTag = this.forms.get(compositeType);
        if (formTag == null) {
            synchronized (this.forms) {
                formTag = this.forms.get(compositeType);
                if (formTag == null) {
                    formTag = HtmlTagCreator.createFormTag(compositeType);
                    this.forms.put(compositeType, formTag);
                }
            }
        }
        return formTag;
    }

    private List<HtmlTag.InputTag> getFormTags(ObjectType objectType) {
        List<HtmlTag.InputTag> htmlInputs = this.formTags.get(objectType);
        if (htmlInputs == null) {
            synchronized (this.formTags) {
                htmlInputs = this.formTags.get(objectType);
                if (htmlInputs == null) {
                    htmlInputs = new ArrayList<>();
                    for (MemberDef memberType : objectType.schema().memberDefs()) {
                        ActiveType activeType = memberType.activeType();
                        switch (activeType.role()) {
                            case Value:
                                htmlInputs.add(HtmlTagCreator.createInputTag(objectType, (ValueType) activeType));
                                break;
                            case Object:
                                /*
                    		 * TODO: incomplete 
                                 */
                                break;
                            case Array:
                                break;
                            default:
                                break;
                        }
                    }
                    this.formTags.put(objectType, htmlInputs);
                }
            }
        }
        return htmlInputs;
    }

}
