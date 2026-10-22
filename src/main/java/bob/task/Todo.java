package bob.task;

/**
 * Represents a task without a scheduled time.
 */
public class Todo extends Task {
    /**
     * Creates a task with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the todo marker, completion status, and task description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
