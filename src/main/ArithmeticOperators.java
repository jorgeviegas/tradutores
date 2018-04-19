package main;

public enum ArithmeticOperators {
    SUM('+'),
    SUB('-'),
    DIV('/'),
	MLT('*'),
	PWR('^'),
	MOD('%');

    public int asChar() {
        return asChar;
    }

    private final int asChar;

    ArithmeticOperators(int asChar) {
        this.asChar = asChar;
    }
}