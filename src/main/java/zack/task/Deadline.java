package zack.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/** Represents a task that must be completed by a specified date. */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Creates an initially incomplete task with a deadline date.
     *
     * @param description description of the task
     * @param by deadline date in ISO yyyy-MM-dd format
     * @throws IllegalArgumentException if the date cannot be parsed
     */
    public Deadline(String description, String by) {
        super(description);
        try {
            this.by = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Use a valid date in yyyy-MM-dd format, for example: 2026-10-02.", e);
        }
    }

    /**
     * Returns the deadline in the standard format used for storage.
     *
     * @return deadline date as an ISO date string
     */
    public String getBy() {
        return by.toString();
    }

    /**
     * Returns the deadline as a date object for date-based queries.
     *
     * @return deadline date
     */
    public LocalDate getDueDate() {
        return by;
    }

    /**
     * Returns the text used to display this task.
     *
     * @return description with completion status and any type-specific details
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + by.format(DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH)) + ")";
    }
}