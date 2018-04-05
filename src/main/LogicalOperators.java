package main;

public enum LogicalOperators {
    AND('&'),
    OR('|'),
    NOT('!');

    public int asChar() {
        return asChar;
    }

    private final int asChar;

    LogicalOperators(int asChar) {
        this.asChar = asChar;
    }

}