package main;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

public class LexicalAnalyzer {

    public String analyze(String programToAnalyze) {

        StringReader stringReader = new StringReader(programToAnalyze);
        StreamTokenizer streamTokenizer = new StreamTokenizer(stringReader);

        streamTokenizer.commentChar(35);

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

                    case () :
                        Lexeme lexema = new Lexeme(TokenType.NUMERICAL, currentToken);
                        lexemeList.add(lexema);
                }

            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return "asdasd";
    }
}
