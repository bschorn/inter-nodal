

lexer grammar SpecLexer;

// Sections
ATTRIBUTES:             'Attributes';
FIELD_TYPES:            'FieldTypes';
VALUE_TYPES:            'ValueTypes';
FLAGS:                  'Flags';
FRAGMENTS:              'Fragments';
OBJECT_TYPES:           'ObjectTypes';
BASE_TYPES:             'BaseTypes';
ARRAY_TYPES:            'ArrayTypes';
MEMBERS:                'Members';
PARENTS:                'Parents';
DATA_TYPE:              'DataType';
OBJECT_CATEGORY:        'ObjectCategory';
OBJECT_PURPOSE:         'ObjectPurpose';
OBJECT_LEVEL:           'ObjectLevel';
FIELD_TYPE:             'FieldType';
VALUE_TYPE:             'ValueType';
VALUE_FLAG:             'ValueFlag';
FRAGMENT:               'Fragment';
OBJECT_TYPE:            'ObjectType';
BASE_TYPE:              'BaseType';
ARRAY_TYPE:             'ArrayType';
MEMBER:                 'Member';
PARENT:                 'Parent';
BOND_TYPE:              'BondType';
CMD_ACTION_READ:        'Read';
CMD_OBJECT_FILE:        'File';
DEFINE:                 'def';
ADD:                    'add';
MAX:                    'MAX';
MIN:                    'MIN';


TYPE_NAME_DEF:          '"' TYPE_NAME_REF '"';
ENUM_ID:                UpperLetter (UpperLetterOrUnder)*;
TYPE_NAME_REF:          Letter (LetterOrDigitOrUnder)*;

              
TEXT_LENGTH:            Digits;
DECIMAL_LITERAL:        ('0' | [1-9] (Digits? | '_'+ Digits)) [1L]?;
FLOAT_LITERAL:          (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
             |          Digits (ExponentPart [fFdD]? | [fFdD])
             ;
//BOOL_LITERAL:           'true'
//            |           'false'
//            ;
CHAR_LITERAL:           '\'' (~['\\\r\n] | EscapeSequence) '\'';
TEXT_PATTERN:           '"' '^' .+? '$' '"';
STRING_LITERAL:         '"' (~["\\\r\n] | EscapeSequence)* '"';
NULL_LITERAL:           'null';


OPHAT:                  '^';
OPDOLLAR:               '$';
OPTILDE:                '~';
OPAT:                   '@';
OPHASH:                 '#';
OPPIPE:                 '|';
OPDBLQUOTE:             '"';

// Containers
LPAREN:                 '(';
RPAREN:                 ')';
LSBRACKET:              '[';
RSBRACKET:              ']';
LABRACKET:              '<';
RABRACKET:              '>';
LBRACE:                 '{';
RBRACE:                 '}';

// Separators
COMMA:                  ',';
DOT:                    '.';
COLON:                  ':';
SEMI:                   ';';   

WS:                     [ \t\r\n\u000c]+  -> channel(HIDDEN);
COMMENT:                '/*' .*? '*/'   -> channel(HIDDEN);
LINE_COMMENT:           '//' ~[\r\n]*   -> channel(HIDDEN);

fragment ExponentPart
    : [eE] [+-]? Digits
    ;

fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u' + HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigits
    : HexDigit ((HexDigit | '_')* HexDigit)?
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;

fragment Digits
    : [0-9] ([0-9_]* [0-9])?
    ;

fragment Digit
    : [0-9]
    ;

fragment LowerLetterOrUnder: [a-z] | '_';
fragment UpperLetterOrUnder: [A-Z] | '_';
fragment LetterOrUnder: [a-zA-Z] | '_';
fragment LetterOrDigitOrUnder: Letter | [0-9] | '_';
fragment LowerLetter: [a-z];
fragment UpperLetter: [A-Z];
fragment Letter: [a-zA-Z];
fragment LetterOrDigit: Letter | [0-9];




fragment DateTime
    : [1-2] [0-9] [0-9] [0-9] '-' [0-1] [0-9] '-' [0-3] [0-9]
    ;


/*
STRING
    : '"' (ESC | ~ ["\\])* '"'
    ;
fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;
fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;
fragment HEX
    : [0-9a-fA-F]
    ;
NUMBER
    : '-'? INT '.' [0-9] + EXP? | '-'? INT EXP | '-'? INT
    ;
fragment INT
    : '0' | [1-9] [0-9]*
    ;
fragment EXP
    : [Ee] [+\-]? INT
    ;
*/