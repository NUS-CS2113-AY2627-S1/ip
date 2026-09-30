package twitchchat.tasks;

/** Represents a task that can be completed or left incomplete. */
public abstract class Task {

    private String taskName;
    private boolean isDone;

    /** Creates a task with the specified description.
     *
     * @param taskName task description
     */
    public Task(String taskName) {
        this.taskName = taskName;
    }

    /** Returns the task description.
     *
     * @return task description
     */
    public String getTaskName() {
        return taskName;
    }

    /** Updates the task description.
     *
     * @param taskName new task description
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /** Returns the display icon for this task's completion status.
     *
     * @return {@code X} if the task is done, otherwise a blank icon
     */
    public String getStatusIcon() {
        if (this.isDone) {
            return "X";
        }
        return " ";
    }

    /** Returns the icon representing this task type.
     *
     * @return task type icon
     */
    public abstract String getIcon();

    /**
     * Returns whether this task is completed.
     *
     * @return true if the task is completed, otherwise false
     */
    public boolean isDone() {
        return this.isDone;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /** Prints this task in the user-facing task-list format. */
    public abstract void printTask();
}
