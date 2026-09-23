package lumi.task;

/** Represents a task that has no date attached to it. */
public class Todo extends Task {
    private static final String TYPE_ICON = "T";

    /**
     * Creates a todo.
     *
     * @param description What has to be done.
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns the letter that marks a todo in the listing. */
    @Override
    public String getTypeIcon() {
        return TYPE_ICON;
    }
}
