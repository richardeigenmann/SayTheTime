package org.example;


import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class de_Analyser {
    public boolean fast = false;
    public int fiveMinuteStepIndex;
    public int hourindex;
    public boolean gewesen = false;
    public int tageszeit;

    public final static List<String> de_CH_ZH_Words = Arrays.asList("Es", "isch",
            "fascht", "foif:ab", "zäh:ab", "viertel:ab", "zwänzg:ab", "ab", "foif vor halbi",
            "halbi", "foif über halbi", "zwänzg", "viertel", "zäh", "foif", "vor", "zwölvi",
            "eis", "zwei", "drü", "vieri", "foifi", "sächsi", "sibni", "achti", "nüni",
            "zäni", "elfi", "z'Nacht", "am", "Morge", "Mittag", "Namitag",
            "Abig", "gsi");
    public final static String[] fiveMinuteSteps = {"foif:ab", "zäh:ab", "viertel:ab",
            "zwänzg:ab", "foif vor halbi", "halbi", "foif über halbi", "zwänzg", "viertel",
            "zäh", "foif"};
    public final static String[] hours = {"zwölvi", "eis", "zwei", "drü", "vieri", "foifi",
            "sächsi", "sibni", "achti", "nüni", "zäni", "elfi"};
    public final static String[] tageszeiten = {"z'Nacht", "Morge", "Mittag", "Namitag", "Abig"};


    /**
     * 0 = Nacht
     * 1 = Morgen
     * 2 = Mittag
     * 3 = Nachmittag
     * 4 = Abend
     */
    private final static int[] tageszeitindex = { 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3,
            3, 3, 4, 4, 4, 4, 4, 0, 0 };

    public de_Analyser() {
        this(0);
    }

    public de_Analyser( int offsetMinutes ) {
        final Calendar cal = Calendar.getInstance();
        cal.add( Calendar.MINUTE, offsetMinutes );
        final int hour = cal.get( Calendar.HOUR_OF_DAY );
        final int minutes = cal.get( Calendar.MINUTE );
        analyse( hour, minutes );
    }

    public void analyse( int hour, int minutes ) {
        if ( minutes >= 23 ) {
            hour++;
            //hour = hour % 24; // 24:xx --> 00:xx
        }
        hourindex = hour % 12;

        int fastMinuten = minutes % 5;
        if ( fastMinuten > 2 ) {
            minutes = ( minutes + 2 ) % 60;
            fast = true;
        }

        fiveMinuteStepIndex = minutes / 5;

        tageszeit = tageszeitindex[ hour % 24 ];

        if ( ( fastMinuten > 0 ) && ( fastMinuten <= 2 ) ) {
            gewesen = true;
        }
    }
}