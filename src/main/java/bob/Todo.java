package bob;

/**
 * Represents a task without a deadline or scheduled time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo task.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the display representation of this todo.
     *
     * @return The todo type, completion status, and description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
