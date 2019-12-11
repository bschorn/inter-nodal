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
		FIELD_TYPES=1, VALUE_TYPES=2, FRAGMENTS=3, OBJECT_TYPES=4, TEMPLATES=5, 
		ARRAY_TYPES=6, MEMBERS=7, PARENTS=8, DATA_TYPE=9, FIELD_TYPE=10, VALUE_TYPE=11, 
		FRAGMENT=12, OBJECT_TYPE=13, TEMPLATE=14, ARRAY_TYPE=15, MEMBER=16, PARENT=17, 
		BOND_TYPE=18, CMD_ACTION_READ=19, CMD_OBJECT_FILE=20, DEFINE=21, ADD=22, 
		MAX=23, MIN=24, TYPE_NAME_DEF=25, ENUM_ID=26, TYPE_NAME_REF=27, TEXT_LENGTH=28, 
		DECIMAL_LITERAL=29, FLOAT_LITERAL=30, CHAR_LITERAL=31, TEXT_PATTERN=32, 
		STRING_LITERAL=33, NULL_LITERAL=34, OPHAT=35, OPDOLLAR=36, OPTILDE=37, 
		OPAT=38, OPHASH=39, OPPIPE=40, OPDBLQUOTE=41, LPAREN=42, RPAREN=43, LSBRACKET=44, 
		RSBRACKET=45, LABRACKET=46, RABRACKET=47, LBRACE=48, RBRACE=49, COMMA=50, 
		DOT=51, COLON=52, SEMI=53, WS=54, COMMENT=55, LINE_COMMENT=56;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"FIELD_TYPES", "VALUE_TYPES", "FRAGMENTS", "OBJECT_TYPES", "TEMPLATES", 
			"ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", "FIELD_TYPE", "VALUE_TYPE", 
			"FRAGMENT", "OBJECT_TYPE", "TEMPLATE", "ARRAY_TYPE", "MEMBER", "PARENT", 
			"BOND_TYPE", "CMD_ACTION_READ", "CMD_OBJECT_FILE", "DEFINE", "ADD", "MAX", 
			"MIN", "TYPE_NAME_DEF", "ENUM_ID", "TYPE_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
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
			null, "'FieldTypes'", "'ValueTypes'", "'Fragments'", "'ObjectTypes'", 
			"'Templates'", "'ArrayTypes'", "'Members'", "'Parents'", "'DataType'", 
			"'FieldType'", "'ValueType'", "'Fragment'", "'ObjectType'", "'Template'", 
			"'ArrayType'", "'Member'", "'Parent'", "'BondType'", "'Read'", "'File'", 
			"'def'", "'add'", "'MAX'", "'MIN'", null, null, null, null, null, null, 
			null, null, null, "'null'", "'^'", "'$'", "'~'", "'@'", "'#'", "'|'", 
			"'\"'", "'('", "')'", "'['", "']'", "'<'", "'>'", "'{'", "'}'", "','", 
			"'.'", "':'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "FIELD_TYPES", "VALUE_TYPES", "FRAGMENTS", "OBJECT_TYPES", "TEMPLATES", 
			"ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", "FIELD_TYPE", "VALUE_TYPE", 
			"FRAGMENT", "OBJECT_TYPE", "TEMPLATE", "ARRAY_TYPE", "MEMBER", "PARENT", 
			"BOND_TYPE", "CMD_ACTION_READ", "CMD_OBJECT_FILE", "DEFINE", "ADD", "MAX", 
			"MIN", "TYPE_NAME_DEF", "ENUM_ID", "TYPE_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2:\u0254\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3"+
		"\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3"+
		"\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\7"+
		"\33\u015e\n\33\f\33\16\33\u0161\13\33\3\34\3\34\7\34\u0165\n\34\f\34\16"+
		"\34\u0168\13\34\3\35\3\35\3\36\3\36\3\36\5\36\u016f\n\36\3\36\6\36\u0172"+
		"\n\36\r\36\16\36\u0173\3\36\5\36\u0177\n\36\5\36\u0179\n\36\3\36\5\36"+
		"\u017c\n\36\3\37\3\37\3\37\5\37\u0181\n\37\3\37\3\37\5\37\u0185\n\37\3"+
		"\37\5\37\u0188\n\37\3\37\5\37\u018b\n\37\3\37\3\37\3\37\5\37\u0190\n\37"+
		"\3\37\5\37\u0193\n\37\5\37\u0195\n\37\3 \3 \3 \5 \u019a\n \3 \3 \3!\3"+
		"!\3!\6!\u01a1\n!\r!\16!\u01a2\3!\3!\3!\3\"\3\"\3\"\7\"\u01ab\n\"\f\"\16"+
		"\"\u01ae\13\"\3\"\3\"\3#\3#\3#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3"+
		")\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62"+
		"\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\6\67\u01de\n\67\r\67\16"+
		"\67\u01df\3\67\3\67\38\38\38\38\78\u01e8\n8\f8\168\u01eb\138\38\38\38"+
		"\38\38\39\39\39\39\79\u01f6\n9\f9\169\u01f9\139\39\39\3:\3:\5:\u01ff\n"+
		":\3:\3:\3;\3;\3;\3;\5;\u0207\n;\3;\5;\u020a\n;\3;\3;\3;\6;\u020f\n;\r"+
		";\16;\u0210\3;\3;\3;\3;\3;\5;\u0218\n;\3<\3<\3<\7<\u021d\n<\f<\16<\u0220"+
		"\13<\3<\5<\u0223\n<\3=\3=\3>\3>\7>\u0229\n>\f>\16>\u022c\13>\3>\5>\u022f"+
		"\n>\3?\3?\3@\5@\u0234\n@\3A\5A\u0237\nA\3B\5B\u023a\nB\3C\3C\5C\u023e"+
		"\nC\3D\3D\3E\3E\3F\3F\3G\3G\5G\u0248\nG\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H"+
		"\3H\4\u01a2\u01e9\2I\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63"+
		"e\64g\65i\66k\67m8o9q:s\2u\2w\2y\2{\2}\2\177\2\u0081\2\u0083\2\u0085\2"+
		"\u0087\2\u0089\2\u008b\2\u008d\2\u008f\2\3\2\31\3\2\63;\4\2\63\63NN\6"+
		"\2FFHHffhh\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\5\2\13\f\16\17\"\"\4\2"+
		"\f\f\17\17\4\2GGgg\4\2--//\n\2$$))^^ddhhppttvv\3\2\62\65\3\2\629\5\2\62"+
		";CHch\3\2\62;\4\2\62;aa\4\2aac|\4\2C\\aa\5\2C\\aac|\3\2c|\3\2C\\\4\2C"+
		"\\c|\3\2\63\64\3\2\62\63\2\u0266\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2"+
		"\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2"+
		"\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O"+
		"\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2"+
		"\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
		"\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\3\u0091\3\2\2"+
		"\2\5\u009c\3\2\2\2\7\u00a7\3\2\2\2\t\u00b1\3\2\2\2\13\u00bd\3\2\2\2\r"+
		"\u00c7\3\2\2\2\17\u00d2\3\2\2\2\21\u00da\3\2\2\2\23\u00e2\3\2\2\2\25\u00eb"+
		"\3\2\2\2\27\u00f5\3\2\2\2\31\u00ff\3\2\2\2\33\u0108\3\2\2\2\35\u0113\3"+
		"\2\2\2\37\u011c\3\2\2\2!\u0126\3\2\2\2#\u012d\3\2\2\2%\u0134\3\2\2\2\'"+
		"\u013d\3\2\2\2)\u0142\3\2\2\2+\u0147\3\2\2\2-\u014b\3\2\2\2/\u014f\3\2"+
		"\2\2\61\u0153\3\2\2\2\63\u0157\3\2\2\2\65\u015b\3\2\2\2\67\u0162\3\2\2"+
		"\29\u0169\3\2\2\2;\u0178\3\2\2\2=\u0194\3\2\2\2?\u0196\3\2\2\2A\u019d"+
		"\3\2\2\2C\u01a7\3\2\2\2E\u01b1\3\2\2\2G\u01b6\3\2\2\2I\u01b8\3\2\2\2K"+
		"\u01ba\3\2\2\2M\u01bc\3\2\2\2O\u01be\3\2\2\2Q\u01c0\3\2\2\2S\u01c2\3\2"+
		"\2\2U\u01c4\3\2\2\2W\u01c6\3\2\2\2Y\u01c8\3\2\2\2[\u01ca\3\2\2\2]\u01cc"+
		"\3\2\2\2_\u01ce\3\2\2\2a\u01d0\3\2\2\2c\u01d2\3\2\2\2e\u01d4\3\2\2\2g"+
		"\u01d6\3\2\2\2i\u01d8\3\2\2\2k\u01da\3\2\2\2m\u01dd\3\2\2\2o\u01e3\3\2"+
		"\2\2q\u01f1\3\2\2\2s\u01fc\3\2\2\2u\u0217\3\2\2\2w\u0219\3\2\2\2y\u0224"+
		"\3\2\2\2{\u0226\3\2\2\2}\u0230\3\2\2\2\177\u0233\3\2\2\2\u0081\u0236\3"+
		"\2\2\2\u0083\u0239\3\2\2\2\u0085\u023d\3\2\2\2\u0087\u023f\3\2\2\2\u0089"+
		"\u0241\3\2\2\2\u008b\u0243\3\2\2\2\u008d\u0247\3\2\2\2\u008f\u0249\3\2"+
		"\2\2\u0091\u0092\7H\2\2\u0092\u0093\7k\2\2\u0093\u0094\7g\2\2\u0094\u0095"+
		"\7n\2\2\u0095\u0096\7f\2\2\u0096\u0097\7V\2\2\u0097\u0098\7{\2\2\u0098"+
		"\u0099\7r\2\2\u0099\u009a\7g\2\2\u009a\u009b\7u\2\2\u009b\4\3\2\2\2\u009c"+
		"\u009d\7X\2\2\u009d\u009e\7c\2\2\u009e\u009f\7n\2\2\u009f\u00a0\7w\2\2"+
		"\u00a0\u00a1\7g\2\2\u00a1\u00a2\7V\2\2\u00a2\u00a3\7{\2\2\u00a3\u00a4"+
		"\7r\2\2\u00a4\u00a5\7g\2\2\u00a5\u00a6\7u\2\2\u00a6\6\3\2\2\2\u00a7\u00a8"+
		"\7H\2\2\u00a8\u00a9\7t\2\2\u00a9\u00aa\7c\2\2\u00aa\u00ab\7i\2\2\u00ab"+
		"\u00ac\7o\2\2\u00ac\u00ad\7g\2\2\u00ad\u00ae\7p\2\2\u00ae\u00af\7v\2\2"+
		"\u00af\u00b0\7u\2\2\u00b0\b\3\2\2\2\u00b1\u00b2\7Q\2\2\u00b2\u00b3\7d"+
		"\2\2\u00b3\u00b4\7l\2\2\u00b4\u00b5\7g\2\2\u00b5\u00b6\7e\2\2\u00b6\u00b7"+
		"\7v\2\2\u00b7\u00b8\7V\2\2\u00b8\u00b9\7{\2\2\u00b9\u00ba\7r\2\2\u00ba"+
		"\u00bb\7g\2\2\u00bb\u00bc\7u\2\2\u00bc\n\3\2\2\2\u00bd\u00be\7V\2\2\u00be"+
		"\u00bf\7g\2\2\u00bf\u00c0\7o\2\2\u00c0\u00c1\7r\2\2\u00c1\u00c2\7n\2\2"+
		"\u00c2\u00c3\7c\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c5\7g\2\2\u00c5\u00c6"+
		"\7u\2\2\u00c6\f\3\2\2\2\u00c7\u00c8\7C\2\2\u00c8\u00c9\7t\2\2\u00c9\u00ca"+
		"\7t\2\2\u00ca\u00cb\7c\2\2\u00cb\u00cc\7{\2\2\u00cc\u00cd\7V\2\2\u00cd"+
		"\u00ce\7{\2\2\u00ce\u00cf\7r\2\2\u00cf\u00d0\7g\2\2\u00d0\u00d1\7u\2\2"+
		"\u00d1\16\3\2\2\2\u00d2\u00d3\7O\2\2\u00d3\u00d4\7g\2\2\u00d4\u00d5\7"+
		"o\2\2\u00d5\u00d6\7d\2\2\u00d6\u00d7\7g\2\2\u00d7\u00d8\7t\2\2\u00d8\u00d9"+
		"\7u\2\2\u00d9\20\3\2\2\2\u00da\u00db\7R\2\2\u00db\u00dc\7c\2\2\u00dc\u00dd"+
		"\7t\2\2\u00dd\u00de\7g\2\2\u00de\u00df\7p\2\2\u00df\u00e0\7v\2\2\u00e0"+
		"\u00e1\7u\2\2\u00e1\22\3\2\2\2\u00e2\u00e3\7F\2\2\u00e3\u00e4\7c\2\2\u00e4"+
		"\u00e5\7v\2\2\u00e5\u00e6\7c\2\2\u00e6\u00e7\7V\2\2\u00e7\u00e8\7{\2\2"+
		"\u00e8\u00e9\7r\2\2\u00e9\u00ea\7g\2\2\u00ea\24\3\2\2\2\u00eb\u00ec\7"+
		"H\2\2\u00ec\u00ed\7k\2\2\u00ed\u00ee\7g\2\2\u00ee\u00ef\7n\2\2\u00ef\u00f0"+
		"\7f\2\2\u00f0\u00f1\7V\2\2\u00f1\u00f2\7{\2\2\u00f2\u00f3\7r\2\2\u00f3"+
		"\u00f4\7g\2\2\u00f4\26\3\2\2\2\u00f5\u00f6\7X\2\2\u00f6\u00f7\7c\2\2\u00f7"+
		"\u00f8\7n\2\2\u00f8\u00f9\7w\2\2\u00f9\u00fa\7g\2\2\u00fa\u00fb\7V\2\2"+
		"\u00fb\u00fc\7{\2\2\u00fc\u00fd\7r\2\2\u00fd\u00fe\7g\2\2\u00fe\30\3\2"+
		"\2\2\u00ff\u0100\7H\2\2\u0100\u0101\7t\2\2\u0101\u0102\7c\2\2\u0102\u0103"+
		"\7i\2\2\u0103\u0104\7o\2\2\u0104\u0105\7g\2\2\u0105\u0106\7p\2\2\u0106"+
		"\u0107\7v\2\2\u0107\32\3\2\2\2\u0108\u0109\7Q\2\2\u0109\u010a\7d\2\2\u010a"+
		"\u010b\7l\2\2\u010b\u010c\7g\2\2\u010c\u010d\7e\2\2\u010d\u010e\7v\2\2"+
		"\u010e\u010f\7V\2\2\u010f\u0110\7{\2\2\u0110\u0111\7r\2\2\u0111\u0112"+
		"\7g\2\2\u0112\34\3\2\2\2\u0113\u0114\7V\2\2\u0114\u0115\7g\2\2\u0115\u0116"+
		"\7o\2\2\u0116\u0117\7r\2\2\u0117\u0118\7n\2\2\u0118\u0119\7c\2\2\u0119"+
		"\u011a\7v\2\2\u011a\u011b\7g\2\2\u011b\36\3\2\2\2\u011c\u011d\7C\2\2\u011d"+
		"\u011e\7t\2\2\u011e\u011f\7t\2\2\u011f\u0120\7c\2\2\u0120\u0121\7{\2\2"+
		"\u0121\u0122\7V\2\2\u0122\u0123\7{\2\2\u0123\u0124\7r\2\2\u0124\u0125"+
		"\7g\2\2\u0125 \3\2\2\2\u0126\u0127\7O\2\2\u0127\u0128\7g\2\2\u0128\u0129"+
		"\7o\2\2\u0129\u012a\7d\2\2\u012a\u012b\7g\2\2\u012b\u012c\7t\2\2\u012c"+
		"\"\3\2\2\2\u012d\u012e\7R\2\2\u012e\u012f\7c\2\2\u012f\u0130\7t\2\2\u0130"+
		"\u0131\7g\2\2\u0131\u0132\7p\2\2\u0132\u0133\7v\2\2\u0133$\3\2\2\2\u0134"+
		"\u0135\7D\2\2\u0135\u0136\7q\2\2\u0136\u0137\7p\2\2\u0137\u0138\7f\2\2"+
		"\u0138\u0139\7V\2\2\u0139\u013a\7{\2\2\u013a\u013b\7r\2\2\u013b\u013c"+
		"\7g\2\2\u013c&\3\2\2\2\u013d\u013e\7T\2\2\u013e\u013f\7g\2\2\u013f\u0140"+
		"\7c\2\2\u0140\u0141\7f\2\2\u0141(\3\2\2\2\u0142\u0143\7H\2\2\u0143\u0144"+
		"\7k\2\2\u0144\u0145\7n\2\2\u0145\u0146\7g\2\2\u0146*\3\2\2\2\u0147\u0148"+
		"\7f\2\2\u0148\u0149\7g\2\2\u0149\u014a\7h\2\2\u014a,\3\2\2\2\u014b\u014c"+
		"\7c\2\2\u014c\u014d\7f\2\2\u014d\u014e\7f\2\2\u014e.\3\2\2\2\u014f\u0150"+
		"\7O\2\2\u0150\u0151\7C\2\2\u0151\u0152\7Z\2\2\u0152\60\3\2\2\2\u0153\u0154"+
		"\7O\2\2\u0154\u0155\7K\2\2\u0155\u0156\7P\2\2\u0156\62\3\2\2\2\u0157\u0158"+
		"\7$\2\2\u0158\u0159\5\67\34\2\u0159\u015a\7$\2\2\u015a\64\3\2\2\2\u015b"+
		"\u015f\5\u0089E\2\u015c\u015e\5\u0081A\2\u015d\u015c\3\2\2\2\u015e\u0161"+
		"\3\2\2\2\u015f\u015d\3\2\2\2\u015f\u0160\3\2\2\2\u0160\66\3\2\2\2\u0161"+
		"\u015f\3\2\2\2\u0162\u0166\5\u008bF\2\u0163\u0165\5\u0085C\2\u0164\u0163"+
		"\3\2\2\2\u0165\u0168\3\2\2\2\u0166\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167"+
		"8\3\2\2\2\u0168\u0166\3\2\2\2\u0169\u016a\5{>\2\u016a:\3\2\2\2\u016b\u0179"+
		"\7\62\2\2\u016c\u0176\t\2\2\2\u016d\u016f\5{>\2\u016e\u016d\3\2\2\2\u016e"+
		"\u016f\3\2\2\2\u016f\u0177\3\2\2\2\u0170\u0172\7a\2\2\u0171\u0170\3\2"+
		"\2\2\u0172\u0173\3\2\2\2\u0173\u0171\3\2\2\2\u0173\u0174\3\2\2\2\u0174"+
		"\u0175\3\2\2\2\u0175\u0177\5{>\2\u0176\u016e\3\2\2\2\u0176\u0171\3\2\2"+
		"\2\u0177\u0179\3\2\2\2\u0178\u016b\3\2\2\2\u0178\u016c\3\2\2\2\u0179\u017b"+
		"\3\2\2\2\u017a\u017c\t\3\2\2\u017b\u017a\3\2\2\2\u017b\u017c\3\2\2\2\u017c"+
		"<\3\2\2\2\u017d\u017e\5{>\2\u017e\u0180\7\60\2\2\u017f\u0181\5{>\2\u0180"+
		"\u017f\3\2\2\2\u0180\u0181\3\2\2\2\u0181\u0185\3\2\2\2\u0182\u0183\7\60"+
		"\2\2\u0183\u0185\5{>\2\u0184\u017d\3\2\2\2\u0184\u0182\3\2\2\2\u0185\u0187"+
		"\3\2\2\2\u0186\u0188\5s:\2\u0187\u0186\3\2\2\2\u0187\u0188\3\2\2\2\u0188"+
		"\u018a\3\2\2\2\u0189\u018b\t\4\2\2\u018a\u0189\3\2\2\2\u018a\u018b\3\2"+
		"\2\2\u018b\u0195\3\2\2\2\u018c\u0192\5{>\2\u018d\u018f\5s:\2\u018e\u0190"+
		"\t\4\2\2\u018f\u018e\3\2\2\2\u018f\u0190\3\2\2\2\u0190\u0193\3\2\2\2\u0191"+
		"\u0193\t\4\2\2\u0192\u018d\3\2\2\2\u0192\u0191\3\2\2\2\u0193\u0195\3\2"+
		"\2\2\u0194\u0184\3\2\2\2\u0194\u018c\3\2\2\2\u0195>\3\2\2\2\u0196\u0199"+
		"\7)\2\2\u0197\u019a\n\5\2\2\u0198\u019a\5u;\2\u0199\u0197\3\2\2\2\u0199"+
		"\u0198\3\2\2\2\u019a\u019b\3\2\2\2\u019b\u019c\7)\2\2\u019c@\3\2\2\2\u019d"+
		"\u019e\7$\2\2\u019e\u01a0\7`\2\2\u019f\u01a1\13\2\2\2\u01a0\u019f\3\2"+
		"\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a3"+
		"\u01a4\3\2\2\2\u01a4\u01a5\7&\2\2\u01a5\u01a6\7$\2\2\u01a6B\3\2\2\2\u01a7"+
		"\u01ac\7$\2\2\u01a8\u01ab\n\6\2\2\u01a9\u01ab\5u;\2\u01aa\u01a8\3\2\2"+
		"\2\u01aa\u01a9\3\2\2\2\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac\u01ad"+
		"\3\2\2\2\u01ad\u01af\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b0\7$\2\2\u01b0"+
		"D\3\2\2\2\u01b1\u01b2\7p\2\2\u01b2\u01b3\7w\2\2\u01b3\u01b4\7n\2\2\u01b4"+
		"\u01b5\7n\2\2\u01b5F\3\2\2\2\u01b6\u01b7\7`\2\2\u01b7H\3\2\2\2\u01b8\u01b9"+
		"\7&\2\2\u01b9J\3\2\2\2\u01ba\u01bb\7\u0080\2\2\u01bbL\3\2\2\2\u01bc\u01bd"+
		"\7B\2\2\u01bdN\3\2\2\2\u01be\u01bf\7%\2\2\u01bfP\3\2\2\2\u01c0\u01c1\7"+
		"~\2\2\u01c1R\3\2\2\2\u01c2\u01c3\7$\2\2\u01c3T\3\2\2\2\u01c4\u01c5\7*"+
		"\2\2\u01c5V\3\2\2\2\u01c6\u01c7\7+\2\2\u01c7X\3\2\2\2\u01c8\u01c9\7]\2"+
		"\2\u01c9Z\3\2\2\2\u01ca\u01cb\7_\2\2\u01cb\\\3\2\2\2\u01cc\u01cd\7>\2"+
		"\2\u01cd^\3\2\2\2\u01ce\u01cf\7@\2\2\u01cf`\3\2\2\2\u01d0\u01d1\7}\2\2"+
		"\u01d1b\3\2\2\2\u01d2\u01d3\7\177\2\2\u01d3d\3\2\2\2\u01d4\u01d5\7.\2"+
		"\2\u01d5f\3\2\2\2\u01d6\u01d7\7\60\2\2\u01d7h\3\2\2\2\u01d8\u01d9\7<\2"+
		"\2\u01d9j\3\2\2\2\u01da\u01db\7=\2\2\u01dbl\3\2\2\2\u01dc\u01de\t\7\2"+
		"\2\u01dd\u01dc\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01dd\3\2\2\2\u01df\u01e0"+
		"\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\b\67\2\2\u01e2n\3\2\2\2\u01e3"+
		"\u01e4\7\61\2\2\u01e4\u01e5\7,\2\2\u01e5\u01e9\3\2\2\2\u01e6\u01e8\13"+
		"\2\2\2\u01e7\u01e6\3\2\2\2\u01e8\u01eb\3\2\2\2\u01e9\u01ea\3\2\2\2\u01e9"+
		"\u01e7\3\2\2\2\u01ea\u01ec\3\2\2\2\u01eb\u01e9\3\2\2\2\u01ec\u01ed\7,"+
		"\2\2\u01ed\u01ee\7\61\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01f0\b8\2\2\u01f0"+
		"p\3\2\2\2\u01f1\u01f2\7\61\2\2\u01f2\u01f3\7\61\2\2\u01f3\u01f7\3\2\2"+
		"\2\u01f4\u01f6\n\b\2\2\u01f5\u01f4\3\2\2\2\u01f6\u01f9\3\2\2\2\u01f7\u01f5"+
		"\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa\3\2\2\2\u01f9\u01f7\3\2\2\2\u01fa"+
		"\u01fb\b9\2\2\u01fbr\3\2\2\2\u01fc\u01fe\t\t\2\2\u01fd\u01ff\t\n\2\2\u01fe"+
		"\u01fd\3\2\2\2\u01fe\u01ff\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\u0201\5{"+
		">\2\u0201t\3\2\2\2\u0202\u0203\7^\2\2\u0203\u0218\t\13\2\2\u0204\u0209"+
		"\7^\2\2\u0205\u0207\t\f\2\2\u0206\u0205\3\2\2\2\u0206\u0207\3\2\2\2\u0207"+
		"\u0208\3\2\2\2\u0208\u020a\t\r\2\2\u0209\u0206\3\2\2\2\u0209\u020a\3\2"+
		"\2\2\u020a\u020b\3\2\2\2\u020b\u0218\t\r\2\2\u020c\u020e\7^\2\2\u020d"+
		"\u020f\7w\2\2\u020e\u020d\3\2\2\2\u020f\u0210\3\2\2\2\u0210\u020e\3\2"+
		"\2\2\u0210\u0211\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\5y=\2\u0213\u0214"+
		"\5y=\2\u0214\u0215\5y=\2\u0215\u0216\5y=\2\u0216\u0218\3\2\2\2\u0217\u0202"+
		"\3\2\2\2\u0217\u0204\3\2\2\2\u0217\u020c\3\2\2\2\u0218v\3\2\2\2\u0219"+
		"\u0222\5y=\2\u021a\u021d\5y=\2\u021b\u021d\7a\2\2\u021c\u021a\3\2\2\2"+
		"\u021c\u021b\3\2\2\2\u021d\u0220\3\2\2\2\u021e\u021c\3\2\2\2\u021e\u021f"+
		"\3\2\2\2\u021f\u0221\3\2\2\2\u0220\u021e\3\2\2\2\u0221\u0223\5y=\2\u0222"+
		"\u021e\3\2\2\2\u0222\u0223\3\2\2\2\u0223x\3\2\2\2\u0224\u0225\t\16\2\2"+
		"\u0225z\3\2\2\2\u0226\u022e\t\17\2\2\u0227\u0229\t\20\2\2\u0228\u0227"+
		"\3\2\2\2\u0229\u022c\3\2\2\2\u022a\u0228\3\2\2\2\u022a\u022b\3\2\2\2\u022b"+
		"\u022d\3\2\2\2\u022c\u022a\3\2\2\2\u022d\u022f\t\17\2\2\u022e\u022a\3"+
		"\2\2\2\u022e\u022f\3\2\2\2\u022f|\3\2\2\2\u0230\u0231\t\17\2\2\u0231~"+
		"\3\2\2\2\u0232\u0234\t\21\2\2\u0233\u0232\3\2\2\2\u0234\u0080\3\2\2\2"+
		"\u0235\u0237\t\22\2\2\u0236\u0235\3\2\2\2\u0237\u0082\3\2\2\2\u0238\u023a"+
		"\t\23\2\2\u0239\u0238\3\2\2\2\u023a\u0084\3\2\2\2\u023b\u023e\5\u008b"+
		"F\2\u023c\u023e\t\20\2\2\u023d\u023b\3\2\2\2\u023d\u023c\3\2\2\2\u023e"+
		"\u0086\3\2\2\2\u023f\u0240\t\24\2\2\u0240\u0088\3\2\2\2\u0241\u0242\t"+
		"\25\2\2\u0242\u008a\3\2\2\2\u0243\u0244\t\26\2\2\u0244\u008c\3\2\2\2\u0245"+
		"\u0248\5\u008bF\2\u0246\u0248\t\17\2\2\u0247\u0245\3\2\2\2\u0247\u0246"+
		"\3\2\2\2\u0248\u008e\3\2\2\2\u0249\u024a\t\27\2\2\u024a\u024b\t\17\2\2"+
		"\u024b\u024c\t\17\2\2\u024c\u024d\t\17\2\2\u024d\u024e\7/\2\2\u024e\u024f"+
		"\t\30\2\2\u024f\u0250\t\17\2\2\u0250\u0251\7/\2\2\u0251\u0252\t\f\2\2"+
		"\u0252\u0253\t\17\2\2\u0253\u0090\3\2\2\2\'\2\u015f\u0166\u016e\u0173"+
		"\u0176\u0178\u017b\u0180\u0184\u0187\u018a\u018f\u0192\u0194\u0199\u01a2"+
		"\u01aa\u01ac\u01df\u01e9\u01f7\u01fe\u0206\u0209\u0210\u0217\u021c\u021e"+
		"\u0222\u022a\u022e\u0233\u0236\u0239\u023d\u0247\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}