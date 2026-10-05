package yanny.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates a new incomplete todo task.
     *
     * @param description the text describing the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the icon representing a todo task.
     *
     * @return the todo task type icon.
     */
    @Override
    public String getTypeIcon() {
        return "T";
    }
}
