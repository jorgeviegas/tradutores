package main;

import syntaticType.IReservedWord;
import syntaticType.ISyntaticType;

public class SyntacticAnalyzer {
	private Lexeme currentLexeme;
	ISyntaticType a;

	private void consume(Lexeme l) {
		if (this.currentLexeme == l) {
			this.advance();
		} else {
			//ERROR
		}
	}
	
	private void advance() {
		//Get next lexeme
	}
	
	private void program() {
		this.decl();
	}
	
	private void decl() {
		if (a instanceof IReservedWord) {
			
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


