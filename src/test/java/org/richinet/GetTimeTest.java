package org.richinet;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.richinet.GetTime.phraseFitsInSuperphrase;
import static org.junit.jupiter.api.Assertions.*;

class GetTimeTest {

    private static Map<String, String> loadExpectedResults() {
        Properties properties = new Properties();
        Map<String, String> map = new HashMap<>();

        try (InputStream input = GetTime.class.getClassLoader().getResourceAsStream("de_CHZH-expectedResults.properties")) {
            if (input == null) {
                throw new IllegalStateException("Resource file 'de_CHZH-expectedResults.properties' not found");
            }
            properties.load(input);
            properties.forEach((key, value) -> map.put((String) key, (String) value));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource file", e);
        }

        return map;
    }

    @Test
    void testGetTime() {
        loadExpectedResults().forEach((inputTime, expectedOutput) -> {
            String actualOutput = GetTime.getTime(inputTime);
            assertEquals(expectedOutput, actualOutput, "The method should return the expected output for input time: " + inputTime);
        });
    }

    @Test
    void testSuperphrase() {
        // The Superphrase must contain all the words in the right order of the individual phrases
        var superphrase = GetTime.getSuperPhrase();

        Set<String> allPhrases = new HashSet<String>();
        for (int h = 0; h <= 23; h++) {
            for (int m = 0; m <= 59; m++) {
                String time = String.format("%02d:%02d", h, m);
                allPhrases.add(GetTime.getTime(time));
            }
        }

        allPhrases.stream().forEach(
                phrase ->
                        assertTrue(
                                phraseFitsInSuperphrase(superphrase, phrase),
                                String.format("%s%ndoesn't fit into%n%s", phrase, superphrase)));

    }



    @Test
    void testPhraseThatFitsInSuperPhrase() {
        assertTrue(phraseFitsInSuperphrase("It is was sunny rainy", "It is sunny"));
    }

    @Test
    void testPhraseThatDoesntFitsInSuperPhrase() {
        assertFalse(phraseFitsInSuperphrase("It is was rainy snowy", "It is sunny"));
    }

    @Test
    void testMerge1() {
        assertEquals("It's raining cats dogs", GetTime.merge("It's raining cats", "It's raining dogs"));
    }
    @Test
    void testMerge2() {
        assertEquals("It's raining hailing cats dogs", GetTime.merge("It's raining cats", "It's hailing dogs"));
    }

    @Test
    void testMerge3() {
        assertEquals("It's Cute raining cats", GetTime.merge("It's raining cats", "Cute cats"));
    }
}