package org.richinet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetTime_deCHZH extends GetTime {

    private final static String PREFIX = "Es isch ";

    private final static String[] hours = {"zwölfi", "eis", "zwei", "drü", "vieri", "foifi",
            "sächsi", "sibni", "achti", "nüni", "zäni", "elfi"};


    @Override
    public String getTime( final Time time) {
        var minute = time.minute();
        var hour = time.hour();

        var sb = new StringBuilder(PREFIX);

        if (minute % 5 > 2 && minute % 5 < 5) {
            sb.append("fascht ");
        }

        if (minute > 57) {
            hour++;
        } else if (minute > 52) {
            sb.append("foif vor ");
            hour++;
        } else if (minute > 47) {
            sb.append("zäh vor ");
            hour++;
        } else if (minute > 42) {
            sb.append("viertel vor ");
            hour++;
        } else if (minute > 37) {
            sb.append("zwänzg vor ");
            hour++;
        } else if (minute > 32 && hour != 24) {
            sb.append("foif über halbi ");
            hour++;
        } else if (minute > 27 && hour != 24) {
            sb.append("halbi ");
            hour++;
        } else if (minute > 22) {
            sb.append("füfezwänzg ab ");
        } else if (minute > 17) {
            sb.append("zwänzg ab ");
        } else if (minute > 12) {
            sb.append("viertel ab ");
        } else if (minute > 7) {
            sb.append("zäh ab ");
        } else if (minute > 2) {
            sb.append("foif ab ");
        }

        //if (hour != 12 && !(hour == 24 && minute > 27 && minute < 58)) {
        //if (hour != 12 && !(hour == 24 && minute > 27 && minute < 58)) {
        if (hour == 0 && minute <3) {
            sb.append("Mitternacht");
        } else if (hour == 24 && minute > 57) {
            sb.append("Mitternacht");
        } else if (hour == 12 && minute < 3) {
            sb.append("Mittag");
        } else if (hour == 12 && minute > 57) {
            sb.append("Mittag");
        } else {
            sb.append(hours[hour % 12]);
        }

        if (hour > 0 && hour < 11) {
            sb.append(" am Morge");
        }
        if (hour > 17 || hour == 0) {
            if ( ! (hour == 0 && minute <3) && ! (hour == 24 && minute > 57) ) {
                sb.append(" am Abig");
            }
        }

        if (minute % 5 > 0 && (minute % 5) < 3) {
            sb.append(" gsi");
        }
        return sb.toString();
    }

    @Override
    public String getSuperPhrase() {
        var sb = new StringBuilder(PREFIX);
        sb.append("fascht ");
        sb.append("foif ");
        sb.append("zäh ");
        sb.append("viertel ");
        sb.append("zwänzg ");
        sb.append("foif ");
        sb.append("über ");
        sb.append("halbi ");
        sb.append("füfezwänzg ");
        sb.append("zwänzg ");
        sb.append("viertel ");
        sb.append("zäh ");
        sb.append("foif ");
        sb.append("vor ");
        sb.append("ab ");
        sb.append(String.join(" ", hours));
        sb.append(" Mittag Mitternacht");
        sb.append(" am ");
        sb.append("Morge ");
        sb.append("Abig");
        sb.append(" gsi");

        return sb.toString();
    }


}

