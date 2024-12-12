package org.example;

import java.util.ArrayList;

public class GetTime {

    final static String PREFIX = "Es isch ";

    public final static String[] hours = {"Mitternacht", "eis", "zwei", "drü", "vieri", "foifi",
            "sächsi", "sibni", "achti", "nüni", "zäni", "elfi"};

    public static String getTime(final String time) {
        var timeSplit = time.split(":");
        var hour = Integer.parseInt(timeSplit[0]);
        var minute = Integer.parseInt(timeSplit[1]);

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
            //var s1 = i < string1Array.length ? string1Array[i] : "";
            //var s2 = i < string2Array.length ? string2Array[i] : "";
            //if ( s1.equals(s2)) {
            //    mergedString.add( s1 );
            //} else {
            //    mergedString.add( s1 );
            //    mergedString.add( s2 );
            //}
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
}

