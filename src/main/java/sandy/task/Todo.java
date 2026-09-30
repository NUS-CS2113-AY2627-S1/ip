package sandy.task;

/**
 * Represents a task without an associated date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo with the given description.
     *
     * @param description Text describing the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the user-interface format.
     *
     * @return The task type, completion status, and description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
