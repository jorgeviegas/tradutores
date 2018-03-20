package main;

public enum ReservedWords {
    VOID("void"),
    MAIN("main"),
    INT("int"),
    IF("if"),
    ELSE("else"),
    RETURN("return");

    private final String text;

    ReservedWords(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}