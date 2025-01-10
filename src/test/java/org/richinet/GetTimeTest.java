package org.richinet;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GetTimeTest {

    private static GetTime myTimeProvider = new GetTime_deCHZH();

    @Test
    void testSuperphrase() {
        // The superphrase must contain all the words in the right order of the individual phrases
        var superphrase = myTimeProvider.getSuperPhrase();

        Set<String> allPhrases = new HashSet<String>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                allPhrases.add(myTimeProvider.getTime(new Time(h, m)));
            }
        }

        allPhrases.stream().forEach(
                phrase ->
                        assertTrue(
                                myTimeProvider.phraseFitsInSuperphrase(superphrase, phrase),
                                String.format("%s%ndoesn't fit into%n%s", phrase, superphrase)));

    }



    @Test
    void testPhraseThatFitsInSuperPhrase() {
        assertTrue(myTimeProvider.phraseFitsInSuperphrase("It is was sunny rainy", "It is sunny"));
    }

    @Test
    void testPhraseThatDoesntFitsInSuperPhrase() {
        assertFalse(myTimeProvider.phraseFitsInSuperphrase("It is was rainy snowy", "It is sunny"));
    }

    @Test
    void testMerge1() {
        assertEquals("It's raining cats dogs", myTimeProvider.merge("It's raining cats", "It's raining dogs"));
    }
    @Test
    void testMerge2() {
        assertEquals("It's raining hailing cats dogs", myTimeProvider.merge("It's raining cats", "It's hailing dogs"));
    }

    @Test
    void testMerge3() {
        assertEquals("It's Cute raining cats", myTimeProvider.merge("It's raining cats", "Cute cats"));
    }
}