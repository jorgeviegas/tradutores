package exceptions;

import main.Lexeme;

public class UnexpectedSymbolException extends UnexpectedTokenException {
	public UnexpectedSymbolException(int line, int column, String expectedSymbol) {
		super("UnexpectedSymbolException", line, column, expectedSymbol);
	}
}
