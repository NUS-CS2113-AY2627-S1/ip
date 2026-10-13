package bobby.task;

/**
 * Represents a task with no specified time restrictions.
 */
public class Todo extends Task {

    /**
     * Creates a Todo task with incomplete status.
     *
     * @param description the description of the task
     */
    public Todo(String description) {
        this(description, false);
    }

    /**
     * Creates a Todo task with a given status.
     *
     * @param description the description of the task
     * @param isDone the status of task's completion
     */
    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Returns a string representation of this Todo task.
     *
     * @return a string containing the task status and description
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
