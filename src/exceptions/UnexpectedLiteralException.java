package exceptions;

public class UnexpectedLiteralException extends UnexpectedTokenException {

	public UnexpectedLiteralException(int line, int column, String expectedSymbol) {
		super("UnexpectedLiteralException", line, column, expectedSymbol);
	}

}
