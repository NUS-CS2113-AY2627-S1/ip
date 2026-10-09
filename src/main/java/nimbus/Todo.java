package nimbus;

/**
 * Represents a task without any date/time attached to it.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns "T", the type icon for a todo task.
     */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}