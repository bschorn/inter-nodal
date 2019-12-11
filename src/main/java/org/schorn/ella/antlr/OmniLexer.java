// Generated from src\main\java\org\schorn\ella\antlr\OmniLexer.g4 by ANTLR 4.7.2
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
public class OmniLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"OTHER_TYPES", "LEAF_TYPES", "BRANCH_TYPES", "MEMBERS", "ADD", "DEF", 
			"DATA_TYPE", "BOND_TYPE", "STRING_TYPE", "OBJECT_TYPE", "TYPE_NAME_DEF", 
			"ENUM_ID", "TYPE_NAME_REF", "VAR_NAME_REF", "TEXT_LENGTH", "DECIMAL_LITERAL", 
			"FLOAT_LITERAL", "CHAR_LITERAL", "TEXT_PATTERN", "STRING_LITERAL", "NULL_LITERAL", 
			"OPDBLQUOTE", "ARRAY_INDICATOR", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"COMMA", "DOT", "SEMI", "WS", "COMMENT", "LINE_COMMENT", "ExponentPart", 
			"EscapeSequence", "HexDigits", "HexDigit", "Digits", "Digit", "LowerLetterOrUnder", 
			"UpperLetterOrUnder", "LetterOrUnder", "LetterOrDigitOrUnder", "LowerLetter", 
			"UpperLetter", "Letter", "LetterOrDigit", "DateTime"
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


	public OmniLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "OmniLexer.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2#\u01a5\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3\r\7\r\u00bb\n\r\f\r\16\r"+
		"\u00be\13\r\3\16\3\16\7\16\u00c2\n\16\f\16\16\16\u00c5\13\16\3\17\3\17"+
		"\7\17\u00c9\n\17\f\17\16\17\u00cc\13\17\3\20\3\20\3\21\3\21\3\21\5\21"+
		"\u00d3\n\21\3\21\6\21\u00d6\n\21\r\21\16\21\u00d7\3\21\5\21\u00db\n\21"+
		"\5\21\u00dd\n\21\3\21\5\21\u00e0\n\21\3\22\3\22\3\22\5\22\u00e5\n\22\3"+
		"\22\3\22\5\22\u00e9\n\22\3\22\5\22\u00ec\n\22\3\22\5\22\u00ef\n\22\3\22"+
		"\3\22\3\22\5\22\u00f4\n\22\3\22\5\22\u00f7\n\22\5\22\u00f9\n\22\3\23\3"+
		"\23\3\23\5\23\u00fe\n\23\3\23\3\23\3\24\3\24\3\24\6\24\u0105\n\24\r\24"+
		"\16\24\u0106\3\24\3\24\3\24\3\25\3\25\3\25\7\25\u010f\n\25\f\25\16\25"+
		"\u0112\13\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3"+
		"\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3"+
		"\37\3 \6 \u012f\n \r \16 \u0130\3 \3 \3!\3!\3!\3!\7!\u0139\n!\f!\16!\u013c"+
		"\13!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\7\"\u0147\n\"\f\"\16\"\u014a\13\""+
		"\3\"\3\"\3#\3#\5#\u0150\n#\3#\3#\3$\3$\3$\3$\5$\u0158\n$\3$\5$\u015b\n"+
		"$\3$\3$\3$\6$\u0160\n$\r$\16$\u0161\3$\3$\3$\3$\3$\5$\u0169\n$\3%\3%\3"+
		"%\7%\u016e\n%\f%\16%\u0171\13%\3%\5%\u0174\n%\3&\3&\3\'\3\'\7\'\u017a"+
		"\n\'\f\'\16\'\u017d\13\'\3\'\5\'\u0180\n\'\3(\3(\3)\5)\u0185\n)\3*\5*"+
		"\u0188\n*\3+\5+\u018b\n+\3,\3,\5,\u018f\n,\3-\3-\3.\3.\3/\3/\3\60\3\60"+
		"\5\60\u0199\n\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61"+
		"\4\u0106\u013a\2\62\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C#E\2G\2I\2K\2M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_"+
		"\2a\2\3\2\31\3\2\63;\4\2\63\63NN\6\2FFHHffhh\6\2\f\f\17\17))^^\6\2\f\f"+
		"\17\17$$^^\5\2\13\f\16\17\"\"\4\2\f\f\17\17\4\2GGgg\4\2--//\n\2$$))^^"+
		"ddhhppttvv\3\2\62\65\3\2\629\5\2\62;CHch\3\2\62;\4\2\62;aa\4\2aac|\4\2"+
		"C\\aa\5\2C\\aac|\3\2c|\3\2C\\\4\2C\\c|\3\2\63\64\3\2\62\63\2\u01b8\2\3"+
		"\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2"+
		"\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31"+
		"\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2"+
		"\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2"+
		"\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2"+
		"\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\3c\3\2\2\2\5n\3\2\2\2\7"+
		"x\3\2\2\2\t\u0084\3\2\2\2\13\u008c\3\2\2\2\r\u0090\3\2\2\2\17\u0094\3"+
		"\2\2\2\21\u009d\3\2\2\2\23\u00a6\3\2\2\2\25\u00ad\3\2\2\2\27\u00b4\3\2"+
		"\2\2\31\u00b8\3\2\2\2\33\u00bf\3\2\2\2\35\u00c6\3\2\2\2\37\u00cd\3\2\2"+
		"\2!\u00dc\3\2\2\2#\u00f8\3\2\2\2%\u00fa\3\2\2\2\'\u0101\3\2\2\2)\u010b"+
		"\3\2\2\2+\u0115\3\2\2\2-\u011a\3\2\2\2/\u011c\3\2\2\2\61\u011f\3\2\2\2"+
		"\63\u0121\3\2\2\2\65\u0123\3\2\2\2\67\u0125\3\2\2\29\u0127\3\2\2\2;\u0129"+
		"\3\2\2\2=\u012b\3\2\2\2?\u012e\3\2\2\2A\u0134\3\2\2\2C\u0142\3\2\2\2E"+
		"\u014d\3\2\2\2G\u0168\3\2\2\2I\u016a\3\2\2\2K\u0175\3\2\2\2M\u0177\3\2"+
		"\2\2O\u0181\3\2\2\2Q\u0184\3\2\2\2S\u0187\3\2\2\2U\u018a\3\2\2\2W\u018e"+
		"\3\2\2\2Y\u0190\3\2\2\2[\u0192\3\2\2\2]\u0194\3\2\2\2_\u0198\3\2\2\2a"+
		"\u019a\3\2\2\2cd\7Q\2\2de\7v\2\2ef\7j\2\2fg\7g\2\2gh\7t\2\2hi\7V\2\2i"+
		"j\7{\2\2jk\7r\2\2kl\7g\2\2lm\7u\2\2m\4\3\2\2\2no\7N\2\2op\7g\2\2pq\7c"+
		"\2\2qr\7h\2\2rs\7V\2\2st\7{\2\2tu\7r\2\2uv\7g\2\2vw\7u\2\2w\6\3\2\2\2"+
		"xy\7D\2\2yz\7t\2\2z{\7c\2\2{|\7p\2\2|}\7e\2\2}~\7j\2\2~\177\7V\2\2\177"+
		"\u0080\7{\2\2\u0080\u0081\7r\2\2\u0081\u0082\7g\2\2\u0082\u0083\7u\2\2"+
		"\u0083\b\3\2\2\2\u0084\u0085\7O\2\2\u0085\u0086\7g\2\2\u0086\u0087\7o"+
		"\2\2\u0087\u0088\7d\2\2\u0088\u0089\7g\2\2\u0089\u008a\7t\2\2\u008a\u008b"+
		"\7u\2\2\u008b\n\3\2\2\2\u008c\u008d\7c\2\2\u008d\u008e\7f\2\2\u008e\u008f"+
		"\7f\2\2\u008f\f\3\2\2\2\u0090\u0091\7f\2\2\u0091\u0092\7g\2\2\u0092\u0093"+
		"\7h\2\2\u0093\16\3\2\2\2\u0094\u0095\7F\2\2\u0095\u0096\7c\2\2\u0096\u0097"+
		"\7v\2\2\u0097\u0098\7c\2\2\u0098\u0099\7V\2\2\u0099\u009a\7{\2\2\u009a"+
		"\u009b\7r\2\2\u009b\u009c\7g\2\2\u009c\20\3\2\2\2\u009d\u009e\7D\2\2\u009e"+
		"\u009f\7q\2\2\u009f\u00a0\7p\2\2\u00a0\u00a1\7f\2\2\u00a1\u00a2\7V\2\2"+
		"\u00a2\u00a3\7{\2\2\u00a3\u00a4\7r\2\2\u00a4\u00a5\7g\2\2\u00a5\22\3\2"+
		"\2\2\u00a6\u00a7\7U\2\2\u00a7\u00a8\7v\2\2\u00a8\u00a9\7t\2\2\u00a9\u00aa"+
		"\7k\2\2\u00aa\u00ab\7p\2\2\u00ab\u00ac\7i\2\2\u00ac\24\3\2\2\2\u00ad\u00ae"+
		"\7Q\2\2\u00ae\u00af\7d\2\2\u00af\u00b0\7l\2\2\u00b0\u00b1\7g\2\2\u00b1"+
		"\u00b2\7e\2\2\u00b2\u00b3\7v\2\2\u00b3\26\3\2\2\2\u00b4\u00b5\7$\2\2\u00b5"+
		"\u00b6\5\33\16\2\u00b6\u00b7\7$\2\2\u00b7\30\3\2\2\2\u00b8\u00bc\5[.\2"+
		"\u00b9\u00bb\5S*\2\u00ba\u00b9\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc\u00ba"+
		"\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bd\32\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf"+
		"\u00c3\5[.\2\u00c0\u00c2\5]/\2\u00c1\u00c0\3\2\2\2\u00c2\u00c5\3\2\2\2"+
		"\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\34\3\2\2\2\u00c5\u00c3"+
		"\3\2\2\2\u00c6\u00ca\5Y-\2\u00c7\u00c9\5]/\2\u00c8\u00c7\3\2\2\2\u00c9"+
		"\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\36\3\2\2"+
		"\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\5M\'\2\u00ce \3\2\2\2\u00cf\u00dd\7"+
		"\62\2\2\u00d0\u00da\t\2\2\2\u00d1\u00d3\5M\'\2\u00d2\u00d1\3\2\2\2\u00d2"+
		"\u00d3\3\2\2\2\u00d3\u00db\3\2\2\2\u00d4\u00d6\7a\2\2\u00d5\u00d4\3\2"+
		"\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"+
		"\u00d9\3\2\2\2\u00d9\u00db\5M\'\2\u00da\u00d2\3\2\2\2\u00da\u00d5\3\2"+
		"\2\2\u00db\u00dd\3\2\2\2\u00dc\u00cf\3\2\2\2\u00dc\u00d0\3\2\2\2\u00dd"+
		"\u00df\3\2\2\2\u00de\u00e0\t\3\2\2\u00df\u00de\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0\"\3\2\2\2\u00e1\u00e2\5M\'\2\u00e2\u00e4\7\60\2\2\u00e3\u00e5"+
		"\5M\'\2\u00e4\u00e3\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e9\3\2\2\2\u00e6"+
		"\u00e7\7\60\2\2\u00e7\u00e9\5M\'\2\u00e8\u00e1\3\2\2\2\u00e8\u00e6\3\2"+
		"\2\2\u00e9\u00eb\3\2\2\2\u00ea\u00ec\5E#\2\u00eb\u00ea\3\2\2\2\u00eb\u00ec"+
		"\3\2\2\2\u00ec\u00ee\3\2\2\2\u00ed\u00ef\t\4\2\2\u00ee\u00ed\3\2\2\2\u00ee"+
		"\u00ef\3\2\2\2\u00ef\u00f9\3\2\2\2\u00f0\u00f6\5M\'\2\u00f1\u00f3\5E#"+
		"\2\u00f2\u00f4\t\4\2\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f7"+
		"\3\2\2\2\u00f5\u00f7\t\4\2\2\u00f6\u00f1\3\2\2\2\u00f6\u00f5\3\2\2\2\u00f7"+
		"\u00f9\3\2\2\2\u00f8\u00e8\3\2\2\2\u00f8\u00f0\3\2\2\2\u00f9$\3\2\2\2"+
		"\u00fa\u00fd\7)\2\2\u00fb\u00fe\n\5\2\2\u00fc\u00fe\5G$\2\u00fd\u00fb"+
		"\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0100\7)\2\2\u0100"+
		"&\3\2\2\2\u0101\u0102\7$\2\2\u0102\u0104\7`\2\2\u0103\u0105\13\2\2\2\u0104"+
		"\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106\u0107\3\2\2\2\u0106\u0104\3\2"+
		"\2\2\u0107\u0108\3\2\2\2\u0108\u0109\7&\2\2\u0109\u010a\7$\2\2\u010a("+
		"\3\2\2\2\u010b\u0110\7$\2\2\u010c\u010f\n\6\2\2\u010d\u010f\5G$\2\u010e"+
		"\u010c\3\2\2\2\u010e\u010d\3\2\2\2\u010f\u0112\3\2\2\2\u0110\u010e\3\2"+
		"\2\2\u0110\u0111\3\2\2\2\u0111\u0113\3\2\2\2\u0112\u0110\3\2\2\2\u0113"+
		"\u0114\7$\2\2\u0114*\3\2\2\2\u0115\u0116\7p\2\2\u0116\u0117\7w\2\2\u0117"+
		"\u0118\7n\2\2\u0118\u0119\7n\2\2\u0119,\3\2\2\2\u011a\u011b\7$\2\2\u011b"+
		".\3\2\2\2\u011c\u011d\7]\2\2\u011d\u011e\7_\2\2\u011e\60\3\2\2\2\u011f"+
		"\u0120\7*\2\2\u0120\62\3\2\2\2\u0121\u0122\7+\2\2\u0122\64\3\2\2\2\u0123"+
		"\u0124\7}\2\2\u0124\66\3\2\2\2\u0125\u0126\7\177\2\2\u01268\3\2\2\2\u0127"+
		"\u0128\7.\2\2\u0128:\3\2\2\2\u0129\u012a\7\60\2\2\u012a<\3\2\2\2\u012b"+
		"\u012c\7=\2\2\u012c>\3\2\2\2\u012d\u012f\t\7\2\2\u012e\u012d\3\2\2\2\u012f"+
		"\u0130\3\2\2\2\u0130\u012e\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\3\2"+
		"\2\2\u0132\u0133\b \2\2\u0133@\3\2\2\2\u0134\u0135\7\61\2\2\u0135\u0136"+
		"\7,\2\2\u0136\u013a\3\2\2\2\u0137\u0139\13\2\2\2\u0138\u0137\3\2\2\2\u0139"+
		"\u013c\3\2\2\2\u013a\u013b\3\2\2\2\u013a\u0138\3\2\2\2\u013b\u013d\3\2"+
		"\2\2\u013c\u013a\3\2\2\2\u013d\u013e\7,\2\2\u013e\u013f\7\61\2\2\u013f"+
		"\u0140\3\2\2\2\u0140\u0141\b!\2\2\u0141B\3\2\2\2\u0142\u0143\7\61\2\2"+
		"\u0143\u0144\7\61\2\2\u0144\u0148\3\2\2\2\u0145\u0147\n\b\2\2\u0146\u0145"+
		"\3\2\2\2\u0147\u014a\3\2\2\2\u0148\u0146\3\2\2\2\u0148\u0149\3\2\2\2\u0149"+
		"\u014b\3\2\2\2\u014a\u0148\3\2\2\2\u014b\u014c\b\"\2\2\u014cD\3\2\2\2"+
		"\u014d\u014f\t\t\2\2\u014e\u0150\t\n\2\2\u014f\u014e\3\2\2\2\u014f\u0150"+
		"\3\2\2\2\u0150\u0151\3\2\2\2\u0151\u0152\5M\'\2\u0152F\3\2\2\2\u0153\u0154"+
		"\7^\2\2\u0154\u0169\t\13\2\2\u0155\u015a\7^\2\2\u0156\u0158\t\f\2\2\u0157"+
		"\u0156\3\2\2\2\u0157\u0158\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015b\t\r"+
		"\2\2\u015a\u0157\3\2\2\2\u015a\u015b\3\2\2\2\u015b\u015c\3\2\2\2\u015c"+
		"\u0169\t\r\2\2\u015d\u015f\7^\2\2\u015e\u0160\7w\2\2\u015f\u015e\3\2\2"+
		"\2\u0160\u0161\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2\2\2\u0162\u0163"+
		"\3\2\2\2\u0163\u0164\5K&\2\u0164\u0165\5K&\2\u0165\u0166\5K&\2\u0166\u0167"+
		"\5K&\2\u0167\u0169\3\2\2\2\u0168\u0153\3\2\2\2\u0168\u0155\3\2\2\2\u0168"+
		"\u015d\3\2\2\2\u0169H\3\2\2\2\u016a\u0173\5K&\2\u016b\u016e\5K&\2\u016c"+
		"\u016e\7a\2\2\u016d\u016b\3\2\2\2\u016d\u016c\3\2\2\2\u016e\u0171\3\2"+
		"\2\2\u016f\u016d\3\2\2\2\u016f\u0170\3\2\2\2\u0170\u0172\3\2\2\2\u0171"+
		"\u016f\3\2\2\2\u0172\u0174\5K&\2\u0173\u016f\3\2\2\2\u0173\u0174\3\2\2"+
		"\2\u0174J\3\2\2\2\u0175\u0176\t\16\2\2\u0176L\3\2\2\2\u0177\u017f\t\17"+
		"\2\2\u0178\u017a\t\20\2\2\u0179\u0178\3\2\2\2\u017a\u017d\3\2\2\2\u017b"+
		"\u0179\3\2\2\2\u017b\u017c\3\2\2\2\u017c\u017e\3\2\2\2\u017d\u017b\3\2"+
		"\2\2\u017e\u0180\t\17\2\2\u017f\u017b\3\2\2\2\u017f\u0180\3\2\2\2\u0180"+
		"N\3\2\2\2\u0181\u0182\t\17\2\2\u0182P\3\2\2\2\u0183\u0185\t\21\2\2\u0184"+
		"\u0183\3\2\2\2\u0185R\3\2\2\2\u0186\u0188\t\22\2\2\u0187\u0186\3\2\2\2"+
		"\u0188T\3\2\2\2\u0189\u018b\t\23\2\2\u018a\u0189\3\2\2\2\u018bV\3\2\2"+
		"\2\u018c\u018f\5]/\2\u018d\u018f\t\20\2\2\u018e\u018c\3\2\2\2\u018e\u018d"+
		"\3\2\2\2\u018fX\3\2\2\2\u0190\u0191\t\24\2\2\u0191Z\3\2\2\2\u0192\u0193"+
		"\t\25\2\2\u0193\\\3\2\2\2\u0194\u0195\t\26\2\2\u0195^\3\2\2\2\u0196\u0199"+
		"\5]/\2\u0197\u0199\t\17\2\2\u0198\u0196\3\2\2\2\u0198\u0197\3\2\2\2\u0199"+
		"`\3\2\2\2\u019a\u019b\t\27\2\2\u019b\u019c\t\17\2\2\u019c\u019d\t\17\2"+
		"\2\u019d\u019e\t\17\2\2\u019e\u019f\7/\2\2\u019f\u01a0\t\30\2\2\u01a0"+
		"\u01a1\t\17\2\2\u01a1\u01a2\7/\2\2\u01a2\u01a3\t\f\2\2\u01a3\u01a4\t\17"+
		"\2\2\u01a4b\3\2\2\2(\2\u00bc\u00c3\u00ca\u00d2\u00d7\u00da\u00dc\u00df"+
		"\u00e4\u00e8\u00eb\u00ee\u00f3\u00f6\u00f8\u00fd\u0106\u010e\u0110\u0130"+
		"\u013a\u0148\u014f\u0157\u015a\u0161\u0168\u016d\u016f\u0173\u017b\u017f"+
		"\u0184\u0187\u018a\u018e\u0198\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}