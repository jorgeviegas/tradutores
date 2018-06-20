package exceptions;

public class UnexpectedStatementException extends UnexpectedTokenException {

	public UnexpectedStatementException(int line, int column, String expectedSymbol) {
		super("UnexpectedStatementException", line, column, expectedSymbol);
	}
}
