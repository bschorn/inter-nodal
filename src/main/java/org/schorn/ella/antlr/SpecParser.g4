

parser grammar SpecParser;

options { tokenVocab=SpecLexer; }


def: typeCreation* EOF;

typeCreation
    : typeFlavor ( '.' defineType )+? ';'
    ;

defineType
    : 'def' '(' typeNameDef ( typeAttributes )*? ')'
    ;

typeAttributes
    : ',' typeAttribute
    ;

typeAttribute
    : enumQualifier '.' enumID '(' parameters ')'
    | enumQualifier '.' enumID
    | typeQualifier '.' typeNameRef
    | attrCreation
    ;

parameters
    : parameter (',' parameter )*
    ;

parameter
    : number
    | datetime
    | pattern
    | typeNameRef '.' typeNameRef
    | command
    | listOfValues
    ;

command
    : cmd '(' cmdPath ')'
    ;

cmdPath
    : STRING_LITERAL
    ;

listOfValues
    : '[' listValue (',' listValue)*? ']'
    ;

listValue
    : STRING_LITERAL
    ;

attrCreation
    : attrFlavor ( '.' addTypeToAttr)+? 
    ;

addTypeToAttr
    : 'add' '(' typeQualifier '.' typeNameRef ( ')' | ',' attributeAttributes ')' )
    | 'add' '(' flagQualifier '.' enumID ')'
    ;

attributeAttributes
    : attributeAttribute ( ',' attributeAttribute )*?
    ;

attributeAttribute
    : enumQualifier '.' enumID '(' parameters ')'
    | enumQualifier '.' enumID
    | typeQualifier '.' typeNameRef
    ;

typeFlavor
    : 'FieldTypes'
    | 'ValueTypes'
    | 'Fragments'
    | 'Templates'
    | 'ObjectTypes'
    | 'ArrayTypes'
    ;

attrFlavor
    : 'Members'
    | 'Parents'
    | 'Attributes'
    ;

typeQualifier
    : 'FieldType'
    | 'ValueType'
    | 'Fragment'
    | 'Template'
    | 'ObjectType'
    | 'ArrayType'
    ;

flagQualifier
    : 'DataCategory'
    | 'DataPurpose'
    | 'DataLevel'
    ;

enumQualifier
    : 'DataType'
    | 'BondType'
    ;
cmdAction
    : CMD_ACTION_READ
    ;
cmdObject
    : CMD_OBJECT_FILE
    ;

cmd: cmdAction '.' cmdObject;
enumID: ENUM_ID;
typeNameRef: TYPE_NAME_REF;
typeNameDef: TYPE_NAME_DEF;
pattern: TEXT_PATTERN;
number: FLOAT_LITERAL;
datetime: STRING_LITERAL;


    
/*
literal
    : integerLiteral
    | floatLiteral
    | CHAR_LITERAL
    | STRING_LITERAL
    | BOOL_LITERAL
    | NULL_LITERAL
    ;

integerLiteral
    : DECIMAL_LITERAL
    ;

floatLiteral
    : FLOAT_LITERAL
    ;
*/ 


