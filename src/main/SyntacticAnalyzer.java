package main;

import syntaticType.IReservedWord;
import syntaticType.ISyntaticType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

//import com.sun.istack.internal.Nullable;

import exceptions.UnexpectedSymbolException;

public class SyntacticAnalyzer {
	private Lexeme currentLexeme;
	private String currentToken;
	private int currentIndex;
	private LinkedList<Lexeme> lexemesList;
	ISyntaticType exTokenType;

	public SyntacticAnalyzer(LinkedList<Lexeme> lexemesList) {
		this.lexemesList = lexemesList;
		this.currentIndex = 0;
		this.currentLexeme = lexemesList.get(currentIndex);
		this.currentToken = this.currentLexeme.getToken();
	}

	public void analyze() throws UnexpectedSymbolException {

		//for (int i = 0;i < lexemesList.size();i++)
		//{
			this.program();
			this.advance();
		//}
	}
	
	private void consume(TokenType type) {
		if (this.currentLexeme.tokenType == type) {
			this.advance();
		} else { 
			error(this.currentLexeme);
		} 
	}
	
	private void advance() {
		currentIndex++;
		currentLexeme = lexemesList.get(currentIndex);
		currentToken = currentLexeme.getToken();
	}

	private void program() throws UnexpectedSymbolException {
		this.var();
		this.func();
	}
	
