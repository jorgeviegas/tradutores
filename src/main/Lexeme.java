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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
    
    public TokenType getTokenType() {
        return this.tokenType;
    }
    
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "[" + tokenType.toString() + "," + token + "]";
    }
}
