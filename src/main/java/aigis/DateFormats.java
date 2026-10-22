package aigis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Provides the date formats used by Aigis for parsing and displaying deadlines.
 */
public final class DateFormats {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private DateFormats() {
    }

    /**
     * Parses a date entered by the user or loaded from storage.
     *
     * @param date The date in yyyy-MM-dd format.
     * @return The parsed date.
     * @throws java.time.format.DateTimeParseException If the date is invalid.
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, INPUT_FORMAT);
    }

    /**
     * Formats a deadline date for display to the user.
     *
     * @param date The date to format.
     * @return The date in MMM dd yyyy format.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
