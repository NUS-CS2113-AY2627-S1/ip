package clammy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Represents a task that has a deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_ISO = createInputFormatter("uuuu-MM-dd");
    private static final DateTimeFormatter INPUT_DAY_FIRST = createInputFormatter("d/M/uuuu");
    private static final DateTimeFormatter STORAGE_DATE = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private static final DateTimeFormatter STORAGE_DATE_TIME = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm");
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM dd uuuu, h:mm a", Locale.ENGLISH);

    private final LocalDateTime by;
    /**
     * Distinguishes a date-only deadline from an explicitly supplied midnight time.
     */
    private final boolean hasTime;

    /**
     * Creates an incomplete task with a deadline.
     *
     * @param description Description of the task.
     * @param by Date in yyyy-MM-dd or d/M/yyyy format, optionally followed by HHmm.
     * @throws IllegalArgumentException If the date or time is invalid.
     */
    public Deadline(String description, String by) {
        super(description);
        String normalizedBy = by.trim().replaceAll("\\s+", " ");
        DateTimeFormatter formatter = normalizedBy.contains("/") ? INPUT_DAY_FIRST : INPUT_ISO;
        try {
            this.by = LocalDateTime.parse(normalizedBy, formatter);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid deadline. Use yyyy-MM-dd or d/M/yyyy,"
                    + " optionally followed by HHmm (e.g., 2/12/2019 1800).", exception);
        }
        this.hasTime = normalizedBy.contains(" ");
    }

    /**
     * Returns the deadline, using midnight when the user supplied only a date.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns an unambiguous date for saving, preserving whether a time was supplied.
     */
    public String toStorageString() {
        return by.format(hasTime ? STORAGE_DATE_TIME : STORAGE_DATE);
    }

    /**
     * Creates a strict parser that uses midnight for dates without a time.
     */
    private static DateTimeFormatter createInputFormatter(String datePattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(datePattern + "[ HHmm]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .toFormatter(Locale.ENGLISH)
                .withResolverStyle(ResolverStyle.STRICT);
    }

    /**
     * Returns the deadline type, completion status, description, and formatted date.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(hasTime ? DISPLAY_DATE_TIME : DISPLAY_DATE) + ")";
    }
}
