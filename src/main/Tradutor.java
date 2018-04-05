package main;

import com.oracle.tools.packager.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tradutor {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("([A-Za-z]([A-Za-z|[0-9]])*)");

   public static void main(String[] args){

       LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

       String tokens = lexicalAnalyzer.analyze("void AlterarVetor(int * vetor, int elementos)");

        System.out.println(tokens);
   }
}
