package bill.task;

/**
 * Defines the shared completion state and display behaviour of a task.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Creates a task with the given description and completion state.
     *
     * @param description Description of the task.
     * @param isDone Whether the task is completed.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return True if the task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns this task's description.
     *
     * @return Description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the letter used to identify this task's type.
     *
     * @return Task type icon.
     */
    protected abstract String getTypeIcon();

    /**
     * Returns any timing information displayed after the task description.
     *
     * @return Additional display details.
     */
    protected String getDetails() {
        return "";
    }

    /**
     * Returns the task status and description for display.
     *
     * @return Display form of this task.
     */
    @Override
    public String toString() {
        String statusIcon = isDone ? "X" : " ";
        return "[" + getTypeIcon() + "][" + statusIcon + "] " + description + getDetails();
    }
}
