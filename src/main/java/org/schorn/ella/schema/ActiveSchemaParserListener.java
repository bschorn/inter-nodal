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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.antlr.SpecParser;
import org.schorn.ella.antlr.SpecParserBaseListener;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType.StandardSets;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType.StandardTypes;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.schema.ActiveSchema.BaseType;
import org.schorn.ella.schema.ActiveSchema.FieldType;
import org.schorn.ella.schema.ActiveSchema.Fragment;
import org.schorn.ella.schema.ActiveSchema.ObjectType;
import org.schorn.ella.schema.ActiveSchema.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class ActiveSchemaParserListener extends SpecParserBaseListener {

    static final Logger LGR = LoggerFactory.getLogger(ActiveSchemaParserListener.class);

    private final ActiveSchema schema = new ActiveSchema();
    private ActiveSchema.Roles currentType = null;
    private ActiveSchema.Roles currentAttrType = null;
    private String currentTypeNameDef = null;

    private ActiveSchema.BaseType currentTemplate = null;
    private ActiveSchema.ObjectType currentObjectType = null;
    private ActiveSchema.Fragment currentFragment = null;
    private ActiveSchema.ValueType currentValueType = null;
    private ActiveSchema.FieldType currentFieldType = null;
    private ActiveSchema.Member currentMemberType = null;

    public ActiveSchema schema() {
        return this.schema;
    }

    @Override
    public void enterTypeCreation(SpecParser.TypeCreationContext ctx) {
        this.currentType = ActiveSchema.Roles.parse(ctx.typeFlavor().getText());
        if (this.currentType == null) {
            LGR.error("The section '{}' was not understood.", ctx.typeFlavor().getText());
        }
        for (SpecParser.DefineTypeContext dctx : ctx.defineType()) {
            if (dctx.typeNameDef() == null) {
                LGR.error("There was no TypeName defined.");
                continue;
            }
            this.currentTypeNameDef = dctx.typeNameDef().getText().replace("\"", "");
            try {
                switch (this.currentType) {
                    case FieldType:
                        this.currentFieldType = this.schema.create(FieldType.class, this.currentTypeNameDef);
                        break;
                    case ValueType:
                        this.currentValueType = this.schema.create(ValueType.class, this.currentTypeNameDef);
                        break;
                    case Fragment:
                        this.currentFragment = this.schema.create(Fragment.class, this.currentTypeNameDef);
                        break;
                    case BaseType:
                        this.currentTemplate = this.schema.create(BaseType.class, this.currentTypeNameDef);
                        break;
                    case ObjectType:
                        this.currentObjectType = this.schema.create(ObjectType.class, this.currentTypeNameDef);
                        break;
                    default:
                        continue;
                }
                //LGR.debug("enterTypeCreation() - create type: '{}.{}'", this.currentType.name(), this.currentTypeNameDef);
                for (SpecParser.TypeAttributesContext tactx : dctx.typeAttributes()) {
                    doTypeAttribute(tactx.typeAttribute());
                }
            } catch (Exception ex) {
                LGR.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void doTypeAttribute(SpecParser.TypeAttributeContext ctx) {
        if (ctx.enumQualifier() != null) {
            ActiveSchema.Roles enumQualifier = ActiveSchema.Roles.parse(ctx.enumQualifier().getText());
            String enumID = ctx.enumID().getText();
            switch (this.currentType) {
                case FieldType:
                    switch (enumQualifier) {
                        case DataType:
                            this.currentFieldType.setDataTypeName(enumID);
                            //LGR.debug("doTypeAttribute() - {}.{} <- {}.{}",this.currentType, this.currentTypeNameDef,enumQualifier.name(), enumID);
                            if (ctx.parameters() != null) {
                                doParameters(ctx.parameters());
                            }
                            break;
                    }
                    break;
                default:
                    break;
            }
        } else if (ctx.typeQualifier() != null) {
            ActiveSchema.Roles typeQualifier = ActiveSchema.Roles.parse(ctx.typeQualifier().getText());
            String typeName = ctx.typeNameRef().getText();
            switch (this.currentType) {
                case ValueType:
                    switch (typeQualifier) {
                        case FieldType:
                            this.currentValueType.setFieldTypeName(typeName);
                            //LGR.debug("doTypeAttribute() - {}.{} <- {}.{}",this.currentType, this.currentTypeNameDef,typeQualifier.name(), typeName);
                            break;
                    }
                    break;
                default:
                    break;
            }
        } else if (ctx.attrCreation() != null) {
            doAttrCreation(ctx.attrCreation());
        }
    }

    private void doParameters(SpecParser.ParametersContext ctx) {
        if (ctx.parameter() == null || ctx.parameter().get(0) == null) {
            return;
        }
        ActiveSchema.Constraints constraints = new ActiveSchema.Constraints();
        switch (this.currentType) {
            case FieldType:
                DataGroup dataGroup = this.currentFieldType.getDataGroup();
                switch (dataGroup) {
                    case ENUM: {
                        if (ctx.parameter().get(0).command() != null) {
                            if (ctx.parameter().get(0).command().cmd() != null) {
                                String cmd = ctx.parameter().get(0).command().cmd().getText();
                                if (ctx.parameter().get(0).command().cmdPath() != null) {
                                    String cmdPath = ctx.parameter().get(0).command().cmdPath().getText().replace("\"", "");
                                    if (cmd.equals("Read.File")) {
                                        constraints.addConstraint("list", ActiveSchema.readFile(null, cmdPath));
                                        this.currentFieldType.setConstraints(constraints);
                                    }
                                }
                            }
                        }
                    }
                    break;
                    case TEXT: {
                        if (ctx.parameter().get(0).pattern() != null) {
                            String pattern = ctx.parameter().get(0).pattern().getText();
                            constraints.addConstraint("pattern", pattern.replace("\"", ""));
                            this.currentFieldType.setConstraints(constraints);
                        } else if (ctx.parameter().get(0).number() != null) {
                            String number = ctx.parameter().get(0).number().getText();
                            constraints.addConstraint("pattern", String.format("^.*{0,%s}", number));
                            this.currentFieldType.setConstraints(constraints);
                        }
                    }
                    break;
                    case NUMBER:
                    case DECIMAL: {
                        if (ctx.parameter().size() > 1) {
                            List<String> literals = new ArrayList<>();
                            ctx.parameter().stream()
                                    .filter(p -> p.number() != null)
                                    .forEach(p -> literals.add(p.number().getText()));
                            if (literals.size() == 3) {
                                List<StandardTypes> standardTypes = null;
                                switch (dataGroup) {
                                    case NUMBER:
                                        standardTypes = StandardSets.Range.getStandardTypes(Integer.class);
                                        break;
                                    case DECIMAL:
                                        standardTypes = StandardSets.Range.getStandardTypes(BigDecimal.class);
                                        break;
                                    default:
                                        break;
                                }
                                if (standardTypes != null) {
                                    for (int i = 0; i < literals.size(); i += 1) {
                                        String value = literals.get(i).replace("\"", "");
                                        if (i < standardTypes.size()) {
                                            StandardTypes standardType = standardTypes.get(i);
                                            Object constraintValue = ActiveSchema.typeConversion(standardType.valueClass(), value);
                                            constraints.addConstraint(standardType.name(), constraintValue);
                                            this.currentFieldType.setConstraints(constraints);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                    case DATE:
                    case TIME:
                    case TIMESTAMP: {
                        if (ctx.parameter().size() == 2) {
                            List<String> literals = new ArrayList<>();
                            ctx.parameter().stream()
                                    .filter(p -> p.datetime() != null)
                                    .forEach(p -> literals.add(p.datetime().getText()));
                            if (literals.size() == 2) {
                                List<StandardTypes> standardTypes = null;
                                switch (dataGroup) {
                                    case DATE:
                                        standardTypes = StandardSets.Range.getStandardTypes(LocalDate.class);
                                        break;
                                    case TIME:
                                        standardTypes = StandardSets.Range.getStandardTypes(LocalTime.class);
                                        break;
                                    case TIMESTAMP:
                                        standardTypes = StandardSets.Range.getStandardTypes(LocalDateTime.class);
                                        break;
                                    default:
                                        break;
                                }
                                if (standardTypes != null) {
                                    for (int i = 0; i < literals.size(); i += 1) {
                                        String value = literals.get(i).replace("\"", "");
                                        if (i < standardTypes.size()) {
                                            StandardTypes standardType = standardTypes.get(i);
                                            Object constraintValue = ActiveSchema.typeConversion(standardType.valueClass(), value);
                                            constraints.addConstraint(standardType.name(), constraintValue);
                                            this.currentFieldType.setConstraints(constraints);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
        }
    }

    private void doAttrCreation(SpecParser.AttrCreationContext ctx) {
        this.currentAttrType = ActiveSchema.Roles.parse(ctx.attrFlavor().getText());
        LGR.debug("doAttrCreation() - create subType: '{}' for '{}.{}'", this.currentAttrType.name(), this.currentType.name(), this.currentTypeNameDef);
        for (SpecParser.AddTypeToAttrContext atctx : ctx.addTypeToAttr()) {
            if (atctx.typeQualifier() != null) {
                doAddTypeToAttr(atctx);
            } else if (atctx.attributeType() != null) {
                doAddAttrToType(atctx);
            }
        }
    }

    private void doAddAttrToType(SpecParser.AddTypeToAttrContext ctx) {
        ActiveSchema.AttributeType attributeType = ActiveSchema.AttributeType.valueOf(ctx.attributeType().getText());
        if (ctx.enumID() == null) {
            return;
        }
        String attributeValue = ctx.enumID().getText();
        switch (this.currentAttrType) {
            case Attribute:
                switch (this.currentType) {
                    case ObjectType:
                        this.currentObjectType.addAttribute(attributeType, attributeValue);
                        break;
                }
                break;
        }
    }

    private void doAddTypeToAttr(SpecParser.AddTypeToAttrContext ctx) {
        ActiveSchema.Roles memberType = ActiveSchema.Roles.parse(ctx.typeQualifier().getText());
        String memberName = ctx.typeNameRef().getText();
        switch (this.currentAttrType) {
            case Flag:
                switch (this.currentType) {
                    case ValueType:
                        this.currentValueType.addFlag(memberName);
                        break;
                    default:
                        // TODO: error
                        break;
                }
                break;
            case Member:
                this.currentMemberType = new ActiveSchema.Member(
                        this.schema, this.currentType, this.currentTypeNameDef,
                        memberType, memberName);
                switch (this.currentType) {
                    case ObjectType:
                        this.currentObjectType.addMember(this.currentMemberType);
                        //LGR.debug("doAddTypeToAttr() - add member '{}' to '{}'",this.currentMemberType.name(),this.currentObjectType.name());
                        break;
                    case BaseType:
                        this.currentTemplate.addMember(this.currentMemberType);
                        //LGR.debug("doAddTypeToAttr() - add member '{}' to '{}'",this.currentMemberType.name(),this.currentTemplate.name());
                        break;
                    case Fragment:
                        this.currentFragment.addMember(this.currentMemberType);
                        //LGR.debug("doAddTypeToAttr() - add member '{}' to '{}'",this.currentMemberType.name(),this.currentFragment.name());
                        break;
                    default:
                        // TODO: error
                        break;
                }
                break;
            case Parent:
                switch (this.currentType) {
                    case BaseType:
                        this.currentTemplate.addParent(
                                new ActiveSchema.Parent(
                                        this.schema, memberType, memberName));
                        break;
                    case ObjectType:
                        this.currentObjectType.addParent(
                                new ActiveSchema.Parent(
                                        this.schema, memberType, memberName));
                        break;
                    default:
                        // TODO: error
                        break;
                }
                break;
        }
        if (ctx.attributeAttributes() != null) {
            doAttributeAttributes(ctx.attributeAttributes());
        }
    }

    private void doAttributeAttributes(SpecParser.AttributeAttributesContext ctx) {
        for (SpecParser.AttributeAttributeContext actx : ctx.attributeAttribute()) {
            switch (this.currentAttrType) {
                case Member:
                    if (actx.enumQualifier() != null && actx.enumID() != null) {
                        ActiveSchema.Roles enumQualifier = ActiveSchema.Roles.parse(actx.enumQualifier().getText());
                        switch (enumQualifier) {
                            case BondType:
                                String bondTypeStr = actx.enumID().getText();
                                this.currentMemberType.setBondType(bondTypeStr);
                                //LGR.debug("doAttributeAttributes() - apply BondType.{} to '{}'", bondTypeStr, this.currentMemberType.name());
                                if (actx.parameters() != null && actx.parameters().parameter() != null) {
                                    String branchName = null;
                                    String leafName = null;
                                    for (SpecParser.ParameterContext parameter : actx.parameters().parameter()) {
                                        for (SpecParser.TypeNameRefContext typeNameRef : parameter.typeNameRef()) {
                                            if (typeNameRef != null) {
                                                if (branchName == null) {
                                                    branchName = typeNameRef.getText();
                                                } else if (leafName == null) {
                                                    leafName = typeNameRef.getText();
                                                }
                                            }
                                        }
                                    }
                                    if (branchName != null && leafName != null) {
                                        this.currentMemberType.addForeignKey(branchName, leafName);
                                    }
                                }
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
