package org.richinet;

import org.junit.jupiter.api.Test;
import org.richinet.saythetime.lib.GetTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class GetTimeTest {

    @Test
    void testPhraseThatFitsInSuperPhrase() {
        assertTrue(GetTime.phraseFitsInSuperphrase("It is was sunny rainy", "It is sunny"));
    }

    @Test
    void testPhraseThatDoesntFitsInSuperPhrase() {
        assertFalse(GetTime.phraseFitsInSuperphrase("It is was rainy snowy", "It is sunny"));
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

    public static Map<String, String> loadExpectedResults(String resourceFileName) {
        final var properties = new Properties();
        final var map = new HashMap<String, String>();

        try (final var input = GetTimeTest.class.getClassLoader().getResourceAsStream(resourceFileName)) {
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
}