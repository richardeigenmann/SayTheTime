package org.richinet;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import static org.richinet.GetTime.justifyText;


@Command(name = "SayTheTime", mixinStandardHelpOptions = true, version = "SayTheTime 0.1",
        description = "Outputs the time in words on STDOUT")
public class CommandLineInterpreter implements Callable<Integer> {

    @Parameters(index = "0", description = "The time in 24h notation to say. I.e. 13:15", arity = "0..1")
    private Time suppliedTime;

    @Override
    public Integer call() throws Exception { // your business logic goes here...

        int hour;
        int minute;

        if (suppliedTime == null) {
            var now = LocalTime.now();
            hour = now.getHour();
            minute = now.getMinute();
         } else {
            hour = suppliedTime.getHours();
            minute = suppliedTime.getMinutes();
        }
        var timeInWords = GetTime.getTime(hour, minute);
        System.out.println(timeInWords);

        return 0;
    }

    private static String getNowAsString() {
        var now = LocalTime.now();
        var hour = now.getHour();
        var minute = now.getMinute();
        String time = String.format("%02d:%02d", hour, minute);
        return time;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new CommandLineInterpreter()).execute(args);
        System.exit(exitCode);

    }

    public static void repeatPrintout() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 60 seconds
        Runnable task = () -> {
            LocalTime now = LocalTime.now();
            int hour = now.getHour();
            int minute = now.getMinute();
            System.out.println("");
            printTime(hour, minute);

        };

        scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        // Add a shutdown hook to gracefully terminate the scheduler when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
        }));

    }

    public static void printAll() {
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                printTime(h,m);
            }
        }
    }

    public static void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush(); // Ensure output is flushed immediately
    }
    private static void printTime(int h, int m) {
        //var superPhrase = GetTime.getReducedSuperPhrase();
        var superPhrase = GetTime.getSuperPhraseStatic();
        int width = 50;
        String time = String.format("%02d:%02d", h, m);
        var timeInWords = GetTime.getTime(time);
        var formattedTime = formatTime(timeInWords, superPhrase);
        List<String> justifiedText = justifyText(formattedTime, width);
        clearScreen();
        for (String line : justifiedText) {
            System.out.println(line);
        }
    }
    private static void printTime(String phrase, String superPhrase) {
        var superphraseArray = superPhrase.split("\\s+");
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

    private static String formatTime(String phrase, String superPhrase) {
        var superphraseArray = superPhrase.split("\\s+");
        var phraseArray = phrase.split("\\s+");
        var sb = new StringBuffer("");

        var phraseIndex = 0;
        for ( var i = 0; i < superphraseArray.length; i++ ) {
            if (phraseIndex < phraseArray.length && superphraseArray[i].equals(phraseArray[phraseIndex])) {
                sb.append(wrapRed(superphraseArray[i]));
                phraseIndex++;
            } else {
                sb.append(wrapWhite(superphraseArray[i]));
            }
        }
        return sb.toString();
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

    public static String wrapRed(String text) {
        return(RED + text + " " + RESET);
    }

    public static void printWhite(String text) {
        System.out.print(WHITE + text + " " + RESET);
    }

    public static String wrapWhite(String text) {
        return(WHITE + text + " " + RESET);
    }

}