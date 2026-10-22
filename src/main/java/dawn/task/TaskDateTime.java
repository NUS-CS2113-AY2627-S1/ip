package dawn.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Encapsulates a task date, date-time, or freeform text value.
 */
public class TaskDateTime {
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm", Locale.ENGLISH);

    private final LocalDate date;
    private final LocalDateTime dateTime;
    private final String rawText;

    /**
     * Constructs a TaskDateTime representing just a date.
     *
     * @param date the date payload.
     */
    public TaskDateTime(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.dateTime = null;
        this.rawText = null;
    }

    /**
     * Constructs a TaskDateTime representing a date and time.
     *
     * @param dateTime the date-time payload.
     */
    public TaskDateTime(LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNull(dateTime);
        this.date = null;
        this.rawText = null;
    }

    /**
     * Constructs a TaskDateTime from freeform text when no valid calendar date is parsed.
     *
     * @param rawText the string literal.
     */
    public TaskDateTime(String rawText) {
        this.rawText = Objects.requireNonNull(rawText).trim();
        this.date = null;
        this.dateTime = null;
    }

    /**
     * Checks if this instance contains a time component.
     *
     * @return {@code true} if time is present.
     */
    public boolean hasTime() {
        return dateTime != null;
    }

    /**
     * Checks if this instance contains a calendar date component.
     *
     * @return {@code true} if date or date-time is present.
     */
    public boolean hasDate() {
        return date != null || dateTime != null;
    }

    /**
     * Extracts the local date component from this instance.
     *
     * @return the local date, or {@code null} if this is raw text.
     */
    public LocalDate toLocalDate() {
        if (dateTime != null) {
            return dateTime.toLocalDate();
        }
        return date;
    }

    /**
     * Checks if this instance falls exactly on the given calendar date.
     *
     * @param targetDate the date to compare against.
     * @return {@code true} if dates match exactly.
     */
    public boolean isOnDate(LocalDate targetDate) {
        if (targetDate == null) {
            return false;
        }
        LocalDate thisDate = toLocalDate();
        return thisDate != null && thisDate.equals(targetDate);
    }

    /**
     * Formats this date or time for UI viewing according to English locale standards.
     *
     * @return the formatted display string.
     */
    public String toDisplayString() {
        if (dateTime != null) {
            return dateTime.format(DISPLAY_DATETIME_FORMATTER);
        }
        if (date != null) {
            return date.format(DISPLAY_DATE_FORMATTER);
        }
        return rawText;
    }

    /**
     * Formats this date or time for persistent storage encoding.
     *
     * @return the formatted string suited for line-based saves.
     */
    public String toStorageString() {
        if (dateTime != null) {
            return dateTime.format(STORAGE_DATETIME_FORMATTER);
        }
        if (date != null) {
            return date.format(STORAGE_DATE_FORMATTER);
        }
        return rawText;
    }

    /**
     * Evaluates logical equality based on internal fields matching precisely.
     *
     * @param obj the object to compare against.
     * @return {@code true} if logically equivalent, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TaskDateTime other)) {
            return false;
        }
        return Objects.equals(date, other.date)
                && Objects.equals(dateTime, other.dateTime)
                && Objects.equals(rawText, other.rawText);
    }

    /**
     * Computes a hash code consistent with logical equality.
     *
     * @return standard JDK hash code computed against all member fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, dateTime, rawText);
    }
}
