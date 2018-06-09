package syntaticType;

public enum Keyword implements IKeyword {
    DEF("def"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return"),
    BREAK("break"),
    CONTINUE("continue"),
    INT("int"),
	BOOL("bool"),
    VOID("void"),
    TRUE("true"),
    FALSE("false");

    private final String text;

    Keyword(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
