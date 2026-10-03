package turing.task;

/**
 * Represents a task with no date or time attached to it,
 * e.g. {@code visit new theme park}.
 */
public class Todo extends Task {
    /** Icon identifying this kind of task, in both the display and the save file. */
    public static final String TYPE_ICON = "T";

    /**
     * Creates a todo that starts off as not done.
     *
     * @param description What the user has to do.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }
}
