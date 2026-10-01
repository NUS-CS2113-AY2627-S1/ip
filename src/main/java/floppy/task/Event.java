package floppy.task;

/**
 * Represents a task that runs from one point in time to another,
 * for example "project meeting (from: Mon 2pm to: 4pm)".
 *
 * <p>The start and end are kept as free text for now; a later increment
 * teaches Floppy to understand real dates.
 */
public class Event extends Task {

    /** Type code written at the start of an event's line in the data file. */
    public static final String FILE_TYPE_CODE = "E";

    /** Type tag shown at the start of an event's display form. */
    private static final String TYPE_ICON = "[E]";

    /** When the event starts, exactly as the user wrote it. */
    protected String from;

    /** When the event ends, exactly as the user wrote it. */
    protected String to;

    /**
     * Constructs an event that starts out not done.
     *
     * @param description what the user asked Floppy to remember.
     * @param from        when the event starts.
     * @param to          when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event tagged with its type and time span, for example
     * {@code [E][ ] project meeting (from: Mon 2pm to: 4pm)}.
     *
     * @return the display form of this event.
     */
    @Override
    public String toString() {
        return TYPE_ICON + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Returns this event as one line of the data file, for example
     * {@code E | 0 | project meeting | Mon 2pm | 4pm}.
     *
     * @return the line to write to the data file.
     */
    @Override
    public String toFileFormat() {
        return FILE_TYPE_CODE + FILE_FIELD_SEPARATOR + super.toFileFormat()
                + FILE_FIELD_SEPARATOR + from + FILE_FIELD_SEPARATOR + to;
    }
}
