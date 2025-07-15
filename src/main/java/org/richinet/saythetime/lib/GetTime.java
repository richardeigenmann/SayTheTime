package org.richinet.saythetime.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GetTime {

    /**
     * The extending Locale specific class must provide the time in words if called with a time.
     * @param time the time such as new Time(17,0);
     * @return A text such as "It is 5 o'clock"
     */
    public abstract String getTime(final Time time);

    /**
     * The extending Locale specific class must provide the superphrase from which all possible
     * time phrases can be built by striking off words.
     * @return The superphrase
     */
    public abstract String getSuperPhrase();

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

    public static String merge(String string1, String string2) {
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

    public String constructSuperPhrase(ArrayList<String> allTimes) {
        final String[] superPhrase = {""};
        allTimes.forEach( timeInWords -> superPhrase[0] = merge(timeInWords, superPhrase[0]));
        return superPhrase[0];
    }


    public ArrayList<String> getAllTimes() {
        var allTimes = new ArrayList<String>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                allTimes.add( this.getTime(new Time(h,m)));
            }
        }
        return allTimes;
    }

    public String removeNthWord(String phrase, int n) {
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
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(words[i]);
            }
        }

        return result.toString();
    }


    public Optional<String> testIfAWordCanBeOmitted(List<String> allTimes, String superPhrase) {
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

    public String getReducedSuperPhrase() {
        var allTimes = getAllTimes();
        var superPhrase = this.constructSuperPhrase(allTimes);
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



}
