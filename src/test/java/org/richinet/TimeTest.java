package org.richinet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {
    @Test
    void testCanonicalConstructor() {
        // Arrange
        int hour = 14;
        int minute = 30;

        // Act
        Time time = new Time(hour, minute);

        // Assert
        assertEquals(14, time.hour());
        assertEquals(30, time.minute());
    }

    @Test
    void testStringConstructor_ValidTime() {
        // Arrange
        String stringTime = "14:30";

        // Act
        Time time = new Time(stringTime);

        // Assert
        assertEquals(14, time.hour());
        assertEquals(30, time.minute());
    }

    @Test
    void testStringConstructor_InvalidFormat() {
        // Arrange
        String invalidTime = "14-30";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> new Time(invalidTime));
    }

    @Test
    void testStringConstructor_NonNumericValues() {
        // Arrange
        String invalidTime = "14:xx";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> new Time(invalidTime));
    }

    @Test
    void testStringConstructor_InvalidHourRange() {
        // Arrange
        String invalidTime = "25:30";

        // Act
        Time time = new Time(invalidTime);

        // Assert
        assertEquals(25, time.hour());
        assertEquals(30, time.minute());
    }

    @Test
    void testStringConstructor_InvalidMinuteRange() {
        // Arrange
        String invalidTime = "14:75";

        // Act
        Time time = new Time(invalidTime);

        // Assert
        assertEquals(14, time.hour());
        assertEquals(75, time.minute());
    }

    @Test
    void testToString() {
        // Arrange
        Time time = new Time(14, 30);

        // Act
        String result = time.toString();

        // Assert
        assertEquals("Time[hour=14, minute=30]", result);
    }

    @Test
    void testStringConstructor_EmptyString() {
        // Arrange
        String emptyTime = "";
        var time = new Time(emptyTime);
        assertTrue(time.hour() >= 0 && time.hour() < 24, "The hour should be between 0 and 24 but it is: " + time.hour());
        assertTrue(time.minute() >= 0 && time.minute() < 60, "The minute should be between 0 and 24 but it is: " + time.minute());
    }
}