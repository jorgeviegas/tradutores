package main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tradutor {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("([A-Za-z]([A-Za-z|[0-9]])*)");

    public static void main(String[] args) {

        Matcher lettersMatcher = letters.matcher("t");
        System.out.println(lettersMatcher.matches());

        StringReader stringReader = new StringReader("int x = 20; x = x/(25-9)");
        StreamTokenizer streamTokenizer = new StreamTokenizer(stringReader);

        boolean streamEOF = false;
        try
        {
            while(!streamEOF)
            {

                if(streamTokenizer.ttype == StreamTokenizer.TT_EOF)
                {
                    streamEOF = true;
                }

                streamTokenizer.nextToken();
                String currentToken = streamTokenizer.toString();
                System.out.println("Token: " + currentToken);


                switch (streamTokenizer.ttype)
                {
                    case (StreamTokenizer.TT_WORD) :
                        // DSAFSDAF
                }

            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
