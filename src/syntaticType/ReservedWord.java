package syntaticType;

public enum ReservedWord implements ISyntaticType {
	BOOL("bool"),
    BREAK("break"),
    DO("do"),
    CASE("case"),
    DOUBLE("double"),
    ELSE("else"),
    FLOAT("float"),
    FOR("for"),
    IF("if"),
    INT("int"),
    MAIN("main"),
    NULL("null"),
    RETURN("return"),
    STRING("string"),
    SWICTH("switch"),
    THEN("then"),
    VOID("void"),
    WHILE("while"),
    INCLUDE("include");

    private final String text;

    ReservedWord(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
