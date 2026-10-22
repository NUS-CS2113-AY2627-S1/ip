package ace.task;

/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    private final String label;
    private boolean isDone;

    /**
     * Creates a task with an empty description.
     */
    public Task() {
        this("");
    }

    /**
     * Creates an incomplete task with the given description.
     *
     * @param taskLabel description of the task
     */
    public Task(String taskLabel) {
        this(taskLabel, false);
    }

    /**
     * Creates a task with the specified description and completion status.
     *
     * @param taskLabel description of the task
     * @param taskStatus whether the task is completed
     */
    public Task(String taskLabel, boolean taskStatus) {
        this.label = taskLabel;
        this.isDone = taskStatus;
    }

    /**
     * Returns the task's description.
     *
     * @return task description
     */
    public String getLabel() {
        return label;
    }

    /**
     * Checks whether the description is blank.
     *
     * @return true if the description is blank
     */
    public boolean isEmpty() {
        return label.isBlank();
    }

    /**
     * Checks whether the task is completed.
     *
     * @return true if the task is completed
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as incomplete.
     */
    public void markAsUndone() {
        isDone = false;
    }
}
