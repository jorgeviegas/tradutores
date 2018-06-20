package main;

import syntaticType.IReservedWord;
import syntaticType.ISyntaticType;

import java.rmi.UnexpectedException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.UnexpectedExpressionException;
import exceptions.UnexpectedSymbolException;
import exceptions.UnexpectedTokenException;
import exceptions.UnexpectedTypeException;

public class SyntacticAnalyzer {
	private final String BINOP_OPS = "'+', '-', '*', '/', '=', '==', '>', '<', '>=', '<=', '!=', '&&' or '||'.";
	private final String TYPE = "'void', 'int' or 'bool'.";
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

		try {
			this.program();
		} catch (UnexpectedSymbolException e){
			System.out.println("The \""+e.lexeme.token+"\" symbol is unexpected.");

			if (e.expectedSymbol != null && !e.expectedSymbol.isEmpty()) {
				System.out.println("The expected symbol is \""+e.expectedSymbol+"\"");
			}

		} catch (UnexpectedTokenException ea){
			System.out.println("The \""+ea.lexeme.token+"\" symbol is unexpected.");

			if (ea.expectedSymbol != null && !ea.expectedSymbol.isEmpty()) {
				System.out.println("The expected symbol is \""+ea.expectedSymbol+"\"");
			}
		}

	}
	
	private void consume(TokenType type) throws UnexpectedTokenException {
		if (this.currentLexeme.tokenType == type) {
			this.advance();
		} else { 
			throwError(new UnexpectedSymbolException(0,0, currentLexeme, " "));
		} 
	}
	
	private void advance() {
		currentIndex++;
		if (currentIndex == lexemesList.size()) return;
		currentLexeme = lexemesList.get(currentIndex);
		currentToken = currentLexeme.getToken();
	}

	private void program() throws UnexpectedTokenException {
		while (currentToken.equals("def") || this.currentToken.equals("int") ||
				this.currentToken.equals("bool") ||
				this.currentToken.equals("void")) {

			if (this.isFunc()) {
				this.func();
			} else {
				this.var();
			}
		}
	}
	private void var()
			throws UnexpectedTokenException {
		this.type();
		this.consume(TokenType.IDENTIFIER);
			
		if (this.currentToken.equals("[")) {
			this.consume(TokenType.SYMBOL);
			this.consume(TokenType.DEC);
			
			if (this.currentToken.equals("]")) {
				this.consume(TokenType.SYMBOL);
			} else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "]"));
			}
		} else if (this.currentToken.equals(";")) {
			this.consume(TokenType.SYMBOL);
		} else {
			this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
		}		
	}
	
	private void func()
			throws UnexpectedTokenException {
		if (this.currentToken.equals("def")) {
			this.consume(TokenType.KEYWORD);
		}
		
		this.type();
		this.consume(TokenType.IDENTIFIER);
		
		if (this.currentToken.equals("(")) {
			this.consume(TokenType.SYMBOL);
		} else {
			this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "("));
		}
		
		this.paramList();
		
		if (this.currentToken.equals(")")) {
			this.consume(TokenType.SYMBOL);
		//} else {
		//	this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ")"));
		}
		
		this.block();
	}
	
	private void paramList() throws UnexpectedTokenException {

		if (currentToken.equals(")"))
			return;

		this.type();
		this.consume(TokenType.IDENTIFIER);
		
		while (this.currentToken.equals(",")) {
			this.consume(TokenType.SYMBOL);
			this.type();
			this.consume(TokenType.IDENTIFIER);
		}
	}
	
	private void block() throws UnexpectedTokenException {
		this.consume(TokenType.SYMBOL);

		while (this.currentToken.equals("int") ||
			   this.currentToken.equals("bool") ||
			   this.currentToken.equals("void")) {
			this.var();
		}
		
		while (this.currentLexeme.tokenType == TokenType.IDENTIFIER ||
			   this.currentToken.equals("if") ||
			   this.currentToken.equals("while") ||
			   this.currentToken.equals("return") ||
			   this.currentToken.equals("break") ||
			   this.currentToken.equals("return")) {
			this.stmt();
		}
		this.consume(TokenType.SYMBOL);

	}
	
	private void stmt()
			throws UnexpectedTokenException {

		if (this.isLoc()) {
			this.loc();
			
			if (this.currentToken.equals("=")) {
				this.consume(TokenType.SYMBOL);
			}
			else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "="));
			}
			
			this.expr();
			
			if (this.currentToken.equals(";")) {
				this.consume(TokenType.SYMBOL);
			} else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
			}
		} else if (this.isFuncCall()) {
			this.funcCall();
			
			if (this.currentToken.equals(";")) {
				this.consume(TokenType.SYMBOL);
			} else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
			}
		} else {
			switch (this.currentToken) {
				case "if":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken.equals("(")) {
						this.consume(TokenType.SYMBOL);
					} else {
						this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "("));
					}
					
					this.expr();
					
					if (this.currentToken.equals(")")) {
						this.consume(TokenType.SYMBOL);
					//} else {
						//this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ")"));
					}
					
					this.block();
					
					if (this.currentToken.equals("else")) {
						this.consume(TokenType.KEYWORD);
						this.block();
					}
					
					break;
				
				case "while":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken.equals("(")) {
						this.consume(TokenType.SYMBOL);
					} else {
						this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "("));
					}
					
					this.expr();
					
					if (this.currentToken.equals(")")) {
						this.consume(TokenType.SYMBOL);
				//	} else {
					//	this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ")"));
					}
					
					this.block();
					
					break;
					
				case "return":
					this.consume(TokenType.KEYWORD);
					
					if (this.isExpr()) {
						this.expr();
					}
					
					if (this.currentToken.equals(";")) {
						this.consume(TokenType.SYMBOL);
					} else {
						this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
					}
					
					break;
					
				case "break":
					this.consume(TokenType.KEYWORD);
					
					if (this.currentToken.equals(";")) {
						this.consume(TokenType.SYMBOL);
					} else {
						this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
					}
					
					break;
					
				case "continue":
                    this.consume(TokenType.KEYWORD);
					
					if (this.currentToken.equals(";")) {
						this.consume(TokenType.SYMBOL);
					} else {
						this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ";"));
					}
					
					break;
					
				default:
					//this.throwError(new UnexpectedTokenException("UnexpectedStatementException", 0, 0, this.currentLexeme, null));
					break;
			}
		}
	}
	
	private void expr() throws UnexpectedTokenException {
		if (this.isUNOP()) {
			this.consume(TokenType.SYMBOL);
			this.expr();
		} else if (this.isLoc() && !isFuncCall()) {
			this.loc();
		} else if (this.isFuncCall()) {
			this.funcCall();
		} else if (this.isLit()) {
			this.lit();
		} else if (this.currentToken.equals("(")) {
			this.consume(TokenType.SYMBOL);
			this.expr();
			
			if (this.currentToken.equals(")")) {
				this.consume(TokenType.SYMBOL);
			//} else {
			//	this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ")"));
			}
		} else if (this.isExpr()) {
			this.expr();
			
			if (this.isBINOP()) {
				this.consume(TokenType.SYMBOL);
			} else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, this.BINOP_OPS));
			}
			
			this.expr();
		} else {
			//this.throwError(new UnexpectedExpressionException(0, 0, this.currentLexeme, null));
		}
	}
	
	private void type() throws UnexpectedTokenException {
		if (this.currentToken.equals("int") ||
			 this.currentToken.equals("bool") ||
			 this.currentToken.equals("void")) {
			this.consume(TokenType.KEYWORD);
		}
	}
	
	private void loc() throws UnexpectedTokenException {
		if (this.currentLexeme.tokenType == TokenType.IDENTIFIER) {
			this.consume(TokenType.IDENTIFIER);
		} else {
			//this.throwError(new UnexpectedTokenException("UnexpectedIdentifier", 0, 0, this.currentLexeme, null));
		}
		
		if (this.currentToken.equals("[")) {
			this.consume(TokenType.SYMBOL);
			this.expr();
			
			if (this.currentToken.equals("]")) {
				this.consume(TokenType.SYMBOL);
			} else {
				this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "]"));
			}
		}
	}
	
	private void funcCall() throws UnexpectedTokenException {
		if (this.currentLexeme.tokenType == TokenType.IDENTIFIER) {
			this.consume(TokenType.IDENTIFIER);
		} else {
			//this.throwError(new UnexpectedTokenException("UnexpectedIdentifier", 0, 0, this.currentLexeme, null));
		}
		
		if (this.currentToken.equals("(")) {
			this.consume(TokenType.SYMBOL);
			
			if (this.isExpr()) {
				this.argList();
			}
			
			if (this.currentToken.equals(")")) {
				this.consume(TokenType.SYMBOL);
		//	} else {
		//		this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, ")"));
			}
		} else {
			this.throwError(new UnexpectedSymbolException(0, 0, this.currentLexeme, "("));
		}
	}
	
	private void argList() throws UnexpectedTokenException {
		while (this.isExpr()) {
			this.expr();
			
			if (this.currentToken.equals(",")) {
				this.consume(TokenType.SYMBOL);
			} else if (!this.isExpr() && !this.currentToken.equals(")")) {
				this.throwError(new UnexpectedExpressionException(0, 0, this.currentLexeme, null));
			}
		}
	}
	
	private void lit() throws UnexpectedTokenException {
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
			if (this.currentToken.equals("true") || this.currentToken.equals("false")) {
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
		return this.currentLexeme.tokenType == TokenType.IDENTIFIER;
	}
	
	private boolean isFuncCall() {
		return (this.lexemesList.get(this.currentIndex + 1).token.equals("(") &&
				this.currentLexeme.tokenType == TokenType.IDENTIFIER);
	}
	
	private boolean isLit() {
		return (this.currentLexeme.tokenType == TokenType.DEC ||
				this.currentLexeme.tokenType == TokenType.HEX ||
				this.currentLexeme.tokenType == TokenType.STR ||
				(this.currentLexeme.tokenType == TokenType.KEYWORD && 
				 (this.currentToken.equals("true") ||
				  this.currentToken.equals("false"))));
	}
	
	private boolean isUNOP() {
		return (this.currentLexeme.tokenType == TokenType.SYMBOL &&
				(this.currentToken.equals("++") ||
				 this.currentToken.equals("--") ||
				 this.currentToken.equals("%")||
				 this.currentToken.equals("!")));
	}
	
	private boolean isBINOP() {
		return (this.currentLexeme.tokenType == TokenType.SYMBOL &&
				(this.currentToken.equals("+")||
				 this.currentToken.equals("-")||
				 this.currentToken.equals("*")||
				 this.currentToken.equals("/")||
				 this.currentToken.equals("=")||
				 this.currentToken.equals("==") ||
				 this.currentToken.equals(">")||
				 this.currentToken.equals("<")||
				 this.currentToken.equals(">=") ||
				 this.currentToken.equals("<=") ||
				 this.currentToken.equals("!=") ||
				 this.currentToken.equals("&&") ||
				 this.currentToken.equals("||")));
	}
	
	private boolean isExpr() {
		return ((this.currentToken == "(" && this.currentLexeme.tokenType == TokenType.SYMBOL) ||
				this.isUNOP() ||
				this.isLoc() || 
				this.isFuncCall() ||
				this.isLit());
	}
	
	private boolean isFunc() {
		return (this.currentLexeme.tokenType == TokenType.KEYWORD && this.currentToken.equals("def"));
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

	private void throwError(UnexpectedTokenException exception) throws UnexpectedTokenException {
		int errorLine = this.currentLexeme.getLineNumber();
		int errorColumn = determineErrorColumn(this.currentLexeme);
		
		exception.line = errorLine;
		exception.column = errorColumn;
		
		throw exception;
	}
}


