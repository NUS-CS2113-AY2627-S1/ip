package glados.task;

/**
 * Represents a todo: a task with no date or time attached to it.
 */
public class Todo extends Task {

    /**
     * Creates a todo with the given description.
     *
     * @param description what the task is.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo as a data file line, e.g. "T | 0 | read book".
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    /**
     * Returns this todo as shown to the user, e.g. "[T][X] read book".
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
