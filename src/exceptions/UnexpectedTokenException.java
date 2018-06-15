package exceptions;

import main.Lexeme;

public class UnexpectedTokenException extends Exception {
	public String error;
	public Lexeme lexeme;
	public String expectedSymbol;
	public int line;
	public int column;
	
	public UnexpectedTokenException(String error, int line, int column, Lexeme lexeme, String expectedSymbol) {
		super(error);
		
		if (error == null || error.isEmpty() ) {
			error = "UnexpectedException";
		}
		
		this.line = line;
		this.column = column;
		this.error = error;
		this.lexeme = lexeme;
		this.expectedSymbol = expectedSymbol;
	}
}
