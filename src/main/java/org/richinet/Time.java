package org.richinet;

import java.time.LocalTime;

public record Time(int hour, int minute) {
    public Time(String stringTime) {
        this(
                getHourFromString(stringTime),
                geMinuteFromString(stringTime)
        );
    }

    private static int getHourFromString(String stringTime) {
        if (stringTime == null || stringTime.isEmpty()) {
            return LocalTime.now().getHour();
        }
        return Integer.parseInt(stringTime.split(":")[0]);
    }

    private static int geMinuteFromString(String stringTime) {
        if (stringTime == null || stringTime.isEmpty()) {
            return LocalTime.now().getMinute();
        }
        return Integer.parseInt(stringTime.split(":")[1]);
    }
}

