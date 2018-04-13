package main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("([A-Za-z]([A-Za-z|[0-9]])*)");

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
        int lastTokenType = 0;

        Collection<Lexeme> lexemeList = new ArrayList<>();

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

                int tokenType = streamTokenizer.ttype;
                System.out.println("Token: " + currentTokenForDebug);

                Lexeme lexema = null;

                switch (tokenType)
                {
                    case (StreamTokenizer.TT_WORD) :

                        try {
                            ReservedWords.valueOf(currentToken.toUpperCase());
                            lexema = new Lexeme(TokenType.RESERVED_WORD, currentToken);
                            lexemeList.add(lexema);
                        } catch (IllegalArgumentException illegal){
                            lexema = new Lexeme(TokenType.IDENTIFIER, currentToken);
                            lexemeList.add(lexema);
                        }

                        break;

                    case (StreamTokenizer.TT_NUMBER) :
                        lexema = new Lexeme(TokenType.NUMERICAL, String.valueOf(currentNumToken));
                        lexemeList.add(lexema);
                        break;

                    default:

                        SpecialCharacters specialChar = findSpecialCharactersByChar(tokenType);
                        if (specialChar != null) {
                            lexema = new Lexeme(TokenType.valueOf(specialChar.toString()), String.valueOf((char)tokenType));
                            lexemeList.add(lexema);
                        }


                        LogicalOperators AndOperator = LogicalOperators.AND;
                        LogicalOperators OrOperator = LogicalOperators.OR;
                        LogicalOperators NotOperator = LogicalOperators.NOT;
                        if(AndOperator.asChar() == tokenType && AndOperator.asChar() == lastTokenType ) {
                            System.out.println("Logical operator: " + (char) tokenType);
                        }
                        if(OrOperator.asChar() == tokenType && OrOperator.asChar() == lastTokenType ) {
                            System.out.println("Logical operator: " + (char) tokenType);
                        }
                        if(NotOperator.asChar() == tokenType) {
                            System.out.println("Logical operator: " + (char) tokenType);
                        }
                        
                        //===============================================================
                        
                        ArithmeticOperators SumOperator = ArithmeticOperators.SUM;
                        ArithmeticOperators SubOperator = ArithmeticOperators.SUB;
                        ArithmeticOperators DivOperator = ArithmeticOperators.DIV;
                        ArithmeticOperators MltOperator = ArithmeticOperators.MLT;
                        ArithmeticOperators PwrOperator = ArithmeticOperators.PWR;
                        if(SumOperator.asChar() == tokenType) {
                            System.out.println("Arithmetic operator: " + (char) tokenType);
                        }
                        if(SubOperator.asChar() == tokenType) {
                            System.out.println("Arithmetic operator: " + (char) tokenType);
                        }
                        if(DivOperator.asChar() == tokenType) {
                            System.out.println("Arithmetic operator: " + (char) tokenType);
                        }
                        if(MltOperator.asChar() == tokenType) {
                            System.out.println("Arithmetic operator: " + (char) tokenType);
                        }
                        if(PwrOperator.asChar() == tokenType) {
                            System.out.println("Arithmetic operator: " + (char) tokenType);
                        }
                        
                        ArithmeticOperators arithmeticOperator = findArithmeticOperatorsByChar(tokenType);
                        if (arithmeticOperator != null) {
                            lexema = new Lexeme(TokenType.ARITHMETIC_OPERATOR, String.valueOf((char)tokenType));
                            lexemeList.add(lexema);
                        }
                        
                        //===============================================================

                        if (lexema == null){
                            // its a literal
                            lexema = new Lexeme(TokenType.LITERAL, currentToken);
                            lexemeList.add(lexema);
                        }
                        lastTokenType = tokenType;

                }

                if(lexema != null)
                System.out.println("Lexema: " + lexema.token + " Tipo: "+lexema.tokenType.toString());


            }


         lexemeList.forEach(lexeme -> System.out.println(lexeme.toString()));



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
}