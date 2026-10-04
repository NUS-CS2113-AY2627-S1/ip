package dobby.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {

    /**
     * Creates an unfinished Todo.
     *
     * @param description the task's description.
     */
    public Todo(String description) {
        super(description);
    }
}
