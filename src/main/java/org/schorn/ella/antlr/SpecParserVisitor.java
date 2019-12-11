// Generated from src\main\java\org\schorn\ella\antlr\SpecParser.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SpecParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SpecParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SpecParser#def}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef(SpecParser.DefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeCreation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCreation(SpecParser.TypeCreationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#defineType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefineType(SpecParser.DefineTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAttributes(SpecParser.TypeAttributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAttribute(SpecParser.TypeAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(SpecParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(SpecParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(SpecParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#cmdPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdPath(SpecParser.CmdPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#listOfValues}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListOfValues(SpecParser.ListOfValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#listValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListValue(SpecParser.ListValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#attrCreation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrCreation(SpecParser.AttrCreationContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#addTypeToAttr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddTypeToAttr(SpecParser.AddTypeToAttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#attributeAttributes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeAttributes(SpecParser.AttributeAttributesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#attributeAttribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeAttribute(SpecParser.AttributeAttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeFlavor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFlavor(SpecParser.TypeFlavorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#attrFlavor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrFlavor(SpecParser.AttrFlavorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeQualifier(SpecParser.TypeQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#enumQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumQualifier(SpecParser.EnumQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#cmdAction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdAction(SpecParser.CmdActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#cmdObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmdObject(SpecParser.CmdObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#cmd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmd(SpecParser.CmdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#enumID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumID(SpecParser.EnumIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeNameRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNameRef(SpecParser.TypeNameRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#typeNameDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNameDef(SpecParser.TypeNameDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPattern(SpecParser.PatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(SpecParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link SpecParser#datetime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatetime(SpecParser.DatetimeContext ctx);
}