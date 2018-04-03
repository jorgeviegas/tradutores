package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tradutor {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("([A-Za-z]([A-Za-z|[0-9]])*)");

    public static void main(String[] args) {

        Matcher lettersMatcher = letters.matcher("t");
        System.out.println(lettersMatcher.matches());

        StringReader stringReader = new StringReader("'sda';int x = 20; {x = x/(25-9)} //teste \n /* test block */ 1+2");
        StreamTokenizer streamTokenizer = new StreamTokenizer(stringReader);
        
        streamTokenizer.ordinaryChar('/');        //O StreamTokenizer não considera o '/' com um char comum
        streamTokenizer.slashSlashComments(true); //Seta o StreamTokenizer para ignorar commentários //
        streamTokenizer.slashStarComments(true);  //Seta o StreamTokenizer para ignorar commentários /**/

        boolean streamEOF = false;


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
                System.out.println("Token: " + currentTokenForDebug);

                switch (streamTokenizer.ttype)
                {
                    case (StreamTokenizer.TT_WORD) :

                        try {

                            ReservedWords.valueOf(currentToken.toUpperCase());
                            System.out.println("Palavra reservada: "+ currentToken);
                        } catch (IllegalArgumentException illegal){
                            System.out.println("Identificador: "+ currentToken);
                        }

                    case (StreamTokenizer.TT_NUMBER) :
                       Lexeme lexema = new Lexeme(TokenType.NUMERICAL, currentToken);
                       lexemeList.add(lexema);
                }

            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
