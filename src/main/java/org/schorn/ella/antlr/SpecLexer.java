// Generated from src\main\java\org\schorn\ella\antlr\SpecLexer.g4 by ANTLR 4.7.2
package org.schorn.ella.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SpecLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ATTRIBUTES=1, FIELD_TYPES=2, VALUE_TYPES=3, FRAGMENTS=4, OBJECT_TYPES=5, 
		TEMPLATES=6, ARRAY_TYPES=7, MEMBERS=8, PARENTS=9, DATA_TYPE=10, DATA_CATEGORY=11, 
		DATA_PURPOSE=12, DATA_LEVEL=13, FIELD_TYPE=14, VALUE_TYPE=15, FRAGMENT=16, 
		OBJECT_TYPE=17, TEMPLATE=18, ARRAY_TYPE=19, MEMBER=20, PARENT=21, BOND_TYPE=22, 
		CMD_ACTION_READ=23, CMD_OBJECT_FILE=24, DEFINE=25, ADD=26, MAX=27, MIN=28, 
		TYPE_NAME_DEF=29, ENUM_ID=30, TYPE_NAME_REF=31, TEXT_LENGTH=32, DECIMAL_LITERAL=33, 
		FLOAT_LITERAL=34, CHAR_LITERAL=35, TEXT_PATTERN=36, STRING_LITERAL=37, 
		NULL_LITERAL=38, OPHAT=39, OPDOLLAR=40, OPTILDE=41, OPAT=42, OPHASH=43, 
		OPPIPE=44, OPDBLQUOTE=45, LPAREN=46, RPAREN=47, LSBRACKET=48, RSBRACKET=49, 
		LABRACKET=50, RABRACKET=51, LBRACE=52, RBRACE=53, COMMA=54, DOT=55, COLON=56, 
		SEMI=57, WS=58, COMMENT=59, LINE_COMMENT=60;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ATTRIBUTES", "FIELD_TYPES", "VALUE_TYPES", "FRAGMENTS", "OBJECT_TYPES", 
			"TEMPLATES", "ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", "DATA_CATEGORY", 
			"DATA_PURPOSE", "DATA_LEVEL", "FIELD_TYPE", "VALUE_TYPE", "FRAGMENT", 
			"OBJECT_TYPE", "TEMPLATE", "ARRAY_TYPE", "MEMBER", "PARENT", "BOND_TYPE", 
			"CMD_ACTION_READ", "CMD_OBJECT_FILE", "DEFINE", "ADD", "MAX", "MIN", 
			"TYPE_NAME_DEF", "ENUM_ID", "TYPE_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
			"FLOAT_LITERAL", "CHAR_LITERAL", "TEXT_PATTERN", "STRING_LITERAL", "NULL_LITERAL", 
			"OPHAT", "OPDOLLAR", "OPTILDE", "OPAT", "OPHASH", "OPPIPE", "OPDBLQUOTE", 
			"LPAREN", "RPAREN", "LSBRACKET", "RSBRACKET", "LABRACKET", "RABRACKET", 
			"LBRACE", "RBRACE", "COMMA", "DOT", "COLON", "SEMI", "WS", "COMMENT", 
			"LINE_COMMENT", "ExponentPart", "EscapeSequence", "HexDigits", "HexDigit", 
			"Digits", "Digit", "LowerLetterOrUnder", "UpperLetterOrUnder", "LetterOrUnder", 
			"LetterOrDigitOrUnder", "LowerLetter", "UpperLetter", "Letter", "LetterOrDigit", 
			"DateTime"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'Attributes'", "'FieldTypes'", "'ValueTypes'", "'Fragments'", 
			"'ObjectTypes'", "'Templates'", "'ArrayTypes'", "'Members'", "'Parents'", 
			"'DataType'", "'DataCategory'", "'DataPurpose'", "'DataLevel'", "'FieldType'", 
			"'ValueType'", "'Fragment'", "'ObjectType'", "'Template'", "'ArrayType'", 
			"'Member'", "'Parent'", "'BondType'", "'Read'", "'File'", "'def'", "'add'", 
			"'MAX'", "'MIN'", null, null, null, null, null, null, null, null, null, 
			"'null'", "'^'", "'$'", "'~'", "'@'", "'#'", "'|'", "'\"'", "'('", "')'", 
			"'['", "']'", "'<'", "'>'", "'{'", "'}'", "','", "'.'", "':'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ATTRIBUTES", "FIELD_TYPES", "VALUE_TYPES", "FRAGMENTS", "OBJECT_TYPES", 
			"TEMPLATES", "ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", "DATA_CATEGORY", 
			"DATA_PURPOSE", "DATA_LEVEL", "FIELD_TYPE", "VALUE_TYPE", "FRAGMENT", 
			"OBJECT_TYPE", "TEMPLATE", "ARRAY_TYPE", "MEMBER", "PARENT", "BOND_TYPE", 
			"CMD_ACTION_READ", "CMD_OBJECT_FILE", "DEFINE", "ADD", "MAX", "MIN", 
			"TYPE_NAME_DEF", "ENUM_ID", "TYPE_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
			"FLOAT_LITERAL", "CHAR_LITERAL", "TEXT_PATTERN", "STRING_LITERAL", "NULL_LITERAL", 
			"OPHAT", "OPDOLLAR", "OPTILDE", "OPAT", "OPHASH", "OPPIPE", "OPDBLQUOTE", 
			"LPAREN", "RPAREN", "LSBRACKET", "RSBRACKET", "LABRACKET", "RABRACKET", 
			"LBRACE", "RBRACE", "COMMA", "DOT", "COLON", "SEMI", "WS", "COMMENT", 
			"LINE_COMMENT"
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


	public SpecLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SpecLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2>\u028a\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\36\3\37\3\37\7\37\u0194\n\37\f\37\16\37\u0197\13"+
		"\37\3 \3 \7 \u019b\n \f \16 \u019e\13 \3!\3!\3\"\3\"\3\"\5\"\u01a5\n\""+
		"\3\"\6\"\u01a8\n\"\r\"\16\"\u01a9\3\"\5\"\u01ad\n\"\5\"\u01af\n\"\3\""+
		"\5\"\u01b2\n\"\3#\3#\3#\5#\u01b7\n#\3#\3#\5#\u01bb\n#\3#\5#\u01be\n#\3"+
		"#\5#\u01c1\n#\3#\3#\3#\5#\u01c6\n#\3#\5#\u01c9\n#\5#\u01cb\n#\3$\3$\3"+
		"$\5$\u01d0\n$\3$\3$\3%\3%\3%\6%\u01d7\n%\r%\16%\u01d8\3%\3%\3%\3&\3&\3"+
		"&\7&\u01e1\n&\f&\16&\u01e4\13&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3)\3)\3"+
		"*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63"+
		"\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\6;"+
		"\u0214\n;\r;\16;\u0215\3;\3;\3<\3<\3<\3<\7<\u021e\n<\f<\16<\u0221\13<"+
		"\3<\3<\3<\3<\3<\3=\3=\3=\3=\7=\u022c\n=\f=\16=\u022f\13=\3=\3=\3>\3>\5"+
		">\u0235\n>\3>\3>\3?\3?\3?\3?\5?\u023d\n?\3?\5?\u0240\n?\3?\3?\3?\6?\u0245"+
		"\n?\r?\16?\u0246\3?\3?\3?\3?\3?\5?\u024e\n?\3@\3@\3@\7@\u0253\n@\f@\16"+
		"@\u0256\13@\3@\5@\u0259\n@\3A\3A\3B\3B\7B\u025f\nB\fB\16B\u0262\13B\3"+
		"B\5B\u0265\nB\3C\3C\3D\5D\u026a\nD\3E\5E\u026d\nE\3F\5F\u0270\nF\3G\3"+
		"G\5G\u0274\nG\3H\3H\3I\3I\3J\3J\3K\3K\5K\u027e\nK\3L\3L\3L\3L\3L\3L\3"+
		"L\3L\3L\3L\3L\4\u01d8\u021f\2M\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61"+
		"\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61"+
		"a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{\2}\2\177\2\u0081\2\u0083\2\u0085"+
		"\2\u0087\2\u0089\2\u008b\2\u008d\2\u008f\2\u0091\2\u0093\2\u0095\2\u0097"+
		"\2\3\2\31\3\2\63;\4\2\63\63NN\6\2FFHHffhh\6\2\f\f\17\17))^^\6\2\f\f\17"+
		"\17$$^^\5\2\13\f\16\17\"\"\4\2\f\f\17\17\4\2GGgg\4\2--//\n\2$$))^^ddh"+
		"hppttvv\3\2\62\65\3\2\629\5\2\62;CHch\3\2\62;\4\2\62;aa\4\2aac|\4\2C\\"+
		"aa\5\2C\\aac|\3\2c|\3\2C\\\4\2C\\c|\3\2\63\64\3\2\62\63\2\u029c\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2"+
		"\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o"+
		"\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\3\u0099"+
		"\3\2\2\2\5\u00a4\3\2\2\2\7\u00af\3\2\2\2\t\u00ba\3\2\2\2\13\u00c4\3\2"+
		"\2\2\r\u00d0\3\2\2\2\17\u00da\3\2\2\2\21\u00e5\3\2\2\2\23\u00ed\3\2\2"+
		"\2\25\u00f5\3\2\2\2\27\u00fe\3\2\2\2\31\u010b\3\2\2\2\33\u0117\3\2\2\2"+
		"\35\u0121\3\2\2\2\37\u012b\3\2\2\2!\u0135\3\2\2\2#\u013e\3\2\2\2%\u0149"+
		"\3\2\2\2\'\u0152\3\2\2\2)\u015c\3\2\2\2+\u0163\3\2\2\2-\u016a\3\2\2\2"+
		"/\u0173\3\2\2\2\61\u0178\3\2\2\2\63\u017d\3\2\2\2\65\u0181\3\2\2\2\67"+
		"\u0185\3\2\2\29\u0189\3\2\2\2;\u018d\3\2\2\2=\u0191\3\2\2\2?\u0198\3\2"+
		"\2\2A\u019f\3\2\2\2C\u01ae\3\2\2\2E\u01ca\3\2\2\2G\u01cc\3\2\2\2I\u01d3"+
		"\3\2\2\2K\u01dd\3\2\2\2M\u01e7\3\2\2\2O\u01ec\3\2\2\2Q\u01ee\3\2\2\2S"+
		"\u01f0\3\2\2\2U\u01f2\3\2\2\2W\u01f4\3\2\2\2Y\u01f6\3\2\2\2[\u01f8\3\2"+
		"\2\2]\u01fa\3\2\2\2_\u01fc\3\2\2\2a\u01fe\3\2\2\2c\u0200\3\2\2\2e\u0202"+
		"\3\2\2\2g\u0204\3\2\2\2i\u0206\3\2\2\2k\u0208\3\2\2\2m\u020a\3\2\2\2o"+
		"\u020c\3\2\2\2q\u020e\3\2\2\2s\u0210\3\2\2\2u\u0213\3\2\2\2w\u0219\3\2"+
		"\2\2y\u0227\3\2\2\2{\u0232\3\2\2\2}\u024d\3\2\2\2\177\u024f\3\2\2\2\u0081"+
		"\u025a\3\2\2\2\u0083\u025c\3\2\2\2\u0085\u0266\3\2\2\2\u0087\u0269\3\2"+
		"\2\2\u0089\u026c\3\2\2\2\u008b\u026f\3\2\2\2\u008d\u0273\3\2\2\2\u008f"+
		"\u0275\3\2\2\2\u0091\u0277\3\2\2\2\u0093\u0279\3\2\2\2\u0095\u027d\3\2"+
		"\2\2\u0097\u027f\3\2\2\2\u0099\u009a\7C\2\2\u009a\u009b\7v\2\2\u009b\u009c"+
		"\7v\2\2\u009c\u009d\7t\2\2\u009d\u009e\7k\2\2\u009e\u009f\7d\2\2\u009f"+
		"\u00a0\7w\2\2\u00a0\u00a1\7v\2\2\u00a1\u00a2\7g\2\2\u00a2\u00a3\7u\2\2"+
		"\u00a3\4\3\2\2\2\u00a4\u00a5\7H\2\2\u00a5\u00a6\7k\2\2\u00a6\u00a7\7g"+
		"\2\2\u00a7\u00a8\7n\2\2\u00a8\u00a9\7f\2\2\u00a9\u00aa\7V\2\2\u00aa\u00ab"+
		"\7{\2\2\u00ab\u00ac\7r\2\2\u00ac\u00ad\7g\2\2\u00ad\u00ae\7u\2\2\u00ae"+
		"\6\3\2\2\2\u00af\u00b0\7X\2\2\u00b0\u00b1\7c\2\2\u00b1\u00b2\7n\2\2\u00b2"+
		"\u00b3\7w\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7V\2\2\u00b5\u00b6\7{\2\2"+
		"\u00b6\u00b7\7r\2\2\u00b7\u00b8\7g\2\2\u00b8\u00b9\7u\2\2\u00b9\b\3\2"+
		"\2\2\u00ba\u00bb\7H\2\2\u00bb\u00bc\7t\2\2\u00bc\u00bd\7c\2\2\u00bd\u00be"+
		"\7i\2\2\u00be\u00bf\7o\2\2\u00bf\u00c0\7g\2\2\u00c0\u00c1\7p\2\2\u00c1"+
		"\u00c2\7v\2\2\u00c2\u00c3\7u\2\2\u00c3\n\3\2\2\2\u00c4\u00c5\7Q\2\2\u00c5"+
		"\u00c6\7d\2\2\u00c6\u00c7\7l\2\2\u00c7\u00c8\7g\2\2\u00c8\u00c9\7e\2\2"+
		"\u00c9\u00ca\7v\2\2\u00ca\u00cb\7V\2\2\u00cb\u00cc\7{\2\2\u00cc\u00cd"+
		"\7r\2\2\u00cd\u00ce\7g\2\2\u00ce\u00cf\7u\2\2\u00cf\f\3\2\2\2\u00d0\u00d1"+
		"\7V\2\2\u00d1\u00d2\7g\2\2\u00d2\u00d3\7o\2\2\u00d3\u00d4\7r\2\2\u00d4"+
		"\u00d5\7n\2\2\u00d5\u00d6\7c\2\2\u00d6\u00d7\7v\2\2\u00d7\u00d8\7g\2\2"+
		"\u00d8\u00d9\7u\2\2\u00d9\16\3\2\2\2\u00da\u00db\7C\2\2\u00db\u00dc\7"+
		"t\2\2\u00dc\u00dd\7t\2\2\u00dd\u00de\7c\2\2\u00de\u00df\7{\2\2\u00df\u00e0"+
		"\7V\2\2\u00e0\u00e1\7{\2\2\u00e1\u00e2\7r\2\2\u00e2\u00e3\7g\2\2\u00e3"+
		"\u00e4\7u\2\2\u00e4\20\3\2\2\2\u00e5\u00e6\7O\2\2\u00e6\u00e7\7g\2\2\u00e7"+
		"\u00e8\7o\2\2\u00e8\u00e9\7d\2\2\u00e9\u00ea\7g\2\2\u00ea\u00eb\7t\2\2"+
		"\u00eb\u00ec\7u\2\2\u00ec\22\3\2\2\2\u00ed\u00ee\7R\2\2\u00ee\u00ef\7"+
		"c\2\2\u00ef\u00f0\7t\2\2\u00f0\u00f1\7g\2\2\u00f1\u00f2\7p\2\2\u00f2\u00f3"+
		"\7v\2\2\u00f3\u00f4\7u\2\2\u00f4\24\3\2\2\2\u00f5\u00f6\7F\2\2\u00f6\u00f7"+
		"\7c\2\2\u00f7\u00f8\7v\2\2\u00f8\u00f9\7c\2\2\u00f9\u00fa\7V\2\2\u00fa"+
		"\u00fb\7{\2\2\u00fb\u00fc\7r\2\2\u00fc\u00fd\7g\2\2\u00fd\26\3\2\2\2\u00fe"+
		"\u00ff\7F\2\2\u00ff\u0100\7c\2\2\u0100\u0101\7v\2\2\u0101\u0102\7c\2\2"+
		"\u0102\u0103\7E\2\2\u0103\u0104\7c\2\2\u0104\u0105\7v\2\2\u0105\u0106"+
		"\7g\2\2\u0106\u0107\7i\2\2\u0107\u0108\7q\2\2\u0108\u0109\7t\2\2\u0109"+
		"\u010a\7{\2\2\u010a\30\3\2\2\2\u010b\u010c\7F\2\2\u010c\u010d\7c\2\2\u010d"+
		"\u010e\7v\2\2\u010e\u010f\7c\2\2\u010f\u0110\7R\2\2\u0110\u0111\7w\2\2"+
		"\u0111\u0112\7t\2\2\u0112\u0113\7r\2\2\u0113\u0114\7q\2\2\u0114\u0115"+
		"\7u\2\2\u0115\u0116\7g\2\2\u0116\32\3\2\2\2\u0117\u0118\7F\2\2\u0118\u0119"+
		"\7c\2\2\u0119\u011a\7v\2\2\u011a\u011b\7c\2\2\u011b\u011c\7N\2\2\u011c"+
		"\u011d\7g\2\2\u011d\u011e\7x\2\2\u011e\u011f\7g\2\2\u011f\u0120\7n\2\2"+
		"\u0120\34\3\2\2\2\u0121\u0122\7H\2\2\u0122\u0123\7k\2\2\u0123\u0124\7"+
		"g\2\2\u0124\u0125\7n\2\2\u0125\u0126\7f\2\2\u0126\u0127\7V\2\2\u0127\u0128"+
		"\7{\2\2\u0128\u0129\7r\2\2\u0129\u012a\7g\2\2\u012a\36\3\2\2\2\u012b\u012c"+
		"\7X\2\2\u012c\u012d\7c\2\2\u012d\u012e\7n\2\2\u012e\u012f\7w\2\2\u012f"+
		"\u0130\7g\2\2\u0130\u0131\7V\2\2\u0131\u0132\7{\2\2\u0132\u0133\7r\2\2"+
		"\u0133\u0134\7g\2\2\u0134 \3\2\2\2\u0135\u0136\7H\2\2\u0136\u0137\7t\2"+
		"\2\u0137\u0138\7c\2\2\u0138\u0139\7i\2\2\u0139\u013a\7o\2\2\u013a\u013b"+
		"\7g\2\2\u013b\u013c\7p\2\2\u013c\u013d\7v\2\2\u013d\"\3\2\2\2\u013e\u013f"+
		"\7Q\2\2\u013f\u0140\7d\2\2\u0140\u0141\7l\2\2\u0141\u0142\7g\2\2\u0142"+
		"\u0143\7e\2\2\u0143\u0144\7v\2\2\u0144\u0145\7V\2\2\u0145\u0146\7{\2\2"+
		"\u0146\u0147\7r\2\2\u0147\u0148\7g\2\2\u0148$\3\2\2\2\u0149\u014a\7V\2"+
		"\2\u014a\u014b\7g\2\2\u014b\u014c\7o\2\2\u014c\u014d\7r\2\2\u014d\u014e"+
		"\7n\2\2\u014e\u014f\7c\2\2\u014f\u0150\7v\2\2\u0150\u0151\7g\2\2\u0151"+
		"&\3\2\2\2\u0152\u0153\7C\2\2\u0153\u0154\7t\2\2\u0154\u0155\7t\2\2\u0155"+
		"\u0156\7c\2\2\u0156\u0157\7{\2\2\u0157\u0158\7V\2\2\u0158\u0159\7{\2\2"+
		"\u0159\u015a\7r\2\2\u015a\u015b\7g\2\2\u015b(\3\2\2\2\u015c\u015d\7O\2"+
		"\2\u015d\u015e\7g\2\2\u015e\u015f\7o\2\2\u015f\u0160\7d\2\2\u0160\u0161"+
		"\7g\2\2\u0161\u0162\7t\2\2\u0162*\3\2\2\2\u0163\u0164\7R\2\2\u0164\u0165"+
		"\7c\2\2\u0165\u0166\7t\2\2\u0166\u0167\7g\2\2\u0167\u0168\7p\2\2\u0168"+
		"\u0169\7v\2\2\u0169,\3\2\2\2\u016a\u016b\7D\2\2\u016b\u016c\7q\2\2\u016c"+
		"\u016d\7p\2\2\u016d\u016e\7f\2\2\u016e\u016f\7V\2\2\u016f\u0170\7{\2\2"+
		"\u0170\u0171\7r\2\2\u0171\u0172\7g\2\2\u0172.\3\2\2\2\u0173\u0174\7T\2"+
		"\2\u0174\u0175\7g\2\2\u0175\u0176\7c\2\2\u0176\u0177\7f\2\2\u0177\60\3"+
		"\2\2\2\u0178\u0179\7H\2\2\u0179\u017a\7k\2\2\u017a\u017b\7n\2\2\u017b"+
		"\u017c\7g\2\2\u017c\62\3\2\2\2\u017d\u017e\7f\2\2\u017e\u017f\7g\2\2\u017f"+
		"\u0180\7h\2\2\u0180\64\3\2\2\2\u0181\u0182\7c\2\2\u0182\u0183\7f\2\2\u0183"+
		"\u0184\7f\2\2\u0184\66\3\2\2\2\u0185\u0186\7O\2\2\u0186\u0187\7C\2\2\u0187"+
		"\u0188\7Z\2\2\u01888\3\2\2\2\u0189\u018a\7O\2\2\u018a\u018b\7K\2\2\u018b"+
		"\u018c\7P\2\2\u018c:\3\2\2\2\u018d\u018e\7$\2\2\u018e\u018f\5? \2\u018f"+
		"\u0190\7$\2\2\u0190<\3\2\2\2\u0191\u0195\5\u0091I\2\u0192\u0194\5\u0089"+
		"E\2\u0193\u0192\3\2\2\2\u0194\u0197\3\2\2\2\u0195\u0193\3\2\2\2\u0195"+
		"\u0196\3\2\2\2\u0196>\3\2\2\2\u0197\u0195\3\2\2\2\u0198\u019c\5\u0093"+
		"J\2\u0199\u019b\5\u008dG\2\u019a\u0199\3\2\2\2\u019b\u019e\3\2\2\2\u019c"+
		"\u019a\3\2\2\2\u019c\u019d\3\2\2\2\u019d@\3\2\2\2\u019e\u019c\3\2\2\2"+
		"\u019f\u01a0\5\u0083B\2\u01a0B\3\2\2\2\u01a1\u01af\7\62\2\2\u01a2\u01ac"+
		"\t\2\2\2\u01a3\u01a5\5\u0083B\2\u01a4\u01a3\3\2\2\2\u01a4\u01a5\3\2\2"+
		"\2\u01a5\u01ad\3\2\2\2\u01a6\u01a8\7a\2\2\u01a7\u01a6\3\2\2\2\u01a8\u01a9"+
		"\3\2\2\2\u01a9\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab"+
		"\u01ad\5\u0083B\2\u01ac\u01a4\3\2\2\2\u01ac\u01a7\3\2\2\2\u01ad\u01af"+
		"\3\2\2\2\u01ae\u01a1\3\2\2\2\u01ae\u01a2\3\2\2\2\u01af\u01b1\3\2\2\2\u01b0"+
		"\u01b2\t\3\2\2\u01b1\u01b0\3\2\2\2\u01b1\u01b2\3\2\2\2\u01b2D\3\2\2\2"+
		"\u01b3\u01b4\5\u0083B\2\u01b4\u01b6\7\60\2\2\u01b5\u01b7\5\u0083B\2\u01b6"+
		"\u01b5\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7\u01bb\3\2\2\2\u01b8\u01b9\7\60"+
		"\2\2\u01b9\u01bb\5\u0083B\2\u01ba\u01b3\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb"+
		"\u01bd\3\2\2\2\u01bc\u01be\5{>\2\u01bd\u01bc\3\2\2\2\u01bd\u01be\3\2\2"+
		"\2\u01be\u01c0\3\2\2\2\u01bf\u01c1\t\4\2\2\u01c0\u01bf\3\2\2\2\u01c0\u01c1"+
		"\3\2\2\2\u01c1\u01cb\3\2\2\2\u01c2\u01c8\5\u0083B\2\u01c3\u01c5\5{>\2"+
		"\u01c4\u01c6\t\4\2\2\u01c5\u01c4\3\2\2\2\u01c5\u01c6\3\2\2\2\u01c6\u01c9"+
		"\3\2\2\2\u01c7\u01c9\t\4\2\2\u01c8\u01c3\3\2\2\2\u01c8\u01c7\3\2\2\2\u01c9"+
		"\u01cb\3\2\2\2\u01ca\u01ba\3\2\2\2\u01ca\u01c2\3\2\2\2\u01cbF\3\2\2\2"+
		"\u01cc\u01cf\7)\2\2\u01cd\u01d0\n\5\2\2\u01ce\u01d0\5}?\2\u01cf\u01cd"+
		"\3\2\2\2\u01cf\u01ce\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1\u01d2\7)\2\2\u01d2"+
		"H\3\2\2\2\u01d3\u01d4\7$\2\2\u01d4\u01d6\7`\2\2\u01d5\u01d7\13\2\2\2\u01d6"+
		"\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d8\u01d6\3\2"+
		"\2\2\u01d9\u01da\3\2\2\2\u01da\u01db\7&\2\2\u01db\u01dc\7$\2\2\u01dcJ"+
		"\3\2\2\2\u01dd\u01e2\7$\2\2\u01de\u01e1\n\6\2\2\u01df\u01e1\5}?\2\u01e0"+
		"\u01de\3\2\2\2\u01e0\u01df\3\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3\2"+
		"\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5"+
		"\u01e6\7$\2\2\u01e6L\3\2\2\2\u01e7\u01e8\7p\2\2\u01e8\u01e9\7w\2\2\u01e9"+
		"\u01ea\7n\2\2\u01ea\u01eb\7n\2\2\u01ebN\3\2\2\2\u01ec\u01ed\7`\2\2\u01ed"+
		"P\3\2\2\2\u01ee\u01ef\7&\2\2\u01efR\3\2\2\2\u01f0\u01f1\7\u0080\2\2\u01f1"+
		"T\3\2\2\2\u01f2\u01f3\7B\2\2\u01f3V\3\2\2\2\u01f4\u01f5\7%\2\2\u01f5X"+
		"\3\2\2\2\u01f6\u01f7\7~\2\2\u01f7Z\3\2\2\2\u01f8\u01f9\7$\2\2\u01f9\\"+
		"\3\2\2\2\u01fa\u01fb\7*\2\2\u01fb^\3\2\2\2\u01fc\u01fd\7+\2\2\u01fd`\3"+
		"\2\2\2\u01fe\u01ff\7]\2\2\u01ffb\3\2\2\2\u0200\u0201\7_\2\2\u0201d\3\2"+
		"\2\2\u0202\u0203\7>\2\2\u0203f\3\2\2\2\u0204\u0205\7@\2\2\u0205h\3\2\2"+
		"\2\u0206\u0207\7}\2\2\u0207j\3\2\2\2\u0208\u0209\7\177\2\2\u0209l\3\2"+
		"\2\2\u020a\u020b\7.\2\2\u020bn\3\2\2\2\u020c\u020d\7\60\2\2\u020dp\3\2"+
		"\2\2\u020e\u020f\7<\2\2\u020fr\3\2\2\2\u0210\u0211\7=\2\2\u0211t\3\2\2"+
		"\2\u0212\u0214\t\7\2\2\u0213\u0212\3\2\2\2\u0214\u0215\3\2\2\2\u0215\u0213"+
		"\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0218\b;\2\2\u0218"+
		"v\3\2\2\2\u0219\u021a\7\61\2\2\u021a\u021b\7,\2\2\u021b\u021f\3\2\2\2"+
		"\u021c\u021e\13\2\2\2\u021d\u021c\3\2\2\2\u021e\u0221\3\2\2\2\u021f\u0220"+
		"\3\2\2\2\u021f\u021d\3\2\2\2\u0220\u0222\3\2\2\2\u0221\u021f\3\2\2\2\u0222"+
		"\u0223\7,\2\2\u0223\u0224\7\61\2\2\u0224\u0225\3\2\2\2\u0225\u0226\b<"+
		"\2\2\u0226x\3\2\2\2\u0227\u0228\7\61\2\2\u0228\u0229\7\61\2\2\u0229\u022d"+
		"\3\2\2\2\u022a\u022c\n\b\2\2\u022b\u022a\3\2\2\2\u022c\u022f\3\2\2\2\u022d"+
		"\u022b\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u0230\3\2\2\2\u022f\u022d\3\2"+
		"\2\2\u0230\u0231\b=\2\2\u0231z\3\2\2\2\u0232\u0234\t\t\2\2\u0233\u0235"+
		"\t\n\2\2\u0234\u0233\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0236\3\2\2\2\u0236"+
		"\u0237\5\u0083B\2\u0237|\3\2\2\2\u0238\u0239\7^\2\2\u0239\u024e\t\13\2"+
		"\2\u023a\u023f\7^\2\2\u023b\u023d\t\f\2\2\u023c\u023b\3\2\2\2\u023c\u023d"+
		"\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u0240\t\r\2\2\u023f\u023c\3\2\2\2\u023f"+
		"\u0240\3\2\2\2\u0240\u0241\3\2\2\2\u0241\u024e\t\r\2\2\u0242\u0244\7^"+
		"\2\2\u0243\u0245\7w\2\2\u0244\u0243\3\2\2\2\u0245\u0246\3\2\2\2\u0246"+
		"\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u0248\3\2\2\2\u0248\u0249\5\u0081"+
		"A\2\u0249\u024a\5\u0081A\2\u024a\u024b\5\u0081A\2\u024b\u024c\5\u0081"+
		"A\2\u024c\u024e\3\2\2\2\u024d\u0238\3\2\2\2\u024d\u023a\3\2\2\2\u024d"+
		"\u0242\3\2\2\2\u024e~\3\2\2\2\u024f\u0258\5\u0081A\2\u0250\u0253\5\u0081"+
		"A\2\u0251\u0253\7a\2\2\u0252\u0250\3\2\2\2\u0252\u0251\3\2\2\2\u0253\u0256"+
		"\3\2\2\2\u0254\u0252\3\2\2\2\u0254\u0255\3\2\2\2\u0255\u0257\3\2\2\2\u0256"+
		"\u0254\3\2\2\2\u0257\u0259\5\u0081A\2\u0258\u0254\3\2\2\2\u0258\u0259"+
		"\3\2\2\2\u0259\u0080\3\2\2\2\u025a\u025b\t\16\2\2\u025b\u0082\3\2\2\2"+
		"\u025c\u0264\t\17\2\2\u025d\u025f\t\20\2\2\u025e\u025d\3\2\2\2\u025f\u0262"+
		"\3\2\2\2\u0260\u025e\3\2\2\2\u0260\u0261\3\2\2\2\u0261\u0263\3\2\2\2\u0262"+
		"\u0260\3\2\2\2\u0263\u0265\t\17\2\2\u0264\u0260\3\2\2\2\u0264\u0265\3"+
		"\2\2\2\u0265\u0084\3\2\2\2\u0266\u0267\t\17\2\2\u0267\u0086\3\2\2\2\u0268"+
		"\u026a\t\21\2\2\u0269\u0268\3\2\2\2\u026a\u0088\3\2\2\2\u026b\u026d\t"+
		"\22\2\2\u026c\u026b\3\2\2\2\u026d\u008a\3\2\2\2\u026e\u0270\t\23\2\2\u026f"+
		"\u026e\3\2\2\2\u0270\u008c\3\2\2\2\u0271\u0274\5\u0093J\2\u0272\u0274"+
		"\t\20\2\2\u0273\u0271\3\2\2\2\u0273\u0272\3\2\2\2\u0274\u008e\3\2\2\2"+
		"\u0275\u0276\t\24\2\2\u0276\u0090\3\2\2\2\u0277\u0278\t\25\2\2\u0278\u0092"+
		"\3\2\2\2\u0279\u027a\t\26\2\2\u027a\u0094\3\2\2\2\u027b\u027e\5\u0093"+
		"J\2\u027c\u027e\t\17\2\2\u027d\u027b\3\2\2\2\u027d\u027c\3\2\2\2\u027e"+
		"\u0096\3\2\2\2\u027f\u0280\t\27\2\2\u0280\u0281\t\17\2\2\u0281\u0282\t"+
		"\17\2\2\u0282\u0283\t\17\2\2\u0283\u0284\7/\2\2\u0284\u0285\t\30\2\2\u0285"+
		"\u0286\t\17\2\2\u0286\u0287\7/\2\2\u0287\u0288\t\f\2\2\u0288\u0289\t\17"+
		"\2\2\u0289\u0098\3\2\2\2\'\2\u0195\u019c\u01a4\u01a9\u01ac\u01ae\u01b1"+
		"\u01b6\u01ba\u01bd\u01c0\u01c5\u01c8\u01ca\u01cf\u01d8\u01e0\u01e2\u0215"+
		"\u021f\u022d\u0234\u023c\u023f\u0246\u024d\u0252\u0254\u0258\u0260\u0264"+
		"\u0269\u026c\u026f\u0273\u027d\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}