// Generated from src\main\java\org\schorn\ella\antlr\SpecParser.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SpecParser}.
 */
public interface SpecParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SpecParser#def}.
	 * @param ctx the parse tree
	 */
	void enterDef(SpecParser.DefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#def}.
	 * @param ctx the parse tree
	 */
	void exitDef(SpecParser.DefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeCreation}.
	 * @param ctx the parse tree
	 */
	void enterTypeCreation(SpecParser.TypeCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeCreation}.
	 * @param ctx the parse tree
	 */
	void exitTypeCreation(SpecParser.TypeCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#defineType}.
	 * @param ctx the parse tree
	 */
	void enterDefineType(SpecParser.DefineTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#defineType}.
	 * @param ctx the parse tree
	 */
	void exitDefineType(SpecParser.DefineTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeAttributes}.
	 * @param ctx the parse tree
	 */
	void enterTypeAttributes(SpecParser.TypeAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeAttributes}.
	 * @param ctx the parse tree
	 */
	void exitTypeAttributes(SpecParser.TypeAttributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeAttribute}.
	 * @param ctx the parse tree
	 */
	void enterTypeAttribute(SpecParser.TypeAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeAttribute}.
	 * @param ctx the parse tree
	 */
	void exitTypeAttribute(SpecParser.TypeAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(SpecParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(SpecParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(SpecParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(SpecParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(SpecParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(SpecParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#cmdPath}.
	 * @param ctx the parse tree
	 */
	void enterCmdPath(SpecParser.CmdPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#cmdPath}.
	 * @param ctx the parse tree
	 */
	void exitCmdPath(SpecParser.CmdPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#listOfValues}.
	 * @param ctx the parse tree
	 */
	void enterListOfValues(SpecParser.ListOfValuesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#listOfValues}.
	 * @param ctx the parse tree
	 */
	void exitListOfValues(SpecParser.ListOfValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#listValue}.
	 * @param ctx the parse tree
	 */
	void enterListValue(SpecParser.ListValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#listValue}.
	 * @param ctx the parse tree
	 */
	void exitListValue(SpecParser.ListValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#attrCreation}.
	 * @param ctx the parse tree
	 */
	void enterAttrCreation(SpecParser.AttrCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#attrCreation}.
	 * @param ctx the parse tree
	 */
	void exitAttrCreation(SpecParser.AttrCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 */
	void enterAddTypeToAttr(SpecParser.AddTypeToAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 */
	void exitAddTypeToAttr(SpecParser.AddTypeToAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#attributeAttributes}.
	 * @param ctx the parse tree
	 */
	void enterAttributeAttributes(SpecParser.AttributeAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#attributeAttributes}.
	 * @param ctx the parse tree
	 */
	void exitAttributeAttributes(SpecParser.AttributeAttributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#attributeAttribute}.
	 * @param ctx the parse tree
	 */
	void enterAttributeAttribute(SpecParser.AttributeAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#attributeAttribute}.
	 * @param ctx the parse tree
	 */
	void exitAttributeAttribute(SpecParser.AttributeAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeFlavor}.
	 * @param ctx the parse tree
	 */
	void enterTypeFlavor(SpecParser.TypeFlavorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeFlavor}.
	 * @param ctx the parse tree
	 */
	void exitTypeFlavor(SpecParser.TypeFlavorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#attrFlavor}.
	 * @param ctx the parse tree
	 */
	void enterAttrFlavor(SpecParser.AttrFlavorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#attrFlavor}.
	 * @param ctx the parse tree
	 */
	void exitAttrFlavor(SpecParser.AttrFlavorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeQualifier(SpecParser.TypeQualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeQualifier(SpecParser.TypeQualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#attributeType}.
	 * @param ctx the parse tree
	 */
	void enterAttributeType(SpecParser.AttributeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#attributeType}.
	 * @param ctx the parse tree
	 */
	void exitAttributeType(SpecParser.AttributeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#enumQualifier}.
	 * @param ctx the parse tree
	 */
	void enterEnumQualifier(SpecParser.EnumQualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#enumQualifier}.
	 * @param ctx the parse tree
	 */
	void exitEnumQualifier(SpecParser.EnumQualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#cmdAction}.
	 * @param ctx the parse tree
	 */
	void enterCmdAction(SpecParser.CmdActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#cmdAction}.
	 * @param ctx the parse tree
	 */
	void exitCmdAction(SpecParser.CmdActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#cmdObject}.
	 * @param ctx the parse tree
	 */
	void enterCmdObject(SpecParser.CmdObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#cmdObject}.
	 * @param ctx the parse tree
	 */
	void exitCmdObject(SpecParser.CmdObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#cmd}.
	 * @param ctx the parse tree
	 */
	void enterCmd(SpecParser.CmdContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#cmd}.
	 * @param ctx the parse tree
	 */
	void exitCmd(SpecParser.CmdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#enumID}.
	 * @param ctx the parse tree
	 */
	void enterEnumID(SpecParser.EnumIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#enumID}.
	 * @param ctx the parse tree
	 */
	void exitEnumID(SpecParser.EnumIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeNameRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeNameRef(SpecParser.TypeNameRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeNameRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeNameRef(SpecParser.TypeNameRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#typeNameDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeNameDef(SpecParser.TypeNameDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#typeNameDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeNameDef(SpecParser.TypeNameDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPattern(SpecParser.PatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPattern(SpecParser.PatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(SpecParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(SpecParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link SpecParser#datetime}.
	 * @param ctx the parse tree
	 */
	void enterDatetime(SpecParser.DatetimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SpecParser#datetime}.
	 * @param ctx the parse tree
	 */
	void exitDatetime(SpecParser.DatetimeContext ctx);
}