package org.richinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetTime {

    final static String PREFIX = "Es isch ";

    public final static String[] hours = {"Mitternacht", "eis", "zwei", "drü", "vieri", "foifi",
            "sächsi", "sibni", "achti", "nüni", "zäni", "elfi"};

    public static String getTime(final String time) {
        var timeSplit = time.split(":");
        var hour = Integer.parseInt(timeSplit[0]);
        var minute = Integer.parseInt(timeSplit[1]);

        return getTime(hour, minute);
    }

    public static String getTime(int hour, int minute) {
        var sb = new StringBuilder(PREFIX);

        if (minute % 5 > 2 && minute % 5 < 5) {
            sb.append("fascht ");
        }

        if (minute > 57) {
            hour++;
        } else if (minute > 52) {
            sb.append("foif vor ");
            hour++;
        } else if (minute > 47) {
            sb.append("zäh vor ");
            hour++;
        } else if (minute > 42) {
            sb.append("viertel vor ");
            hour++;
        } else if (minute > 37) {
            sb.append("zwänzg vor ");
            hour++;
        } else if (minute > 32 && hour != 24) {
            sb.append("foif über halbi ");
            hour++;
        } else if (minute > 27 && hour != 24) {
            sb.append("halbi ");
            hour++;
        } else if (minute > 22) {
            sb.append("füfezwänzg ab ");
        } else if (minute > 17) {
            sb.append("zwänzg ab ");
        } else if (minute > 12) {
            sb.append("viertel ab ");
        } else if (minute > 7) {
            sb.append("zäh ab ");
        } else if (minute > 2) {
            sb.append("foif ab ");
        }

        if (hour != 12 && !(hour == 24 && minute > 27 && minute < 58)) {
            sb.append(hours[hour % 12]);
        }

        if (hour == 24 && minute > 27 && minute < 58) {
            sb.append("zwölfi");
        }

        if (hour == 12) {
            sb.append("Mittag");
        }

        if (hour > 0 && hour < 11) {
            sb.append(" am Morge");
        }
        if (hour > 17 && !(hour == 24 && minute > 57)) {
            sb.append(" am Abig");
        }

        if (minute % 5 > 0 && (minute % 5) < 3) {
            sb.append(" gsi");
        }
        return sb.toString();
    }

    public static String getSuperPhrase() {
        var sb = new StringBuilder(PREFIX);
        sb.append("fascht ");
        sb.append("foif ");
        sb.append("zäh ");
        sb.append("viertel ");
        sb.append("zwänzg ");
        sb.append("foif ");
        sb.append("über ");
        sb.append("halbi ");
        sb.append("füfezwänzg ");
        sb.append("zwänzg ");
        sb.append("viertel ");
        sb.append("zäh ");
        sb.append("foif ");
        sb.append("vor ");
        sb.append("ab ");
        sb.append(String.join(" ", hours));
        sb.append(" zwölfi ");
        sb.append("Mittag");
        sb.append(" am ");
        sb.append("Morge ");
        sb.append("Abig");
        sb.append(" gsi");

        return sb.toString();
    }

    public static String getSuperPhraseStatic() {
        return "Es isch fascht zäh viertel foif zwänzg vor über halbi zwölfi füfezwänzg ab elfi zäni nüni achti sibni sächsi am Abig Mittag foifi vieri drü zwei eis am Morge Mitternacht gsi";
    }

    public static boolean phraseFitsInSuperphrase(String superphrase, String phrase) {
        //System.out.println(String.format("is %s part of %s", phrase, superphrase));
        var phraseArray = phrase.split("\\s+"); // split by whitespace
        var superphraseArray = superphrase.split("\\s+"); // split by whitespace

        var superphraseIndex = 0;
        outerloop:
        for (int checkIndex = 0; checkIndex < phraseArray.length; checkIndex++) {
            //System.out.println("Checking: " + phraseArray[checkIndex]);
            for (int i = superphraseIndex; i < superphraseArray.length; i++) {
                //System.out.println("index: " + i + " " + superphraseArray[i]);
                if (phraseArray[checkIndex].equals(superphraseArray[i])) {
                    //System.out.println(phraseArray[checkIndex] + " found at position " + i);
                    superphraseIndex = i + 1;
                    continue outerloop;
                }
            }
            return false;
        }
        return true;
    }

    public static String merge (String string1, String string2) {
        var string1Array = string1.split("\\s+");
        var string2Array = string2.split("\\s+");
        var mergedString = new ArrayList<String>();
        for ( var i = 0; i < Math.max (string1Array.length, string2Array.length); i++ ) {
            if ( i < string1Array.length ) {
                mergedString.add(string1Array[i]);
            }
            if ( i < string2Array.length ) {
                mergedString.add(string2Array[i]);
            }
        }

        //remove duplicate words
        var duplicatesRemoved = new ArrayList<String>();
        String priorString = "";
        for ( var i=0; i<mergedString.size(); i++ ) {
            if ( ! priorString.equals(mergedString.get(i))) {
                duplicatesRemoved.add(mergedString.get(i));
            }
            priorString = mergedString.get(i);
        }

        return String.join(" ", duplicatesRemoved);
    }

    public static String constructSuperPhrase(ArrayList<String> allTimes) {
        final String[] superPhrase = {""};
        allTimes.forEach( timeInWords -> superPhrase[0] = GetTime.merge(timeInWords, superPhrase[0]));
        return superPhrase[0];
    }


    public static ArrayList<String> getAllTimes() {
        var allTimes = new ArrayList<String>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                String time = String.format("%02d:%02d", h, m);
                allTimes.add( GetTime.getTime(time));
            }
        }
        return allTimes;
    }

    public static String removeNthWord(String phrase, int n) {
        // Split the phrase into words
        String[] words = phrase.split("\\s+");

        // If n is out of bounds, return the original phrase
        if (n < 0 || n >= words.length) {
            return phrase;
        }

        // Build a new phrase without the n-th word
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i != n) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(words[i]);
            }
        }

        return result.toString();
    }


    public static Optional<String> testIfAWordCanBeOmitted(List<String>allTimes, String superPhrase) {
        var superPhraseArray = superPhrase.split("\\s+");
        var words = superPhraseArray.length;

        for ( int i=0; i < words; i++) {
            System.out.println("Testing if I can remove word "+i);
            var testSuperPhrase = removeNthWord(superPhrase,i);
            var allMatch = allTimes.stream().allMatch( time -> phraseFitsInSuperphrase(testSuperPhrase, time));
            if (allMatch) {
                return Optional.of(testSuperPhrase);
            }
        }
        return Optional.empty();

    }

    public static String getReducedSuperPhrase() {
        var allTimes = getAllTimes();
        var superPhrase = GetTime.constructSuperPhrase(allTimes);
        var superPhraseArray = superPhrase.split("\\s+");
        var words = superPhraseArray.length;

        for ( int i=0; i < words; i++) {
            System.out.println("Pass  "+i);
            var returnValue = testIfAWordCanBeOmitted(allTimes, superPhrase);
            if ( returnValue.isPresent() ) {
                superPhrase = returnValue.get();
            } else {
                return superPhrase;
            }
        }
        return superPhrase; // should never arrive here
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

        int totalSpaces = width - words.stream().mapToInt(GetTime::visibleLength).sum();
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

