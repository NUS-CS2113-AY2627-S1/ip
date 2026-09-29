package chillguy.task;

/**
 * Represents a task that needs to be done before a specific date or time.
 */
public class Deadline extends Task {
    private static final String TASK_TYPE_ICON = "D";

    private final String by;

    /**
     * Creates a deadline task that is not done yet.
     *
     * @param description Description of the deadline task.
     * @param by Date or time by which the task should be done.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the due date or time as supplied when the task was created.
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns {@code D}, the icon identifying a deadline task.
     */
    @Override
    public String getTaskTypeIcon() {
        return TASK_TYPE_ICON;
    }

    /**
     * Returns the task's console representation with its due date or time appended.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
