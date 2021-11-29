package cc.funkemunky.test.utils;

public class StringUtil {

    public static boolean isNumberFormat(String string) {
        try {
            double dubbb = Double.parseDouble(string);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }
}
