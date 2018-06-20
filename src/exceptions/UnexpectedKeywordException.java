package exceptions;

import main.Lexeme;

public class UnexpectedKeywordException extends UnexpectedTokenException {
	public UnexpectedKeywordException(int line, int column, String expectedSymbol) {
		super("UnexpectedKeywordException", line, column, expectedSymbol);
	}
}
