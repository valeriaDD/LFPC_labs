public enum TokenType {

    NUMBER,     // integers or floats
    WORD,       // Identifier can include "_" symbol only
    BOOLEAN,    // true or false
    STRING,     // strings or chars [firs character is char]

    PLUS,       // +
    MINUS,      // -
    SLASH,      // /
    STAR,       // *
    EQ,         // =
    D_QUOTE,     // "
    GREATER,    // >
    SMALLER,    // <
    COMMA,      // ,
    DOT,        // .


    //Keywords:
    SAY,        // variable declaration
    STAMP,      // equivalent to print
    IF,
    ELSE,
    WHILE,
    FOR,
    MAIN,       // main function declaration
    FUNCTION,   // function declaration
    AND,        // logic and
    OR,         // logic or
    NOT,        // logic not
    RETURN,     // return

    LPAREN,     // (
    RPAREN,     // )
    LBRACE,     // {
    RBRACE,     // }
}
