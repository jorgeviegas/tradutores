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
			if (currentToken)


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
		currentToken = currentLexeme.getToken();
	}

	private LinkedList<Lexeme> filterLineNumber(int lineNumber){
		return lexemesList.stream().filter(l -> l.lineNumber == lineNumber).collect(Collectors.toCollection(LinkedList::new));
	}
}


