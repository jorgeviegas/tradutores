package main;

import com.oracle.tools.packager.Log;

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
                        lastTokenType = tokenType;

                }

                if(lexema != null)
                System.out.println("Lexema: " + lexema.token + " Tipo: "+lexema.tokenType.toString());


            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}