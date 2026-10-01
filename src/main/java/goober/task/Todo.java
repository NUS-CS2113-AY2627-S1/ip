package goober.task;

/**
 * Represents a task without a deadline or event period.
 */
public class Todo extends Task {

    /**
     * Creates a todo task.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTaskType() {
        return "T";
    }
}
