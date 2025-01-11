package org.richinet;

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


@Command(name = "SayTheTime", mixinStandardHelpOptions = true, version = "SayTheTime 0.1",
        description = "Outputs the time in words on STDOUT")
public class CommandLineInterpreter implements Callable<Integer> {

    public static GetTime myTimeProvider = new GetTime_deCHZH();

    public static final String ANSI_CLEAR_SCREEN = "\033[H\033[2J";
    @Option(names = {"-w", "--wordhighlight"}, description = "Word highlight mode")
    boolean wordHighlightMode;

    @Option(names = {"-s", "--superphrase"}, description = "Show superphrase. This is the union all merge of all time phrases that are possible")
    boolean showSuperphrase;

    @Option(names = {"-a", "--all"}, description = "Show all time texts on STDOUT. Can be used in combination with -w or --wordhighlight")
    boolean showAll;

    @Option(names = {"-n", "--no-wrap"}, description = "Do not wrap the lines")
    boolean noWrap;

    @Option(names = {"-o", "--omit-clear"}, description = "Do not clear the screen in wrap mode")
    boolean omitClear;

    @Option(names = {"-c", "--wrap-col"}, description = "Which column to wrap at in wrap mode. Default is ${DEFAULT-VALUE}")
    int wrapCol = 50;

    @Option(names = {"-r", "--run"}, description = "Run forever and refresh every minute")
    boolean runForever;

    // Define an enum for allowed color values
    public enum Color {
        RED, GREEN, BLUE, YELLOW, CYAN, PURPLE, WHITE;
    }

    // Use the enum as the type for the option
    @Option(names = {"--highlightcolor"}, description = "Highlight Color, if provided, must be one of: ${COMPLETION-CANDIDATES}. Default is ${DEFAULT-VALUE}", required = false)
    private Color color = Color.RED;

    @Parameters(index = "0", description = "The time in 24h notation to say. I.e. 13:15", arity = "0..1")
    private String suppliedTime;


    @Override
    public Integer call() throws Exception { // your business logic goes here...
        if (showSuperphrase) {
            showSuperphrase();
            return 0;
        }

        if (showAll) {
            showAll(wordHighlightMode, color);
            return 0;
        }

        if ( ! runForever ) {
            showTime(new Time(suppliedTime), wordHighlightMode, noWrap, omitClear, wrapCol, color);
        } else {
            runForever(wordHighlightMode, noWrap, omitClear, wrapCol, color);
        }
        return 0;
    }

    /**
     * Prints the superphrase which is the union all merge of
     * all time phrases that are possible on STDOUT.
     */
    private static void showSuperphrase() {
        System.out.println(myTimeProvider.getSuperPhrase());
    }

    /**
     * Prints all possible time values on STDOUT
     */
    public static void showAll(boolean wordHighlightMode, Color color) {
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute <= 59; minute++) {
                var timeInWords = myTimeProvider.getTime(new Time(hour, minute));
                if (!wordHighlightMode) {
                    System.out.println(timeInWords);
                } else {
                    showTime( new Time(hour, minute), true, true, true, -1, color);
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
    private static void showTime(final Time suppliedTime, boolean wordHighlightMode, boolean noWrap, boolean omitClear, int wrapCol, Color color) {
        var timeInWords = myTimeProvider.getTime(suppliedTime);
        if (wordHighlightMode) {
            var highlightedTime = highlightTime(timeInWords, myTimeProvider.getSuperPhrase(), color);
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

    public static void runForever(boolean wordHighlightMode, boolean noWrap, boolean omitClear, int wrapCol, Color color) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 60 seconds
        Runnable task = () -> {
            showTime( null, wordHighlightMode, noWrap, omitClear, wrapCol, color);
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

    public static void clearScreen() {
        // ANSI escape code to clear screen
        System.out.print(ANSI_CLEAR_SCREEN);
        System.out.flush(); // Ensure output is flushed immediately
    }


    private static String highlightTime(String phrase, String superPhrase, Color color) {
        var superphraseArray = superPhrase.split("\\s+");
        var phraseArray = phrase.split("\\s+");
        var collector = new ArrayList<String>();

        // step through the words in the time phrase
        var phraseIndex = 0;
        // visit every word in the superphrase and wrap it in a color if it matches the phrase word
        for (var i = 0; i < superphraseArray.length; i++) {
            if (phraseIndex < phraseArray.length && superphraseArray[i].equals(phraseArray[phraseIndex])) {
                collector.add(wrapColor(superphraseArray[i], color));
                phraseIndex++; // advance to the next word in the phrase
            } else {
                collector.add(superphraseArray[i]);
            }
        }
        return collector.stream().collect(Collectors.joining(" "));
    }


    // ANSI escape codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final Map<Color, String> ANSI_COLOR_CODES = Map.of(
            Color.RED, "\u001B[31m",
            Color.GREEN, "\u001B[32m",
            Color.YELLOW, "\u001B[33m",
            Color.BLUE, "\u001B[34m",
            Color.PURPLE, "\u001B[35m",
            Color.CYAN, "\u001B[36m",
            Color.WHITE, "\u001B[37m"
    );


    public static String wrapColor(String text, Color color) {
         return (ANSI_COLOR_CODES.get(color) + text + ANSI_RESET);
    }

    public static String wrapWhite(String text) {
        return (ANSI_WHITE + text + " " + ANSI_RESET);
    }
    public static List<String> justifyText(String input, int width) {
        String[] words = input.split("\\s+");
        List<String> lines = new ArrayList<>();
        List<String> currentLine = new ArrayList<>();
        int currentLength = 0;

        for (String word : words) {
            int wordLength = visibleLength(word);
            if (currentLength + wordLength + currentLine.size() > width) {
                lines.add(justifyLine(currentLine, width));
                currentLine.clear();
                currentLength = 0;
            }
            currentLine.add(word);
            currentLength += wordLength;
        }

        // Handle the last line (left-aligned)
        if (!currentLine.isEmpty()) {
            lines.add(String.join(" ", currentLine));
        }

        return lines;
    }

    private static String justifyLine(List<String> words, int width) {
        if (words.size() == 1) {
            return padRight(words.get(0), width);
        }

        int totalSpaces = width - words.stream().mapToInt(CommandLineInterpreter::visibleLength).sum();
        int spaceBetweenWords = totalSpaces / (words.size() - 1);
        int extraSpaces = totalSpaces % (words.size() - 1);

        StringBuilder justifiedLine = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            justifiedLine.append(words.get(i));
            if (i < words.size() - 1) {
                int spaces = spaceBetweenWords + (i < extraSpaces ? 1 : 0);
                justifiedLine.append(" ".repeat(spaces));
            }
        }
        return justifiedLine.toString();
    }

    private static int visibleLength(String word) {
        // Remove ANSI control sequences using regex and calculate visible length
        return word.replaceAll("\u001B\\[[;\\d]*m", "").length();
    }

    private static String padRight(String word, int width) {
        return word + " ".repeat(width - visibleLength(word));
    }

}