package dobby.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {

    /** The parsed date, when this deadline contains a recognized date. */
    private final LocalDate date;

    /** The parsed date and time, when this deadline contains a recognized time. */
    private final LocalDateTime dateTime;

    /** The original text used when the deadline is not a recognized date or time. */
    private final String originalDeadline;

    /**
     * Creates an unfinished deadline.
     *
     * @param description the task's description.
     * @param deadline the date or time by which the task should be completed.
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.dateTime = DateTimeParser.parseDateTime(deadline);
        this.date = dateTime == null ? DateTimeParser.parseDate(deadline) : dateTime.toLocalDate();
        this.originalDeadline = deadline;
    }

    /**
     * Creates an unfinished deadline for a date.
     *
     * @param description the task's description.
     * @param deadline the date by which the task should be completed.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.date = deadline;
        this.dateTime = null;
        this.originalDeadline = null;
    }

    /**
     * Creates an unfinished deadline for a date and time.
     *
     * @param description the task's description.
     * @param deadline the date and time by which the task should be completed.
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.date = deadline.toLocalDate();
        this.dateTime = deadline;
        this.originalDeadline = null;
    }

    /**
     * Returns the date or time by which this task should be completed.
     *
     * @return the formatted deadline, or the original text for an unrecognized deadline.
     */
    public String getBy() {
        if (dateTime != null) {
            return DateTimeParser.formatDateTime(dateTime);
        }

        if (date != null) {
            return DateTimeParser.formatDate(date);
        }

        return originalDeadline;
    }

    /**
     * Returns the parsed date for this deadline when one is available.
     *
     * @return the deadline date, or {@code null} when the deadline is free-form text.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the parsed date and time for this deadline when one is available.
     *
     * @return the deadline date and time, or {@code null} when no time was supplied.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns this deadline in the compact format used by the user interface.
     *
     * @return the deadline type icon, completion icon, description, and due time.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + description + " (by: " + getBy() + ")";
    }
}
