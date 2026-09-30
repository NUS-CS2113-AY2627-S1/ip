package nico.tasks;

import nico.NicoException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Represents the date or time attached to a deadline or event.
 * Input written as <code>yyyy-mm-dd</code> (optionally followed by a 24-hour time such as
 * <code>1800</code>) is understood as a real date and shown in a friendlier format,
 * e.g. <code>2026-10-15 1800</code> is shown as <code>Oct 15 2026, 6:00PM</code>.
 * Any other text, e.g. <code>Sunday</code>, is kept exactly as typed.
 */
public class TaskDateTime {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma", Locale.ENGLISH);

    // Text shaped like a date (e.g. 2026-13-45) that fails to parse is a typo, not free text.
    private static final Pattern DATE_LIKE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}( \\d{4})?");

    private final String rawText;
    private final LocalDateTime dateTime;
    private final boolean hasTime;

    private TaskDateTime(String rawText, LocalDateTime dateTime, boolean hasTime) {
        this.rawText = rawText;
        this.dateTime = dateTime;
        this.hasTime = hasTime;
    }

    /**
     * Creates a TaskDateTime from what the user typed.
     *
     * @param text The date text, e.g. <code>2026-10-15</code>, <code>2026-10-15 1800</code>
     *     or <code>Sunday</code>.
     * @return The parsed date and time, or plain text if it is not in a date format.
     * @throws NicoException If the text looks like a date but is not a real one,
     *     e.g. <code>2026-02-30</code>.
     */
    public static TaskDateTime parse(String text) throws NicoException {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(text, INPUT_DATE_TIME_FORMAT);
            return new TaskDateTime(text, dateTime, true);
        } catch (DateTimeParseException e) {
            // Not a date with a time; try a date on its own next.
        }

        try {
            LocalDate date = LocalDate.parse(text, INPUT_DATE_FORMAT);
            return new TaskDateTime(text, date.atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            // Not a date either; decide below whether it is a typo or free text.
        }

        if (DATE_LIKE_PATTERN.matcher(text).matches()) {
            throw new NicoException("'" + text + "' is not a valid date. "
                    + "Use yyyy-mm-dd, optionally followed by a time like 1800.");
        }
        return new TaskDateTime(text, null, false);
    }

    /**
     * Returns whether this holds a real date, rather than free text such as "Sunday".
     *
     * @return True if a date was recognised.
     */
    public boolean hasDate() {
        return dateTime != null;
    }

    /**
     * Returns the calendar date, or null if this is free text.
     *
     * @return The date part, or null.
     */
    public LocalDate getDate() {
        return hasDate() ? dateTime.toLocalDate() : null;
    }

    /**
     * Returns whether this comes strictly before another TaskDateTime.
     * Always false unless both hold real dates.
     *
     * @param other The TaskDateTime to compare with.
     * @return True if both are dates and this one is earlier.
     */
    public boolean isBefore(TaskDateTime other) {
        return hasDate() && other.hasDate() && dateTime.isBefore(other.dateTime);
    }

    /**
     * Returns the text exactly as the user typed it, for saving to disk.
     * Saving the original text means the save file format is unchanged.
     *
     * @return The original text.
     */
    public String toSaveString() {
        return rawText;
    }

    @Override
    public String toString() {
        if (!hasDate()) {
            return rawText;
        }
        return hasTime ? dateTime.format(OUTPUT_DATE_TIME_FORMAT) : dateTime.format(OUTPUT_DATE_FORMAT);
    }
}