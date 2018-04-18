package main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("([A-Za-z]([A-Za-z|[0-9]])*)");

    private LinkedList<Lexeme> lexemeList = new LinkedList<>();

    public void analyze() {

        BufferedReader reader = null;

        try {
            reader = Files.newBufferedReader(Paths.get("TestCode.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StreamTokenizer streamTokenizer = new StreamTokenizer(reader);

        streamTokenizer.ordinaryChar('/');        //O StreamTokenizer n�o considera o '/' com um char comum
        streamTokenizer.slashSlashComments(true); //Seta o StreamTokenizer para ignorar comment�rios //
        streamTokenizer.slashStarComments(true);  //Seta o StreamTokenizer para ignorar comment�rios /**/

        boolean streamEOF = false;
        boolean isSamelogicalOperator = false;
        int lastTokenType = 0;

        try
        {
            while(!streamEOF)
            {
                streamTokenizer.nextToken();

                if(streamTokenizer.ttype == StreamTokenizer.TT_EOF)
                {
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

                        try {
                            ReservedWords.valueOf(currentToken.toUpperCase());
                            lexema = new Lexeme(TokenType.RESERVED_WORD, currentToken, lineNumber);
                            lexemeList.add(lexema);
                        } catch (IllegalArgumentException illegal){
                            lexema = new Lexeme(TokenType.IDENTIFIER, currentToken, lineNumber);
                            lexemeList.add(lexema);
                        }

                        break;

                    case (StreamTokenizer.TT_NUMBER) :
                        lexema = new Lexeme(TokenType.NUMERICAL, String.valueOf(currentNumToken), lineNumber);
                        lexemeList.add(lexema);
                        break;

                    default:
                        LogicalOperators logicalOperator = findLogicalOperatorByChar(tokenType);
                        LogicalOperators lastLogicalOperator = findLogicalOperatorByChar(lastTokenType);

                        if (logicalOperator != null) {
                            lexema = findLogicalOperator(lastLogicalOperator, logicalOperator, this.lexemeList.getLast());
                            if (lexema != null) {
                                if (lexemeList.getLast().tokenType == lexema.tokenType) {
                                    isSamelogicalOperator = true;
                                    lexemeList.removeLast();
                                } else
                                    isSamelogicalOperator = false;
                                lexema.setLineNumber(lineNumber);
                                lexemeList.add(lexema);
                            }
                        }

                        //===============================================================

                        SpecialCharacters specialChar = findSpecialCharactersByChar(tokenType);
                        if (specialChar != null && lexema == null) {
                            lexema = new Lexeme(TokenType.valueOf(specialChar.toString()), String.valueOf(tokenType), lineNumber);
                            lexemeList.add(lexema);
                        }
                        
                        //===============================================================
                        
                        ArithmeticOperators arithmeticOperator = findArithmeticOperatorsByChar(tokenType);
                        if (arithmeticOperator != null) {
                        	System.out.println("Arithmetic operator: " + (char) tokenType);
                            lexema = new Lexeme(TokenType.ARITHMETIC_OPERATOR, String.valueOf((char)tokenType), lineNumber);
                            lexemeList.add(lexema);
                        }
                        
                        //===============================================================

                        if (lexema == null){
                            // its a literal
                            lexema = new Lexeme(TokenType.LITERAL, currentToken, lineNumber);
                            lexemeList.add(lexema);
                        }

                }
                lastTokenType = tokenType;

                //if(lexema != null)
                //System.out.println("Lexema: " + lexema.token + " Tipo: "+lexema.tokenType.toString());

            }


            printLexemeList();
            //lexemeList.forEach(lexeme -> System.out.println(lexeme.toString()));



        } catch (IOException e)
        {
            e.printStackTrace();
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
            else if (lastLexeme.tokenType != TokenType.RESERVED_WORD)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.GREATER.toString(), 0);
            else
                return null;
        }

        if (lastLo == LogicalOperators.LESS) {
            if (lo == LogicalOperators.EQUALS)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "<=", 0);
            else if (lastLexeme.tokenType != TokenType.RESERVED_WORD)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.LESS.toString(), 0);
            else
                return null;
        }

        if (lastLo == LogicalOperators.NOT) {
            if (lo == LogicalOperators.EQUALS)
                return new Lexeme(TokenType.RELATIONAL_OPERATOR, "!=", 0);
            else if (lastLexeme.tokenType != TokenType.RESERVED_WORD)
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

        if (lo == LogicalOperators.GREATER && lastLexeme.tokenType != TokenType.RESERVED_WORD) {
            return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.GREATER.toString(), 0);
        }

        if (lo == LogicalOperators.LESS && lastLexeme.tokenType != TokenType.RESERVED_WORD) {
            return new Lexeme(TokenType.RELATIONAL_OPERATOR, LogicalOperators.LESS.toString(), 0);
        }
        return null;
    }
}