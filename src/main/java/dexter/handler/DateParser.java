package dexter.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/** Converts deadline input and saved ISO dates into date-time values. */
public class DateParser {
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Parses yyyy-MM-dd, d/M/uuuu HHmm, or an ISO date-time saved by Storage
     * into a {@link LocalDateTime}. Dates without a time use midnight.
     *
     * @param text the date text entered by the user or loaded from storage
     * @return the parsed date and time
     * @throws java.time.format.DateTimeParseException if the text is not a valid supported date
     */
    public static LocalDateTime parse(String text) {
        text = text.trim();
        if (text.contains("T")) {
            return LocalDateTime.parse(text);
        }
        if (text.contains("/")) {
            return LocalDateTime.parse(text, DATE_TIME_FORMAT);
        }
        return LocalDate.parse(text).atStartOfDay();
    }
}
