package task;

/**
 * Represents a task without a deadline or scheduled time.
 */
public class ToDo extends Task {
    /**
     * Creates a to-do task with the given description.
     *
     * @param description Description of the to-do task.
     */
    public ToDo(String description) {
        super(description, TaskType.TODO);
    }

    /** Returns a display-friendly representation of this to-do task. */
    /**
     * Returns a display-friendly representation of this to-do task.
     *
     * @return Display text for this to-do task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
