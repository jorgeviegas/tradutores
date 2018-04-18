package main;

public class Lexeme {

    public TokenType tokenType;

    public String token;

    public int lineNumber;

    public Lexeme(TokenType tokenType, String token, int lineNumber) {
        this.tokenType = tokenType;
        this.token = token;
        this.lineNumber = lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public String toString() {
        return "[" + tokenType.toString() + "," + token + "]";
    }
}
