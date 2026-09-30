package nico.tasks;

import java.time.LocalDate;

/**
 * Represents a task that starts and ends at specific times,
 * e.g. "project meeting (from: Oct 15 2026, 2:00PM to: Oct 15 2026, 4:00PM)".
 */
public class Event extends Task {
    protected TaskDateTime from;
    protected TaskDateTime to;

    /**
     * Creates an event with the given description, start and end.
     *
     * @param description What the event is about.
     * @param from When the event starts.
     * @param to When the event ends.
     */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns when this event starts.
     *
     * @return The start date or time.
     */
    public TaskDateTime getFrom() {
        return from;
    }

    /**
     * Returns when this event ends.
     *
     * @return The end date or time.
     */
    public TaskDateTime getTo() {
        return to;
    }

    /**
     * Returns whether the event is happening on the given date.
     * If both ends are dates, any day from the start to the end (inclusive) matches.
     * If only one end is a date, only that day matches.
     *
     * @param date The date to check.
     * @return True if the event falls on that date.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        if (from.hasDate() && to.hasDate()) {
            return !date.isBefore(from.getDate()) && !date.isAfter(to.getDate());
        }
        if (from.hasDate()) {
            return from.getDate().equals(date);
        }
        return to.hasDate() && to.getDate().equals(date);
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + SAVE_DELIMITER + from.toSaveString()
                + SAVE_DELIMITER + to.toSaveString();
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}