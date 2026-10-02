package glados.task;

import java.time.LocalDate;

/**
 * Represents a deadline: a task that needs to be done before a specific date/time.
 */
public class Deadline extends Task {
    /** When the task is due, with or without a time of day. */
    protected TaskDateTime by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description what the task is.
     * @param by when the task is due.
     */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns true if this deadline is due on the given date.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return by.getDate().equals(date);
    }

    /**
     * Returns this deadline as a data file line, e.g. "D | 0 | return book | 2019-12-02 1800".
     */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + by.toFileString();
    }

    /**
     * Returns this deadline as shown to the user,
     * e.g. "[D][ ] return book (by: Dec 02 2019 6:00 PM)".
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
