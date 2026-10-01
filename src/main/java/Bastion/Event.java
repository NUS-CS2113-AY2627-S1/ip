package Bastion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A task that takes place during a specified period. */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu h:mm a");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event with structured start and end date-times.
     *
     * @param description event description
     * @param from event start date and time
     * @param to event end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns this event in the format used in the user interface. */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(DISPLAY_FORMAT)
                + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }

    /** Returns this event in the format used for persistence. */
    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }
}
