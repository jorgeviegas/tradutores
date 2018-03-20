package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tradutor {

    static Pattern letters = Pattern.compile("[A-Za-z]");
    static Pattern digits = Pattern.compile("[0-9]");
    static Pattern identifiers = Pattern.compile("[A-Za-z]([A-Z|[0-9]])");

    public static void main(String[] args) {
        Matcher lettersMatcher = letters.matcher("t");
        System.out.println(lettersMatcher.matches());
    }
}
