package main;

public enum SpecialCharacters {
    EQUAL('='),
    L_PAREN('('),
    R_PAREN(')'),
    L_BRACKET('{'),
    R_BRACKET('}'),
    COMMA(','),
    SEMICOLON(';');

    public int asChar() {
        return asChar;
    }

    private final int asChar;

    SpecialCharacters(int asChar) {
        this.asChar = asChar;
    }

}