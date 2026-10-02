package chre.task;

/**
 * Task represents a single task with a description and completion status.
 */
public class Task {
    private String name;
    private boolean isDone;

    /**
     * Constructs a Task with the given name.
     * Initially marked as not done.
     *
     * @param name the task description
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    /**
     * Returns whether this task is done.
     *
     * @return true if done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks this task as done or not done.
     *
     * @param done true to mark as done, false to mark as not done
     */
    public void setDone(boolean done) {
        isDone = done;
    }

    /**
     * Returns the task name.
     *
     * @return the task description
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the task name.
     *
     * @param name the task description
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the task with its completion status (without type icon).
     * Format: [X] name (if done) or [ ] name (if not done)
     *
     * @return the task string with status indicator
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return status + " " + name;
    }
}
