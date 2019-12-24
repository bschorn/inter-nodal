// Generated from src\main\java\org\schorn\ella\antlr\SpecParser.g4 by ANTLR 4.7.2
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
public class SpecParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ATTRIBUTES=1, FIELD_TYPES=2, VALUE_TYPES=3, FLAGS=4, FRAGMENTS=5, OBJECT_TYPES=6, 
		BASE_TYPES=7, ARRAY_TYPES=8, MEMBERS=9, PARENTS=10, DATA_TYPE=11, OBJECT_CATEGORY=12, 
		OBJECT_PURPOSE=13, OBJECT_LEVEL=14, FIELD_TYPE=15, VALUE_TYPE=16, VALUE_FLAG=17, 
		FRAGMENT=18, OBJECT_TYPE=19, BASE_TYPE=20, ARRAY_TYPE=21, MEMBER=22, PARENT=23, 
		BOND_TYPE=24, CMD_ACTION_READ=25, CMD_OBJECT_FILE=26, DEFINE=27, ADD=28, 
		MAX=29, MIN=30, TYPE_NAME_DEF=31, ENUM_ID=32, TYPE_NAME_REF=33, TEXT_LENGTH=34, 
		DECIMAL_LITERAL=35, FLOAT_LITERAL=36, CHAR_LITERAL=37, TEXT_PATTERN=38, 
		STRING_LITERAL=39, NULL_LITERAL=40, OPHAT=41, OPDOLLAR=42, OPTILDE=43, 
		OPAT=44, OPHASH=45, OPPIPE=46, OPDBLQUOTE=47, LPAREN=48, RPAREN=49, LSBRACKET=50, 
		RSBRACKET=51, LABRACKET=52, RABRACKET=53, LBRACE=54, RBRACE=55, COMMA=56, 
		DOT=57, COLON=58, SEMI=59, WS=60, COMMENT=61, LINE_COMMENT=62;
	public static final int
		RULE_def = 0, RULE_typeCreation = 1, RULE_defineType = 2, RULE_typeAttributes = 3, 
		RULE_typeAttribute = 4, RULE_parameters = 5, RULE_parameter = 6, RULE_command = 7, 
		RULE_cmdPath = 8, RULE_listOfValues = 9, RULE_listValue = 10, RULE_attrCreation = 11, 
		RULE_addTypeToAttr = 12, RULE_attributeAttributes = 13, RULE_attributeAttribute = 14, 
		RULE_typeFlavor = 15, RULE_attrFlavor = 16, RULE_typeQualifier = 17, RULE_attributeType = 18, 
		RULE_enumQualifier = 19, RULE_cmdAction = 20, RULE_cmdObject = 21, RULE_cmd = 22, 
		RULE_enumID = 23, RULE_typeNameRef = 24, RULE_typeNameDef = 25, RULE_pattern = 26, 
		RULE_number = 27, RULE_datetime = 28;
	private static String[] makeRuleNames() {
		return new String[] {
			"def", "typeCreation", "defineType", "typeAttributes", "typeAttribute", 
			"parameters", "parameter", "command", "cmdPath", "listOfValues", "listValue", 
			"attrCreation", "addTypeToAttr", "attributeAttributes", "attributeAttribute", 
			"typeFlavor", "attrFlavor", "typeQualifier", "attributeType", "enumQualifier", 
			"cmdAction", "cmdObject", "cmd", "enumID", "typeNameRef", "typeNameDef", 
			"pattern", "number", "datetime"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'Attributes'", "'FieldTypes'", "'ValueTypes'", "'Flags'", "'Fragments'", 
			"'ObjectTypes'", "'BaseTypes'", "'ArrayTypes'", "'Members'", "'Parents'", 
			"'DataType'", "'ObjectCategory'", "'ObjectPurpose'", "'ObjectLevel'", 
			"'FieldType'", "'ValueType'", "'ValueFlag'", "'Fragment'", "'ObjectType'", 
			"'BaseType'", "'ArrayType'", "'Member'", "'Parent'", "'BondType'", "'Read'", 
			"'File'", "'def'", "'add'", "'MAX'", "'MIN'", null, null, null, null, 
			null, null, null, null, null, "'null'", "'^'", "'$'", "'~'", "'@'", "'#'", 
			"'|'", "'\"'", "'('", "')'", "'['", "']'", "'<'", "'>'", "'{'", "'}'", 
			"','", "'.'", "':'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ATTRIBUTES", "FIELD_TYPES", "VALUE_TYPES", "FLAGS", "FRAGMENTS", 
			"OBJECT_TYPES", "BASE_TYPES", "ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", 
			"OBJECT_CATEGORY", "OBJECT_PURPOSE", "OBJECT_LEVEL", "FIELD_TYPE", "VALUE_TYPE", 
			"VALUE_FLAG", "FRAGMENT", "OBJECT_TYPE", "BASE_TYPE", "ARRAY_TYPE", "MEMBER", 
			"PARENT", "BOND_TYPE", "CMD_ACTION_READ", "CMD_OBJECT_FILE", "DEFINE", 
			"ADD", "MAX", "MIN", "TYPE_NAME_DEF", "ENUM_ID", "TYPE_NAME_REF", "TEXT_LENGTH", 
			"DECIMAL_LITERAL", "FLOAT_LITERAL", "CHAR_LITERAL", "TEXT_PATTERN", "STRING_LITERAL", 
			"NULL_LITERAL", "OPHAT", "OPDOLLAR", "OPTILDE", "OPAT", "OPHASH", "OPPIPE", 
			"OPDBLQUOTE", "LPAREN", "RPAREN", "LSBRACKET", "RSBRACKET", "LABRACKET", 
			"RABRACKET", "LBRACE", "RBRACE", "COMMA", "DOT", "COLON", "SEMI", "WS", 
			"COMMENT", "LINE_COMMENT"
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
	public String getGrammarFileName() { return "SpecParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SpecParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class DefContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SpecParser.EOF, 0); }
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
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitDef(this);
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
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FIELD_TYPES) | (1L << VALUE_TYPES) | (1L << FRAGMENTS) | (1L << OBJECT_TYPES) | (1L << BASE_TYPES) | (1L << ARRAY_TYPES))) != 0)) {
				{
				{
				setState(58);
				typeCreation();
				}
				}
				setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(64);
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
		public TerminalNode SEMI() { return getToken(SpecParser.SEMI, 0); }
		public List<TerminalNode> DOT() { return getTokens(SpecParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SpecParser.DOT, i);
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
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeCreation(this);
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
			setState(66);
			typeFlavor();
			setState(69); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(67);
					match(DOT);
					setState(68);
					defineType();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(71); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(73);
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
		public TerminalNode DEFINE() { return getToken(SpecParser.DEFINE, 0); }
		public TerminalNode LPAREN() { return getToken(SpecParser.LPAREN, 0); }
		public TypeNameDefContext typeNameDef() {
			return getRuleContext(TypeNameDefContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SpecParser.RPAREN, 0); }
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
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterDefineType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitDefineType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitDefineType(this);
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
			setState(75);
			match(DEFINE);
			setState(76);
			match(LPAREN);
			setState(77);
			typeNameDef();
			setState(81);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(78);
					typeAttributes();
					}
					} 
				}
				setState(83);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(84);
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
		public TerminalNode COMMA() { return getToken(SpecParser.COMMA, 0); }
		public TypeAttributeContext typeAttribute() {
			return getRuleContext(TypeAttributeContext.class,0);
		}
		public TypeAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeAttributes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeAttributes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAttributesContext typeAttributes() throws RecognitionException {
		TypeAttributesContext _localctx = new TypeAttributesContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeAttributes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			match(COMMA);
			setState(87);
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
		public EnumQualifierContext enumQualifier() {
			return getRuleContext(EnumQualifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SpecParser.DOT, 0); }
		public EnumIDContext enumID() {
			return getRuleContext(EnumIDContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(SpecParser.LPAREN, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SpecParser.RPAREN, 0); }
		public TypeQualifierContext typeQualifier() {
			return getRuleContext(TypeQualifierContext.class,0);
		}
		public TypeNameRefContext typeNameRef() {
			return getRuleContext(TypeNameRefContext.class,0);
		}
		public AttrCreationContext attrCreation() {
			return getRuleContext(AttrCreationContext.class,0);
		}
		public TypeAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeAttributeContext typeAttribute() throws RecognitionException {
		TypeAttributeContext _localctx = new TypeAttributeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_typeAttribute);
		try {
			setState(105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(89);
				enumQualifier();
				setState(90);
				match(DOT);
				setState(91);
				enumID();
				setState(92);
				match(LPAREN);
				setState(93);
				parameters();
				setState(94);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				enumQualifier();
				setState(97);
				match(DOT);
				setState(98);
				enumID();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(100);
				typeQualifier();
				setState(101);
				match(DOT);
				setState(102);
				typeNameRef();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(104);
				attrCreation();
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

	public static class ParametersContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SpecParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SpecParser.COMMA, i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			parameter();
			setState(112);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(108);
				match(COMMA);
				setState(109);
				parameter();
				}
				}
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class ParameterContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public DatetimeContext datetime() {
			return getRuleContext(DatetimeContext.class,0);
		}
		public PatternContext pattern() {
			return getRuleContext(PatternContext.class,0);
		}
		public List<TypeNameRefContext> typeNameRef() {
			return getRuleContexts(TypeNameRefContext.class);
		}
		public TypeNameRefContext typeNameRef(int i) {
			return getRuleContext(TypeNameRefContext.class,i);
		}
		public TerminalNode DOT() { return getToken(SpecParser.DOT, 0); }
		public CommandContext command() {
			return getRuleContext(CommandContext.class,0);
		}
		public ListOfValuesContext listOfValues() {
			return getRuleContext(ListOfValuesContext.class,0);
		}
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parameter);
		try {
			setState(124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FLOAT_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(115);
				number();
				}
				break;
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(116);
				datetime();
				}
				break;
			case TEXT_PATTERN:
				enterOuterAlt(_localctx, 3);
				{
				setState(117);
				pattern();
				}
				break;
			case TYPE_NAME_REF:
				enterOuterAlt(_localctx, 4);
				{
				setState(118);
				typeNameRef();
				setState(119);
				match(DOT);
				setState(120);
				typeNameRef();
				}
				break;
			case CMD_ACTION_READ:
				enterOuterAlt(_localctx, 5);
				{
				setState(122);
				command();
				}
				break;
			case LSBRACKET:
				enterOuterAlt(_localctx, 6);
				{
				setState(123);
				listOfValues();
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

	public static class CommandContext extends ParserRuleContext {
		public CmdContext cmd() {
			return getRuleContext(CmdContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(SpecParser.LPAREN, 0); }
		public CmdPathContext cmdPath() {
			return getRuleContext(CmdPathContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SpecParser.RPAREN, 0); }
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_command);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			cmd();
			setState(127);
			match(LPAREN);
			setState(128);
			cmdPath();
			setState(129);
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

	public static class CmdPathContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(SpecParser.STRING_LITERAL, 0); }
		public CmdPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdPath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterCmdPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitCmdPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitCmdPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmdPathContext cmdPath() throws RecognitionException {
		CmdPathContext _localctx = new CmdPathContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_cmdPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(STRING_LITERAL);
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

	public static class ListOfValuesContext extends ParserRuleContext {
		public TerminalNode LSBRACKET() { return getToken(SpecParser.LSBRACKET, 0); }
		public List<ListValueContext> listValue() {
			return getRuleContexts(ListValueContext.class);
		}
		public ListValueContext listValue(int i) {
			return getRuleContext(ListValueContext.class,i);
		}
		public TerminalNode RSBRACKET() { return getToken(SpecParser.RSBRACKET, 0); }
		public List<TerminalNode> COMMA() { return getTokens(SpecParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SpecParser.COMMA, i);
		}
		public ListOfValuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listOfValues; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterListOfValues(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitListOfValues(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitListOfValues(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListOfValuesContext listOfValues() throws RecognitionException {
		ListOfValuesContext _localctx = new ListOfValuesContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_listOfValues);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(LSBRACKET);
			setState(134);
			listValue();
			setState(139);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(135);
					match(COMMA);
					setState(136);
					listValue();
					}
					} 
				}
				setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(142);
			match(RSBRACKET);
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

	public static class ListValueContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(SpecParser.STRING_LITERAL, 0); }
		public ListValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterListValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitListValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitListValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListValueContext listValue() throws RecognitionException {
		ListValueContext _localctx = new ListValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_listValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(STRING_LITERAL);
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
		public List<TerminalNode> DOT() { return getTokens(SpecParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(SpecParser.DOT, i);
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
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAttrCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAttrCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAttrCreation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrCreationContext attrCreation() throws RecognitionException {
		AttrCreationContext _localctx = new AttrCreationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_attrCreation);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			attrFlavor();
			setState(149); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(147);
					match(DOT);
					setState(148);
					addTypeToAttr();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(151); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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
		public TerminalNode ADD() { return getToken(SpecParser.ADD, 0); }
		public TerminalNode LPAREN() { return getToken(SpecParser.LPAREN, 0); }
		public TypeQualifierContext typeQualifier() {
			return getRuleContext(TypeQualifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SpecParser.DOT, 0); }
		public TypeNameRefContext typeNameRef() {
			return getRuleContext(TypeNameRefContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SpecParser.RPAREN, 0); }
		public TerminalNode COMMA() { return getToken(SpecParser.COMMA, 0); }
		public AttributeAttributesContext attributeAttributes() {
			return getRuleContext(AttributeAttributesContext.class,0);
		}
		public AttributeTypeContext attributeType() {
			return getRuleContext(AttributeTypeContext.class,0);
		}
		public EnumIDContext enumID() {
			return getRuleContext(EnumIDContext.class,0);
		}
		public AddTypeToAttrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addTypeToAttr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAddTypeToAttr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAddTypeToAttr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAddTypeToAttr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AddTypeToAttrContext addTypeToAttr() throws RecognitionException {
		AddTypeToAttrContext _localctx = new AddTypeToAttrContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_addTypeToAttr);
		try {
			setState(172);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(153);
				match(ADD);
				setState(154);
				match(LPAREN);
				setState(155);
				typeQualifier();
				setState(156);
				match(DOT);
				setState(157);
				typeNameRef();
				setState(163);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case RPAREN:
					{
					setState(158);
					match(RPAREN);
					}
					break;
				case COMMA:
					{
					setState(159);
					match(COMMA);
					setState(160);
					attributeAttributes();
					setState(161);
					match(RPAREN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(165);
				match(ADD);
				setState(166);
				match(LPAREN);
				setState(167);
				attributeType();
				setState(168);
				match(DOT);
				setState(169);
				enumID();
				setState(170);
				match(RPAREN);
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

	public static class AttributeAttributesContext extends ParserRuleContext {
		public List<AttributeAttributeContext> attributeAttribute() {
			return getRuleContexts(AttributeAttributeContext.class);
		}
		public AttributeAttributeContext attributeAttribute(int i) {
			return getRuleContext(AttributeAttributeContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(SpecParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(SpecParser.COMMA, i);
		}
		public AttributeAttributesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeAttributes; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAttributeAttributes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAttributeAttributes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAttributeAttributes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeAttributesContext attributeAttributes() throws RecognitionException {
		AttributeAttributesContext _localctx = new AttributeAttributesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_attributeAttributes);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			attributeAttribute();
			setState(179);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(175);
					match(COMMA);
					setState(176);
					attributeAttribute();
					}
					} 
				}
				setState(181);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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

	public static class AttributeAttributeContext extends ParserRuleContext {
		public EnumQualifierContext enumQualifier() {
			return getRuleContext(EnumQualifierContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SpecParser.DOT, 0); }
		public EnumIDContext enumID() {
			return getRuleContext(EnumIDContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(SpecParser.LPAREN, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(SpecParser.RPAREN, 0); }
		public TypeQualifierContext typeQualifier() {
			return getRuleContext(TypeQualifierContext.class,0);
		}
		public TypeNameRefContext typeNameRef() {
			return getRuleContext(TypeNameRefContext.class,0);
		}
		public AttributeAttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeAttribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAttributeAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAttributeAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAttributeAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeAttributeContext attributeAttribute() throws RecognitionException {
		AttributeAttributeContext _localctx = new AttributeAttributeContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_attributeAttribute);
		try {
			setState(197);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				enumQualifier();
				setState(183);
				match(DOT);
				setState(184);
				enumID();
				setState(185);
				match(LPAREN);
				setState(186);
				parameters();
				setState(187);
				match(RPAREN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(189);
				enumQualifier();
				setState(190);
				match(DOT);
				setState(191);
				enumID();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(193);
				typeQualifier();
				setState(194);
				match(DOT);
				setState(195);
				typeNameRef();
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

	public static class TypeFlavorContext extends ParserRuleContext {
		public TerminalNode FIELD_TYPES() { return getToken(SpecParser.FIELD_TYPES, 0); }
		public TerminalNode VALUE_TYPES() { return getToken(SpecParser.VALUE_TYPES, 0); }
		public TerminalNode FRAGMENTS() { return getToken(SpecParser.FRAGMENTS, 0); }
		public TerminalNode BASE_TYPES() { return getToken(SpecParser.BASE_TYPES, 0); }
		public TerminalNode OBJECT_TYPES() { return getToken(SpecParser.OBJECT_TYPES, 0); }
		public TerminalNode ARRAY_TYPES() { return getToken(SpecParser.ARRAY_TYPES, 0); }
		public TypeFlavorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeFlavor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeFlavor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeFlavor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeFlavor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeFlavorContext typeFlavor() throws RecognitionException {
		TypeFlavorContext _localctx = new TypeFlavorContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_typeFlavor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FIELD_TYPES) | (1L << VALUE_TYPES) | (1L << FRAGMENTS) | (1L << OBJECT_TYPES) | (1L << BASE_TYPES) | (1L << ARRAY_TYPES))) != 0)) ) {
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
		public TerminalNode MEMBERS() { return getToken(SpecParser.MEMBERS, 0); }
		public TerminalNode PARENTS() { return getToken(SpecParser.PARENTS, 0); }
		public TerminalNode ATTRIBUTES() { return getToken(SpecParser.ATTRIBUTES, 0); }
		public TerminalNode FLAGS() { return getToken(SpecParser.FLAGS, 0); }
		public AttrFlavorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attrFlavor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAttrFlavor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAttrFlavor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAttrFlavor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttrFlavorContext attrFlavor() throws RecognitionException {
		AttrFlavorContext _localctx = new AttrFlavorContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_attrFlavor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTES) | (1L << FLAGS) | (1L << MEMBERS) | (1L << PARENTS))) != 0)) ) {
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

	public static class TypeQualifierContext extends ParserRuleContext {
		public TerminalNode FIELD_TYPE() { return getToken(SpecParser.FIELD_TYPE, 0); }
		public TerminalNode VALUE_TYPE() { return getToken(SpecParser.VALUE_TYPE, 0); }
		public TerminalNode FRAGMENT() { return getToken(SpecParser.FRAGMENT, 0); }
		public TerminalNode BASE_TYPE() { return getToken(SpecParser.BASE_TYPE, 0); }
		public TerminalNode OBJECT_TYPE() { return getToken(SpecParser.OBJECT_TYPE, 0); }
		public TerminalNode ARRAY_TYPE() { return getToken(SpecParser.ARRAY_TYPE, 0); }
		public TerminalNode VALUE_FLAG() { return getToken(SpecParser.VALUE_FLAG, 0); }
		public TypeQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeQualifierContext typeQualifier() throws RecognitionException {
		TypeQualifierContext _localctx = new TypeQualifierContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_typeQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << FIELD_TYPE) | (1L << VALUE_TYPE) | (1L << VALUE_FLAG) | (1L << FRAGMENT) | (1L << OBJECT_TYPE) | (1L << BASE_TYPE) | (1L << ARRAY_TYPE))) != 0)) ) {
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

	public static class AttributeTypeContext extends ParserRuleContext {
		public TerminalNode OBJECT_CATEGORY() { return getToken(SpecParser.OBJECT_CATEGORY, 0); }
		public TerminalNode OBJECT_PURPOSE() { return getToken(SpecParser.OBJECT_PURPOSE, 0); }
		public TerminalNode OBJECT_LEVEL() { return getToken(SpecParser.OBJECT_LEVEL, 0); }
		public AttributeTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterAttributeType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitAttributeType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitAttributeType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeTypeContext attributeType() throws RecognitionException {
		AttributeTypeContext _localctx = new AttributeTypeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_attributeType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << OBJECT_CATEGORY) | (1L << OBJECT_PURPOSE) | (1L << OBJECT_LEVEL))) != 0)) ) {
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

	public static class EnumQualifierContext extends ParserRuleContext {
		public TerminalNode DATA_TYPE() { return getToken(SpecParser.DATA_TYPE, 0); }
		public TerminalNode BOND_TYPE() { return getToken(SpecParser.BOND_TYPE, 0); }
		public EnumQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterEnumQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitEnumQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitEnumQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumQualifierContext enumQualifier() throws RecognitionException {
		EnumQualifierContext _localctx = new EnumQualifierContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_enumQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			_la = _input.LA(1);
			if ( !(_la==DATA_TYPE || _la==BOND_TYPE) ) {
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

	public static class CmdActionContext extends ParserRuleContext {
		public TerminalNode CMD_ACTION_READ() { return getToken(SpecParser.CMD_ACTION_READ, 0); }
		public CmdActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdAction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterCmdAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitCmdAction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitCmdAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmdActionContext cmdAction() throws RecognitionException {
		CmdActionContext _localctx = new CmdActionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_cmdAction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(CMD_ACTION_READ);
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

	public static class CmdObjectContext extends ParserRuleContext {
		public TerminalNode CMD_OBJECT_FILE() { return getToken(SpecParser.CMD_OBJECT_FILE, 0); }
		public CmdObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmdObject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterCmdObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitCmdObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitCmdObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmdObjectContext cmdObject() throws RecognitionException {
		CmdObjectContext _localctx = new CmdObjectContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_cmdObject);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(CMD_OBJECT_FILE);
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

	public static class CmdContext extends ParserRuleContext {
		public CmdActionContext cmdAction() {
			return getRuleContext(CmdActionContext.class,0);
		}
		public TerminalNode DOT() { return getToken(SpecParser.DOT, 0); }
		public CmdObjectContext cmdObject() {
			return getRuleContext(CmdObjectContext.class,0);
		}
		public CmdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmd; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterCmd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitCmd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitCmd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CmdContext cmd() throws RecognitionException {
		CmdContext _localctx = new CmdContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_cmd);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			cmdAction();
			setState(214);
			match(DOT);
			setState(215);
			cmdObject();
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

	public static class EnumIDContext extends ParserRuleContext {
		public TerminalNode ENUM_ID() { return getToken(SpecParser.ENUM_ID, 0); }
		public EnumIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterEnumID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitEnumID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitEnumID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumIDContext enumID() throws RecognitionException {
		EnumIDContext _localctx = new EnumIDContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_enumID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			match(ENUM_ID);
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
		public TerminalNode TYPE_NAME_REF() { return getToken(SpecParser.TYPE_NAME_REF, 0); }
		public TypeNameRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeNameRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeNameRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeNameRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameRefContext typeNameRef() throws RecognitionException {
		TypeNameRefContext _localctx = new TypeNameRefContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_typeNameRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
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
		public TerminalNode TYPE_NAME_DEF() { return getToken(SpecParser.TYPE_NAME_DEF, 0); }
		public TypeNameDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterTypeNameDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitTypeNameDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitTypeNameDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameDefContext typeNameDef() throws RecognitionException {
		TypeNameDefContext _localctx = new TypeNameDefContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_typeNameDef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
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

	public static class PatternContext extends ParserRuleContext {
		public TerminalNode TEXT_PATTERN() { return getToken(SpecParser.TEXT_PATTERN, 0); }
		public PatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PatternContext pattern() throws RecognitionException {
		PatternContext _localctx = new PatternContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_pattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			match(TEXT_PATTERN);
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

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode FLOAT_LITERAL() { return getToken(SpecParser.FLOAT_LITERAL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(FLOAT_LITERAL);
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

	public static class DatetimeContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(SpecParser.STRING_LITERAL, 0); }
		public DatetimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datetime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).enterDatetime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SpecParserListener ) ((SpecParserListener)listener).exitDatetime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SpecParserVisitor ) return ((SpecParserVisitor<? extends T>)visitor).visitDatetime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatetimeContext datetime() throws RecognitionException {
		DatetimeContext _localctx = new DatetimeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_datetime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			match(STRING_LITERAL);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3@\u00e8\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\7\2>\n\2\f\2\16"+
		"\2A\13\2\3\2\3\2\3\3\3\3\3\3\6\3H\n\3\r\3\16\3I\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\7\4R\n\4\f\4\16\4U\13\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6l\n\6\3\7\3\7\3\7\7\7q\n\7"+
		"\f\7\16\7t\13\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\177\n\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\7\13\u008c\n\13\f\13\16\13"+
		"\u008f\13\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\6\r\u0098\n\r\r\r\16\r\u0099"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00a6\n\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u00af\n\16\3\17\3\17\3\17\7\17\u00b4"+
		"\n\17\f\17\16\17\u00b7\13\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00c8\n\20\3\21\3\21\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\7IS"+
		"\u008d\u0099\u00b5\2\37\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*"+
		",.\60\62\64\668:\2\7\4\2\4\5\7\n\5\2\3\3\6\6\13\f\3\2\21\27\3\2\16\20"+
		"\4\2\r\r\32\32\2\u00dd\2?\3\2\2\2\4D\3\2\2\2\6M\3\2\2\2\bX\3\2\2\2\nk"+
		"\3\2\2\2\fm\3\2\2\2\16~\3\2\2\2\20\u0080\3\2\2\2\22\u0085\3\2\2\2\24\u0087"+
		"\3\2\2\2\26\u0092\3\2\2\2\30\u0094\3\2\2\2\32\u00ae\3\2\2\2\34\u00b0\3"+
		"\2\2\2\36\u00c7\3\2\2\2 \u00c9\3\2\2\2\"\u00cb\3\2\2\2$\u00cd\3\2\2\2"+
		"&\u00cf\3\2\2\2(\u00d1\3\2\2\2*\u00d3\3\2\2\2,\u00d5\3\2\2\2.\u00d7\3"+
		"\2\2\2\60\u00db\3\2\2\2\62\u00dd\3\2\2\2\64\u00df\3\2\2\2\66\u00e1\3\2"+
		"\2\28\u00e3\3\2\2\2:\u00e5\3\2\2\2<>\5\4\3\2=<\3\2\2\2>A\3\2\2\2?=\3\2"+
		"\2\2?@\3\2\2\2@B\3\2\2\2A?\3\2\2\2BC\7\2\2\3C\3\3\2\2\2DG\5 \21\2EF\7"+
		";\2\2FH\5\6\4\2GE\3\2\2\2HI\3\2\2\2IJ\3\2\2\2IG\3\2\2\2JK\3\2\2\2KL\7"+
		"=\2\2L\5\3\2\2\2MN\7\35\2\2NO\7\62\2\2OS\5\64\33\2PR\5\b\5\2QP\3\2\2\2"+
		"RU\3\2\2\2ST\3\2\2\2SQ\3\2\2\2TV\3\2\2\2US\3\2\2\2VW\7\63\2\2W\7\3\2\2"+
		"\2XY\7:\2\2YZ\5\n\6\2Z\t\3\2\2\2[\\\5(\25\2\\]\7;\2\2]^\5\60\31\2^_\7"+
		"\62\2\2_`\5\f\7\2`a\7\63\2\2al\3\2\2\2bc\5(\25\2cd\7;\2\2de\5\60\31\2"+
		"el\3\2\2\2fg\5$\23\2gh\7;\2\2hi\5\62\32\2il\3\2\2\2jl\5\30\r\2k[\3\2\2"+
		"\2kb\3\2\2\2kf\3\2\2\2kj\3\2\2\2l\13\3\2\2\2mr\5\16\b\2no\7:\2\2oq\5\16"+
		"\b\2pn\3\2\2\2qt\3\2\2\2rp\3\2\2\2rs\3\2\2\2s\r\3\2\2\2tr\3\2\2\2u\177"+
		"\58\35\2v\177\5:\36\2w\177\5\66\34\2xy\5\62\32\2yz\7;\2\2z{\5\62\32\2"+
		"{\177\3\2\2\2|\177\5\20\t\2}\177\5\24\13\2~u\3\2\2\2~v\3\2\2\2~w\3\2\2"+
		"\2~x\3\2\2\2~|\3\2\2\2~}\3\2\2\2\177\17\3\2\2\2\u0080\u0081\5.\30\2\u0081"+
		"\u0082\7\62\2\2\u0082\u0083\5\22\n\2\u0083\u0084\7\63\2\2\u0084\21\3\2"+
		"\2\2\u0085\u0086\7)\2\2\u0086\23\3\2\2\2\u0087\u0088\7\64\2\2\u0088\u008d"+
		"\5\26\f\2\u0089\u008a\7:\2\2\u008a\u008c\5\26\f\2\u008b\u0089\3\2\2\2"+
		"\u008c\u008f\3\2\2\2\u008d\u008e\3\2\2\2\u008d\u008b\3\2\2\2\u008e\u0090"+
		"\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091\7\65\2\2\u0091\25\3\2\2\2\u0092"+
		"\u0093\7)\2\2\u0093\27\3\2\2\2\u0094\u0097\5\"\22\2\u0095\u0096\7;\2\2"+
		"\u0096\u0098\5\32\16\2\u0097\u0095\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009a"+
		"\3\2\2\2\u0099\u0097\3\2\2\2\u009a\31\3\2\2\2\u009b\u009c\7\36\2\2\u009c"+
		"\u009d\7\62\2\2\u009d\u009e\5$\23\2\u009e\u009f\7;\2\2\u009f\u00a5\5\62"+
		"\32\2\u00a0\u00a6\7\63\2\2\u00a1\u00a2\7:\2\2\u00a2\u00a3\5\34\17\2\u00a3"+
		"\u00a4\7\63\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a0\3\2\2\2\u00a5\u00a1\3"+
		"\2\2\2\u00a6\u00af\3\2\2\2\u00a7\u00a8\7\36\2\2\u00a8\u00a9\7\62\2\2\u00a9"+
		"\u00aa\5&\24\2\u00aa\u00ab\7;\2\2\u00ab\u00ac\5\60\31\2\u00ac\u00ad\7"+
		"\63\2\2\u00ad\u00af\3\2\2\2\u00ae\u009b\3\2\2\2\u00ae\u00a7\3\2\2\2\u00af"+
		"\33\3\2\2\2\u00b0\u00b5\5\36\20\2\u00b1\u00b2\7:\2\2\u00b2\u00b4\5\36"+
		"\20\2\u00b3\u00b1\3\2\2\2\u00b4\u00b7\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b5"+
		"\u00b3\3\2\2\2\u00b6\35\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b8\u00b9\5(\25"+
		"\2\u00b9\u00ba\7;\2\2\u00ba\u00bb\5\60\31\2\u00bb\u00bc\7\62\2\2\u00bc"+
		"\u00bd\5\f\7\2\u00bd\u00be\7\63\2\2\u00be\u00c8\3\2\2\2\u00bf\u00c0\5"+
		"(\25\2\u00c0\u00c1\7;\2\2\u00c1\u00c2\5\60\31\2\u00c2\u00c8\3\2\2\2\u00c3"+
		"\u00c4\5$\23\2\u00c4\u00c5\7;\2\2\u00c5\u00c6\5\62\32\2\u00c6\u00c8\3"+
		"\2\2\2\u00c7\u00b8\3\2\2\2\u00c7\u00bf\3\2\2\2\u00c7\u00c3\3\2\2\2\u00c8"+
		"\37\3\2\2\2\u00c9\u00ca\t\2\2\2\u00ca!\3\2\2\2\u00cb\u00cc\t\3\2\2\u00cc"+
		"#\3\2\2\2\u00cd\u00ce\t\4\2\2\u00ce%\3\2\2\2\u00cf\u00d0\t\5\2\2\u00d0"+
		"\'\3\2\2\2\u00d1\u00d2\t\6\2\2\u00d2)\3\2\2\2\u00d3\u00d4\7\33\2\2\u00d4"+
		"+\3\2\2\2\u00d5\u00d6\7\34\2\2\u00d6-\3\2\2\2\u00d7\u00d8\5*\26\2\u00d8"+
		"\u00d9\7;\2\2\u00d9\u00da\5,\27\2\u00da/\3\2\2\2\u00db\u00dc\7\"\2\2\u00dc"+
		"\61\3\2\2\2\u00dd\u00de\7#\2\2\u00de\63\3\2\2\2\u00df\u00e0\7!\2\2\u00e0"+
		"\65\3\2\2\2\u00e1\u00e2\7(\2\2\u00e2\67\3\2\2\2\u00e3\u00e4\7&\2\2\u00e4"+
		"9\3\2\2\2\u00e5\u00e6\7)\2\2\u00e6;\3\2\2\2\16?ISkr~\u008d\u0099\u00a5"+
		"\u00ae\u00b5\u00c7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}