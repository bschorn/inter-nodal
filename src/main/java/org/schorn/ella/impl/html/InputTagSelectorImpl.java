/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
import org.schorn.ella.html.ActiveHtml.InputTagSelector;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.ValueFlag;

/**
 *
 * @author bschorn
 */
public class InputTagSelectorImpl implements InputTagSelector {

    @Override
    public ActiveHtml.HtmlInputElement.Type apply(ValueType valueType) {
        MetaTypes.DataTypes metaDataTypes = MetaTypes.DataTypes.valueOf(valueType.fieldType().dataType());
        ActiveHtml.HtmlInputElement.Type inputType;
        if (ValueFlag.HIDDEN.hasFlag(valueType.valueFlags())) {
            inputType = ActiveHtml.HtmlInputElement.Type.HIDDEN;
        } else {
            inputType = this.getType(metaDataTypes);
        }
        return inputType;
    }

    private ActiveHtml.HtmlInputElement.Type getType(MetaTypes.DataTypes metaDataTypes) {
        switch (metaDataTypes) {
            case BOOL:
                return ActiveHtml.HtmlInputElement.Type.CHECKBOX;
            case DATE:
                return ActiveHtml.HtmlInputElement.Type.DATE;
            case ENUM:
                return ActiveHtml.HtmlInputElement.Type.LIST;
            case DECIMAL:
            case NUMBER:
                return ActiveHtml.HtmlInputElement.Type.NUMBER;
            case TIME:
                return ActiveHtml.HtmlInputElement.Type.TIME;
            case TIMESTAMP:
                return ActiveHtml.HtmlInputElement.Type.DATETIME;
            case TEXT:
                return ActiveHtml.HtmlInputElement.Type.TEXT;
            default:
                return ActiveHtml.HtmlInputElement.Type.TEXT;
        }
    }

}
