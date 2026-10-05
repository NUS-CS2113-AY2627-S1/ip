package ernest.task;

/**
 * Represents a task in Ernest's to-do list.
 */
public class Task {
    private String taskName;
    private boolean isDone;

    /**
     * Creates an empty, incomplete task.
     */
    public Task() {
        this("");
    }

    /**
     * Creates an incomplete task with the given description.
     *
     * @param taskName description of the task.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
    }

    /**
     * Returns this task's completion status and description for display.
     *
     * @return display representation of this task.
     */
    @Override
    public String toString() {
        String status = this.isDone ? "[X]" : "[ ]";
        return status + " " + this.taskName;
    }

    /**
     * Returns whether this task is completed.
     *
     * @return true if this task is completed; otherwise false.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns this task's description.
     *
     * @return task description.
     */
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Sets whether this task is completed.
     *
     * @param isDone new completion status.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
