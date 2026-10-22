package clanky.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Deadline task.
 * A {@code Deadline} extends {@link Task} by adding a due date, which is
 * displayed in a user-friendly format and persists to storage
 */

public class Deadline extends Task{

    protected LocalDate by;

    /**
     * Creates a new Deadline task with the given description and due date.
     *
     * @param description a description of the deadline task
     * @param by          the date by which the task must be completed
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the single-letter type icon used to identify task type
     * in list and save-file output
     *
     * @return "D", representing a Deadline task.
     */
    @Override
    public String getTaskType(){
        return "D";
    }

    /**
     * Returns a user-facing string representation of this task, including
     * its status icon, description, and due date formatted as "MMM dd yyyy"
     * (e.g. "Dec 3 2026").
     *
     * @return the formatted display string for this task.
     */
    @Override
    public String toString(){
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return super.toString() + " (by: " + by.format(outputFormat) + ")";
    }

    /**
     * Returns a string representation of this task suitable for saving to
     * disk. The due date is stored in yyyy-mm-dd format so it can be
     * parsed back exactly via {@link LocalDate#parse(CharSequence)}.
     *
     * @return the save-format string for this task.
     */
    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + by;
    }
}
