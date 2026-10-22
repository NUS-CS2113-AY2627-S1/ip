package yuwei.task;

/**
 * A task with only a description and no date or time, e.g. {@code read book}.
 */
public class ToDo extends Task {
    /** Creates a to-do with the given description. */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getType() {
        return "[T]";
    }

    @Override
    public String toFileString() {
        return "T | " + getDoneFlag() + " | " + description;
    }
}
