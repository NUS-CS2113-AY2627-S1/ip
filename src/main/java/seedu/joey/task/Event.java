package seedu.joey.task;

/**
 * Represents a task that starts at a specific date or time
 * and ends at a specific date or time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an Event with the given description and start/end times.
     *
     * @param description Human-readable description of the event.
     * @param from        Start time of the event, as free-form text.
     * @param to          End time of the event, as free-form text.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the type icon for an event, which is {@code "E"}.
     *
     * @return The single-character type icon.
     */
    @Override
    public String getTypeIcon() {
        return "E";
    }

    /**
     * Returns a human-readable representation of this event, appending
     * the start and end times.
     *
     * @return The formatted representation of this event.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the save-file line for this event, in the form
     * {@code E | DONE | DESCRIPTION | FROM | TO}.
     *
     * @return The pipe-delimited save-file line for this event.
     */
    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + from +  " | " + to;
    }
}