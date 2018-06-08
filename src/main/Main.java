package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

   public static void main(String[] args){

       LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

       LinkedList<Lexeme> lexemeList = lexicalAnalyzer.analyze();

       SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(lexemeList);
   }
}
