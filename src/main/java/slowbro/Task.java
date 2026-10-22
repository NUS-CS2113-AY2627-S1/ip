package slowbro;

/** Represents a task that can be marked as done or not done. */
public class Task {
    private static final String DONE_STATUS_ICON = "X";
    private static final String UNDONE_STATUS_ICON = " ";
    private static final String STATUS_FORMAT = "[%s] ";

    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns the status icon for this task. */
    public String getStatusIcon() {
        return isDone ? DONE_STATUS_ICON : UNDONE_STATUS_ICON;
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void unmarkAsDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        return String.format(STATUS_FORMAT, getStatusIcon()) + description;
    }
}
