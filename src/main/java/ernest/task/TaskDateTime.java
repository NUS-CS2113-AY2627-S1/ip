package ernest.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a required task date with an optional time.
 */
public final class TaskDateTime {
    /** Format used to show dates to users. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    /** Format used to show dates and times to users. */
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a", Locale.ENGLISH);

    /** Required calendar date. */
    private final LocalDate date;
    /** Optional time of day, or {@code null} for a date-only value. */
    private final LocalTime time;

    /**
     * Creates a date-only task value.
     *
     * @param date calendar date.
     */
    public TaskDateTime(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.time = null;
    }

    /**
     * Creates a task value containing a date and time.
     *
     * @param dateTime calendar date and time.
     */
    public TaskDateTime(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime);
        this.date = dateTime.toLocalDate();
        this.time = dateTime.toLocalTime();
    }

    /**
     * Returns this value in the ISO format used by storage.
     *
     * @return ISO date or date-time text.
     */
    public String toStorageString() {
        return time == null ? date.toString() : LocalDateTime.of(date, time).toString();
    }

    /**
     * Returns whether this value occurs before another value.
     * Dates are always compared. Times are compared only when both values include one.
     *
     * @param other value to compare against.
     * @return true if this value occurs before the other value.
     */
    public boolean isBefore(TaskDateTime other) {
        Objects.requireNonNull(other);
        int dateComparison = date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison < 0;
        }
        return time != null && other.time != null && time.isBefore(other.time);
    }

    /**
     * Returns this value in the user-facing date or date-time format.
     *
     * @return formatted date or date-time text.
     */
    @Override
    public String toString() {
        return time == null
                ? date.format(DISPLAY_DATE_FORMAT)
                : LocalDateTime.of(date, time).format(DISPLAY_DATE_TIME_FORMAT);
    }
}
