package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                String time = String.format("%02d:%02d", h, m);
                printTime(GetTime.getTime(time));
                System.out.println("");
            }
        }
    }

    private static void printTime(String phrase) {
        var superphraseArray = GetTime.getSuperPhrase().split("\\s+");
        var phraseArray = phrase.split("\\s+");

        var phraseIndex = 0;
        for ( var i = 0; i < superphraseArray.length; i++ ) {
            if (phraseIndex < phraseArray.length && superphraseArray[i].equals(phraseArray[phraseIndex])) {
                printRed(superphraseArray[i]);
                phraseIndex++;
            } else {
                printWhite(superphraseArray[i]);
            }
        }
    }


    // ANSI escape codes for colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void print() {
        System.out.println(RED + "This is red text." + RESET);
        System.out.println(GREEN + "This is green text." + RESET);
        System.out.println(YELLOW + "This is yellow text." + RESET);
        System.out.println(BLUE + "This is blue text." + RESET);
        System.out.println(PURPLE + "This is purple text." + RESET);
        System.out.println(CYAN + "This is cyan text." + RESET);
        System.out.println(WHITE + "This is white text." + RESET);
    }

    public static void printRed(String text) {
        System.out.print(RED + text + " " + RESET);
    }

    public static void printWhite(String text) {
        System.out.print(WHITE + text + " " + RESET);
    }

}