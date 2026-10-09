package nimbus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses and formats the dates and times used by Nimbus.
 */
public class DateTimes {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu, h:mm a", Locale.ENGLISH);

    /**
     * Parses a date or a date with a 24-hour time.
     * Date-only input is interpreted as midnight.
     *
     * @param text Date in yyyy-MM-dd or yyyy-MM-dd HHmm format.
     * @return Parsed date and time.
     * @throws IllegalArgumentException If the input is invalid.
     */
    public static LocalDateTime parse(String text) {
        String input = text.trim();

        try {
            if (input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(input, DATE_FORMAT).atStartOfDay();
            }

            if (input.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                return LocalDateTime.parse(input, INPUT_FORMAT);
            }
        } catch (java.time.format.DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "That date doesn't check out. Use yyyy-MM-dd or yyyy-MM-dd HHmm.");
        }

        throw new IllegalArgumentException(
                "Use yyyy-MM-dd or yyyy-MM-dd HHmm. Example: 2026-10-03 1800.");
    }

    /**
     * Formats a date and time for display.
     *
     * @param dateTime Date and time to format.
     * @return Readable date and time.
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMAT);
    }

    /**
     * Formats a date and time for storage.
     *
     * @param dateTime Date and time to store.
     * @return Date and time in the accepted input format.
     */
    public static String toStorage(LocalDateTime dateTime) {
        return dateTime.format(INPUT_FORMAT);
    }
}
