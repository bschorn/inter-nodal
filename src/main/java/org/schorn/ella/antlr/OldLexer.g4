

lexer grammar SpecLexer;

// Sections
TEMPLATES:              'templates';
OBJECT_TYPES:           'object_types';
ARRAY_TYPES:            'array_types';
VALUE_TYPES:            'value_types';
FIELD_TYPES:            'field_types';

// Keywords
JAVA:                   'java';
CONSTRAINTS:            'constraints';

// Operators
LEFT_ARROW:             '<-';
COLON:                  ':';
COLONCOLON:             '::';

DECIMAL_LITERAL:        ('0' | [1-9] (Digits? | '_'+ Digits)) [1L]?;
FLOAT_LITERAL:          (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
             |          Digits (ExponentPart [fFdD]? | [fFdD])
             ;
BOOL_LITERAL:           'true'
            |           'false'
            ;
CHAR_LITERAL:           '\'' (~['\\\r\n] | EscapeSequence) '\'';
STRING_LITERAL:         '"' (~["\\\r\n] | EscapeSequence)* '"';
NULL_LITERAL:           'null';


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

WS:                     [ \t\r\n\u000c]+  -> channel(HIDDEN);
COMMENT:                '/*' .*? '*/'   -> channel(HIDDEN);
LINE_COMMENT:           '//' ~[\r\n]*   -> channel(HIDDEN);

DATA_TYPE_NAME:         UpperLetter+;
BRANCH_TYPE_NAME:       UpperLetter LetterOrDigit*;
LEAF_TYPE_NAME:         LowerLetter LetterOrDigit*;

// Members
TEMPLATE_MEMBER
    : Tilde UpperLetter LetterOrDigit* Tilde
    ;
VALUE_TYPE_MEMBER
    : ValueTypePrefix LowerLetter LetterOrDigit*
    | LowerLetter LetterOrDigit*
    ;
OBJECT_TYPE_MEMBER
    : ObjectTypePrefix UpperLetter LetterOrDigit*
    ;
ARRAY_TYPE_MEMBER
    : ArrayTypePrefix UpperLetter LetterOrDigit*
    | UpperLetter LetterOrDigit*
    ;

// Modifiers
BOND_TYPE_TAG:          'BondType' DOT;
DATA_ROLE_TAG:          'DataRole' DOT;

BOND_TYPE_ENUM:         UpperLetter+;
DATA_ROLE_ENUM:         UpperLetter+;

IDENTIFIER:             Letter LetterOrDigit*;

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

fragment LetterOrDigit
    : Letter
    | [0-9]
    ;
fragment LowerLetter
    : [a-z]
    ;
fragment Letter
    : [a-zA-Z$_]
    ;

fragment UpperLetter
    : [A-Z]
    ;

fragment ValueTypePrefix: '$';
fragment ObjectTypePrefix: '#';
fragment ArrayTypePrefix: '@';
fragment Tilde: '~';


