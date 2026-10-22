package ace.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task occurring over a range of dates.
 */
public class Event extends Task {
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the event
     * @param from first date of the event
     * @param to last date of the event
     */
    public Event(String description, LocalDate from, LocalDate to) {
        this(description, from, to, false);
    }

    /**
     * Creates an event with its saved completion status.
     *
     * @param description description of the event
     * @param from first date of the event
     * @param to last date of the event
     * @param isDone whether the event has been completed
     */
    public Event(String description, LocalDate from, LocalDate to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the first date of the event.
     *
     * @return event start date
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns the last date of the event.
     *
     * @return event end date
     */
    public LocalDate getTo() {
        return to;
    }

    @Override
    public String toString() {
        String status = isDone() ? "X" : " ";
        return "[E][" + status + "] " + getLabel()
                + " (from: " + from.format(DISPLAY_DATE)
                + " to: " + to.format(DISPLAY_DATE) + ")";
    }
}
