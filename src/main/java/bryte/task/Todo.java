package bryte.task;

/**
 * Represents a task without any date/time attached to it.
 */
public class Todo extends Task {

    /**
     * Constructs a new {@code Todo} task with the specified description.
     *
     * @param description The description or name of the task.
     */
    public Todo(String description) {
        super(description, false);
    }

    /**
     * Returns the string representation of the Todo task.
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the string representation of the Todo task for file storage.
     *
     * @return The formatted string for saving to a file.
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}

