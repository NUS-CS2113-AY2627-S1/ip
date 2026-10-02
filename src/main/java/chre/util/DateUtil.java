package chre.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting dates.
 * Input format: yyyy-MM-dd (e.g., 2019-10-15)
 * Output format: MMM dd yyyy (e.g., Oct 15 2019)
 */
public class DateUtil {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Parses a date string in yyyy-MM-dd format to LocalDate.
     *
     * @param dateString the date string to parse
     * @return the parsed LocalDate
     * @throws DateTimeParseException if the date format is invalid
     */
    public static LocalDate parseDate(String dateString) throws DateTimeParseException {
        return LocalDate.parse(dateString.trim(), INPUT_FORMAT);
    }

    /**
     * Formats a LocalDate to MMM dd yyyy format.
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String formatDate(LocalDate date) {
        return date.format(OUTPUT_FORMAT);
    }
}
