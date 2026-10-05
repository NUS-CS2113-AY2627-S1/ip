package ted.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo with the given description.
     *
     * @param description The todo description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the display form of this todo.
     *
     * @return The todo type, completion status, and description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

