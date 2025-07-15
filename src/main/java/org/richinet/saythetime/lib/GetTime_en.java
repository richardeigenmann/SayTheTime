package org.richinet.saythetime.lib;

import org.richinet.Time;

public class GetTime_en extends GetTime {

    public boolean almost = false;
    public int fiveMinuteStepIndex;
    public int hourIndex;
    public boolean was = false;
    public int daytimeIndex;

    private final static String[] hours = {"twelve", "one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine", "ten", "eleven"};

    /**
     * 0 = Night
     * 1 = Morning
     * 2 = Mid Day
     * 3 = Afternoon
     * 4 = Evening
     */
    private final static Daytime[] daytimeindex = {
            Daytime.NIGHT,    // 00
            Daytime.MORNING,  // 01
            Daytime.MORNING,  // 02
            Daytime.MORNING,  // 03
            Daytime.MORNING,  // 04
            Daytime.MORNING,  // 05
            Daytime.MORNING,  // 06
            Daytime.MORNING,  // 07
            Daytime.MORNING,  // 08
            Daytime.MORNING,  // 09
            Daytime.MORNING,  // 10
            Daytime.MID_DAY,  // 11
            Daytime.MID_DAY,  // 12
            Daytime.AFTERNOON,// 13
            Daytime.AFTERNOON,// 14
            Daytime.AFTERNOON,// 15
            Daytime.AFTERNOON,// 16
            Daytime.EVENING,  // 17
            Daytime.EVENING,  // 18
            Daytime.EVENING,  // 19
            Daytime.EVENING,  // 20
            Daytime.NIGHT,    // 21
            Daytime.NIGHT,    // 22
            Daytime.NIGHT };  // 23

    public enum Daytime {
        NIGHT, MORNING, MID_DAY, AFTERNOON, EVENING
    }

    @Override
    public String getTime( final Time time) {
        var minute = time.minute();
        var hour = time.hour();

        var sb = new StringBuilder( "It ");

        int almostMinutes = minute % 5;

        if ( almostMinutes == 0 ) {
            sb.append("is ");
        } else  if ( ( almostMinutes > 0 ) && ( almostMinutes <= 2 ) ) {
            sb.append( "was " );
        } else if ( almostMinutes > 2 ) {
            sb.append( "is almost ");
        }

        if (minute > 57) {
            hour++;
        } else if (minute > 52) {
            sb.append("five to ");
            hour++;
        } else if (minute > 47) {
            sb.append("ten to ");
            hour++;
        } else if (minute > 42) {
            sb.append("quarter to ");
            hour++;
        } else if (minute > 37) {
            sb.append("twenty to ");
            hour++;
        } else if (minute > 32 && hour != 24) {
            sb.append("twentyfive to ");
            hour++;
        } else if (minute > 27 && hour != 24) {
            sb.append("half past ");
        } else if (minute > 22) {
            sb.append("twentyfive past ");
        } else if (minute > 17) {
            sb.append("twenty past ");
        } else if (minute > 12) {
            sb.append("quarter past ");
        } else if (minute > 7) {
            sb.append("ten past ");
        } else if (minute > 2) {
            sb.append("five past ");
        }

        if (hour == 0 && minute <3) {
            sb.append("midnight");
        } else if (hour == 24 && minute > 57) {
            sb.append("midnight");
        } else {
            sb.append(hours[hour % 12]);
        }


        switch ( daytimeindex[ hour % 24 ]) {
            case NIGHT -> {
                if (! (hour == 0  && minute < 3) && (! (hour ==24 && minute > 57) ) ) {
                    sb.append( " at night");
                }}
            case MORNING -> sb.append( " in the morning" );
            case MID_DAY -> sb.append( "" );
            case AFTERNOON -> sb.append( " in the afternoon" );
            case EVENING -> sb.append( " in the evening" );
        }

        return sb.toString();
    }

    @Override
    public String getSuperPhrase() {
        var sb = new StringBuilder("It is was almost ");
        sb.append("five ");
        sb.append("ten ");
        sb.append("quarter ");
        sb.append("twenty ");
        sb.append("twentyfive ");
        sb.append("half ");
        sb.append("to ");
        sb.append("past ");
        sb.append("midnight ");
        sb.append(String.join(" ", hours));
        sb.append( " in the morning " );
        sb.append( "afternoon " );
        sb.append( "evening " );
        sb.append( "at night ");
        return sb.toString();
    }


}

