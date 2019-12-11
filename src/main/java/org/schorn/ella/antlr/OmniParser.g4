

parser grammar OmniParser;

options { tokenVocab=OmniLexer; }


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
    : attrCreation
    ;

attrCreation
    : attrFlavor ( '.' addTypeToAttr)+? 
    ;

addTypeToAttr
    : 'add' '(' memberType varNameRef  ')'
    ;

typeFlavor
    : 'OtherTypes'
    | 'LeafTypes'
    | 'BranchTypes'
    ;

attrFlavor
    : members
    ;

memberType
    : memberBaseType
    | memberBaseTypeArray
    ;

memberBaseTypeArray
    : memberBaseType arrayIndicator
    ;

memberBaseType
    : 'String'
    | 'Object'
    | 'DataType'
    | 'BondType'
    | typeNameRef
    ;

members: MEMBERS;
arrayIndicator: ARRAY_INDICATOR;
typeNameRef: TYPE_NAME_REF;
typeNameDef: TYPE_NAME_DEF;
varNameRef: VAR_NAME_REF;



