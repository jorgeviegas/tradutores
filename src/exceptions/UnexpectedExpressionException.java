package exceptions;

import main.Lexeme;

public class UnexpectedExpressionException extends UnexpectedTokenException {
	public UnexpectedExpressionException(int line, int column, Lexeme lexeme, String expectedSymbol) {
		super("UnexpectedExpressionException", line, column, lexeme, expectedSymbol);
	}

}
