// Generated from src\main\java\org\schorn\ella\antlr\OmniParser.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OmniParser}.
 */
public interface OmniParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OmniParser#def}.
	 * @param ctx the parse tree
	 */
	void enterDef(OmniParser.DefContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#def}.
	 * @param ctx the parse tree
	 */
	void exitDef(OmniParser.DefContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeCreation}.
	 * @param ctx the parse tree
	 */
	void enterTypeCreation(OmniParser.TypeCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeCreation}.
	 * @param ctx the parse tree
	 */
	void exitTypeCreation(OmniParser.TypeCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#defineType}.
	 * @param ctx the parse tree
	 */
	void enterDefineType(OmniParser.DefineTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#defineType}.
	 * @param ctx the parse tree
	 */
	void exitDefineType(OmniParser.DefineTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeAttributes}.
	 * @param ctx the parse tree
	 */
	void enterTypeAttributes(OmniParser.TypeAttributesContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeAttributes}.
	 * @param ctx the parse tree
	 */
	void exitTypeAttributes(OmniParser.TypeAttributesContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeAttribute}.
	 * @param ctx the parse tree
	 */
	void enterTypeAttribute(OmniParser.TypeAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeAttribute}.
	 * @param ctx the parse tree
	 */
	void exitTypeAttribute(OmniParser.TypeAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#attrCreation}.
	 * @param ctx the parse tree
	 */
	void enterAttrCreation(OmniParser.AttrCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#attrCreation}.
	 * @param ctx the parse tree
	 */
	void exitAttrCreation(OmniParser.AttrCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 */
	void enterAddTypeToAttr(OmniParser.AddTypeToAttrContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 */
	void exitAddTypeToAttr(OmniParser.AddTypeToAttrContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeFlavor}.
	 * @param ctx the parse tree
	 */
	void enterTypeFlavor(OmniParser.TypeFlavorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeFlavor}.
	 * @param ctx the parse tree
	 */
	void exitTypeFlavor(OmniParser.TypeFlavorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#attrFlavor}.
	 * @param ctx the parse tree
	 */
	void enterAttrFlavor(OmniParser.AttrFlavorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#attrFlavor}.
	 * @param ctx the parse tree
	 */
	void exitAttrFlavor(OmniParser.AttrFlavorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#memberType}.
	 * @param ctx the parse tree
	 */
	void enterMemberType(OmniParser.MemberTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#memberType}.
	 * @param ctx the parse tree
	 */
	void exitMemberType(OmniParser.MemberTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#memberBaseTypeArray}.
	 * @param ctx the parse tree
	 */
	void enterMemberBaseTypeArray(OmniParser.MemberBaseTypeArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#memberBaseTypeArray}.
	 * @param ctx the parse tree
	 */
	void exitMemberBaseTypeArray(OmniParser.MemberBaseTypeArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#memberBaseType}.
	 * @param ctx the parse tree
	 */
	void enterMemberBaseType(OmniParser.MemberBaseTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#memberBaseType}.
	 * @param ctx the parse tree
	 */
	void exitMemberBaseType(OmniParser.MemberBaseTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#members}.
	 * @param ctx the parse tree
	 */
	void enterMembers(OmniParser.MembersContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#members}.
	 * @param ctx the parse tree
	 */
	void exitMembers(OmniParser.MembersContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#arrayIndicator}.
	 * @param ctx the parse tree
	 */
	void enterArrayIndicator(OmniParser.ArrayIndicatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#arrayIndicator}.
	 * @param ctx the parse tree
	 */
	void exitArrayIndicator(OmniParser.ArrayIndicatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeNameRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeNameRef(OmniParser.TypeNameRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeNameRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeNameRef(OmniParser.TypeNameRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#typeNameDef}.
	 * @param ctx the parse tree
	 */
	void enterTypeNameDef(OmniParser.TypeNameDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#typeNameDef}.
	 * @param ctx the parse tree
	 */
	void exitTypeNameDef(OmniParser.TypeNameDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link OmniParser#varNameRef}.
	 * @param ctx the parse tree
	 */
	void enterVarNameRef(OmniParser.VarNameRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link OmniParser#varNameRef}.
	 * @param ctx the parse tree
	 */
	void exitVarNameRef(OmniParser.VarNameRefContext ctx);
}