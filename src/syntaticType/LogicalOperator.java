package syntaticType;

public enum LogicalOperator implements ILogical {
	AND('&'),
    OR('|'),
    NOT('!'),
    EQUALS('='),
    GREATER('>'),
    LESS('<');

    public int asChar() {
        return asChar;
    }

    public String toString() {
        return Character.toString((char) asChar);
    }

    private final int asChar;

    LogicalOperator(int asChar) {
        this.asChar = asChar;
    }
}
