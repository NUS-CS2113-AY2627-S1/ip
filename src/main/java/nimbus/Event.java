package nimbus;

import java.time.LocalDateTime;

/**
 * Represents a task with a start and end date/time.
 * Previously saved text events remain readable.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    /**
     * Creates an event from date/time values or legacy saved text.
     *
     * @param description Event description.
     * @param from Start date/time or legacy text.
     * @param to End date/time or legacy text.
     */
    public Event(String description, String from, String to) {
        super(description);

        this.startDateTime = parseSavedDateTime(from);
        this.endDateTime = parseSavedDateTime(to);

        this.from = startDateTime == null
                ? from
                : DateTimes.toStorage(startDateTime);
        this.to = endDateTime == null
                ? to
                : DateTimes.toStorage(endDateTime);
    }

    /**
     * Parses saved date/time text, preserving compatibility with older records.
     */
    private LocalDateTime parseSavedDateTime(String text) {
        try {
            return DateTimes.parse(text);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    /**
     * Returns the event task type icon.
     *
     * @return Event icon.
     */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns the event with readable start and end values.
     *
     * @return Formatted event task.
     */
    @Override
    public String toString() {
        String displayedStart = startDateTime == null
                ? from
                : DateTimes.format(startDateTime);

        String displayedEnd = endDateTime == null
                ? to
                : DateTimes.format(endDateTime);

        return super.toString()
                + " (from: " + displayedStart
                + " to: " + displayedEnd + ")";
    }
}