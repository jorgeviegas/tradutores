package main;

import com.oracle.tools.packager.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

   public static void main(String[] args){

       LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();

       lexicalAnalyzer.analyze();
   }
}
