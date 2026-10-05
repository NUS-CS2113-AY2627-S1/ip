package bigbrother.task;

/**
 * Represents a task without a specific deadline or event time.
 */
public class ToDo extends Task {
    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text that describes the task
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the formatted representation of this todo task.
     *
     * @return the todo type, status, and description
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }
}
