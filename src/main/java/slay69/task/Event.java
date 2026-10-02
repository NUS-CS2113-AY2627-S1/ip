package slay69.task;

/**
 * A task scheduled between the supplied start and end times.
 */
public class Event extends Task {
    /** Start time text displayed to the user and written to storage. */
    protected String from;

    /** End time text displayed to the user and written to storage. */
    protected String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description text describing the event
     * @param from start time text supplied by the user
     * @param to end time text supplied by the user
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the displayed task text including the event times.
     *
     * @return the displayed event
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns the saved task fields including the event times.
     *
     * @return the serialized event
     */
    @Override
    public String toStorageString() {
        return "E|" + super.toStorageString() + "|" + from + "|" + to;
    }
}
