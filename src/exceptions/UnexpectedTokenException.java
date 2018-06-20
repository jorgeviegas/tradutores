package exceptions;

import main.Lexeme;

public class UnexpectedTokenException extends Exception {
	public String error;
	public String expectedSymbol;
	public int line;
	public int column;
	
	public UnexpectedTokenException(String error, int line, int column, String expectedSymbol) {
		super(error);
		
		this.line = line;
		this.column = column;
		this.error = error;
		this.expectedSymbol = expectedSymbol;
	}
	
	public UnexpectedTokenException(int line, int column, String expectedSymbol) {
		this("UnexpectedException", line, column, expectedSymbol);
	}
}
