package token

enum class TokenType {
    // Single-character tokens.
    LEFT_PARENTHESES,
    RIGHT_PARENTHESES,
    LEFT_BRACE,
    RIGHT_BRACE,
    COMMA,
    DOT,
    MINUS,
    PLUS,
    SEMICOLON,
    SLASH,
    STAR,

    // One or two character tokens.
    NEGATION,
    NEGATION_EQUAL,
    EQUAL,
    EQUAL_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,

    // Literals.
    IDENTIFIER,
    STRING,
    NUMBER,

    // Keywords.
    AND,
    CLASS,
    ELSE,
    FALSE,
    FUN,
    FOR,
    IF,
    NULL,
    OR,

    PRINT,
    RETURN,
    TRUE,
    VAR,
    WHILE,

    EOF
}