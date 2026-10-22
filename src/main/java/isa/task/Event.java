package isa.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a task that occurs over a time range.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_HOUR_FORMAT =
            DateTimeFormatter.ofPattern("h", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_HOUR_MINUTE_FORMAT =
            DateTimeFormatter.ofPattern("h:mm", Locale.ENGLISH);

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Parses an event time in the required input format.
     *
     * @param value Date and time in yyyy-MM-ddTHH:mm format.
     * @return Parsed date and time.
     * @throws DateTimeParseException If the value is not a valid date and time
     *         in the required format.
     */
    public static LocalDateTime parseTime(String value) {
        return LocalDateTime.parse(value, INPUT_FORMAT);
    }

    /**
     * Creates an event with its description and time range.
     *
     * @param description Description of the event.
     * @param startTime Date and time at which the event starts.
     * @param endTime Date and time at which the event ends.
     * @throws IllegalArgumentException If the end time is before the start time.
     */
    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("event end time must not be before start time");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Checks whether this event occurs on a given date, including either endpoint.
     *
     * @param date Date to check.
     * @return Whether the event spans the date.
     */
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(startTime.toLocalDate())
                && !date.isAfter(endTime.toLocalDate());
    }

    /**
     * Formats an event endpoint as a date with a 12-hour time.
     *
     * @param dateTime Endpoint to display.
     * @return Readable date and time, such as Aug 06 2026 2pm.
     */
    private String formatForDisplay(LocalDateTime dateTime) {
        DateTimeFormatter timeFormat = dateTime.getMinute() == 0
                ? DISPLAY_HOUR_FORMAT : DISPLAY_HOUR_MINUTE_FORMAT;
        String period = dateTime.getHour() < 12 ? "am" : "pm";

        return dateTime.format(DISPLAY_DATE_FORMAT) + " "
                + dateTime.format(timeFormat) + period;
    }

    /**
     * Returns the event in the format used by the data file.
     *
     * @return File representation of this event.
     */
    @Override
    public String toDataString() {
        return getBaseDataString("E")
                + " | " + startTime.format(INPUT_FORMAT)
                + " | " + endTime.format(INPUT_FORMAT);
    }

    /**
     * Returns the event with its start and end times for display.
     *
     * @return Display representation of this event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + formatForDisplay(startTime)
                + " to: " + formatForDisplay(endTime) + ")";
    }
}
