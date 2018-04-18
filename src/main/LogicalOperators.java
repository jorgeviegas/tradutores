package main;

public enum LogicalOperators {
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

    LogicalOperators(int asChar) {
        this.asChar = asChar;
    }

}