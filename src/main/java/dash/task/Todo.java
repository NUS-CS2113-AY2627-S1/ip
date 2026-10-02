package dash.task;

/**
 * Represents a task without date or time information.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete to-do task with the specified description.
     *
     * @param description The text describing the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
