package syntaticType;

public enum ArithmeticOperator implements IArithmetic {
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

    ArithmeticOperator(int asChar) {
        this.asChar = asChar;
    }
}
