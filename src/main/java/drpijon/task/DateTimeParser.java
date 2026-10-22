package drpijon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Parses and serializes the date and date-time values used by tasks.
 */
public final class DateTimeParser {
    /** Strict formatter for date-only values. */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    /** Strict formatter for date-time values with a 24-hour clock. */
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Prevents construction of this utility class.
     */
    private DateTimeParser() {
    }

    /**
     * Parses a date or date-time value.
     *
     * @param text date or date-time text
     * @return parsed value, using midnight for date-only text
     * @throws DateTimeParseException when the text is not supported
     */
    public static LocalDateTime parse(String text) throws DateTimeParseException {
        String normalizedText = normalize(text);
        try {
            return LocalDateTime.parse(normalizedText, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(normalizedText).atStartOfDay();
        }
    }

    /**
     * Parses a date-only value.
     *
     * @param text date text
     * @return parsed date
     * @throws DateTimeParseException when the text is not a supported date
     */
    public static LocalDate parseDate(String text) throws DateTimeParseException {
        return LocalDate.parse(normalize(text), DATE_FORMAT);
    }

    /**
     * Returns whether a date or date-time input includes an explicit time.
     *
     * @param text date or date-time text
     * @return true when the text includes a time component
     */
    public static boolean hasTime(String text) {
        return normalize(text).contains(" ");
    }

    /**
     * Converts a parsed value to the storage format.
     *
     * @param value parsed date or date-time value
     * @param hasTime whether the original input included a time
     * @return ISO date or date-time text
     */
    public static String formatForStorage(LocalDateTime value, boolean hasTime) {
        return hasTime ? value.format(DATE_TIME_FORMAT) : value.toLocalDate().toString();
    }

    /**
     * Trims input and collapses repeated whitespace for consistent parsing.
     *
     * @param text raw date or date-time text
     * @return normalized text
     */
    private static String normalize(String text) {
        return text.trim().replaceAll("\\s+", " ");
    }
}
