package net.elva.lang.tokens

enum class TokenType{

    // Single char symbols
    LPAREN, RPAREN, LBRACE, RBRACE, COMMA, DOT, MINUS, PLUS, SLASH, EQUAL, COLON, 
    
    PIPE, // The | in type A = B | C

    // Multi char symbols
    ARROW, // |->

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    FN, PURPOSE, MSG,

    EOF
}