package syntaticType;

public enum ReservedWord implements IReservedWord {
    FOR("for"),
    CALLOUT("callout"),
    CLASS("class"),
    INTERFACE("interface"),
    EXTENDS("extends"),
    IMPLEMENTS("implements"),
    NEW("new"),
    THIS("this"),
    STRING("string"),
    FLOAT("float"),
    DOUBLE("double"),
    NULL("null");

    private final String text;

    ReservedWord(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}