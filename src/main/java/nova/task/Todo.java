package nova.task;
/**
 * Represents a simple task with only a description and no associated dates.
 */

public class Todo extends Task {
    /**
     * Creates a new Todo with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the type icon for a Todo task.
     */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}