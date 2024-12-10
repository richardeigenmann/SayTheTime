package org.example;

public class GetTime {

    final static String PREFIX = "Es isch ";

    public final static String[] hours = {"Mitternacht", "eis", "zwei", "drü", "vieri", "foifi",
            "sächsi", "sibni", "achti", "nüni", "zäni", "elfi"};
    public static String getTime( final String time ) {
        var timeSplit = time.split(":");
        var hour = Integer.parseInt(timeSplit[0]);
        var minute = Integer.parseInt(timeSplit[1]);

        var sb = new StringBuilder(PREFIX);

        if ( minute > 2 && minute < 5 ) {
            sb.append( "fascht ");
        }

        if ( minute > 2 ) {
            sb.append( "foif ab " );
        }

        if ( hour != 12)  {
            sb.append( hours[hour % 12] );
        }

        if ( hour == 12 ) {
            sb.append( "Mittag" );
        }

        if ( hour > 0 && hour < 11 ) {
            sb.append( " am Morge" );
        }
        if (hour > 17 ) {
            sb.append( " am Abig");
        }

        if ( minute % 5 > 0 && (minute % 5) < 3 ) {
            sb.append( " gsi" );
        }

        return sb.toString();
    }
}

