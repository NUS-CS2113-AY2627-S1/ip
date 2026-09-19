package buddy.task;

/**
 * Represents a task without any date or time.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description text that describes the task
     */
    public Todo(String description) {
        super(description);
    }
}
