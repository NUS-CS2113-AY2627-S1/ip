package goober.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that takes place during a specified time period.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter WHOLE_HOUR_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("ha", Locale.ENGLISH);
    private static final DateTimeFormatter MINUTE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description Description of the event.
     * @param from        Start date and time of the event.
     * @param to          End date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start date and time.
     *
     * @return Event start date and time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event end date and time.
     *
     * @return Event end date and time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    protected String getTaskType() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + formatDateTime(from)
                + " to: " + formatDateTime(to) + ") [" + getEventStatus() + "]";
    }

    /**
     * Determines the event status using the current Singapore time.
     *
     * @return Upcoming, ongoing, or ended status.
     */
    private String getEventStatus() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Singapore"));
        if (now.isAfter(to)) {
            return "ENDED";
        }
        if (now.isBefore(from)) {
            return "UPCOMING";
        }
        return "ONGOING";
    }

    /**
     * Formats an event date and time for display.
     *
     * @param dateTime Date and time to format.
     * @return Human-readable date and time.
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter timeFormat = dateTime.getMinute() == 0
                ? WHOLE_HOUR_DISPLAY_FORMAT
                : MINUTE_DISPLAY_FORMAT;
        String formattedTime = dateTime.format(timeFormat).toLowerCase(Locale.ENGLISH);
        return dateTime.format(DATE_DISPLAY_FORMAT) + " " + formattedTime;
    }

}
