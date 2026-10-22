package bill.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo with the given description.
     *
     * @param description Description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a todo with the given description and completion state.
     *
     * @param description Description of the todo.
     * @param isDone Whether the todo is completed.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Identifies this task as a todo in task listings.
     *
     * @return The todo type icon.
     */
    @Override
    protected String getTypeIcon() {
        return "T";
    }
}
