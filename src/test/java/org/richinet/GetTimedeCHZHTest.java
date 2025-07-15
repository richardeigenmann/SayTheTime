package org.richinet;

import org.junit.jupiter.api.Test;
import org.richinet.saythetime.lib.GetTime;
import org.richinet.saythetime.lib.GetTime_deCHZH;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.richinet.GetTimeTest.loadExpectedResults;

class GetTimedeCHZHTest {

    private static final GetTime myTimeProvider = new GetTime_deCHZH();

    @Test
    void compareAllPossibleTimesVsExpectedResults() {
        loadExpectedResults("de_CHZH-expectedResults.properties").forEach((inputTime, expectedOutput) -> {
            String actualOutput = myTimeProvider.getTime(new Time(inputTime));
            assertEquals(expectedOutput, actualOutput, "The method should return the expected output for input time: " + inputTime);
        });
    }
    @Test
    void testSuperphrase() {
        // The superphrase must contain all the words in the right order of the individual phrases
        var superphrase = myTimeProvider.getSuperPhrase();

        Set<String> allPhrases = new HashSet<>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                allPhrases.add(myTimeProvider.getTime(new Time(h, m)));
            }
        }

        allPhrases.forEach(
                phrase ->
                        assertTrue(
                                myTimeProvider.phraseFitsInSuperphrase(superphrase, phrase),
                                String.format("%s%ndoesn't fit into%n%s", phrase, superphrase)));

    }

}