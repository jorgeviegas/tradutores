package main;

import syntaticType.Keyword;
import syntaticType.ReservedWord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private LinkedList<Lexeme> lexemeList = new LinkedList<>();

    public LinkedList<Lexeme> analyze() {

        BufferedReader reader = null;

        try {
            reader = Files.newBufferedReader(Paths.get("TestCode.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

        streamTokenizer.ordinaryChar('/');        //O StreamTokenizer nao considera o '/' com um char comum
        streamTokenizer.slashSlashComments(true); //Seta o StreamTokenizer para ignorar commentarios //
        streamTokenizer.slashStarComments(true);  //Seta o StreamTokenizer para ignorar commentarios /**/

        boolean streamEOF = false;
        int lastTokenType = 0;
        Lexeme lastLexeme = null;

        try
        {
            while(!streamEOF)
            {
                streamTokenizer.nextToken();

                if(streamTokenizer.ttype == StreamTokenizer.TT_EOF)
                {
                	if (lastLexeme != null) {
                    	this.lexemeList.add(lastLexeme);
                    }
                	
                    streamEOF = true;
                    break;
                }

                String currentTokenForDebug = streamTokenizer.toString();
                String currentToken = streamTokenizer.sval;
                double currentNumToken = streamTokenizer.nval;
                int lineNumber = streamTokenizer.lineno();

                int tokenType = streamTokenizer.ttype;
                System.out.println("Token: " + currentTokenForDebug);

                Lexeme lexema = null;

                switch (tokenType)
                {
                    case (StreamTokenizer.TT_WORD) :
                        lexema = buildWordLexeme(currentToken, lineNumber);
                        break;

                    case (StreamTokenizer.TT_NUMBER) :
                        lexema = new Lexeme(TokenType.NUMERICAL, String.valueOf(currentNumToken), lineNumber);
                        break;

                    default:
                        LogicalOperators logicalOperator = findLogicalOperatorByChar(tokenType);
                        LogicalOperators lastLogicalOperator = findLogicalOperatorByChar(lastTokenType);

                        if (logicalOperator != null) {
                            lexema = findLogicalOperator(lastLogicalOperator, logicalOperator, lastLexeme);
                            if (lexema != null) {
                            	//Testa pelas variaveis, pois os objetos lexema e lastLexeme .tokenType podem ser LogialOperator e RelationalOperator
                            	if (lastTokenType == tokenType) { 
                                    lastLexeme = null;
                                } 
                            	
                                lexema.setLineNumber(lineNumber);
                                
                            } else {
                            	lexema = new Lexeme(TokenType.LOGICAL_OPERATOR, String.valueOf((char)tokenType), lineNumber);
                            }
                        }

                        //===============================================================

                        SpecialCharacters specialChar = findSpecialCharactersByChar(tokenType);
                        if (specialChar != null && lexema == null) {
                        	lexema = new Lexeme(TokenType.valueOf(specialChar.toString()), String.valueOf((char)tokenType), lineNumber);

                        }
                        
                        //===============================================================

                        ArithmeticOperators arithmeticOperator = findArithmeticOperatorsByChar(tokenType);
                        ArithmeticOperators lastArithOp = findArithmeticOperatorsByChar(lastTokenType);
                        if (arithmeticOperator != null) {
                        	lexema = findArithmeticOperator(lastArithOp, arithmeticOperator);
                        	if (lexema != null) {
	                        	if (lastLexeme.tokenType == lexema.tokenType) {
	                                 lastLexeme = null;
	                        	 } 
	                        	
                             lexema.setLineNumber(lineNumber);
                             
                        	} else {
                        		lexema = new Lexeme(TokenType.ARITHMETIC_OPERATOR, String.valueOf((char)tokenType), lineNumber);
                        	}
                        }
                        
                        //===============================================================

                        if (lexema == null && currentToken != null){
                            // its a literal
                            lexema = new Lexeme(TokenType.LITERAL, currentToken, lineNumber);
                        }
                }
                
                if (lastLexeme != null) {
                	this.lexemeList.add(lastLexeme);
                }
                lastLexeme = lexema;
                lastTokenType = tokenType;
            }


            printLexemeList();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lexemeList;
    }

    private Lexeme buildWordLexeme(String currentToken, int lineNumber) {
        Lexeme lexema;
        if(enumContains(Keyword.class, currentToken.toUpperCase())) {
            lexema = new Lexeme(TokenType.KEYWORD, currentToken, lineNumber);
        } else if (enumContains(ReservedWord.class, currentToken.toUpperCase())) {
            lexema = new Lexeme(TokenType.RESERVED_WORD, currentToken, lineNumber);
        } else {
            lexema = new Lexeme(TokenType.IDENTIFIER, currentToken, lineNumber);
        }
        return lexema;
    }

    private boolean isWordLexeme(Lexeme lexeme) {
        if (lexeme.tokenType == TokenType.KEYWORD ||
            lexeme.tokenType == TokenType.RESERVED_WORD)
            return true;
        return false;
    }

    public static <E extends Enum<E>> boolean enumContains(Class<E> _enumClass, String value) {
        try {
            return EnumSet.allOf(_enumClass)
                    .contains(Enum.valueOf(_enumClass, value));
        } catch (Exception e) {
            return false;
        }
    }

    public SpecialCharacters findSpecialCharactersByChar(int chartoFind){
        for (SpecialCharacters sc : SpecialCharacters.values()){
            if (sc.asChar() == chartoFind ){
                return sc;
            }
        }
        return null;
    }
    
    public ArithmeticOperators findArithmeticOperatorsByChar(int chartoFind){
        for (ArithmeticOperators sc : ArithmeticOperators.values()){
            if (sc.asChar() == chartoFind ){
                return sc;
            }
        }
        return null;
    }

    public LogicalOperators findLogicalOperatorByChar(int chartoFind){
        for (LogicalOperators lo : LogicalOperators.values()){
            if (lo.asChar() == chartoFind ){
                return lo;
            }
        }
        return null;
    }

    public void printLexemeList() {
        for (Lexeme lexeme : lexemeList)
            if (lexeme != null)
                System.out.println("Lexema: " + lexeme.token + " Tipo: " + lexeme.tokenType.toString() + " Linha: " + Integer.toString(lexeme.lineNumber));
    }

    public Lexeme findArithmeticOperator(ArithmeticOperators lastOp, ArithmeticOperators currentOp) {
    	if (lastOp == ArithmeticOperators.SUM) {
            if (currentOp == ArithmeticOperators.SUM) {
                return new Lexeme(TokenType.ARITHMETIC_OPERATOR, "++", 0);
            }
        }
    	
    	if (lastOp == ArithmeticOperators.SUB) {
            if (currentOp == ArithmeticOperators.SUB) {
                return new Lexeme(TokenType.ARITHMETIC_OPERATOR, "--", 0);
            }
    	}
    	
    	return null;
    }
    
    public Lexeme findLogicalOperator(LogicalOperators lastLo, LogicalOperators lo, Lexeme lastLexeme) {
        if (lastLo == LogicalOperators.EQUALS) {
            if (lo == LogicalOperators.EQUALS) {
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "==", 0);
            } else
                return null;
        }

        if (lastLo == LogicalOperators.GREATER) {
            if (lo == LogicalOperators.EQUALS)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, ">=", 0);
            else if (!isWordLexeme(lastLexeme))
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.GREATER.toString(), 0);
            else
                return null;
        }

        if (lastLo == LogicalOperators.LESS) {
            if (lo == LogicalOperators.EQUALS)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "<=", 0);
            else if (!isWordLexeme(lastLexeme))
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.LESS.toString(), 0);
            else
                return null;
        }

        if (lastLo == LogicalOperators.NOT) {
            if (lo == LogicalOperators.EQUALS)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "!=", 0);
            else if (!isWordLexeme(lastLexeme))
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.NOT.toString(), 0);
            else
                return null;
        }

        if (lastLo == LogicalOperators.AND) {
            if (lo == LogicalOperators.AND) {
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "&&", 0);
            } else
                return new Lexeme(TokenType.LOGICAL_OPERATOR, LogicalOperators.AND.toString(), 0);
        }

        if (lastLo == LogicalOperators.OR) {
            if (lo == LogicalOperators.OR) {
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "||", 0);
            } else
                return new Lexeme(TokenType.LOGICAL_OPERATOR, LogicalOperators.OR.toString(), 0);
        }

        if (lo == LogicalOperators.GREATER) {
            return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.GREATER.toString(), 0);
        }

        if (lo == LogicalOperators.LESS) {
            return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.LESS.toString(), 0);
        }
        return null;
    }
}