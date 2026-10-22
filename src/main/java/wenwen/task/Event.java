package wenwen.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that occurs between a specified start and end time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event that is initially not done.
     *
     * @param description The text describing the event.
     * @param from event start date and time
     * @param to event end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        this(description, from, to, false);
    }

    /**
     * Creates an event with its saved completion state.
     *
     * @param description The text describing the event.
     * @param from event start date and time
     * @param to event end date and time
     * @param isDone Whether the event is completed.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's starting date or time.
     *
     * @return starting date and time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event's ending date or time.
     *
     * @return ending date and time
     */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate startDate = from.toLocalDate();
        LocalDate endDate = to.toLocalDate();
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }
}
