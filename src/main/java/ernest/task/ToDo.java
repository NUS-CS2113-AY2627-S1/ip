package ernest.task;

/**
 * Represents a simple task without a date or time range.
 */
public class ToDo extends Task {
    /**
     * Creates a to-do task.
     *
     * @param description description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns this task formatted as a to-do item.
     *
     * @return display representation of this to-do task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
