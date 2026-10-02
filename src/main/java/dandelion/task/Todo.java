package dandelion.task;

/**
 * Represents a task without a specific deadline or event time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo task in display-ready format.
     *
     * @return Display-ready todo representation.
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }
}
