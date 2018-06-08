package syntaticType;

public enum SpecialCharacter implements ISpecialCharacter {
    EQUAL('='),
    L_PAREN('('),
    R_PAREN(')'),
    L_BRACKET('['),
    R_BRACKET(']'),
    L_BRACE('{'),
    R_BRACE('}'),
    COMMA(','),
    SEMICOLON(';'),
    SHARP('#');
	

    public int asChar() {
        return asChar;
    }

    private final int asChar;

    SpecialCharacter(int asChar) {
        this.asChar = asChar;
    }
}
