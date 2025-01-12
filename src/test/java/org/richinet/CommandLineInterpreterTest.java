package org.richinet;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErr;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineInterpreterTest {

    @Test
    void testShowAllShort() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-a");
            });

            String[] lines = stdOut.split("\r\n|\r|\n");
            assertEquals(24*60, lines.length);
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowAllLong() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--all");
            });

            String[] lines = stdOut.split("\r\n|\r|\n");
            assertEquals(24*60, lines.length);
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowSuperphraseShort() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-s");
            });

            assertTrue(stdOut.length() > 40, "The superstring should be longer than 40 characters");
            assertTrue(stdOut.startsWith( "Es isch" ), "The superstring should start with 'Es isch'");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowSuperphraseLong() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--superphrase");
            });

            assertTrue(stdOut.length() > 40, "The superstring should be longer than 40 characters");
            assertTrue(stdOut.startsWith( "Es isch" ), "The superstring should start with 'Es isch'");
        });
        assertEquals("", stdErr);
    }

    static final int HELP_LINES = 18;
    @Test
    void testHelpShort() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-h");
            });

            String[] lines = stdOut.split("\r\n|\r|\n");
            assertTrue(lines.length >= HELP_LINES, "The help must have more than " + HELP_LINES + " lines of output" );
            assertTrue(stdOut.contains("SayTheTime"), "The help option should mention the program name 'SayTheTime'");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testHelpLong() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--help");
            });

            String[] lines = stdOut.split("\r\n|\r|\n");
            assertTrue(lines.length >= HELP_LINES, "The help must have more than " + HELP_LINES + " lines of output" );
            assertTrue(stdOut.contains("SayTheTime"), "The help option should mention the program name 'SayTheTime'");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testVersionShort() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-V");
            });

            String[] lines = stdOut.split("\\s+");
            assertEquals("SayTheTime", lines[0], "The Version option should show the program name 'SayTheTime");
            assertTrue(Float.parseFloat(lines[1]) >= 0.1, "The version number should be greater than 0.1");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testVersionLong() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--version");
            });

            String[] lines = stdOut.split("\\s+");
            assertEquals("SayTheTime", lines[0], "The Version option should show the program name 'SayTheTime");
            assertTrue(Float.parseFloat(lines[1]) >= 0.1, "The version number should be greater than 0.1");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowSpecifiedTime() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("13:15");
            });

            assertEquals("Es isch viertel ab eis\n", stdOut);
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowUnspecifiedTime() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute();
            });

            assertTrue(stdOut.startsWith("Es isch"), "All messages start with 'Es isch'");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowUnspecifiedTimeWordhighlightNoWrap() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-w", "-n");
            });

            // Strip ANSI codes
            String ansiRegex = "\u001B\\[[;\\d]*m";
            String cleanedOutput = stdOut.replaceAll(ansiRegex, "");
            // Remove trailing non-printable characters
            String cleanedOutput2 =  cleanedOutput.replaceAll("[ \t\r\n]+$", "");

            assertTrue(cleanedOutput2.startsWith("Es isch"), "All messages start with 'Es isch' but we got: " + cleanedOutput2);
            assertEquals((new GetTime_deCHZH()).getSuperPhrase(), cleanedOutput2, "Essentially after cleaning the highlighting we should be getting back the superphrase");
        });
        assertEquals("", stdErr);
    }

    private static String extractColorWords(String input) {
        // Regular expression to match words formatted in ANSI color codes \u001B[3[1-7]m to \u001B[0m)
        String regex = "\u001B\\[3[1-7]m(.*?)\u001B\\[0m";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // List to hold the extracted red words
        List<String> redWords = new ArrayList<>();

        // Find all occurrences of words in red
        while (matcher.find()) {
            // Add the word (captured group) to the list
            redWords.add(matcher.group(1).trim());
        }

        return redWords.stream().collect(Collectors.joining(" "));
    }

    @Test
    void testShowSpecifiedTimeWordhighlightNoWrap() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-w", "-n",  "13:15");
            });

            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Strip ANSI codes
            var ansiRegex = "\u001B\\[[;\\d]*m";
            var cleanedOutput = stdOut.replaceAll(ansiRegex, "");
            // Remove trailing non-printable characters
            var cleanedOutput2 =  cleanedOutput.replaceAll("[ \t\r\n]+$", "");
            assertEquals((new GetTime_deCHZH()).getSuperPhrase(), cleanedOutput2, "Essentially after cleaning the highlighting we should be getting back the superphrase");
        });
        assertEquals("", stdErr);
    }

    @Test
    void testHighlightColor() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--wordhighlight", "--no-wrap",  "13:15");
            });

            // Did we get valid output?
            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Extract the first ANSI color sequence
            var ansiRegex = "\u001B\\[[;\\d]*m"; // Matches ANSI escape codes
            var ansiPattern = java.util.regex.Pattern.compile(ansiRegex);
            var matcher = ansiPattern.matcher(stdOut);

            assertTrue(matcher.find(), "No ANSI color code found in output");

            // Get the first ANSI color sequence
            var firstAnsiCode = matcher.group();

            // Check if the first ANSI color code matches the color for red
            // ANSI color for red is typically "\u001B[31m"
            assertEquals("\u001B[31m", firstAnsiCode, "First color code is not red");
        });

        assertEquals("", stdErr, "Expected no error output");
    }

    @Test
    void testHighlightColorGreen() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("--wordhighlight", "--no-wrap", "--highlightcolor=GREEN", "13:15");
            });

            // Did we get valid output?
            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Extract the first ANSI color sequence
            var ansiRegex = "\u001B\\[[;\\d]*m"; // Matches ANSI escape codes
            var ansiPattern = java.util.regex.Pattern.compile(ansiRegex);
            var matcher = ansiPattern.matcher(stdOut);

            assertTrue(matcher.find(), "No ANSI color code found in output");

            // Get the first ANSI color sequence
            var firstAnsiCode = matcher.group();

            // Check if the first ANSI color code matches the color for green
            assertEquals("\u001B[32m", firstAnsiCode, "First color code is not GREEN");
        });

        assertEquals("", stdErr, "Expected no error output");
    }

    @Test
    void testShowSpecifiedTimeWordhighlightWithWrap() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-w", "13:15");
            });

            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Strip ANSI codes
            var ansiRegex = "\u001B\\[[;\\d]*m";
            var cleanedOutput = stdOut.replaceAll(ansiRegex, "");
            var ansiClearRegex = "\u001B\\[H\u001B\\[2J";
            var cleanedOutput2 = cleanedOutput.replaceAll(ansiClearRegex, "");
            // Remove non-printable characters
            var cleanedOutput3 =  cleanedOutput2.replaceAll("[\n]+", " ");
            // Remove duplicate spaces
            var cleanedOutput4 =  cleanedOutput3.replaceAll("\\s+", " ");
            // Remove trailing non-printable characters
            var cleanedOutput5 =  cleanedOutput4.replaceAll("[ \t\r\n]+$", "");
            assertEquals((new GetTime_deCHZH()).getSuperPhrase(), cleanedOutput5, "Essentially after cleaning the highlighting we should be getting back the superphrase");

            var lines = stdOut.split("\r\n|\r|\n");
            assertEquals(5, lines.length, "We expect 5 lines at default line wrapping at col 50");

        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowSpecifiedTimeWordhighlightWithWrapOmitClear() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-w", "-o", "13:15");
            });

            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Strip ANSI codes
            var ansiRegex = "\u001B\\[[;\\d]*m";
            var cleanedOutput = stdOut.replaceAll(ansiRegex, "");
            // do not remove the clear sequence, if it exists so that we get an error for ignoring the omit instruction
            // Remove non-printable characters
            var cleanedOutput3 =  cleanedOutput.replaceAll("[\n]+", " ");
            // Remove duplicate spaces
            var cleanedOutput4 =  cleanedOutput3.replaceAll("\\s+", " ");
            // Remove trailing non-printable characters
            var cleanedOutput5 =  cleanedOutput4.replaceAll("[ \t\r\n]+$", "");
            assertEquals((new GetTime_deCHZH()).getSuperPhrase(), cleanedOutput5, "Essentially after cleaning the highlighting we should be getting back the superphrase");

            var lines = stdOut.split("\r\n|\r|\n");
            assertEquals(5, lines.length, "We expect 5 lines at default line wrapping at col 50");

        });
        assertEquals("", stdErr);
    }

    @Test
    void testShowSpecifiedTimeWordhighlightWithWrapCol100() throws Exception {
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                new CommandLine(new CommandLineInterpreter()).execute("-w", "--wrap-col=100", "13:15");
            });

            var highlightedSentence = extractColorWords(stdOut);
            assertEquals("Es isch viertel ab eis", highlightedSentence);

            // Strip ANSI codes
            var ansiRegex = "\u001B\\[[;\\d]*m";
            var cleanedOutput = stdOut.replaceAll(ansiRegex, "");
            var ansiClearRegex = "\u001B\\[H\u001B\\[2J";
            var cleanedOutput2 = cleanedOutput.replaceAll(ansiClearRegex, "");
            // Remove non-printable characters
            var cleanedOutput3 =  cleanedOutput2.replaceAll("[\n]+", " ");
            // Remove duplicate spaces
            var cleanedOutput4 =  cleanedOutput3.replaceAll("\\s+", " ");
            // Remove trailing non-printable characters
            var cleanedOutput5 =  cleanedOutput4.replaceAll("[ \t\r\n]+$", "");
            assertEquals((new GetTime_deCHZH()).getSuperPhrase(), cleanedOutput5, "Essentially after cleaning the highlighting we should be getting back the superphrase");

            var lines = stdOut.split("\r\n|\r|\n");
            assertEquals(3, lines.length, "We expect 5 lines at default line wrapping at col 50");

        });
        assertEquals("", stdErr);
    }
    @Test
    void testRunForever() throws Exception {
        // Capture standard error and output
        String stdErr = tapSystemErr(() -> {
            String stdOut = tapSystemOutNormalized(() -> {
                // Create a separate thread to execute the command
                Thread commandThread = new Thread(() -> {
                    new CommandLine(new CommandLineInterpreter()).execute("--wordhighlight", "--no-wrap", "--no-clear", "--run");
                });

                // Start the command thread
                commandThread.start();

                // Wait for 1 second
                Thread.sleep(1000);

                // Interrupt the thread to simulate sending SIGKILL
                commandThread.interrupt();

                // Wait for the thread to terminate
                commandThread.join(1000);

                // Assert that the thread is no longer alive (terminated successfully)
                assertFalse(commandThread.isAlive());
            });

            // make sure we got some output
            assertTrue(stdOut.startsWith("\u001B[31mEs\u001B[0m \u001B[31misch\u001B[0m"), "Assert that we got Es isch in the German output");
        });

        // Ensure no error output
        assertEquals("", stdErr);
    }

}