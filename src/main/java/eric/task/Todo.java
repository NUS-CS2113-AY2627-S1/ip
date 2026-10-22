package eric.task;

/**
 * A task with no date attached, e.g. "todo read book".
 */
public class Todo extends Task {
    /** One-letter tag of this task type, used in the list and in the save file. */
    public static final String TYPE_ICON = "T";

    /**
     * Creates a todo task.
     *
     * @param description What the task is about.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }
}
