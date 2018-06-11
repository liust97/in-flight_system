package main.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    /**
     * The date formatter.
     */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
     * to a {@link LocalDate} object.
     * <p>
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    private static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public String ChangeFormat(double time) {
        String report;
        long temp = (long) (time / 1000);
        int hour = (int) (temp / 3600);
        int min = (int) ((temp % 3600) / 60);
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