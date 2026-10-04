package dobby.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Parses and formats the date and time values used by Dobby's tasks.
 */
public final class DateTimeParser {

    /** Formats date-only values for display. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);

    /** Formats date-time values for display. */
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d uuuu, h:mm a", Locale.ENGLISH);

    /** Date-only input formats accepted by Dobby. */
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd"),
        DateTimeFormatter.ofPattern("d/M/uuuu"),
        DISPLAY_DATE_FORMAT
    };

    /** Date-time input formats accepted by Dobby. */
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
        DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("d/M/uuuu HHmm"),
        DISPLAY_DATE_TIME_FORMAT
    };

    /** Prevents instantiation of this utility class. */
    private DateTimeParser() {
    }

    /**
     * Parses a date or date-time value into a date.
     *
     * @param value the date or date-time text to parse.
     * @return the parsed date, or {@code null} when the text is not recognized.
     */
    public static LocalDate parseDate(String value) {
        if (value == null) {
            return null;
        }

        LocalDateTime dateTime = parseDateTime(value);
        if (dateTime != null) {
            return dateTime.toLocalDate();
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException exception) {
                // Try the next supported format.
            }
        }

        return null;
    }

    /**
     * Parses a date-time value.
     *
     * @param value the date-time text to parse.
     * @return the parsed date and time, or {@code null} when the text is not recognized.
     */
    public static LocalDateTime parseDateTime(String value) {
        if (value == null) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(value, formatter);
            } catch (DateTimeParseException exception) {
                // Try the next supported format.
            }
        }

        return null;
    }

    /**
     * Formats a date for display in task messages.
     *
     * @param date the date to format.
     * @return the formatted date, such as {@code Oct 15 2019}.
     */
    public static String formatDate(LocalDate date) {
        return DISPLAY_DATE_FORMAT.format(date);
    }

    /**
     * Formats a date and time for display in task messages.
     *
     * @param dateTime the date and time to format.
     * @return the formatted date and time, such as {@code Oct 15 2019, 6:00 PM}.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return DISPLAY_DATE_TIME_FORMAT.format(dateTime);
    }
}
