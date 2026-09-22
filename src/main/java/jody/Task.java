package jody;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the supplied description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the display marker for this task's completion status.
     *
     * @return {@code [X]} if completed, otherwise {@code [ ]}
     */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether this task is completed.
     *
     * @return {@code true} if completed, otherwise {@code false}
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the completion marker followed by the task description.
     *
     * @return the task's display text
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}