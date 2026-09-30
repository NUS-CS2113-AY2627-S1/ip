package xxlilchatxx;

/**
 * Represents a task with no date or time attached.
 */
public class Todo extends Task {
    /**
     * Creates a new todo task with the given description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat() + " | " + description;
    }
}