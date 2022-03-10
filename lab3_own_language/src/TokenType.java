public enum TokenType {

    NUMBER,     // integers or floats
    WORD,       // strings or chars [firs character is char] can include "_" symbol only
    BOOLEAN,    // true or false

    PLUS,       // +
    MINUS,      // -
    SLASH,      // /
    STAR,       // *
    EQ,         // =
    QUOTE,      // '
    MQUOTE,     // "
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
