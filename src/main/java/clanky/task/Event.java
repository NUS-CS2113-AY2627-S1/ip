package clanky.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event task.
 * A {@code Event} extends {@link Task} by adding a start and
 * end date, which is displayed in a user-friendly format and
 * persists to storage.
 */
public class Event extends Task{

    protected LocalDate from, to;

    /**
     * Creates a new Event task with the given description,
     * start date, and end date.
     *
     * @param description a description of the event
     * @param from        date from which the event starts
     * @param to          date that the event ends on
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the single-letter type icon used to identify this task type
     * in list and save-file output
     *
     * @return "E", representing an Event task
     */
    @Override
    public String getTaskType() {
        return "E";
    }

    /**
     * Returns a user-facing string representation of this task, including
     * its status icon, description, start date, and end date formatted as
     * "MMM dd yyyy" (e.g. "Dec 3 2026").
     *
     * @return the formatted display string for this task.
     */
    @Override
    public String toString() {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return super.toString() + " (from: " + from.format(outputFormat) + " to: " + to.format(outputFormat) + ")";
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
        return super.toSaveFormat() + " | " + from + " | " + to;
    }
}
