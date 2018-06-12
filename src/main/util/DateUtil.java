package main.util;

/**
 * Helper functions for handling dates.
 *
 * @author Marco Jakob
 */
public class DateUtil {

    /**
     * The date pattern that is used for conversion. Change as you wish.
     */
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    public String changeFormat(double time) {
        String report;
        long temp = (long) (time / 1000);
        int hour = (int) (temp / 3600);
        int min   = (int) ((temp % 3600) / 60);
        int second = (int) ((temp % 3600) % 60);
//        System.out.println(time);
//        System.out.println(temp);
        if (hour == 0) {
            if (min == 0) {
                report = second + "'''";
            } else {
                report = min + "''" + second + "'''";
            }
        } else {
            if (min == 0) {
                report = hour + "'" + second + "'''";
            } else {
                report = hour + "'" + min + "''" + second + "'''";
            }
        }

        return report;
    }
}