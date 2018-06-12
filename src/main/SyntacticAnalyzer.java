package main;

import syntaticType.IReservedWord;
import syntaticType.ISyntaticType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

	public void analyze(){

		for (int i = 0;i < currentIndex;i++)
		{
			//if (currentToken)


			advance();
		}


	}

	private void consume(Lexeme l) {
		if (this.currentLexeme == l) {// Não tem outro lexema para comparar, a não ser o proprio current
			this.advance();
		} else { // o currentLexeme sempre vai ser igual a ele mesmo, não faz sentido 
			error(l); // Outro motivo, é que aqui o erro seria generalizado, pois o consume não sabe de onde é chamado
					  // O ideal é que cada função chame o seu próprio error, podendo indicar os valores esperados...
		}             // Ex.: Erro linha(x), coluna(y): valor inesperado 'value'. As possibilidades de valores aqui são 'a', 'b' ou 'c'... 
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

	private void program() {
		try {
			this.var();
			this.func();
		} 
		catch (Exception e) {
			
		}
	}
	
	private void var() {
		this.type();
		this.consume(TokenType.IDENTIFIER);
			
		if (this.currentToken == "[") {
			this.consume(TokenType.SYMBOL);
			this.consume(TokenType.DEC);
			
			if (this.currentToken == "]") {
				this.consume(TokenType.SYMBOL);
			} else {
				//ERROR
			}
		} else if (this.currentToken == ";") {
			this.consume(TokenType.SYMBOL);
		} else {
			//ERROR
		}		
	}
	
	private void func() {
		if (this.currentToken == "def") {
			this.consume(TokenType.KEYWORD);
		}
		
		this.type();
		this.consume(TokenType.IDENTIFIER);
		
		if (this.currentToken == "(") {
			this.consume(TokenType.SYMBOL);
		} else {
			//ERROR
		}
		
		this.paramList();
		
		if (this.currentToken == ")") {
			this.consume(TokenType.SYMBOL);
		} else {
			//ERROR
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
	
	private void block() {
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
	
	private void stmt() {
		
	}
	
	private void expr() {
		
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
		
	}
	
	private void funcCall() {
		
	}
	
	private void argList() {
		
	}
	
	private void lit() {
		
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


