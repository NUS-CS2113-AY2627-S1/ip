package floppy.task;

/**
 * Represents a task with no date or time attached to it,
 * for example "borrow book".
 */
public class Todo extends Task {

    /** Type code written at the start of a todo's line in the data file. */
    public static final String FILE_TYPE_CODE = "T";

    /** Type tag shown at the start of a todo's display form. */
    private static final String TYPE_ICON = "[T]";

    /**
     * Constructs a todo that starts out not done.
     *
     * @param description what the user asked Floppy to remember.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo tagged with its type, for example {@code [T][ ] borrow book}.
     *
     * @return the display form of this todo.
     */
    @Override
    public String toString() {
        return TYPE_ICON + super.toString();
    }

    /**
     * Returns this todo as one line of the data file, for example
     * {@code T | 0 | borrow book}.
     *
     * @return the line to write to the data file.
     */
    @Override
    public String toFileFormat() {
        return FILE_TYPE_CODE + FILE_FIELD_SEPARATOR + super.toFileFormat();
    }
}
