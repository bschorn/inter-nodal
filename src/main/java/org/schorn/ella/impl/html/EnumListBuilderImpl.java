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
import org.schorn.ella.html.ActiveHtml.SelectBuilder;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.DataGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class EnumListBuilderImpl extends SelectBuilderImpl implements SelectBuilder {

    static final Logger LGR = LoggerFactory.getLogger(EnumListBuilderImpl.class);

    private ValueType valueType;

    static public SelectBuilder enum_builder(ValueType valueType) {
        return new EnumListBuilderImpl(valueType);
    }

    public EnumListBuilderImpl(ValueType valueType) {
        this.valueType = valueType;
        List<ConstraintType<?>> constraintTypes = this.valueType.fieldType().constraints().constraintTypes();
        for (ConstraintType constraintType : constraintTypes) {
            ConstraintData constraintData = this.valueType.fieldType().constraints().constraint(constraintType);
            List<Object> values = constraintData.constraintValues();
            if (values != null && !values.isEmpty()) {
                String constraintName = constraintType.name();
                DataGroup dataGroup = constraintData.constraintType().dataGroup();
                switch (dataGroup) {
                    case ENUM:
                        switch (constraintName) {
                            case "list":
                                for (Object obj : values) {
                                    if (obj instanceof ActiveData) {
                                        ActiveData adata = (ActiveData) obj;
                                        if (adata != null && adata.activeValue() != null) {
                                            String[] keyValue = adata.activeValue().toString().split("\\|");
                                            if (keyValue.length == 2) {
                                                this.addOption(keyValue[0], keyValue[1]);
                                            } else {
                                                this.addOption(keyValue[0], keyValue[0]);
                                            }
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
