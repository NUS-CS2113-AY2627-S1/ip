package bryte.task;

import java.time.LocalDateTime;

import bryte.exception.BryteException;
import bryte.util.DateTimeUtil;

/**
 * Represents a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {

    /** The parsed date and time the task is due, or null if unparseable. */
    protected LocalDateTime dueDate;
    /** The raw due date string provided by the user or file. */
    protected String rawDueDate;

    /**
     * Constructs a new {@code Deadline} task with the specified description and deadline.
     *
     * @param description The description or name of the task.
     * @param dueDateString The date/time the task needs to be done by.
     */
    public Deadline(String description, String dueDateString) {
        super(description, false);
        this.rawDueDate = dueDateString.trim();
        try {
            this.dueDate = DateTimeUtil.parse(dueDateString);
        } catch (BryteException e) {
            this.dueDate = null;
        }
    }

    /**
     * Constructs a new {@code Deadline} task with a parsed LocalDateTime.
     * Used for new tasks where date format is strictly enforced.
     *
     * @param description The description or name of the task.
     * @param dueDate The parsed LocalDateTime.
     */
    public Deadline(String description, LocalDateTime dueDate) {
        super(description, false);
        this.dueDate = dueDate;
        this.rawDueDate = DateTimeUtil.formatForStorage(dueDate);
    }

    /**
     * Returns the string representation of the Deadline task.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        if (dueDate != null) {
            return "[D]" + super.toString() + " (by: " + DateTimeUtil.formatForDisplay(dueDate) + ")";
        }
        return "[D]" + super.toString() + " (by: " + rawDueDate + ")";
    }

    /**
     * Returns the string representation of the Deadline task for file storage.
     *
     * @return The formatted string for saving to a file.
     */
    @Override
    public String toFileFormat() {
        if (dueDate != null) {
            return "D | " + super.toFileFormat() + " | " + DateTimeUtil.formatForStorage(dueDate);
        }
        return "D | " + super.toFileFormat() + " | " + rawDueDate;
    }

    /**
     * Checks if the deadline occurs on the specified date.
     *
     * @param date The date to check.
     * @return {@code true} if the deadline is on the date, {@code false} otherwise.
     */
    @Override
    public boolean isOnDate(java.time.LocalDate date) {
        if (dueDate != null) {
            return dueDate.toLocalDate().isEqual(date);
        }
        return false;
    }
}
