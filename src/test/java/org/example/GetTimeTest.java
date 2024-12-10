package org.example;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GetTimeTest {

    private static final Map<String, String> expectedResults = Map.ofEntries(
        Map.entry("00:00", "Es isch Mitternacht"),
        Map.entry("00:01", "Es isch Mitternacht gsi"),
        Map.entry("00:02", "Es isch Mitternacht gsi"),
        Map.entry("00:03", "Es isch fascht foif ab Mitternacht"),
        Map.entry("00:04", "Es isch fascht foif ab Mitternacht"),
        Map.entry("00:05", "Es isch foif ab Mitternacht"),
        Map.entry("00:06", "Es isch foif ab Mitternacht gsi"),
        Map.entry("00:07", "Es isch foif ab Mitternacht gsi"),
        Map.entry("01:00", "Es isch eis am Morge"),
        Map.entry("02:00", "Es isch zwei am Morge"),
        Map.entry("06:00", "Es isch sächsi am Morge"),
        Map.entry("12:00", "Es isch Mittag"),
        Map.entry("18:00", "Es isch sächsi am Abig")
    );

    @Test
    void testGetTime() {
        expectedResults.forEach((inputTime, expectedOutput) -> {
            String actualOutput = GetTime.getTime(inputTime);
            assertEquals(expectedOutput, actualOutput, "The method should return the expected output for input time: " + inputTime);
        });
    }
}