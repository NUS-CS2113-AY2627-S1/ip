package mimimeow.task;

/** Represents a todo task without a specific deadline or time period. */
public class Todo extends Task {

    /**
     * Creates a todo task with the specified description.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns this todo in the storage format. */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    /** Returns this todo in the user-facing display format. */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
