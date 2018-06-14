package exceptions;

import main.Lexeme;

public class UnexpectedSymbolException extends Exception {
	public String error;
	public Lexeme lexeme;
	public String expectedSymbol;
	public int line;
	public int column;
	
	public UnexpectedSymbolException(String error, int line, int column, Lexeme lexeme, String expectedSymbol) {
		super(error);
		
		this.line = line;
		this.column = column;
		this.error = error;
		this.lexeme = lexeme;
		this.expectedSymbol = expectedSymbol;
	}
}
