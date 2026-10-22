package bond.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {

    /**
     * Creates a to-do task that is initially not done.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this to-do task in the file storage format.
     *
     * @return Storage form of this to-do task.
     */
    @Override
    public String toDataString() {
        return "T" + DATA_FIELD_DELIMITER + super.toDataString();
    }

    /**
     * Returns the task type, status icon, and description.
     *
     * @return Display form of this to-do task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
