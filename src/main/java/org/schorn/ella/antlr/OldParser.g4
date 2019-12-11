

parser grammar SpecParser;

options { tokenVocab=SpecLexer; }

def
    : parts* EOF
    ;

parts
    : objectTypesSection
    | objectTypes
    | templatesSection
    | templates
    | valueTypesSection
    | valueTypes
    | fieldTypesSection
    | fieldTypes
    ;

objectTypesSection
    : LSBRACKET OBJECT_TYPES RSBRACKET
    ;

valueTypesSection
    : LSBRACKET VALUE_TYPES RSBRACKET
    ;

fieldTypesSection
    : LSBRACKET FIELD_TYPES RSBRACKET
    ;

templatesSection
    : LSBRACKET TEMPLATES RSBRACKET
    ;

objectTypes
    : (objectType)+
    ;

templates
    : (template)+
    ;

valueTypes
    : (valueType)+
    ;

fieldTypes
    : (fieldType)+
    ;

objectType
    : objectTypeName (COLON objectTypeName)* LBRACE objectTypeMembers* RBRACE (objectTypeModifier)*
    ;

template
    : templateName LBRACE templateMembers* RBRACE
    | literal LBRACE literal* RBRACE
    ;

valueType
    : valueTypeName COLON fieldTypeName
    | valueTypeName
    ;

fieldType
    : fieldTypeName COLON dataTypeName LPAREN literal+ RPAREN
    | fieldTypeName COLON dataTypeName
    ;

objectTypeMembers
    : valueTypeMember memberModification
    | valueTypeMember
    | objectTypeMember memberModification
    | objectTypeMember
    | arrayTypeMember memberModification
    | arrayTypeMember
    | templateMember memberModification
    | templateMember
    ;

templateMembers
    : valueTypeMember memberModification
    | valueTypeMember
    | objectTypeMember memberModification
    | objectTypeMember
    | arrayTypeMember memberModification
    | arrayTypeMember
    | templateMember memberModification
    | templateMember
    ;

objectTypeModifier
    : LEFT_ARROW (dataRole)
    ;

memberModification
    : LEFT_ARROW (bondType) LPAREN foreignKey RPAREN
    | LEFT_ARROW (bondType)
    ;

bondType
    : BOND_TYPE_TAG BOND_TYPE_ENUM
    ;

dataRole
    : DATA_ROLE_TAG DATA_ROLE_ENUM
    ;

dataTypeName: DATA_TYPE_NAME;
objectTypeName: BRANCH_TYPE_NAME;
valueTypeName: LEAF_TYPE_NAME;
fieldTypeName: LEAF_TYPE_NAME;
templateName: BRANCH_TYPE_NAME;

templateMember: TEMPLATE_MEMBER;
objectTypeMember: OBJECT_TYPE_MEMBER;
valueTypeMember: VALUE_TYPE_MEMBER;
arrayTypeMember: ARRAY_TYPE_MEMBER;

foreignKey: objectTypeName DOT valueTypeName;

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
    

