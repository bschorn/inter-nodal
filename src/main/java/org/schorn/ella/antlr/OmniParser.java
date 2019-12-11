// Generated from src\main\java\org\schorn\ella\antlr\OmniParser.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class OmniParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OTHER_TYPES=1, LEAF_TYPES=2, BRANCH_TYPES=3, MEMBERS=4, ADD=5, DEF=6, 
		DATA_TYPE=7, BOND_TYPE=8, STRING_TYPE=9, OBJECT_TYPE=10, TYPE_NAME_DEF=11, 
		ENUM_ID=12, TYPE_NAME_REF=13, VAR_NAME_REF=14, TEXT_LENGTH=15, DECIMAL_LITERAL=16, 
		FLOAT_LITERAL=17, CHAR_LITERAL=18, TEXT_PATTERN=19, STRING_LITERAL=20, 
		NULL_LITERAL=21, OPDBLQUOTE=22, ARRAY_INDICATOR=23, LPAREN=24, RPAREN=25, 
		LBRACE=26, RBRACE=27, COMMA=28, DOT=29, SEMI=30, WS=31, COMMENT=32, LINE_COMMENT=33;
	public static final int
		RULE_def = 0, RULE_typeCreation = 1, RULE_defineType = 2, RULE_typeAttributes = 3, 
		RULE_typeAttribute = 4, RULE_attrCreation = 5, RULE_addTypeToAttr = 6, 
		RULE_typeFlavor = 7, RULE_attrFlavor = 8, RULE_memberType = 9, RULE_memberBaseTypeArray = 10, 
		RULE_memberBaseType = 11, RULE_members = 12, RULE_arrayIndicator = 13, 
		RULE_typeNameRef = 14, RULE_typeNameDef = 15, RULE_varNameRef = 16;
	private static String[] makeRuleNames() {
		return new String[] {
			"def", "typeCreation", "defineType", "typeAttributes", "typeAttribute", 
			"attrCreation", "addTypeToAttr", "typeFlavor", "attrFlavor", "memberType", 
			"memberBaseTypeArray", "memberBaseType", "members", "arrayIndicator", 
			"typeNameRef", "typeNameDef", "varNameRef"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'OtherTypes'", "'LeafTypes'", "'BranchTypes'", "'Members'", "'add'", 
			"'def'", "'DataType'", "'BondType'", "'String'", "'Object'", null, null, 
			null, null, null, null, null, null, null, null, "'null'", "'\"'", "'[]'", 
			"'('", "')'", "'{'", "'}'", "','", "'.'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "OTHER_TYPES", "LEAF_TYPES", "BRANCH_TYPES", "MEMBERS", "ADD", 
			"DEF", "DATA_TYPE", "BOND_TYPE", "STRING_TYPE", "OBJECT_TYPE", "TYPE_NAME_DEF", 
			"ENUM_ID", "TYPE_NAME_REF", "VAR_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
			"FLOAT_LITERAL", "CHAR_LITERAL", "TEXT_PATTERN", "STRING_LITERAL", "NULL_LITERAL", 
			"OPDBLQUOTE", "ARRAY_INDICATOR", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"COMMA", "DOT", "SEMI", "WS", "COMMENT", "LINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "OmniParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OmniParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class DefContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(OmniParser.EOF, 0); }
		public List<TypeCreationContext> typeCreation() {
			return getRuleContexts(TypeCreationContext.class);
		}
		public TypeCreationContext typeCreation(int i) {
			return getRuleContext(TypeCreationContext.class,i);
		}
		public DefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefContext def() throws RecognitionException {
		DefContext _localctx = new DefContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_def);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OTHER_TYPES) | (1L << LEAF_TYPES) | (1L << BRANCH_TYPES))) != 0)) {
				{
				{
				setState(34);
				typeCreation();
				}
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(40);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeCreationContext extends ParserRuleContext {
		public TypeFlavorContext typeFlavor() {
			return getRuleContext(TypeFlavorContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(OmniParser.SEMI, 0); }
		public List<TerminalNode> DOT() { return getTokens(OmniParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(OmniParser.DOT, i);
		}
		public List<DefineTypeContext> defineType() {
			return getRuleContexts(DefineTypeContext.class);
		}
		public DefineTypeContext defineType(int i) {
			return getRuleContext(DefineTypeContext.class,i);
		}
		public TypeCreationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeCreation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeCreation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeCreationContext typeCreation() throws RecognitionException {
		TypeCreationContext _localctx = new TypeCreationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_typeCreation);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			typeFlavor();
			setState(45); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(43);
					match(DOT);
					setState(44);
					defineType();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(47); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(49);
			match(SEMI);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefineTypeContext extends ParserRuleContext {
		public TerminalNode DEF() { return getToken(OmniParser.DEF, 0); }
		public TerminalNode LPAREN() { return getToken(OmniParser.LPAREN, 0); }
		public TypeNameDefContext typeNameDef() {
			return getRuleContext(TypeNameDefContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(OmniParser.RPAREN, 0); }
		public List<TypeAttributesContext> typeAttributes() {
			return getRuleContexts(TypeAttributesContext.class);
		}
		public TypeAttributesContext typeAttributes(int i) {
			return getRuleContext(TypeAttributesContext.class,i);
		}
		public DefineTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_defineType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterDefineType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitDefineType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitDefineType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefineTypeContext defineType() throws RecognitionException {
		DefineTypeContext _localctx = new DefineTypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_defineType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			match(DEF);
			setState(52);
			match(LPAREN);
			setState(53);
			typeNameDef();
			setState(57);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(54);
					typeAttributes();
					}
					} 
				}
				setState(59);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(60);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAttributesContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(OmniParser.COMMA, 0); }
		public TypeAttributeContext typeAttribute() {
			return getRuleContext(TypeAttributeContext.class,0);
		}
		public TypeAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeAttributes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeAttributes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAttributesContext typeAttributes() throws RecognitionException {
		TypeAttributesContext _localctx = new TypeAttributesContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeAttributes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(COMMA);
			setState(63);
			typeAttribute();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeAttributeContext extends ParserRuleContext {
		public AttrCreationContext attrCreation() {
			return getRuleContext(AttrCreationContext.class,0);
		}
		public TypeAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAttributeContext typeAttribute() throws RecognitionException {
		TypeAttributeContext _localctx = new TypeAttributeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeAttribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			attrCreation();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttrCreationContext extends ParserRuleContext {
		public AttrFlavorContext attrFlavor() {
			return getRuleContext(AttrFlavorContext.class,0);
		}
		public List<TerminalNode> DOT() { return getTokens(OmniParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(OmniParser.DOT, i);
		}
		public List<AddTypeToAttrContext> addTypeToAttr() {
			return getRuleContexts(AddTypeToAttrContext.class);
		}
		public AddTypeToAttrContext addTypeToAttr(int i) {
			return getRuleContext(AddTypeToAttrContext.class,i);
		}
		public AttrCreationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrCreation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterAttrCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitAttrCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitAttrCreation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrCreationContext attrCreation() throws RecognitionException {
		AttrCreationContext _localctx = new AttrCreationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_attrCreation);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			attrFlavor();
			setState(70); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(68);
					match(DOT);
					setState(69);
					addTypeToAttr();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(72); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AddTypeToAttrContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(OmniParser.ADD, 0); }
		public TerminalNode LPAREN() { return getToken(OmniParser.LPAREN, 0); }
		public MemberTypeContext memberType() {
			return getRuleContext(MemberTypeContext.class,0);
		}
		public VarNameRefContext varNameRef() {
			return getRuleContext(VarNameRefContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(OmniParser.RPAREN, 0); }
		public AddTypeToAttrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addTypeToAttr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterAddTypeToAttr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitAddTypeToAttr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitAddTypeToAttr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddTypeToAttrContext addTypeToAttr() throws RecognitionException {
		AddTypeToAttrContext _localctx = new AddTypeToAttrContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_addTypeToAttr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(ADD);
			setState(75);
			match(LPAREN);
			setState(76);
			memberType();
			setState(77);
			varNameRef();
			setState(78);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeFlavorContext extends ParserRuleContext {
		public TerminalNode OTHER_TYPES() { return getToken(OmniParser.OTHER_TYPES, 0); }
		public TerminalNode LEAF_TYPES() { return getToken(OmniParser.LEAF_TYPES, 0); }
		public TerminalNode BRANCH_TYPES() { return getToken(OmniParser.BRANCH_TYPES, 0); }
		public TypeFlavorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeFlavor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeFlavor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeFlavor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeFlavor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeFlavorContext typeFlavor() throws RecognitionException {
		TypeFlavorContext _localctx = new TypeFlavorContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_typeFlavor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OTHER_TYPES) | (1L << LEAF_TYPES) | (1L << BRANCH_TYPES))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AttrFlavorContext extends ParserRuleContext {
		public MembersContext members() {
			return getRuleContext(MembersContext.class,0);
		}
		public AttrFlavorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrFlavor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterAttrFlavor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitAttrFlavor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitAttrFlavor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrFlavorContext attrFlavor() throws RecognitionException {
		AttrFlavorContext _localctx = new AttrFlavorContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_attrFlavor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			members();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberTypeContext extends ParserRuleContext {
		public MemberBaseTypeContext memberBaseType() {
			return getRuleContext(MemberBaseTypeContext.class,0);
		}
		public MemberBaseTypeArrayContext memberBaseTypeArray() {
			return getRuleContext(MemberBaseTypeArrayContext.class,0);
		}
		public MemberTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterMemberType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitMemberType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitMemberType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberTypeContext memberType() throws RecognitionException {
		MemberTypeContext _localctx = new MemberTypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_memberType);
		try {
			setState(86);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				memberBaseType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				memberBaseTypeArray();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberBaseTypeArrayContext extends ParserRuleContext {
		public MemberBaseTypeContext memberBaseType() {
			return getRuleContext(MemberBaseTypeContext.class,0);
		}
		public ArrayIndicatorContext arrayIndicator() {
			return getRuleContext(ArrayIndicatorContext.class,0);
		}
		public MemberBaseTypeArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberBaseTypeArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterMemberBaseTypeArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitMemberBaseTypeArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitMemberBaseTypeArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberBaseTypeArrayContext memberBaseTypeArray() throws RecognitionException {
		MemberBaseTypeArrayContext _localctx = new MemberBaseTypeArrayContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_memberBaseTypeArray);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			memberBaseType();
			setState(89);
			arrayIndicator();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberBaseTypeContext extends ParserRuleContext {
		public TerminalNode STRING_TYPE() { return getToken(OmniParser.STRING_TYPE, 0); }
		public TerminalNode OBJECT_TYPE() { return getToken(OmniParser.OBJECT_TYPE, 0); }
		public TerminalNode DATA_TYPE() { return getToken(OmniParser.DATA_TYPE, 0); }
		public TerminalNode BOND_TYPE() { return getToken(OmniParser.BOND_TYPE, 0); }
		public TypeNameRefContext typeNameRef() {
			return getRuleContext(TypeNameRefContext.class,0);
		}
		public MemberBaseTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberBaseType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterMemberBaseType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitMemberBaseType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitMemberBaseType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberBaseTypeContext memberBaseType() throws RecognitionException {
		MemberBaseTypeContext _localctx = new MemberBaseTypeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_memberBaseType);
		try {
			setState(96);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_TYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(91);
				match(STRING_TYPE);
				}
				break;
			case OBJECT_TYPE:
				enterOuterAlt(_localctx, 2);
				{
				setState(92);
				match(OBJECT_TYPE);
				}
				break;
			case DATA_TYPE:
				enterOuterAlt(_localctx, 3);
				{
				setState(93);
				match(DATA_TYPE);
				}
				break;
			case BOND_TYPE:
				enterOuterAlt(_localctx, 4);
				{
				setState(94);
				match(BOND_TYPE);
				}
				break;
			case TYPE_NAME_REF:
				enterOuterAlt(_localctx, 5);
				{
				setState(95);
				typeNameRef();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MembersContext extends ParserRuleContext {
		public TerminalNode MEMBERS() { return getToken(OmniParser.MEMBERS, 0); }
		public MembersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_members; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterMembers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitMembers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitMembers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembersContext members() throws RecognitionException {
		MembersContext _localctx = new MembersContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_members);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(MEMBERS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayIndicatorContext extends ParserRuleContext {
		public TerminalNode ARRAY_INDICATOR() { return getToken(OmniParser.ARRAY_INDICATOR, 0); }
		public ArrayIndicatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayIndicator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterArrayIndicator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitArrayIndicator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitArrayIndicator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayIndicatorContext arrayIndicator() throws RecognitionException {
		ArrayIndicatorContext _localctx = new ArrayIndicatorContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_arrayIndicator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(ARRAY_INDICATOR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameRefContext extends ParserRuleContext {
		public TerminalNode TYPE_NAME_REF() { return getToken(OmniParser.TYPE_NAME_REF, 0); }
		public TypeNameRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeNameRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeNameRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeNameRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameRefContext typeNameRef() throws RecognitionException {
		TypeNameRefContext _localctx = new TypeNameRefContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_typeNameRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(TYPE_NAME_REF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameDefContext extends ParserRuleContext {
		public TerminalNode TYPE_NAME_DEF() { return getToken(OmniParser.TYPE_NAME_DEF, 0); }
		public TypeNameDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterTypeNameDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitTypeNameDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitTypeNameDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameDefContext typeNameDef() throws RecognitionException {
		TypeNameDefContext _localctx = new TypeNameDefContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeNameDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(TYPE_NAME_DEF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarNameRefContext extends ParserRuleContext {
		public TerminalNode VAR_NAME_REF() { return getToken(OmniParser.VAR_NAME_REF, 0); }
		public VarNameRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varNameRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).enterVarNameRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OmniParserListener ) ((OmniParserListener)listener).exitVarNameRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OmniParserVisitor ) return ((OmniParserVisitor<? extends T>)visitor).visitVarNameRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarNameRefContext varNameRef() throws RecognitionException {
		VarNameRefContext _localctx = new VarNameRefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_varNameRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(VAR_NAME_REF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3#o\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4\f"+
		"\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\3\2\7\2"+
		"&\n\2\f\2\16\2)\13\2\3\2\3\2\3\3\3\3\3\3\6\3\60\n\3\r\3\16\3\61\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\7\4:\n\4\f\4\16\4=\13\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6"+
		"\3\7\3\7\3\7\6\7I\n\7\r\7\16\7J\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\13\3\13\5\13Y\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\5\rc\n\r\3\16"+
		"\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\5\61;J\2\23\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"\2\3\3\2\3\5\2f\2\'\3\2\2\2\4,\3\2\2"+
		"\2\6\65\3\2\2\2\b@\3\2\2\2\nC\3\2\2\2\fE\3\2\2\2\16L\3\2\2\2\20R\3\2\2"+
		"\2\22T\3\2\2\2\24X\3\2\2\2\26Z\3\2\2\2\30b\3\2\2\2\32d\3\2\2\2\34f\3\2"+
		"\2\2\36h\3\2\2\2 j\3\2\2\2\"l\3\2\2\2$&\5\4\3\2%$\3\2\2\2&)\3\2\2\2\'"+
		"%\3\2\2\2\'(\3\2\2\2(*\3\2\2\2)\'\3\2\2\2*+\7\2\2\3+\3\3\2\2\2,/\5\20"+
		"\t\2-.\7\37\2\2.\60\5\6\4\2/-\3\2\2\2\60\61\3\2\2\2\61\62\3\2\2\2\61/"+
		"\3\2\2\2\62\63\3\2\2\2\63\64\7 \2\2\64\5\3\2\2\2\65\66\7\b\2\2\66\67\7"+
		"\32\2\2\67;\5 \21\28:\5\b\5\298\3\2\2\2:=\3\2\2\2;<\3\2\2\2;9\3\2\2\2"+
		"<>\3\2\2\2=;\3\2\2\2>?\7\33\2\2?\7\3\2\2\2@A\7\36\2\2AB\5\n\6\2B\t\3\2"+
		"\2\2CD\5\f\7\2D\13\3\2\2\2EH\5\22\n\2FG\7\37\2\2GI\5\16\b\2HF\3\2\2\2"+
		"IJ\3\2\2\2JK\3\2\2\2JH\3\2\2\2K\r\3\2\2\2LM\7\7\2\2MN\7\32\2\2NO\5\24"+
		"\13\2OP\5\"\22\2PQ\7\33\2\2Q\17\3\2\2\2RS\t\2\2\2S\21\3\2\2\2TU\5\32\16"+
		"\2U\23\3\2\2\2VY\5\30\r\2WY\5\26\f\2XV\3\2\2\2XW\3\2\2\2Y\25\3\2\2\2Z"+
		"[\5\30\r\2[\\\5\34\17\2\\\27\3\2\2\2]c\7\13\2\2^c\7\f\2\2_c\7\t\2\2`c"+
		"\7\n\2\2ac\5\36\20\2b]\3\2\2\2b^\3\2\2\2b_\3\2\2\2b`\3\2\2\2ba\3\2\2\2"+
		"c\31\3\2\2\2de\7\6\2\2e\33\3\2\2\2fg\7\31\2\2g\35\3\2\2\2hi\7\17\2\2i"+
		"\37\3\2\2\2jk\7\r\2\2k!\3\2\2\2lm\7\20\2\2m#\3\2\2\2\b\'\61;JXb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}