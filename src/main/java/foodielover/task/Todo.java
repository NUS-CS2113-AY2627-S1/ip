package foodielover.task;

/**
 * Represents a todo task without a specific date or time.
 */
public class Todo extends Task {
    /**
     * Constructs a new Todo task with the specified description.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the formatted string representation of the todo task for file storage.
     *
     * @return Formatted todo string for file storage.
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }

    /**
     * Returns a string representation of the todo task.
     *
     * @return Formatted todo task string.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
