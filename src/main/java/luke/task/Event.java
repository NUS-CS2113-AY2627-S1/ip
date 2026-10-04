package luke.task;

import java.time.LocalDateTime;

import luke.util.DateTimeUtil;

/**
 * Represents a task scheduled between a start and end time.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an incomplete event.
     *
     * @param description Description of the event.
     * @param from Start date and time of the event.
     * @param to End date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + DateTimeUtil.formatDateTime(from)
                + " to: " + DateTimeUtil.formatDateTime(to) + ")";
    }

    @Override
    public String toDataString() {
        String status = isDone() ? "1" : "0";

        return "E | " + status
                + " | " + getDescription()
                + " | " + DateTimeUtil.formatDateTimeForStorage(from)
                + " | " + DateTimeUtil.formatDateTimeForStorage(to);
    }
}
