package seedu.joey.task;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

/**
 * Represents a task that needs to be done before a specific date or time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    protected LocalDate by;

    /**
     * Creates a Deadline with the given description and due date.
     *
     * @param description Human-readable description of the task.
     * @param by          The due date for the task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the type icon for a deadline, which is {@code "D"}.
     *
     * @return The single-character type icon.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns a human-readable representation of this deadline, appending
     * the due date formatted as {@code MMM dd yyyy} (e.g. "Oct 15 2019").
     *
     * @return The formatted representation of this deadline.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the save-file line for this deadline, in the form
     * {@code D | DONE | DESCRIPTION | yyyy-MM-dd}.
     *
     * @return The pipe-delimited save-file line for this deadline.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + by;
    }
}
