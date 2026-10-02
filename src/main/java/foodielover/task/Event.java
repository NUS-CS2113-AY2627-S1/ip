package foodielover.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import foodielover.parser.DateTimeParser;

/**
 * Represents an event task that occurs within a specific time period.
 */
public class Event extends Task {
    /** Raw start date/time string of the event. */
    private final String from;

    /** Raw end date/time string of the event. */
    private final String to;

    /** Parsed start date if applicable, otherwise null. */
    private final LocalDate startDate;

    /** Parsed start date-time if applicable, otherwise null. */
    private final LocalDateTime startDateTime;

    /** Parsed end date if applicable, otherwise null. */
    private final LocalDate endDate;

    /** Parsed end date-time if applicable, otherwise null. */
    private final LocalDateTime endDateTime;

    /**
     * Constructs a new Event task with the specified description and start/end time.
     *
     * @param description Description of the event task.
     * @param from Start date/time of the event.
     * @param to End date/time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;

        this.startDateTime = DateTimeParser.parseDateTime(from);
        this.startDate = DateTimeParser.resolveDate(from, this.startDateTime);

        this.endDateTime = DateTimeParser.parseDateTime(to);
        this.endDate = DateTimeParser.resolveDate(to, this.endDateTime);
    }

    /**
     * Checks if this event occurs on the specified date.
     *
     * @param targetDate Date to compare against.
     * @return True if the event spans or occurs on the target date, false otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate targetDate) {
        if (startDate != null && endDate != null) {
            return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
        } else if (startDate != null) {
            return startDate.equals(targetDate);
        } else if (endDate != null) {
            return endDate.equals(targetDate);
        }
        return false;
    }

    /**
     * Returns the formatted string representation of the event task for file storage.
     *
     * @return Formatted event string for file storage.
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from + " | " + to;
    }

    /**
     * Returns a string representation of the event task.
     *
     * @return Formatted event task string.
     */
    @Override
    public String toString() {
        String displayFrom = DateTimeParser.formatDisplay(from, startDate, startDateTime);
        String displayTo = DateTimeParser.formatDisplay(to, endDate, endDateTime);
        return "[E]" + super.toString() + " (from: " + displayFrom + " to: " + displayTo + ")";
    }
}
