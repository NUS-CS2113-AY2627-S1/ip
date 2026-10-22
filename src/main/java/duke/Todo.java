package duke;

/**
 * Represents a task with no date or time attached.
 */
public class Todo extends Task {

    /**
     * Creates a new todo task.
     *
     * @param description What the task is about.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}