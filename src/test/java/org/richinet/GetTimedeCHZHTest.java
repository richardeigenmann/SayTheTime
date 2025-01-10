package org.richinet;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GetTimedeCHZHTest {

    private static GetTime myTimeProvider = new GetTime_deCHZH();

    private static Map<String, String> loadExpectedResults(String resourceFileName) {
        final var properties = new Properties();
        final var map = new HashMap<String, String>();

        try (final var input = GetTimedeCHZHTest.class.getClassLoader().getResourceAsStream(resourceFileName)) {
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
    void compareAllPossibleTimesVsExpectedResults() {
        loadExpectedResults("de_CHZH-expectedResults.properties").forEach((inputTime, expectedOutput) -> {
            String actualOutput = myTimeProvider.getTime(new Time(inputTime));
            assertEquals(expectedOutput, actualOutput, "The method should return the expected output for input time: " + inputTime);
        });
    }

}