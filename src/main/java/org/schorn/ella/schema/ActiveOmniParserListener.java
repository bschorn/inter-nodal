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
package org.schorn.ella.schema;

import java.util.HashMap;
import java.util.Map;
import org.schorn.ella.antlr.OmniParser;
import org.schorn.ella.antlr.OmniParserBaseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class ActiveOmniParserListener extends OmniParserBaseListener {

    static final Logger LGR = LoggerFactory.getLogger(ActiveOmniParserListener.class);

    @Override
    public void enterTypeCreation(OmniParser.TypeCreationContext ctx) {
        String typeFlavor = ctx.typeFlavor().getText();
        switch (typeFlavor) {
            case "OtherTypes":
                for (OmniParser.DefineTypeContext dctx : ctx.defineType()) {
                    genOtherType(dctx);
                }
                break;
            case "LeafTypes":
                for (OmniParser.DefineTypeContext dctx : ctx.defineType()) {
                    genLeafType(dctx);
                }
                break;
            case "BranchTypes":
                for (OmniParser.DefineTypeContext dctx : ctx.defineType()) {
                    genBranchType(dctx);
                }
                break;
        }
    }

    private void genOtherType(OmniParser.DefineTypeContext dctx) {
        String typeName = dctx.typeNameDef().getText();
        Map<String, String> methods = new HashMap<>();
        for (OmniParser.TypeAttributesContext actx : dctx.typeAttributes()) {
            if (actx.typeAttribute() == null) {
                continue;
            }
            if (actx.typeAttribute().attrCreation() == null) {
                continue;
            }
            if (actx.typeAttribute().attrCreation().attrFlavor() == null) {
                continue;
            }
            if (actx.typeAttribute().attrCreation().attrFlavor().members() == null) {
                continue;
            }
            if (actx.typeAttribute().attrCreation().addTypeToAttr() == null) {
                continue;
            }
            for (OmniParser.AddTypeToAttrContext tactx : actx.typeAttribute().attrCreation().addTypeToAttr()) {
                String varType = null;
                if (tactx.memberType().memberBaseType() != null) {
                    varType = tactx.memberType().memberBaseType().getText();
                } else if (tactx.memberType().memberBaseTypeArray() != null) {
                    varType = tactx.memberType().memberBaseTypeArray().getText();
                }
                String varName = tactx.varNameRef().getText();
                if (varType != null && varName != null) {
                    methods.put(varType, varName.replace("\"", ""));
                }
            }
        }
        if (typeName != null) {
            System.out.println(new OtherType(typeName.replace("\"", ""), methods).toString());
        }
    }

    private void genLeafType(OmniParser.DefineTypeContext dctx) {
        String typeName = dctx.typeNameDef().getText();
        for (OmniParser.TypeAttributesContext actx : dctx.typeAttributes()) {

        }

    }

    private void genBranchType(OmniParser.DefineTypeContext dctx) {
        String typeName = dctx.typeNameDef().getText();
        for (OmniParser.TypeAttributesContext actx : dctx.typeAttributes()) {

        }

    }
}
