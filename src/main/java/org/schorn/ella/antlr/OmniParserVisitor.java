// Generated from src\main\java\org\schorn\ella\antlr\OmniParser.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OmniParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OmniParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OmniParser#def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef(OmniParser.DefContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeCreation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCreation(OmniParser.TypeCreationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#defineType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineType(OmniParser.DefineTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAttributes(OmniParser.TypeAttributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAttribute(OmniParser.TypeAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#attrCreation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrCreation(OmniParser.AttrCreationContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddTypeToAttr(OmniParser.AddTypeToAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeFlavor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFlavor(OmniParser.TypeFlavorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#attrFlavor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrFlavor(OmniParser.AttrFlavorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#memberType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberType(OmniParser.MemberTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#memberBaseTypeArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberBaseTypeArray(OmniParser.MemberBaseTypeArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#memberBaseType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberBaseType(OmniParser.MemberBaseTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#members}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembers(OmniParser.MembersContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#arrayIndicator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayIndicator(OmniParser.ArrayIndicatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeNameRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNameRef(OmniParser.TypeNameRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#typeNameDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNameDef(OmniParser.TypeNameDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link OmniParser#varNameRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarNameRef(OmniParser.VarNameRefContext ctx);
}