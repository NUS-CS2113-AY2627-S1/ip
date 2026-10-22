package yao.task;

import java.time.LocalDate;
import yao.DateTimeUtil;

/**
 * Represents an event task with a start time and an end time.
 */
public class Event extends Task {
    /** Start date/time string of the event. */
    protected String from;
    /** End date/time string of the event. */
    protected String to;
    /** Parsed start date/time container, or null if not a date format. */
    protected DateTimeUtil.ParsedDateTime fromDateTime;
    /** Parsed end date/time container, or null if not a date format. */
    protected DateTimeUtil.ParsedDateTime toDateTime;

    /**
     * Constructs an Event task with description, start time, and end time.
     *
     * @param description Description of the event task.
     * @param from Start date/time string.
     * @param to End date/time string.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
        this.fromDateTime = DateTimeUtil.tryParseDateTime(from);
        this.toDateTime = DateTimeUtil.tryParseDateTime(to);
    }

    /**
     * Returns the formatted start date/time string.
     *
     * @return Formatted start string.
     */
    public String getFrom() {
        return fromDateTime != null ? fromDateTime.toDisplayString() : from;
    }

    /**
     * Returns the formatted end date/time string.
     *
     * @return Formatted end string.
     */
    public String getTo() {
        return toDateTime != null ? toDateTime.toDisplayString() : to;
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        if (fromDateTime == null) {
            return false;
        }
        LocalDate startDate = fromDateTime.getDate();
        if (toDateTime == null) {
            return startDate.equals(date);
        }
        LocalDate endDate = toDateTime.getDate();
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + getFrom() + " to: " + getTo() + ")";
    }

    @Override
    public String toFileFormat() {
        String fromStorage = fromDateTime != null ? fromDateTime.toStorageString() : from;
        String toStorage = toDateTime != null ? toDateTime.toStorageString() : to;
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + fromStorage + " | " + toStorage;
    }
}
