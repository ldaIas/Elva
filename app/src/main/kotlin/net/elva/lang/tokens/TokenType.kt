package net.elva.lang.tokens

enum class TokenType{

    // Single char symbols
    LPAREN, RPAREN, LBRACE, RBRACE, COMMA, DOT, MINUS, PLUS, SLASH, EQUAL, COLON,

    // Type constraint notation ("<:")
    TYPE_CONST,
    
    // The | in "type A = B | C"
    PIPE, 

    // |->
    FN_ARROW, 

    /** Match cases. For example:
     *  match expr:
     *      case1 -> body
     *      case2 -> body
     *      ...
     *      _ -> body
     */
    MATCH, CASE_ARROW,

    // Literals
    IDENTIFIER, STRING, NUMBER, TRUE, FALSE,

    // Keywords
    FN, MSG,

    RECORD, TYPEDEF,

    EOF
}