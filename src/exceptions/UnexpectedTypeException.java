package exceptions;

import main.Lexeme;

public class UnexpectedTypeException extends UnexpectedTokenException {

	public UnexpectedTypeException(int line, int column, String expectedSymbol) {
		super("UnexpectedTypeException", line, column, expectedSymbol);
	}

}
