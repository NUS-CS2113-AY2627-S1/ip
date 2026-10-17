package noah.task;

/**
 * Represents a task that can be marked as done or not done.
 */
public class Todo extends Task {

    /**
     * Creates a task with the given description.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the task in the format used when displaying the task list.
     *
     * @return Task status followed by its description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
