package maple.task;

/**
 * Represents a task with no additional date or time attached.
 */
public class Todo extends Task {
    /** Type identifier used when saving to and loading from the data file. */
    public static final String TYPE = "T";

    /**
     * Constructs a todo with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[" + TYPE + "]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        return TYPE + " | " + super.toFileFormat();
    }
}
