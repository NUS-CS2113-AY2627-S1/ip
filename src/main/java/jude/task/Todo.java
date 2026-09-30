package jude.task;

/**
 * Represents a todo task, which is a task without any date/time attached to it.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task for displaying to the user.
     *
     * @return A string representation of the todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns a string representation of the todo task formatted for saving to a file.
     *
     * @return A string representation of the todo task for file storage.
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}
