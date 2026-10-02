package glados.task;

import java.time.LocalDate;

/**
 * Represents an event: a task that starts and ends at specific date/times.
 */
public class Event extends Task {
    /** When the event starts, with or without a time of day. */
    protected TaskDateTime from;

    /** When the event ends, never before it starts. */
    protected TaskDateTime to;

    /**
     * Creates an event with the given description, start, and end.
     *
     * @param description what the task is.
     * @param from when the event starts.
     * @param to when the event ends, not before it starts.
     */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns true if the given date is any day from this event's start date to
     * its end date, inclusive, so a multi-day event falls on every day it spans.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from.getDate()) && !date.isAfter(to.getDate());
    }

    /**
     * Returns this event as a data file line,
     * e.g. "E | 0 | meeting | 2019-08-06 1400 | 2019-08-06 1600".
     */
    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from.toFileString() + " | " + to.toFileString();
    }

    /**
     * Returns this event as shown to the user,
     * e.g. "[E][ ] camp (from: Oct 15 2019 to: Oct 17 2019)".
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
