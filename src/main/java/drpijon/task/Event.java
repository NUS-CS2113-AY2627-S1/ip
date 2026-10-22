package drpijon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task scheduled between a start time and an end time.
 */
public class Event extends Task {
    /** Parsed event start date and time, or null for legacy text. */
    private final LocalDateTime from;
    /** Parsed event end date and time, or null for legacy text. */
    private final LocalDateTime to;
    /** Whether the event start input included an explicit time. */
    private final boolean hasFromTime;
    /** Whether the event end input included an explicit time. */
    private final boolean hasToTime;
    /** Original event start text retained for legacy data. */
    private final String legacyFrom;
    /** Original event end text retained for legacy data. */
    private final String legacyTo;

    /**
     * Creates an event with typed date or date-time boundaries.
     *
     * @param description event description
     * @param from event start date and time
     * @param to event end date and time
     * @param hasFromTime whether the start includes an explicit time
     * @param hasToTime whether the end includes an explicit time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to, boolean hasFromTime, boolean hasToTime) {
        super(description);
        this.from = from;
        this.to = to;
        this.hasFromTime = hasFromTime;
        this.hasToTime = hasToTime;
        this.legacyFrom = null;
        this.legacyTo = null;
    }

    /**
     * Parses an event with typed date or date-time boundaries.
     *
     * @param description event description
     * @param from event start date or date-time text
     * @param to event end date or date-time text
     * @return parsed event
     * @throws DateTimeParseException when either boundary is not supported
     */
    public static Event fromText(String description, String from, String to) throws DateTimeParseException {
        return new Event(description, DateTimeParser.parse(from), DateTimeParser.parse(to),
                DateTimeParser.hasTime(from), DateTimeParser.hasTime(to));
    }

    /**
     * Creates an event from the application's older free-text format.
     *
     * @param description event description
     * @param legacyFrom original start text
     * @param legacyTo original end text
     * @return event that preserves the original text
     */
    public static Event fromLegacy(String description, String legacyFrom, String legacyTo) {
        return new Event(description, null, null, false, false, legacyFrom, legacyTo);
    }

    private Event(String description, LocalDateTime from, LocalDateTime to, boolean hasFromTime, boolean hasToTime,
                   String legacyFrom, String legacyTo) {
        super(description);
        this.from = from;
        this.to = to;
        this.hasFromTime = hasFromTime;
        this.hasToTime = hasToTime;
        this.legacyFrom = legacyFrom;
        this.legacyTo = legacyTo;
    }

    /**
     * Returns the parsed event start.
     *
     * @return event start, or null for a legacy free-text event
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the parsed event end.
     *
     * @return event end, or null for a legacy free-text event
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns whether the event has a parsed start date.
     *
     * @return true when the start uses a typed date or date-time format
     */
    public boolean hasFromDate() {
        return from != null;
    }

    /**
     * Returns whether the event has a parsed end date.
     *
     * @return true when the end uses a typed date or date-time format
     */
    public boolean hasToDate() {
        return to != null;
    }

    /**
     * Returns whether the event start includes an explicit time.
     *
     * @return true when the start has a time component
     */
    public boolean hasFromTime() {
        return hasFromTime;
    }

    /**
     * Returns whether the event end includes an explicit time.
     *
     * @return true when the end has a time component
     */
    public boolean hasToTime() {
        return hasToTime;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        if (!hasFromDate() || !hasToDate()) {
            return false;
        }
        LocalDate fromDate = from.toLocalDate();
        LocalDate toDate = to.toLocalDate();
        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }

    /**
     * Returns the event start in storage format.
     *
     * @return ISO date or date-time text, or the original legacy text
     */
    public String getFromText() {
        return hasFromDate() ? DateTimeParser.formatForStorage(from, hasFromTime) : legacyFrom;
    }

    /**
     * Returns the event end in storage format.
     *
     * @return ISO date or date-time text, or the original legacy text
     */
    public String getToText() {
        return hasToDate() ? DateTimeParser.formatForStorage(to, hasToTime) : legacyTo;
    }

    /**
     * Returns the marker used for event tasks.
     *
     * @return event task marker
     */
    @Override
    public char getTaskType() {
        return 'E';
    }
}
