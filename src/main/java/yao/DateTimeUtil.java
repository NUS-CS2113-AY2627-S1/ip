package yao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for parsing and formatting dates and times in Yao.
 */
public class DateTimeUtil {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
        DateTimeFormatter.ofPattern("d-M-yyyy HHmm"),
        DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")
    };

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ofPattern("d/M/yyyy"),
        DateTimeFormatter.ofPattern("d-M-yyyy")
    };

    /**
     * Prevents instantiation of this utility class.
     */
    private DateTimeUtil() {
    }

    /**
     * Container holding a parsed LocalDate and an optional LocalTime.
     */
    public static class ParsedDateTime {
        private final LocalDate date;
        private final LocalTime time;

        /**
         * Constructs a ParsedDateTime with date and optional time.
         *
         * @param date The parsed LocalDate.
         * @param time The parsed LocalTime, or null if only date was provided.
         */
        public ParsedDateTime(LocalDate date, LocalTime time) {
            this.date = date;
            this.time = time;
        }

        /**
         * Returns the date component.
         *
         * @return The LocalDate object.
         */
        public LocalDate getDate() {
            return date;
        }

        /**
         * Returns the time component, if present.
         *
         * @return The LocalTime object, or null if absent.
         */
        public LocalTime getTime() {
            return time;
        }

        /**
         * Formats the date and optional time for user-facing display.
         *
         * @return Formatted string, e.g. "Oct 15 2019" or "Dec 02 2019, 6:00PM".
         */
        public String toDisplayString() {
            if (time != null) {
                return date.format(DISPLAY_DATE_FORMAT) + ", " + time.format(DISPLAY_TIME_FORMAT);
            }
            return date.format(DISPLAY_DATE_FORMAT);
        }

        /**
         * Formats the date and optional time for persistent storage.
         *
         * @return String formatted for file persistence, e.g. "2019-10-15" or "2019-12-02 1800".
         */
        public String toStorageString() {
            if (time != null) {
                return date.toString() + " " + time.format(DateTimeFormatter.ofPattern("HHmm"));
            }
            return date.toString();
        }
    }

    /**
     * Parses an input string into a ParsedDateTime object.
     *
     * @param input Raw date or date-time string.
     * @return The parsed ParsedDateTime instance.
     * @throws YaoException If the input string cannot be parsed.
     */
    public static ParsedDateTime parseDateTime(String input) throws YaoException {
        String trimmed = input.trim();
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(trimmed, formatter);
                return new ParsedDateTime(ldt.toLocalDate(), ldt.toLocalTime());
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate ld = LocalDate.parse(trimmed, formatter);
                return new ParsedDateTime(ld, null);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        throw new YaoException("OOPS!!! Invalid date/time format: '" + trimmed
                + "'. Please use yyyy-MM-dd (e.g. 2019-10-15) or d/M/yyyy HHmm (e.g. 2/12/2019 1800).");
    }

    /**
     * Attempts to parse an input string into a ParsedDateTime without throwing an exception.
     *
     * @param input Raw date or date-time string.
     * @return The ParsedDateTime, or null if parsing fails.
     */
    public static ParsedDateTime tryParseDateTime(String input) {
        try {
            return parseDateTime(input);
        } catch (YaoException e) {
            return null;
        }
    }

    /**
     * Parses a date-only string into a LocalDate.
     *
     * @param input Raw date string.
     * @return The parsed LocalDate.
     * @throws YaoException If the input cannot be parsed as a date.
     */
    public static LocalDate parseDate(String input) throws YaoException {
        String trimmed = input.trim();
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter).toLocalDate();
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }
        throw new YaoException("OOPS!!! Invalid date format: '" + trimmed
                + "'. Please use yyyy-MM-dd (e.g. 2019-10-15) or d/M/yyyy (e.g. 2/12/2019).");
    }

    /**
     * Formats a LocalDate into a human-friendly string (e.g., "Oct 15 2019").
     *
     * @param date The LocalDate to format.
     * @return Formatted date string.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMAT);
    }
}
