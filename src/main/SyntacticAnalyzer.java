package main;

import syntaticType.IReservedWord;
import syntaticType.ISyntaticType;

import java.util.LinkedList;

public class SyntacticAnalyzer {
	private Lexeme currentLexeme;
	private int currentIndex;

	private LinkedList<Lexeme> lexemesList;

	ISyntaticType a;
	ISyntaticType exTokenType;

	public SyntacticAnalyzer(LinkedList<Lexeme> lexemesList) {
		this.lexemesList = lexemesList;
		this.currentIndex = 0;
		this.currentLexeme = lexemesList.get(currentIndex);
	}

	public void analyze(){


		for (int i = 0;i < currentIndex;i++)
		{







			advance();
		}


	}

	private void consume(Lexeme l) {
		if (this.currentLexeme == l) {
			this.advance();
		} else {
			//ERROR
		}
	}
	
	private void advance() {
		currentIndex++;
		currentLexeme = lexemesList.get(currentIndex);
	}
	
	private void program() {
		this.decl();
	}
	
	private void decl() {
		if (exTokenType instanceof IReservedWord) {
			
		}
	}
	
	private void variableDecl() {
		
	}
	
	private void variable() {
		
	}
	
	private void type() {
		
	}
	
	private void functionDecl() {
		
	}
	
	private void formals() {
		
	}
	
	private void classDecl() {
		
	}
	
	private void field() {
		
	}
	
	private void interfaceDecl() {
		
	}
	
	private void prototype() {
		
	}
	
	private void stmtBlock() {
		
	}
	
	private void stmt() {
		
	}
	
	private void ifStmt() {
		
	}
	
	private void whileStmt() {
		
	}
	
	private void forStmt() {
		
	}
	
	private void returnStmt() {
		
	}
	
	private void breakStmt() {
		
	}
	
	private void printStmt() {
		
	}
	
	private void expr() {
		
	}
	
	private void lValue() {
		
	}
	
	private void call() {
		
	}
	
	private void actuals() {
		
	}
	
	private void constant() {
		
	}
}


