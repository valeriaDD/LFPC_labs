import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lexer {

    private final String input;
    private final int length;
    private final List<Token> tokens;
    private final String CHAR_OPERATION_REPRESENTATION = "+-/*(){}=\"><,.";
    private final TokenType[] TOKEN_OPERATORS = {
            TokenType.PLUS, TokenType.MINUS,
            TokenType.STAR, TokenType.SLASH,
            TokenType.LPAREN, TokenType.RPAREN,
            TokenType.LBRACE, TokenType.RBRACE,
            TokenType.EQ, TokenType.D_QUOTE,
            TokenType.GREATER, TokenType.SMALLER,
            TokenType.COMMA, TokenType.DOT
    };
    private int pos;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
    }

    public List<Token> tokenizer() {
        while (pos < length) {
            final char current = peek(0);

            if (Character.isDigit(current))
                tokenizeNumber();
            else if (Character.isLetter(current))
                tokenizeWord();
            else if (CHAR_OPERATION_REPRESENTATION.indexOf(current) != -1)
                tokenizeOperator();
            else
                next();
        }
        return tokens;
    }

    private void tokenizeWord() {
        StringBuffer buffer = new StringBuffer();
        char current = peek(0);

        while (true) {
            if (!Character.isLetterOrDigit(current)
                    && (current != '_'))
                break;
            else
                buffer.append(current);
            current = next();
        }

        String word = buffer.toString();
        switch (word) {
            case "say":
                addToken(TokenType.SAY, word);
                break;
            case "stamp":
                addToken(TokenType.STAMP, word);
                break; // my print
            case "true":
            case "false":
                addToken(TokenType.BOOLEAN, word);
                break;
            case "if":
                addToken(TokenType.IF, word);
                break;
            case "else":
                addToken(TokenType.ELSE, word);
                break;
            case "while":
                addToken(TokenType.WHILE, word);
                break;
            case "main":
                addToken(TokenType.MAIN, word);
                break;
            case "function":
                addToken(TokenType.FUNCTION, word);
                break;
            case "and":
                addToken(TokenType.AND, word);
                break;
            case "or":
                addToken(TokenType.OR, word);
                break;
            case "not":
                addToken(TokenType.NOT, word);
                break;
            case "return":
                addToken(TokenType.RETURN, word);
            case "for":
                addToken(TokenType.FOR, word);
            default:
                addToken(TokenType.WORD, buffer.toString());
                break;
        }
    }

    private void tokenizeNumber() {
        StringBuffer buffer = new StringBuffer();
        char current = peek(0);

        while (true) {
            if (current == '.')
                if (buffer.indexOf(".") != -1)
                    throw new RuntimeException("(Invalid float nr)");
                else
                    buffer.append(".");
            else if (!Character.isDigit(current))
                break;
            else
                buffer.append(current);

            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private void tokenizeOperator() {
        int position = CHAR_OPERATION_REPRESENTATION.indexOf(peek(0));
        String representation = Character.toString(CHAR_OPERATION_REPRESENTATION.charAt(position));

        if (representation.equals("\"")) {
            next();
            StringBuffer buffer = new StringBuffer();
            char current = peek(0);

            while (!Objects.equals(current, '\"')) {
                buffer.append(current);
                current = next();
            }
            addToken(TokenType.STRING, buffer.toString());
        } else
            addToken(TOKEN_OPERATORS[position], representation);
        next();
    }

    private char peek(int relativePosition) {
        // if relative position = 0 we peek current position
        // if 1 -> the next position
        int position = pos + relativePosition;
        if (position >= length)
            return '\0';
        return input.charAt(position);
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text));
    }
}

