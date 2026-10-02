package foodielover.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import foodielover.parser.DateTimeParser;

/**
 * Represents a deadline task that needs to be done before a specific time.
 */
public class Deadline extends Task {
    /** Raw due date/time string of the deadline. */
    private final String by;

    /** Parsed LocalDate if applicable, otherwise null. */
    private final LocalDate dueDate;

    /** Parsed LocalDateTime if applicable, otherwise null. */
    private final LocalDateTime dueDateTime;

    /**
     * Constructs a new Deadline task with the specified description and due date/time.
     *
     * @param description Description of the deadline task.
     * @param by Due date/time string of the deadline.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.dueDateTime = DateTimeParser.parseDateTime(by);
        this.dueDate = DateTimeParser.resolveDate(by, this.dueDateTime);
    }

    /**
     * Checks if this deadline is due on the specified date.
     *
     * @param targetDate Date to compare against.
     * @return True if the deadline date matches the target date, false otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate targetDate) {
        return dueDate != null && dueDate.equals(targetDate);
    }

    /**
     * Returns the parsed due date, or null if no valid date was parsed.
     *
     * @return Parsed LocalDate or null.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the parsed due date-time, or null if no valid date-time was parsed.
     *
     * @return Parsed LocalDateTime or null.
     */
    public LocalDateTime getDueDateTime() {
        return dueDateTime;
    }

    /**
     * Returns the formatted string representation of the deadline task for file storage.
     *
     * @return Formatted deadline string for file storage.
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by;
    }

    /**
     * Returns a string representation of the deadline task.
     *
     * @return Formatted deadline task string.
     */
    @Override
    public String toString() {
        String displayBy = DateTimeParser.formatDisplay(by, dueDate, dueDateTime);
        return "[D]" + super.toString() + " (by: " + displayBy + ")";
    }
}
