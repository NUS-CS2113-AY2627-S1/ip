package foodielover.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses and formats dates and times for tasks.
 */
public class DateTimeParser {
    /** Output formatter for dates. */
    private static final DateTimeFormatter DATE_OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Output formatter for date-times. */
    private static final DateTimeFormatter DATE_TIME_OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);

    /** Regex matching formatted calendar date strings with optional time component. */
    private static final String DATE_LIKE_REGEX =
            "^(\\d{4}[-/]\\d{1,2}[-/]\\d{1,2}|\\d{1,2}[-/]\\d{1,2}[-/]\\d{4})(\\s+\\d{1,2}:?\\d{2})?$";

    /** Supported input date-time formatters. */
    private static final DateTimeFormatter[] DATE_TIME_INPUT_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("uuuu-M-d Hmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu-M-d H:mm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d/M/uuuu Hmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d/M/uuuu H:mm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-M-uuuu Hmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-M-uuuu H:mm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/M/d Hmm").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/M/d H:mm").withResolverStyle(ResolverStyle.STRICT)
    };

    /** Supported input date formatters. */
    private static final DateTimeFormatter[] DATE_INPUT_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d/M/uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT),
            DateTimeFormatter.ofPattern("uuuu/M/d").withResolverStyle(ResolverStyle.STRICT)
    };

    /**
     * Prevents instantiation of this utility class.
     */
    private DateTimeParser() {
    }

    /**
     * Checks if the text resembles a formatted date or date-time string.
     *
     * @param text String to inspect.
     * @return True if the string looks like a formatted date, false otherwise.
     */
    public static boolean isDateLike(String text) {
        if (text == null) {
            return false;
        }
        return text.trim().matches(DATE_LIKE_REGEX);
    }

    /**
     * Parses the input string into a LocalDateTime if it matches a supported date-time format.
     *
     * @param text Raw date-time string.
     * @return Parsed LocalDateTime, or null if no format matches.
     */
    public static LocalDateTime parseDateTime(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        for (DateTimeFormatter formatter : DATE_TIME_INPUT_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }
        return null;
    }

    /**
     * Parses the input string into a LocalDate if it matches a supported date format.
     *
     * @param text Raw date string.
     * @return Parsed LocalDate, or null if no format matches.
     */
    public static LocalDate parseDate(String text) {
        if (text == null) {
            return null;
        }
        String trimmed = text.trim();
        for (DateTimeFormatter formatter : DATE_INPUT_FORMATTERS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }
        return null;
    }

    /**
     * Formats a LocalDate into the standard user-facing format (e.g. Oct 15 2019).
     *
     * @param date Date to format.
     * @return Formatted date string.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_OUTPUT_FORMATTER);
    }

    /**
     * Formats a LocalDateTime into the standard user-facing format (e.g. Dec 02 2019, 6:00PM).
     *
     * @param dateTime Date-time to format.
     * @return Formatted date-time string.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_OUTPUT_FORMATTER);
    }

    /**
     * Resolves the LocalDate from the parsed LocalDateTime or by parsing as a LocalDate.
     *
     * @param text Raw date or date-time text.
     * @param dateTime Parsed LocalDateTime, or null.
     * @return Resolved LocalDate, or null if unparseable.
     */
    public static LocalDate resolveDate(String text, LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.toLocalDate();
        }
        return parseDate(text);
    }

    /**
     * Formats a raw text or parsed date/time for display.
     *
     * @param raw Raw input text fallback.
     * @param date Parsed LocalDate, if available.
     * @param dateTime Parsed LocalDateTime, if available.
     * @return Formatted date/time string, or raw text if unparsed.
     */
    public static String formatDisplay(String raw, LocalDate date, LocalDateTime dateTime) {
        if (dateTime != null) {
            return formatDateTime(dateTime);
        } else if (date != null) {
            return formatDate(date);
        }
        return raw;
    }
}
