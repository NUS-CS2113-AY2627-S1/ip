package wenwen.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates a todo that is initially not done.
     *
     * @param description The text describing the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a todo with its saved completion state.
     *
     * @param description The text describing the todo.
     * @param isDone Whether the todo is completed.
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }
}
