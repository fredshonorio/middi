package pt.ua.rlaas.util;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Util {

    public static final double EPSILON = 0.0000001;

    public static double clampScore(double score) {
        return Math.max(0d, Math.min(1d, score));
    }

    public static boolean nearEquals(double a, double b) {
        return (a - b) < EPSILON;
    }

    public static String[] toStringArray(double[] values) {
        String[] arr = new String[values.length];

        int i = 0;
        for (double d : values) {
            arr[i] = Double.toString(d);
            i++;
        }

        return arr;
    }

    public static String concat(String separator, String... strings) {
        if (strings == null) {
            return "";
        }
        
        StringBuilder b = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            b.append(strings[i]);
            if (i != strings.length - 1) {
                b.append(separator);
            }
        }

        return b.toString();
    }
}
