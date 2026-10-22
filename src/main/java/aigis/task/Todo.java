package aigis.task;

/**
 * Represents a task without a deadline or event time.
 */
public class Todo extends Task {
    /**
     * Creates an unfinished todo task.
     *
     * @param description The text describing the task.
     * @throws IllegalArgumentException If the description is null or blank.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the storage type marker for todo tasks.
     *
     * @return The todo storage type marker.
     */
    @Override
    protected String getStorageType() {
        return "T";
    }

    /**
     * Returns the display form of this todo.
     *
     * @return The formatted todo description.
     */
    @Override
    public String toString() {
        return "[T] " + super.toString();
    }
}
