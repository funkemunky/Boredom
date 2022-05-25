package cc.funkemunky.test.utils;

import org.apache.commons.lang.time.DurationFormatUtils;

public class StringUtil {

    public static boolean isNumberFormat(String string) {
        try {
            double dubbb = Double.parseDouble(string);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static String formatDuration(long duration) {
        if(duration > 3600000L) { //If more than 1 hour, format in hours
            return DurationFormatUtils.formatDuration(duration, "h'h', m'm', s's'");
        } else if(duration > 60000L) {  //If more than 1 minute, format in minutes
            return DurationFormatUtils.formatDuration(duration, "m'm', s's'");
        } else {
            return DurationFormatUtils.formatDuration(duration, "s's'");
        }
    }
}
