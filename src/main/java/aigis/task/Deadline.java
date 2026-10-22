package aigis.task;

import java.time.LocalDate;

import aigis.DateFormats;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The date by which this task should be completed. */
    private final LocalDate due;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description The text describing the task.
     * @param due The date by which the task should be completed.
     * @throws IllegalArgumentException If the description is invalid or the due date is null.
     */
    public Deadline(String description, LocalDate due) {
        super(description);
        if (due == null) {
            throw new IllegalArgumentException("Deadline due date cannot be null.");
        }
        this.due = due;
    }

    /**
     * Returns the date by which this task should be completed.
     *
     * @return The deadline date.
     */
    public LocalDate getDue() {
        return due;
    }

    /**
     * Returns the storage type marker for deadline tasks.
     *
     * @return The deadline storage type marker.
     */
    @Override
    protected String getStorageType() {
        return "D";
    }

    /**
     * Returns the serialized deadline description and due date.
     *
     * @return The storage details for this deadline.
     */
    @Override
    protected String getStorageDetails() {
        return getDescription() + "  (" + due + ")";
    }

    /**
     * Returns the display form of this deadline.
     *
     * @return The formatted deadline description and due date.
     */
    @Override
    public String toString() {
        return "[D] " + super.toString() + " ( by: " + DateFormats.formatDate(due) + " )";
    }
}
