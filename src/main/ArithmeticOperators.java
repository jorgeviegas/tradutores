package main;

public enum ArithmeticOperators {
    SUM('+'),
    SUB('-'),
    DIV('/'),
	MLT('*'),
	PWR('^');

    public int asChar() {
        return asChar;
    }

    private final int asChar;

    ArithmeticOperators(int asChar) {
        this.asChar = asChar;
    }

}
