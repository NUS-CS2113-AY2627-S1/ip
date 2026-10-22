package dawn.parser;

import dawn.exception.DawnException;
import dawn.task.TaskDateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Parses and validates date and time inputs using strict calendar rules.
 */
public class DateTimeParser {
    public static final String DATE_TIME_USAGE =
            "yyyy-MM-dd, yyyy/MM/dd, d/M/yyyy, dd-MM-yyyy, or d MMM yyyy"
                    + " (optional HHmm time; e.g., 2026-10-12 or 12 Oct 2026 1800)";

    private static final Pattern DATE_PATTERN =
            Pattern.compile("^\\d{1,4}[-/]\\d{1,2}([-/]\\d{1,4})?(\\s+.*)?$");
    private static final Pattern NAMED_MONTH_DATE_PATTERN =
            Pattern.compile("^\\d{1,2}\\s+[A-Za-z]{3,}\\s+\\d{1,4}(?:\\s+.*)?$");

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
        strictFormatter("uuuu-MM-dd HHmm"),
        strictFormatter("uuuu/MM/dd HHmm"),
        strictFormatter("d/M/uuuu HHmm"),
        strictFormatter("dd-MM-uuuu HHmm"),
        strictFormatter("d MMM uuuu HHmm")
    };

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
        strictFormatter("uuuu-MM-dd"),
        strictFormatter("uuuu/MM/dd"),
        strictFormatter("d/M/uuuu"),
        strictFormatter("dd-MM-uuuu"),
        strictFormatter("d MMM uuuu")
    };

    /**
     * Builds a strict English date formatter that accepts either case for month names.
     */
    private static DateTimeFormatter strictFormatter(String pattern) {
        return new DateTimeFormatterBuilder().parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Parses a date or date-time string strictly into a {@link TaskDateTime}.
     *
     * @param input the raw date/time string.
     * @return the parsed {@link TaskDateTime}.
     * @throws DawnException if the input is malformed, has invalid values, or unsupported format.
     */
    public static TaskDateTime parseStrict(String input) throws DawnException {
        if (input == null || input.trim().isEmpty()) {
            throw new DawnException("I need a date. Use: " + DATE_TIME_USAGE);
        }
        String trimmed = input.trim();

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(trimmed, formatter);
                return new TaskDateTime(dateTime);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return new TaskDateTime(date);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        throw new DawnException("I couldn't understand that date or time. Use: " + DATE_TIME_USAGE);
    }

    /**
     * Parses a date flexibly into a {@link TaskDateTime}.
     * If the input matches a date-like pattern, strict parsing is enforced.
     * Otherwise, freeform text is preserved.
     *
     * @param input the raw date string.
     * @return the parsed {@link TaskDateTime}.
     * @throws DawnException if the input is blank or contains an invalid calendar date.
     */
    public static TaskDateTime parseFlexible(String input) throws DawnException {
        if (input == null || input.trim().isEmpty()) {
            throw new DawnException("I need a date. Use: " + DATE_TIME_USAGE);
        }
        String trimmed = input.trim();

        if (DATE_PATTERN.matcher(trimmed).matches()
                || NAMED_MONTH_DATE_PATTERN.matcher(trimmed).matches()) {
            return parseStrict(trimmed);
        }

        return new TaskDateTime(trimmed);
    }

}
