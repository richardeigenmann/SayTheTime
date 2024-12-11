package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //System.out.println( GetTime.getTime( "00:00" ));

        //de_Analyser a = new de_Analyser();

        Set<String> phrases = new HashSet<String>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                String time = String.format("%02d:%02d", h, m);
                //a.analyse(h,m);
                //String minutes = a.fast ? a.fiveMinuteSteps [a.fiveMinuteStepIndex] : "";
                //System.out.println(String.format( "%s -> %s, hour: %s, min: %s", time, GetTime.getTime(time), a.hours[a.hourindex], minutes ) );
                //System.out.println( String.format( "%s=%s", time, GetTime.getTime(time) ) );
                //System.out.println( String.format( "Map.entry(\"%s\", \"%s\"),", time, GetTime.getTime(time) ) );
                phrases.add(GetTime.getTime(time));
            }
        }

        // Join unique words into a single string
        //var uniqueWords = String.join(" ", uniqueWords);

        System.out.println(GetTime.getSuperPhrase());
        //Thread.dumpStack();
    }
}