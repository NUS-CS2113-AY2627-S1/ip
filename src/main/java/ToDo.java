/**
 * Represents a todo task.
 */
public class ToDo extends Task {

    /**
     * Constructs a todo task with the given description.
     *
     * @param description description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + getStatusIcon() + getDescription();
    }

    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}