	private void var() 
			throws UnexpectedSymbolException {
		this.type();
		this.consume(TokenType.IDENTIFIER);
			
		if (this.currentToken == "[") {
			this.consume(TokenType.SYMBOL);
			this.consume(TokenType.DEC);
			
			if (this.currentToken == "]") {
				this.consume(TokenType.SYMBOL);
			} else {
				int line = this.currentLexeme.getLineNumber();
				int column = determineErrorColumn(this.currentLexeme);
				throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, "]");
			}
		} else if (this.currentToken == ";") {
			this.consume(TokenType.SYMBOL);
		} else {
			int line = this.currentLexeme.getLineNumber();
			int column = determineErrorColumn(this.currentLexeme);
			throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
		}		
	}
	
	private void func()
			throws UnexpectedSymbolException {
		if (this.currentToken == "def") {
			this.consume(TokenType.KEYWORD);
		}
		
		this.type();
		this.consume(TokenType.IDENTIFIER);
		
		if (this.currentToken == "(") {
			this.consume(TokenType.SYMBOL);
		} else {
			int line = this.currentLexeme.getLineNumber();
			int column = determineErrorColumn(this.currentLexeme);
			throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, "(");
		}
		
		this.paramList();
		
		if (this.currentToken == ")") {
			this.consume(TokenType.SYMBOL);
		} else {
			int line = this.currentLexeme.getLineNumber();
			int column = determineErrorColumn(this.currentLexeme);
			throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ")");
		}
		
		this.block();
	}
	
	private void paramList() {
		this.type();
		this.consume(TokenType.IDENTIFIER);
		
		while (this.currentToken == ",") {
			this.consume(TokenType.SYMBOL);
			this.consume(TokenType.IDENTIFIER);
		}
	}
	
	private void block() throws UnexpectedSymbolException {
		while (this.currentToken == "int" ||
			   this.currentToken == "bool" ||
			   this.currentToken == "void") {
			this.var();
		}
		
		while (this.currentLexeme.tokenType == TokenType.IDENTIFIER ||
			   this.currentToken == "if" ||
			   this.currentToken == "while" ||
			   this.currentToken == "return" ||
			   this.currentToken == "break" ||
			   this.currentToken == "return") {
			this.stmt();
		}
	}
	
	private void stmt()
			throws UnexpectedSymbolException {
		if (this.isLoc()) {
			this.loc();
			
			if (this.currentToken == "=") {
				this.consume(TokenType.SYMBOL);
			}
			else {
				int line = this.currentLexeme.getLineNumber();
				int column = determineErrorColumn(this.currentLexeme);
				throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, "=");
			}
			
			this.expr();
			
			if (this.currentToken == ";") {
				this.consume(TokenType.SYMBOL);
			} else {
				int line = this.currentLexeme.getLineNumber();
				int column = determineErrorColumn(this.currentLexeme);
				throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
			}
		} else if (this.isFuncCall()) {
			this.funcCall();
			
			if (this.currentToken == ";") {
				this.consume(TokenType.SYMBOL);
			} else {
				int line = this.currentLexeme.getLineNumber();
				int column = determineErrorColumn(this.currentLexeme);
				throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
			}
		} else {
			switch (this.currentToken) {
				case "if":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken == "(") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, "(");
					}
					
					this.expr();
					
					if (this.currentToken == ")") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ")");
					}
					
					this.block();
					
					if (this.currentToken == "else") {
						this.consume(TokenType.KEYWORD);
						this.block();
					}
					
					break;
				
				case "while":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken == "(") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, "(");
					}
					
					this.expr();
					
					if (this.currentToken == ")") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ")");
					}
					
					this.block();
					
					break;
					
				case "return":
					this.consume(TokenType.KEYWORD);
					
					if (this.isExpr()) {
						this.expr();
					}
					
					if (this.currentToken == ";") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
					}
					
					break;
					
				case "break":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken == ";") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
					}
					
					break;
					
				case "continue":
                    this.consume(TokenType.KEYWORD);
					
					if (this.currentToken == ";") {
						this.consume(TokenType.SYMBOL);
					} else {
						int line = this.currentLexeme.getLineNumber();
						int column = determineErrorColumn(this.currentLexeme);
						throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ";");
					}
					
					break;
					
				default:
					//ERROR
					break;
			}
		}
	}
	
	private void expr()
	        throws UnexpectedSymbolException {
		if (this.isUNOP()) {
			this.consume(TokenType.SYMBOL);
			this.expr();
		} else if (this.isLoc()) {
			this.loc();
		} else if (this.isFuncCall()) {
			this.funcCall();
		} else if (this.isLit()) {
			this.lit();
		} else if (this.currentToken == "(") {
			this.consume(TokenType.SYMBOL);
			this.expr();
			
			if (this.currentToken == ")") {
				this.consume(TokenType.SYMBOL);
			} else {
				int line = this.currentLexeme.getLineNumber();
				int column = determineErrorColumn(this.currentLexeme);
				throw new UnexpectedSymbolException("UnexpectedSymbolException", line, column, this.currentLexeme, ")");
			}
		} else if (this.isExpr()) {
			this.expr();
			
			if (this.isBINOP()) {
				this.consume(TokenType.SYMBOL);
			} else {
				//ERROR
			}
			
			this.expr();
		} else {
			//ERROR
		}
	}
	
	private void type() {
		if (this.currentToken == "int" ||
			 this.currentToken == "bool" ||
			 this.currentToken == "void") {
			this.consume(TokenType.KEYWORD);						
		} else {
			this.error(this.currentLexeme);
			//OU
			//throw new TypeException()
		}
	}
	
	private void loc() {
		try {
		if (this.currentLexeme.tokenType == TokenType.IDENTIFIER) {
			this.consume(TokenType.IDENTIFIER);
		} else {
			//ERROR
		}
		
		if (this.currentToken == "[") {
			this.consume(TokenType.SYMBOL);
			this.expr();
			
			if (this.currentToken == "]") {
				this.consume(TokenType.SYMBOL);
			} else {
				//ERROR
			}
		} else {
			//ERROR
		}
		} catch (UnexpectedSymbolException e) {
			System.out.println("The \""+e.lexeme.token+"\" symbol is unexpected.");
			
			if (e.expectedSymbol != null && !e.expectedSymbol.isEmpty()) {
				System.out.println("The expected symbol is \""+e.expectedSymbol+"\"");
			}
		}
	}
	
	private void funcCall() throws UnexpectedSymbolException {
		if (this.currentLexeme.tokenType == TokenType.IDENTIFIER) {
			this.consume(TokenType.IDENTIFIER);
		} else {
			//ERROR
		}
		
		if (this.currentToken == "(") {
			this.consume(TokenType.SYMBOL);
			
			if (this.isExpr()) {
				this.argList();
			}
			
			if (this.currentToken == ")") {
				this.consume(TokenType.SYMBOL);
			} else {
				//ERROR
			}
		} else {
			//ERROR
		}
	}
	
	private void argList() throws UnexpectedSymbolException {
		while (this.isExpr()) {
			this.expr();
			
			if (this.currentToken == ",") {
				this.consume(TokenType.SYMBOL);
			} else if (!this.isExpr()) {
				//ERROR
			}
		}
	}
	
	private void lit() {
		switch (this.currentLexeme.tokenType) {
		case DEC:
			this.consume(TokenType.DEC);
			break;
			
		case HEX:
			this.consume(TokenType.HEX);
			break;
			
		case STR:
			this.consume(TokenType.STR);
			break;
			
		case KEYWORD:
			if (this.currentToken == "true" || this.currentToken == "false") {
				this.consume(TokenType.KEYWORD);
			} else {
				//ERROR
			}
			
			break;
			
		default:
			//ERROR
			break;
		}
	}
	
	private boolean isLoc() {
		return (this.lexemesList.get(this.currentIndex + 1).token == "[" &&
				this.currentLexeme.tokenType == TokenType.IDENTIFIER);
	}
	
	private boolean isFuncCall() {
		return (this.lexemesList.get(this.currentIndex + 1).token == "(" &&
				this.currentLexeme.tokenType == TokenType.IDENTIFIER);
	}
	
	private boolean isLit() {
		return (this.currentLexeme.tokenType == TokenType.DEC ||
				this.currentLexeme.tokenType == TokenType.HEX ||
				this.currentLexeme.tokenType == TokenType.STR ||
				(this.currentLexeme.tokenType == TokenType.KEYWORD && 
				 (this.currentToken == "true" ||
				  this.currentToken == "false")));
	}
	
	private boolean isUNOP() {
		return (this.currentLexeme.tokenType == TokenType.SYMBOL &&
				(this.currentToken == "++" ||
				 this.currentToken == "--" ||
				 this.currentToken == "%" ||
				 this.currentToken == "!"));
	}
	
	private boolean isBINOP() {
		return (this.currentLexeme.tokenType == TokenType.SYMBOL &&
				(this.currentToken == "+" ||
				 this.currentToken == "-" ||
				 this.currentToken == "*" ||
				 this.currentToken == "/" ||
				 this.currentToken == "=" ||
				 this.currentToken == "==" ||
				 this.currentToken == ">" ||
				 this.currentToken == "<" ||
				 this.currentToken == ">=" ||
				 this.currentToken == "<=" ||
				 this.currentToken == "!=" ||
				 this.currentToken == "&&" ||
				 this.currentToken == "||"));
	}
	
	private boolean isExpr() {
		return ((this.currentToken == "(" && this.currentLexeme.tokenType == TokenType.SYMBOL) ||
				this.isUNOP() ||
				this.isLoc() || 
				this.isFuncCall() ||
				this.isLit());
	}
	
	private LinkedList<Lexeme> filterLineNumber(int lineNumber){
		return lexemesList.stream().filter(l -> l.lineNumber == lineNumber).collect(Collectors.toCollection(LinkedList::new));
	}

	private int determineErrorColumn(Lexeme lexeme)
	{
		int column = 0;
		LinkedList<Lexeme> lexemesInLine = filterLineNumber(lexeme.getLineNumber());

		for (Lexeme currentLexeme : lexemesInLine) {
			if (currentLexeme.equals(lexeme))
			{
				return column;
			}
			column =+ currentLexeme.getToken().length();
		}

		return 0;
	}

	private void error(Lexeme lexeme){
		int errorLine = lexeme.getLineNumber();
		int errorColumn = determineErrorColumn(lexeme);
	}
}


