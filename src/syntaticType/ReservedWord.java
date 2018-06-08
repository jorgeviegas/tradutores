package syntaticType;

public enum ReservedWord implements IReservedWord {
    DEF("def"),
	BOOL("bool"),
    BREAK("break"),
    ELSE("else"),
    IF("if"),
    INT("int"),
    RETURN("return"),
    VOID("void"),
    WHILE("while"),
    TRUE("true"),
    FALSE("false"),
    CONTINUE("continue");

    private final String text;

    ReservedWord(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
