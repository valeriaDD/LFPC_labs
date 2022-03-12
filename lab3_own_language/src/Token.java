public class Token {

    private String text;
    private TokenType type;

    public Token(TokenType type, String text) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String display() {
        return type + " " + text;
    }
}
