package moistbot.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses and formats the date and optional time used by deadline tasks.
 */
public final class DateTimeUtil {
    private static final DateTimeFormatter ISO_DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DAY_FIRST_DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private DateTimeUtil() {
    }

    /**
     * Parses an ISO or day-first date followed by an optional 24-hour time.
     *
     * @param input Date-time text such as {@code 2019-12-02 1800} or {@code 2/12/2019 1800}
     * @return The parsed date and optional time
     * @throws DateTimeParseException if the input is not a valid supported date and time
     */
    public static ParsedDateTime parse(String input) throws DateTimeParseException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 1 || parts.length > 2) {
            throw new DateTimeParseException("Unsupported date-time format", input, 0);
        }

        LocalDate date = parseDate(parts[0]);
        LocalTime time = parts.length == 2 ? LocalTime.parse(parts[1], INPUT_TIME_FORMAT) : null;
        return new ParsedDateTime(date, time);
    }

    /**
     * Formats a deadline for display in a friendly, unambiguous form.
     *
     * @param date The deadline date
     * @param time The optional deadline time
     * @return The formatted deadline
     */
    public static String formatForDisplay(LocalDate date, LocalTime time) {
        String result = date.format(DISPLAY_DATE_FORMAT);
        if (time != null) {
            result += ", " + time.format(DISPLAY_TIME_FORMAT);
        }
        return result;
    }

    /**
     * Formats a deadline in the stable form used by the save file.
     *
     * @param date The deadline date
     * @param time The optional deadline time
     * @return The canonical stored deadline
     */
    public static String formatForStorage(LocalDate date, LocalTime time) {
        String result = date.format(ISO_DATE_FORMAT);
        if (time != null) {
            result += " " + time.format(INPUT_TIME_FORMAT);
        }
        return result;
    }

    /**
     * Parses a date in either supported input format without accepting a time.
     *
     * @param input Date text such as {@code 2019-12-02} or {@code 2/12/2019}
     * @return The parsed date
     * @throws DateTimeParseException if the input is not a valid supported date
     */
    public static LocalDate parseDate(String input) throws DateTimeParseException {
        try {
            return LocalDate.parse(input.trim(), ISO_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(input.trim(), DAY_FIRST_DATE_FORMAT);
        }
    }

    /**
     * Holds a parsed date and its optional time without losing type information.
     *
     * @param date The parsed date
     * @param time The parsed time, or {@code null} when no time was supplied
     */
    public record ParsedDateTime(LocalDate date, LocalTime time) {
    }
}
