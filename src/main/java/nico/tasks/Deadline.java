package nico.tasks;

import java.time.LocalDate;

/**
 * Represents a task that must be done by a certain date or time,
 * e.g. "return book (by: Oct 15 2026)".
 */
public class Deadline extends Task {
    protected TaskDateTime by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description What the deadline is about.
     * @param by When the task must be done by.
     */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns when this deadline is due.
     *
     * @return The due date or time.
     */
    public TaskDateTime getBy() {
        return by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.hasDate() && by.getDate().equals(date);
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + SAVE_DELIMITER + by.toSaveString();
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}