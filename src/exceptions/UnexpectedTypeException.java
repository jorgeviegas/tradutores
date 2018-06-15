package exceptions;

import main.Lexeme;

public class UnexpectedTypeException extends UnexpectedTokenException {

	public UnexpectedTypeException(int line, int column, Lexeme lexeme, String expectedSymbol) {
		super("UnexpectedTypeException", line, column, lexeme, expectedSymbol);
	}

}
