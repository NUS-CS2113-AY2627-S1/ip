package chillguy.task;

/**
 * Represents a task without any date or time.
 */
public class Todo extends Task {
    private static final String TASK_TYPE_ICON = "T";

    /**
     * Creates a todo task that is not done yet.
     *
     * @param description Description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns {@code T}, the icon identifying a todo task.
     */
    @Override
    public String getTaskTypeIcon() {
        return TASK_TYPE_ICON;
    }
}
