package exceptions;

import main.Lexeme;

public class UnexpectedSymbolException extends UnexpectedTokenException {
	public UnexpectedSymbolException(int line, int column, Lexeme lexeme, String expectedSymbol) {
		super("UnexpectedSymbolException", line, column, lexeme, expectedSymbol);
	}
}
