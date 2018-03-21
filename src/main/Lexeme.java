package main;

public class Lexeme {

    public TokenType tokenType;

    public String token;

    public Lexeme(TokenType tokenType, String token) {
        this.tokenType = tokenType;
        this.token = token;
    }
}
