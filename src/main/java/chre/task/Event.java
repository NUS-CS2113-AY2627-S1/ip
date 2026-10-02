package chre.task;

import java.time.LocalDate;
import chre.util.DateUtil;

/**
 * Event represents a task that starts at a specific date/time and ends at another.
 */
public class Event extends Task {
    private LocalDate from;
    private LocalDate to;

    /**
     * Constructs an Event with the given name, start date, and end date.
     *
     * @param name the task description
     * @param from the start date as a LocalDate
     * @param to the end date as a LocalDate
     */
    public Event(String name, LocalDate from, LocalDate to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date.
     *
     * @return the start date
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns the end date.
     *
     * @return the end date
     */
    public LocalDate getTo() {
        return to;
    }

    /**
     * Returns the event with its type icon, completion status, and date range.
     * Format: [E][X] name (from: MMM dd yyyy to: MMM dd yyyy) if done, or [E][ ] ... if not done
     *
     * @return the formatted event string
     */
    @Override
    public String toString() {
        String fromFormatted = DateUtil.formatDate(from);
        String toFormatted = DateUtil.formatDate(to);
        return "[E]" + super.toString() + " (from: " + fromFormatted + " to: " + toFormatted + ")";
    }
}
