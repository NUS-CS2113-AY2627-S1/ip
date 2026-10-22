package cole;

/**
 * Represents a task with only a description and no date or time.
 */
public class ToDo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description what the todo is about
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the todo as shown to the user, e.g. "[T][ ] read book".
     *
     * @return the todo with its type marker
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the todo in data file format, e.g. "T | 0 | read book".
     *
     * @return one line of the data file
     */
    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }
}
