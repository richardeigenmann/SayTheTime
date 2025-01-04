package org.richinet;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import static org.richinet.GetTime.justifyText;


@Command(name = "SayTheTime", mixinStandardHelpOptions = true, version = "SayTheTime 0.1",
        description = "Outputs the time in words on STDOUT")
public class CommandLineInterpreter implements Callable<Integer> {

    @Option(names = {"-w", "--wordhighlight"}, description = "Word highlight mode")
    boolean wordHighlightMode;

    @Option(names = {"-s", "--superphrase"}, description = "Show superphrase. This is the union all merge of all time phrases that are possible")
    boolean showSuperphrase;

    @Option(names = {"-a", "--all"}, description = "Show all time texts on STDOUT. Can be used in combinationwith -w or --wordhighlight")
    boolean showAll;

    @Option(names = {"-n", "--no-wrap"}, description = "Do not wrap the lines")
    boolean noWrap;

    @Option(names = {"-o", "--omit-clear"}, description = "Do not clear the screen in wrap mode")
    boolean omitClear;

    @Option(names = {"-c", "--wrap-col"}, description = "Which column to wrap at in wrap mode. Default is ${DEFAULT-VALUE}")
    int wrapCol = 50;

    @Option(names = {"-r", "--run"}, description = "Run forever and refresh every minute")
    boolean runForever;

    @Parameters(index = "0", description = "The time in 24h notation to say. I.e. 13:15", arity = "0..1")
    private Time suppliedTime;

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        if (showSuperphrase) {
            showSuperphrase();
            return 0;
        }

        if (showAll) {
            showAll(wordHighlightMode);
            return 0;
        }

        if ( ! runForever ) {
            showTime(suppliedTime, wordHighlightMode, noWrap, omitClear, wrapCol);
        } else {
            runForever(wordHighlightMode, noWrap, omitClear, wrapCol);
        }
        return 0;
    }

    /**
     * Prints the superphrase which is the union all merge of
     * all time phrases that are possible on STDOUT.
     */
    private static void showSuperphrase() {
        System.out.println(GetTime.getSuperPhrase());
    }

    /**
     * Prints all possible time values on STDOUT
     */
    public static void showAll(boolean wordHighlightMode) {
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute <= 59; minute++) {
                var timeInWords = GetTime.getTime(hour, minute);
                if (!wordHighlightMode) {
                    System.out.println(timeInWords);
                } else {
                    showHighlightedTime(hour, minute);
                }
            }
        }
    }


    /**
     * Prints the time as a simple string on STDOUT
     * Example: Es isch viertel ab eis
     *
     * @param suppliedTime      if provided the hours and minutes will be used.
     *                          if null, the hours and minutes of the current time will be used.
     * @param wordHighlightMode
     * @param omitClear
     * @param wrapCol
     */
    private static void showTime(final Time suppliedTime, boolean wordHighlightMode, boolean noWrap, boolean omitClear, int wrapCol) {
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
        if (wordHighlightMode) {
            var highlightedTime = highlightTime(timeInWords, GetTime.getSuperPhrase());

            if ( noWrap) {
                System.out.println(highlightedTime);
            } else {
                List<String> justifiedText = justifyText(highlightedTime, wrapCol);
                if ( ! omitClear ) {
                    clearScreen();
                }
                for (String line : justifiedText) {
                    System.out.println(line);
                }
            }

        } else {
            System.out.println(timeInWords);
        }
    }

    public static void runForever(boolean wordHighlightMode, boolean noWrap, boolean omitClear, int wrapCol) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 60 seconds
        Runnable task = () -> {
            showTime( null, wordHighlightMode, noWrap, omitClear, wrapCol);
        };

        scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        // Add a shutdown hook to gracefully terminate the scheduler when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
        }));

        // Prevent the main thread from exiting immediately
        try {
            // Keep the main thread alive, or you can use Thread.sleep(Long.MAX_VALUE) for indefinite blocking
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (InterruptedException e) {
            // Handle interruption gracefully
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Entrypoint to the Command Line version of the program.
     *
     * @param args various options to control the output
     */
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
            showHighlightedTime(hour, minute);

        };

        scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        // Add a shutdown hook to gracefully terminate the scheduler when the program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
        }));

    }


    public static void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print("\033[H\033[2J");
        System.out.flush(); // Ensure output is flushed immediately
    }

    /**
     * Shows the superphrase, highlighting the words for the supplied time
     *
     * @param h the hour
     * @param m the minute
     */
    private static void showHighlightedTime(int h, int m) {
        var superPhrase = GetTime.getSuperPhraseStatic();
        int width = 50;
        String time = String.format("%02d:%02d", h, m);
        var timeInWords = GetTime.getTime(time);
        var formattedTime = highlightTime(timeInWords, superPhrase);
        List<String> justifiedText = justifyText(formattedTime, width);
        clearScreen();
        for (String line : justifiedText) {
            System.out.println(line);
        }
    }

    private static void showHighlightedTime(String phrase, String superPhrase) {
        var superphraseArray = superPhrase.split("\\s+");
        var phraseArray = phrase.split("\\s+");

        var phraseIndex = 0;
        for (var i = 0; i < superphraseArray.length; i++) {
            if (phraseIndex < phraseArray.length && superphraseArray[i].equals(phraseArray[phraseIndex])) {
                printRed(superphraseArray[i]);
                phraseIndex++;
            } else {
                System.out.print(superphraseArray[i]);
                System.out.print(" ");
            }
        }
    }

    private static String highlightTime(String phrase, String superPhrase) {
        var superphraseArray = superPhrase.split("\\s+");
        var phraseArray = phrase.split("\\s+");
        var collector = new ArrayList<String>();

        // step through the words in the time phrase
        var phraseIndex = 0;
        // visit every word in the superphrase and wrap it in a color if it matches the phrase word
        for (var i = 0; i < superphraseArray.length; i++) {
            if (phraseIndex < phraseArray.length && superphraseArray[i].equals(phraseArray[phraseIndex])) {
                collector.add(wrapRed(superphraseArray[i]));
                phraseIndex++; // advance to the next word in the phrase
            } else {
                collector.add(superphraseArray[i]);
            }
        }
        return collector.stream().collect(Collectors.joining(" "));
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
        return (RED + text + RESET);
    }

    public static void printWhite(String text) {
        System.out.print(WHITE + text + " " + RESET);
    }

    public static String wrapWhite(String text) {
        return (WHITE + text + " " + RESET);
    }

}