package aigis.task;

/**
 * Represents a task that occurs between a start and end time.
 */
public class Event extends Task {
    /** The start time of this event. */
    private final String from;
    /** The end time of this event. */
    private final String till;

    /**
     * Creates an unfinished event task.
     *
     * @param description The text describing the task.
     * @param from The start time of the event.
     * @param till The end time of the event.
     * @throws IllegalArgumentException If any argument is null or blank.
     */
    public Event(String description, String from, String till) {
        super(description);
        validateField(from, "Event start time");
        validateField(till, "Event end time");
        this.from = from;
        this.till = till;
    }

    /**
     * Returns the storage type marker for event tasks.
     *
     * @return The event storage type marker.
     */
    @Override
    protected String getStorageType() {
        return "E";
    }

    /**
     * Returns the serialized event description and times.
     *
     * @return The storage details for this event.
     */
    @Override
    protected String getStorageDetails() {
        return getDescription() + " (" + from + " " + till + ")";
    }

    /**
     * Returns the display form of this event.
     *
     * @return The formatted event description and times.
     */
    @Override
    public String toString() {
        return "[E] " + super.toString() + "( from: " + from + " to: " + till + " )";
    }
}
