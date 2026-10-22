/**
 * Represents a task without a date or time requirement.
 */
public class Todo extends Task {

    /**
     * Creates a to-do task with the supplied description.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats the to-do task for display.
     *
     * @return the formatted to-do task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Formats the to-do task for persistent storage.
     *
     * @return the encoded to-do task
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }

}

