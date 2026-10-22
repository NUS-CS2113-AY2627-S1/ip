package seedu.potato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Parses and formats task dates using the date formats supported by Potato.
 */
final class DateParser {
    static final String DATE_FORMAT_HINT =
            "[date formats: DD-MM-YYYY or DD/MM/YYYY; optional time format: HH:mm]";

    private static final DateTimeFormatter DASH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SLASH_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DASH_DATE_TIME_FORMAT = createDateTimeFormatter("dd-MM-uuuu");
    private static final DateTimeFormatter SLASH_DATE_TIME_FORMAT = createDateTimeFormatter("dd/MM/uuuu");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private DateParser() {
    }

    /**
     * Parses a date written with dashes or slashes.
     *
     * @param dateText Date text to parse.
     * @return Parsed calendar date.
     * @throws DateTimeParseException If the text is not a valid supported date.
     */
    static LocalDate parse(String dateText) {
        try {
            return LocalDate.parse(dateText, DASH_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            return LocalDate.parse(dateText, SLASH_DATE_FORMAT);
        }
    }

    /**
     * Parses a date followed by an optional 24-hour time.
     * A missing time is represented as midnight.
     *
     * @param dateTimeText Date and optional time text to parse.
     * @return Parsed date and time.
     * @throws DateTimeParseException If the text is not a valid supported date and time.
     */
    static LocalDateTime parseDateTime(String dateTimeText) {
        try {
            return LocalDateTime.parse(dateTimeText, DASH_DATE_TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            return LocalDateTime.parse(dateTimeText, SLASH_DATE_TIME_FORMAT);
        }
    }

    /**
     * Formats a date in Potato's canonical dash-separated form.
     *
     * @param date Date to format.
     * @return Date formatted as {@code DD-MM-YYYY}.
     */
    static String format(LocalDate date) {
        return date.format(DASH_DATE_FORMAT);
    }

    /**
     * Formats a date and its optional time in Potato's canonical form.
     * Midnight is omitted so date-only input remains date-only when saved.
     *
     * @param dateTime Date and time to format.
     * @return Date formatted as {@code DD-MM-YYYY} with an optional {@code HH:mm} time.
     */
    static String format(LocalDateTime dateTime) {
        String formattedDate = format(dateTime.toLocalDate());
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return formattedDate;
        }
        return formattedDate + " " + dateTime.format(TIME_FORMAT);
    }

    /**
     * Formats a date for display using an abbreviated English month name.
     *
     * @param date Date to format.
     * @return Date formatted as {@code DD MMM YYYY}.
     */
    static String formatForDisplay(LocalDate date) {
        return date.format(DISPLAY_DATE_FORMAT);
    }

    /**
     * Formats a date and its optional time for display.
     * Midnight is omitted so date-only tasks keep their existing display form.
     *
     * @param dateTime Date and time to format.
     * @return Date formatted as {@code DD MMM YYYY} with an optional {@code HH:mm} time.
     */
    static String formatForDisplay(LocalDateTime dateTime) {
        String formattedDate = formatForDisplay(dateTime.toLocalDate());
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return formattedDate;
        }
        return formattedDate + " " + dateTime.format(TIME_FORMAT);
    }

    /**
     * Creates a strict formatter for a date followed by an optional 24-hour time.
     *
     * @param datePattern Pattern used for the date portion.
     * @return Formatter that defaults an omitted time to midnight.
     */
    private static DateTimeFormatter createDateTimeFormatter(String datePattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(datePattern)
                .optionalStart()
                .appendLiteral(' ')
                .appendPattern("HH:mm")
                .optionalEnd()
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
    }
}
