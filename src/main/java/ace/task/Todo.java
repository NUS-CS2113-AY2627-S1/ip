package ace.task;

/**
 * Represents a task without a deadline or an event date.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete to-do task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Creates a to-do task with its saved completion status.
     *
     * @param description description of the task
     * @param isDone whether the task is completed
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String toString() {
        String status = isDone() ? "X" : " ";
        return "[T][" + status + "] " + getLabel();
    }
}
