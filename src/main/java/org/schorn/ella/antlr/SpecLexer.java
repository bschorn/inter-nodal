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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ATTRIBUTES", "FIELD_TYPES", "VALUE_TYPES", "FLAGS", "FRAGMENTS", "OBJECT_TYPES", 
			"BASE_TYPES", "ARRAY_TYPES", "MEMBERS", "PARENTS", "DATA_TYPE", "OBJECT_CATEGORY", 
			"OBJECT_PURPOSE", "OBJECT_LEVEL", "FIELD_TYPE", "VALUE_TYPE", "VALUE_FLAG", 
			"FRAGMENT", "OBJECT_TYPE", "BASE_TYPE", "ARRAY_TYPE", "MEMBER", "PARENT", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2@\u02a4\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3"+
		"\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 "+
		"\3 \3 \3!\3!\7!\u01ae\n!\f!\16!\u01b1\13!\3\"\3\"\7\"\u01b5\n\"\f\"\16"+
		"\"\u01b8\13\"\3#\3#\3$\3$\3$\5$\u01bf\n$\3$\6$\u01c2\n$\r$\16$\u01c3\3"+
		"$\5$\u01c7\n$\5$\u01c9\n$\3$\5$\u01cc\n$\3%\3%\3%\5%\u01d1\n%\3%\3%\5"+
		"%\u01d5\n%\3%\5%\u01d8\n%\3%\5%\u01db\n%\3%\3%\3%\5%\u01e0\n%\3%\5%\u01e3"+
		"\n%\5%\u01e5\n%\3&\3&\3&\5&\u01ea\n&\3&\3&\3\'\3\'\3\'\6\'\u01f1\n\'\r"+
		"\'\16\'\u01f2\3\'\3\'\3\'\3(\3(\3(\7(\u01fb\n(\f(\16(\u01fe\13(\3(\3("+
		"\3)\3)\3)\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61"+
		"\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39"+
		"\39\3:\3:\3;\3;\3<\3<\3=\6=\u022e\n=\r=\16=\u022f\3=\3=\3>\3>\3>\3>\7"+
		">\u0238\n>\f>\16>\u023b\13>\3>\3>\3>\3>\3>\3?\3?\3?\3?\7?\u0246\n?\f?"+
		"\16?\u0249\13?\3?\3?\3@\3@\5@\u024f\n@\3@\3@\3A\3A\3A\3A\5A\u0257\nA\3"+
		"A\5A\u025a\nA\3A\3A\3A\6A\u025f\nA\rA\16A\u0260\3A\3A\3A\3A\3A\5A\u0268"+
		"\nA\3B\3B\3B\7B\u026d\nB\fB\16B\u0270\13B\3B\5B\u0273\nB\3C\3C\3D\3D\7"+
		"D\u0279\nD\fD\16D\u027c\13D\3D\5D\u027f\nD\3E\3E\3F\5F\u0284\nF\3G\5G"+
		"\u0287\nG\3H\5H\u028a\nH\3I\3I\5I\u028e\nI\3J\3J\3K\3K\3L\3L\3M\3M\5M"+
		"\u0298\nM\3N\3N\3N\3N\3N\3N\3N\3N\3N\3N\3N\4\u01f2\u0239\2O\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C"+
		"#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w"+
		"=y>{?}@\177\2\u0081\2\u0083\2\u0085\2\u0087\2\u0089\2\u008b\2\u008d\2"+
		"\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\u0099\2\u009b\2\3\2\31\3\2\63"+
		";\4\2\63\63NN\6\2FFHHffhh\6\2\f\f\17\17))^^\6\2\f\f\17\17$$^^\5\2\13\f"+
		"\16\17\"\"\4\2\f\f\17\17\4\2GGgg\4\2--//\n\2$$))^^ddhhppttvv\3\2\62\65"+
		"\3\2\629\5\2\62;CHch\3\2\62;\4\2\62;aa\4\2aac|\4\2C\\aa\5\2C\\aac|\3\2"+
		"c|\3\2C\\\4\2C\\c|\3\2\63\64\3\2\62\63\2\u02b6\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2"+
		"\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2"+
		"\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y"+
		"\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2"+
		"\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2"+
		"\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\3\u009d"+
		"\3\2\2\2\5\u00a8\3\2\2\2\7\u00b3\3\2\2\2\t\u00be\3\2\2\2\13\u00c4\3\2"+
		"\2\2\r\u00ce\3\2\2\2\17\u00da\3\2\2\2\21\u00e4\3\2\2\2\23\u00ef\3\2\2"+
		"\2\25\u00f7\3\2\2\2\27\u00ff\3\2\2\2\31\u0108\3\2\2\2\33\u0117\3\2\2\2"+
		"\35\u0125\3\2\2\2\37\u0131\3\2\2\2!\u013b\3\2\2\2#\u0145\3\2\2\2%\u014f"+
		"\3\2\2\2\'\u0158\3\2\2\2)\u0163\3\2\2\2+\u016c\3\2\2\2-\u0176\3\2\2\2"+
		"/\u017d\3\2\2\2\61\u0184\3\2\2\2\63\u018d\3\2\2\2\65\u0192\3\2\2\2\67"+
		"\u0197\3\2\2\29\u019b\3\2\2\2;\u019f\3\2\2\2=\u01a3\3\2\2\2?\u01a7\3\2"+
		"\2\2A\u01ab\3\2\2\2C\u01b2\3\2\2\2E\u01b9\3\2\2\2G\u01c8\3\2\2\2I\u01e4"+
		"\3\2\2\2K\u01e6\3\2\2\2M\u01ed\3\2\2\2O\u01f7\3\2\2\2Q\u0201\3\2\2\2S"+
		"\u0206\3\2\2\2U\u0208\3\2\2\2W\u020a\3\2\2\2Y\u020c\3\2\2\2[\u020e\3\2"+
		"\2\2]\u0210\3\2\2\2_\u0212\3\2\2\2a\u0214\3\2\2\2c\u0216\3\2\2\2e\u0218"+
		"\3\2\2\2g\u021a\3\2\2\2i\u021c\3\2\2\2k\u021e\3\2\2\2m\u0220\3\2\2\2o"+
		"\u0222\3\2\2\2q\u0224\3\2\2\2s\u0226\3\2\2\2u\u0228\3\2\2\2w\u022a\3\2"+
		"\2\2y\u022d\3\2\2\2{\u0233\3\2\2\2}\u0241\3\2\2\2\177\u024c\3\2\2\2\u0081"+
		"\u0267\3\2\2\2\u0083\u0269\3\2\2\2\u0085\u0274\3\2\2\2\u0087\u0276\3\2"+
		"\2\2\u0089\u0280\3\2\2\2\u008b\u0283\3\2\2\2\u008d\u0286\3\2\2\2\u008f"+
		"\u0289\3\2\2\2\u0091\u028d\3\2\2\2\u0093\u028f\3\2\2\2\u0095\u0291\3\2"+
		"\2\2\u0097\u0293\3\2\2\2\u0099\u0297\3\2\2\2\u009b\u0299\3\2\2\2\u009d"+
		"\u009e\7C\2\2\u009e\u009f\7v\2\2\u009f\u00a0\7v\2\2\u00a0\u00a1\7t\2\2"+
		"\u00a1\u00a2\7k\2\2\u00a2\u00a3\7d\2\2\u00a3\u00a4\7w\2\2\u00a4\u00a5"+
		"\7v\2\2\u00a5\u00a6\7g\2\2\u00a6\u00a7\7u\2\2\u00a7\4\3\2\2\2\u00a8\u00a9"+
		"\7H\2\2\u00a9\u00aa\7k\2\2\u00aa\u00ab\7g\2\2\u00ab\u00ac\7n\2\2\u00ac"+
		"\u00ad\7f\2\2\u00ad\u00ae\7V\2\2\u00ae\u00af\7{\2\2\u00af\u00b0\7r\2\2"+
		"\u00b0\u00b1\7g\2\2\u00b1\u00b2\7u\2\2\u00b2\6\3\2\2\2\u00b3\u00b4\7X"+
		"\2\2\u00b4\u00b5\7c\2\2\u00b5\u00b6\7n\2\2\u00b6\u00b7\7w\2\2\u00b7\u00b8"+
		"\7g\2\2\u00b8\u00b9\7V\2\2\u00b9\u00ba\7{\2\2\u00ba\u00bb\7r\2\2\u00bb"+
		"\u00bc\7g\2\2\u00bc\u00bd\7u\2\2\u00bd\b\3\2\2\2\u00be\u00bf\7H\2\2\u00bf"+
		"\u00c0\7n\2\2\u00c0\u00c1\7c\2\2\u00c1\u00c2\7i\2\2\u00c2\u00c3\7u\2\2"+
		"\u00c3\n\3\2\2\2\u00c4\u00c5\7H\2\2\u00c5\u00c6\7t\2\2\u00c6\u00c7\7c"+
		"\2\2\u00c7\u00c8\7i\2\2\u00c8\u00c9\7o\2\2\u00c9\u00ca\7g\2\2\u00ca\u00cb"+
		"\7p\2\2\u00cb\u00cc\7v\2\2\u00cc\u00cd\7u\2\2\u00cd\f\3\2\2\2\u00ce\u00cf"+
		"\7Q\2\2\u00cf\u00d0\7d\2\2\u00d0\u00d1\7l\2\2\u00d1\u00d2\7g\2\2\u00d2"+
		"\u00d3\7e\2\2\u00d3\u00d4\7v\2\2\u00d4\u00d5\7V\2\2\u00d5\u00d6\7{\2\2"+
		"\u00d6\u00d7\7r\2\2\u00d7\u00d8\7g\2\2\u00d8\u00d9\7u\2\2\u00d9\16\3\2"+
		"\2\2\u00da\u00db\7D\2\2\u00db\u00dc\7c\2\2\u00dc\u00dd\7u\2\2\u00dd\u00de"+
		"\7g\2\2\u00de\u00df\7V\2\2\u00df\u00e0\7{\2\2\u00e0\u00e1\7r\2\2\u00e1"+
		"\u00e2\7g\2\2\u00e2\u00e3\7u\2\2\u00e3\20\3\2\2\2\u00e4\u00e5\7C\2\2\u00e5"+
		"\u00e6\7t\2\2\u00e6\u00e7\7t\2\2\u00e7\u00e8\7c\2\2\u00e8\u00e9\7{\2\2"+
		"\u00e9\u00ea\7V\2\2\u00ea\u00eb\7{\2\2\u00eb\u00ec\7r\2\2\u00ec\u00ed"+
		"\7g\2\2\u00ed\u00ee\7u\2\2\u00ee\22\3\2\2\2\u00ef\u00f0\7O\2\2\u00f0\u00f1"+
		"\7g\2\2\u00f1\u00f2\7o\2\2\u00f2\u00f3\7d\2\2\u00f3\u00f4\7g\2\2\u00f4"+
		"\u00f5\7t\2\2\u00f5\u00f6\7u\2\2\u00f6\24\3\2\2\2\u00f7\u00f8\7R\2\2\u00f8"+
		"\u00f9\7c\2\2\u00f9\u00fa\7t\2\2\u00fa\u00fb\7g\2\2\u00fb\u00fc\7p\2\2"+
		"\u00fc\u00fd\7v\2\2\u00fd\u00fe\7u\2\2\u00fe\26\3\2\2\2\u00ff\u0100\7"+
		"F\2\2\u0100\u0101\7c\2\2\u0101\u0102\7v\2\2\u0102\u0103\7c\2\2\u0103\u0104"+
		"\7V\2\2\u0104\u0105\7{\2\2\u0105\u0106\7r\2\2\u0106\u0107\7g\2\2\u0107"+
		"\30\3\2\2\2\u0108\u0109\7Q\2\2\u0109\u010a\7d\2\2\u010a\u010b\7l\2\2\u010b"+
		"\u010c\7g\2\2\u010c\u010d\7e\2\2\u010d\u010e\7v\2\2\u010e\u010f\7E\2\2"+
		"\u010f\u0110\7c\2\2\u0110\u0111\7v\2\2\u0111\u0112\7g\2\2\u0112\u0113"+
		"\7i\2\2\u0113\u0114\7q\2\2\u0114\u0115\7t\2\2\u0115\u0116\7{\2\2\u0116"+
		"\32\3\2\2\2\u0117\u0118\7Q\2\2\u0118\u0119\7d\2\2\u0119\u011a\7l\2\2\u011a"+
		"\u011b\7g\2\2\u011b\u011c\7e\2\2\u011c\u011d\7v\2\2\u011d\u011e\7R\2\2"+
		"\u011e\u011f\7w\2\2\u011f\u0120\7t\2\2\u0120\u0121\7r\2\2\u0121\u0122"+
		"\7q\2\2\u0122\u0123\7u\2\2\u0123\u0124\7g\2\2\u0124\34\3\2\2\2\u0125\u0126"+
		"\7Q\2\2\u0126\u0127\7d\2\2\u0127\u0128\7l\2\2\u0128\u0129\7g\2\2\u0129"+
		"\u012a\7e\2\2\u012a\u012b\7v\2\2\u012b\u012c\7N\2\2\u012c\u012d\7g\2\2"+
		"\u012d\u012e\7x\2\2\u012e\u012f\7g\2\2\u012f\u0130\7n\2\2\u0130\36\3\2"+
		"\2\2\u0131\u0132\7H\2\2\u0132\u0133\7k\2\2\u0133\u0134\7g\2\2\u0134\u0135"+
		"\7n\2\2\u0135\u0136\7f\2\2\u0136\u0137\7V\2\2\u0137\u0138\7{\2\2\u0138"+
		"\u0139\7r\2\2\u0139\u013a\7g\2\2\u013a \3\2\2\2\u013b\u013c\7X\2\2\u013c"+
		"\u013d\7c\2\2\u013d\u013e\7n\2\2\u013e\u013f\7w\2\2\u013f\u0140\7g\2\2"+
		"\u0140\u0141\7V\2\2\u0141\u0142\7{\2\2\u0142\u0143\7r\2\2\u0143\u0144"+
		"\7g\2\2\u0144\"\3\2\2\2\u0145\u0146\7X\2\2\u0146\u0147\7c\2\2\u0147\u0148"+
		"\7n\2\2\u0148\u0149\7w\2\2\u0149\u014a\7g\2\2\u014a\u014b\7H\2\2\u014b"+
		"\u014c\7n\2\2\u014c\u014d\7c\2\2\u014d\u014e\7i\2\2\u014e$\3\2\2\2\u014f"+
		"\u0150\7H\2\2\u0150\u0151\7t\2\2\u0151\u0152\7c\2\2\u0152\u0153\7i\2\2"+
		"\u0153\u0154\7o\2\2\u0154\u0155\7g\2\2\u0155\u0156\7p\2\2\u0156\u0157"+
		"\7v\2\2\u0157&\3\2\2\2\u0158\u0159\7Q\2\2\u0159\u015a\7d\2\2\u015a\u015b"+
		"\7l\2\2\u015b\u015c\7g\2\2\u015c\u015d\7e\2\2\u015d\u015e\7v\2\2\u015e"+
		"\u015f\7V\2\2\u015f\u0160\7{\2\2\u0160\u0161\7r\2\2\u0161\u0162\7g\2\2"+
		"\u0162(\3\2\2\2\u0163\u0164\7D\2\2\u0164\u0165\7c\2\2\u0165\u0166\7u\2"+
		"\2\u0166\u0167\7g\2\2\u0167\u0168\7V\2\2\u0168\u0169\7{\2\2\u0169\u016a"+
		"\7r\2\2\u016a\u016b\7g\2\2\u016b*\3\2\2\2\u016c\u016d\7C\2\2\u016d\u016e"+
		"\7t\2\2\u016e\u016f\7t\2\2\u016f\u0170\7c\2\2\u0170\u0171\7{\2\2\u0171"+
		"\u0172\7V\2\2\u0172\u0173\7{\2\2\u0173\u0174\7r\2\2\u0174\u0175\7g\2\2"+
		"\u0175,\3\2\2\2\u0176\u0177\7O\2\2\u0177\u0178\7g\2\2\u0178\u0179\7o\2"+
		"\2\u0179\u017a\7d\2\2\u017a\u017b\7g\2\2\u017b\u017c\7t\2\2\u017c.\3\2"+
		"\2\2\u017d\u017e\7R\2\2\u017e\u017f\7c\2\2\u017f\u0180\7t\2\2\u0180\u0181"+
		"\7g\2\2\u0181\u0182\7p\2\2\u0182\u0183\7v\2\2\u0183\60\3\2\2\2\u0184\u0185"+
		"\7D\2\2\u0185\u0186\7q\2\2\u0186\u0187\7p\2\2\u0187\u0188\7f\2\2\u0188"+
		"\u0189\7V\2\2\u0189\u018a\7{\2\2\u018a\u018b\7r\2\2\u018b\u018c\7g\2\2"+
		"\u018c\62\3\2\2\2\u018d\u018e\7T\2\2\u018e\u018f\7g\2\2\u018f\u0190\7"+
		"c\2\2\u0190\u0191\7f\2\2\u0191\64\3\2\2\2\u0192\u0193\7H\2\2\u0193\u0194"+
		"\7k\2\2\u0194\u0195\7n\2\2\u0195\u0196\7g\2\2\u0196\66\3\2\2\2\u0197\u0198"+
		"\7f\2\2\u0198\u0199\7g\2\2\u0199\u019a\7h\2\2\u019a8\3\2\2\2\u019b\u019c"+
		"\7c\2\2\u019c\u019d\7f\2\2\u019d\u019e\7f\2\2\u019e:\3\2\2\2\u019f\u01a0"+
		"\7O\2\2\u01a0\u01a1\7C\2\2\u01a1\u01a2\7Z\2\2\u01a2<\3\2\2\2\u01a3\u01a4"+
		"\7O\2\2\u01a4\u01a5\7K\2\2\u01a5\u01a6\7P\2\2\u01a6>\3\2\2\2\u01a7\u01a8"+
		"\7$\2\2\u01a8\u01a9\5C\"\2\u01a9\u01aa\7$\2\2\u01aa@\3\2\2\2\u01ab\u01af"+
		"\5\u0095K\2\u01ac\u01ae\5\u008dG\2\u01ad\u01ac\3\2\2\2\u01ae\u01b1\3\2"+
		"\2\2\u01af\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0B\3\2\2\2\u01b1\u01af"+
		"\3\2\2\2\u01b2\u01b6\5\u0097L\2\u01b3\u01b5\5\u0091I\2\u01b4\u01b3\3\2"+
		"\2\2\u01b5\u01b8\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b6\u01b7\3\2\2\2\u01b7"+
		"D\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b9\u01ba\5\u0087D\2\u01baF\3\2\2\2\u01bb"+
		"\u01c9\7\62\2\2\u01bc\u01c6\t\2\2\2\u01bd\u01bf\5\u0087D\2\u01be\u01bd"+
		"\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c7\3\2\2\2\u01c0\u01c2\7a\2\2\u01c1"+
		"\u01c0\3\2\2\2\u01c2\u01c3\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c3\u01c4\3\2"+
		"\2\2\u01c4\u01c5\3\2\2\2\u01c5\u01c7\5\u0087D\2\u01c6\u01be\3\2\2\2\u01c6"+
		"\u01c1\3\2\2\2\u01c7\u01c9\3\2\2\2\u01c8\u01bb\3\2\2\2\u01c8\u01bc\3\2"+
		"\2\2\u01c9\u01cb\3\2\2\2\u01ca\u01cc\t\3\2\2\u01cb\u01ca\3\2\2\2\u01cb"+
		"\u01cc\3\2\2\2\u01ccH\3\2\2\2\u01cd\u01ce\5\u0087D\2\u01ce\u01d0\7\60"+
		"\2\2\u01cf\u01d1\5\u0087D\2\u01d0\u01cf\3\2\2\2\u01d0\u01d1\3\2\2\2\u01d1"+
		"\u01d5\3\2\2\2\u01d2\u01d3\7\60\2\2\u01d3\u01d5\5\u0087D\2\u01d4\u01cd"+
		"\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d5\u01d7\3\2\2\2\u01d6\u01d8\5\177@\2"+
		"\u01d7\u01d6\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8\u01da\3\2\2\2\u01d9\u01db"+
		"\t\4\2\2\u01da\u01d9\3\2\2\2\u01da\u01db\3\2\2\2\u01db\u01e5\3\2\2\2\u01dc"+
		"\u01e2\5\u0087D\2\u01dd\u01df\5\177@\2\u01de\u01e0\t\4\2\2\u01df\u01de"+
		"\3\2\2\2\u01df\u01e0\3\2\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01e3\t\4\2\2\u01e2"+
		"\u01dd\3\2\2\2\u01e2\u01e1\3\2\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01d4\3\2"+
		"\2\2\u01e4\u01dc\3\2\2\2\u01e5J\3\2\2\2\u01e6\u01e9\7)\2\2\u01e7\u01ea"+
		"\n\5\2\2\u01e8\u01ea\5\u0081A\2\u01e9\u01e7\3\2\2\2\u01e9\u01e8\3\2\2"+
		"\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\7)\2\2\u01ecL\3\2\2\2\u01ed\u01ee\7"+
		"$\2\2\u01ee\u01f0\7`\2\2\u01ef\u01f1\13\2\2\2\u01f0\u01ef\3\2\2\2\u01f1"+
		"\u01f2\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f2\u01f0\3\2\2\2\u01f3\u01f4\3\2"+
		"\2\2\u01f4\u01f5\7&\2\2\u01f5\u01f6\7$\2\2\u01f6N\3\2\2\2\u01f7\u01fc"+
		"\7$\2\2\u01f8\u01fb\n\6\2\2\u01f9\u01fb\5\u0081A\2\u01fa\u01f8\3\2\2\2"+
		"\u01fa\u01f9\3\2\2\2\u01fb\u01fe\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fc\u01fd"+
		"\3\2\2\2\u01fd\u01ff\3\2\2\2\u01fe\u01fc\3\2\2\2\u01ff\u0200\7$\2\2\u0200"+
		"P\3\2\2\2\u0201\u0202\7p\2\2\u0202\u0203\7w\2\2\u0203\u0204\7n\2\2\u0204"+
		"\u0205\7n\2\2\u0205R\3\2\2\2\u0206\u0207\7`\2\2\u0207T\3\2\2\2\u0208\u0209"+
		"\7&\2\2\u0209V\3\2\2\2\u020a\u020b\7\u0080\2\2\u020bX\3\2\2\2\u020c\u020d"+
		"\7B\2\2\u020dZ\3\2\2\2\u020e\u020f\7%\2\2\u020f\\\3\2\2\2\u0210\u0211"+
		"\7~\2\2\u0211^\3\2\2\2\u0212\u0213\7$\2\2\u0213`\3\2\2\2\u0214\u0215\7"+
		"*\2\2\u0215b\3\2\2\2\u0216\u0217\7+\2\2\u0217d\3\2\2\2\u0218\u0219\7]"+
		"\2\2\u0219f\3\2\2\2\u021a\u021b\7_\2\2\u021bh\3\2\2\2\u021c\u021d\7>\2"+
		"\2\u021dj\3\2\2\2\u021e\u021f\7@\2\2\u021fl\3\2\2\2\u0220\u0221\7}\2\2"+
		"\u0221n\3\2\2\2\u0222\u0223\7\177\2\2\u0223p\3\2\2\2\u0224\u0225\7.\2"+
		"\2\u0225r\3\2\2\2\u0226\u0227\7\60\2\2\u0227t\3\2\2\2\u0228\u0229\7<\2"+
		"\2\u0229v\3\2\2\2\u022a\u022b\7=\2\2\u022bx\3\2\2\2\u022c\u022e\t\7\2"+
		"\2\u022d\u022c\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u022d\3\2\2\2\u022f\u0230"+
		"\3\2\2\2\u0230\u0231\3\2\2\2\u0231\u0232\b=\2\2\u0232z\3\2\2\2\u0233\u0234"+
		"\7\61\2\2\u0234\u0235\7,\2\2\u0235\u0239\3\2\2\2\u0236\u0238\13\2\2\2"+
		"\u0237\u0236\3\2\2\2\u0238\u023b\3\2\2\2\u0239\u023a\3\2\2\2\u0239\u0237"+
		"\3\2\2\2\u023a\u023c\3\2\2\2\u023b\u0239\3\2\2\2\u023c\u023d\7,\2\2\u023d"+
		"\u023e\7\61\2\2\u023e\u023f\3\2\2\2\u023f\u0240\b>\2\2\u0240|\3\2\2\2"+
		"\u0241\u0242\7\61\2\2\u0242\u0243\7\61\2\2\u0243\u0247\3\2\2\2\u0244\u0246"+
		"\n\b\2\2\u0245\u0244\3\2\2\2\u0246\u0249\3\2\2\2\u0247\u0245\3\2\2\2\u0247"+
		"\u0248\3\2\2\2\u0248\u024a\3\2\2\2\u0249\u0247\3\2\2\2\u024a\u024b\b?"+
		"\2\2\u024b~\3\2\2\2\u024c\u024e\t\t\2\2\u024d\u024f\t\n\2\2\u024e\u024d"+
		"\3\2\2\2\u024e\u024f\3\2\2\2\u024f\u0250\3\2\2\2\u0250\u0251\5\u0087D"+
		"\2\u0251\u0080\3\2\2\2\u0252\u0253\7^\2\2\u0253\u0268\t\13\2\2\u0254\u0259"+
		"\7^\2\2\u0255\u0257\t\f\2\2\u0256\u0255\3\2\2\2\u0256\u0257\3\2\2\2\u0257"+
		"\u0258\3\2\2\2\u0258\u025a\t\r\2\2\u0259\u0256\3\2\2\2\u0259\u025a\3\2"+
		"\2\2\u025a\u025b\3\2\2\2\u025b\u0268\t\r\2\2\u025c\u025e\7^\2\2\u025d"+
		"\u025f\7w\2\2\u025e\u025d\3\2\2\2\u025f\u0260\3\2\2\2\u0260\u025e\3\2"+
		"\2\2\u0260\u0261\3\2\2\2\u0261\u0262\3\2\2\2\u0262\u0263\5\u0085C\2\u0263"+
		"\u0264\5\u0085C\2\u0264\u0265\5\u0085C\2\u0265\u0266\5\u0085C\2\u0266"+
		"\u0268\3\2\2\2\u0267\u0252\3\2\2\2\u0267\u0254\3\2\2\2\u0267\u025c\3\2"+
		"\2\2\u0268\u0082\3\2\2\2\u0269\u0272\5\u0085C\2\u026a\u026d\5\u0085C\2"+
		"\u026b\u026d\7a\2\2\u026c\u026a\3\2\2\2\u026c\u026b\3\2\2\2\u026d\u0270"+
		"\3\2\2\2\u026e\u026c\3\2\2\2\u026e\u026f\3\2\2\2\u026f\u0271\3\2\2\2\u0270"+
		"\u026e\3\2\2\2\u0271\u0273\5\u0085C\2\u0272\u026e\3\2\2\2\u0272\u0273"+
		"\3\2\2\2\u0273\u0084\3\2\2\2\u0274\u0275\t\16\2\2\u0275\u0086\3\2\2\2"+
		"\u0276\u027e\t\17\2\2\u0277\u0279\t\20\2\2\u0278\u0277\3\2\2\2\u0279\u027c"+
		"\3\2\2\2\u027a\u0278\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027d\3\2\2\2\u027c"+
		"\u027a\3\2\2\2\u027d\u027f\t\17\2\2\u027e\u027a\3\2\2\2\u027e\u027f\3"+
		"\2\2\2\u027f\u0088\3\2\2\2\u0280\u0281\t\17\2\2\u0281\u008a\3\2\2\2\u0282"+
		"\u0284\t\21\2\2\u0283\u0282\3\2\2\2\u0284\u008c\3\2\2\2\u0285\u0287\t"+
		"\22\2\2\u0286\u0285\3\2\2\2\u0287\u008e\3\2\2\2\u0288\u028a\t\23\2\2\u0289"+
		"\u0288\3\2\2\2\u028a\u0090\3\2\2\2\u028b\u028e\5\u0097L\2\u028c\u028e"+
		"\t\20\2\2\u028d\u028b\3\2\2\2\u028d\u028c\3\2\2\2\u028e\u0092\3\2\2\2"+
		"\u028f\u0290\t\24\2\2\u0290\u0094\3\2\2\2\u0291\u0292\t\25\2\2\u0292\u0096"+
		"\3\2\2\2\u0293\u0294\t\26\2\2\u0294\u0098\3\2\2\2\u0295\u0298\5\u0097"+
		"L\2\u0296\u0298\t\17\2\2\u0297\u0295\3\2\2\2\u0297\u0296\3\2\2\2\u0298"+
		"\u009a\3\2\2\2\u0299\u029a\t\27\2\2\u029a\u029b\t\17\2\2\u029b\u029c\t"+
		"\17\2\2\u029c\u029d\t\17\2\2\u029d\u029e\7/\2\2\u029e\u029f\t\30\2\2\u029f"+
		"\u02a0\t\17\2\2\u02a0\u02a1\7/\2\2\u02a1\u02a2\t\f\2\2\u02a2\u02a3\t\17"+
		"\2\2\u02a3\u009c\3\2\2\2\'\2\u01af\u01b6\u01be\u01c3\u01c6\u01c8\u01cb"+
		"\u01d0\u01d4\u01d7\u01da\u01df\u01e2\u01e4\u01e9\u01f2\u01fa\u01fc\u022f"+
		"\u0239\u0247\u024e\u0256\u0259\u0260\u0267\u026c\u026e\u0272\u027a\u027e"+
		"\u0283\u0286\u0289\u028d\u0297\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}