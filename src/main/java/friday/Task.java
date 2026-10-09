package friday;

/**
 * Base class for everything in Friday's task list. Holds the state common
 * to all tasks (a description and a done flag); subclasses add their own
 * details such as a deadline date or event timing.
 */
class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description what the task is about
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Returns "X" if the task is done, or a single space if it is not. */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Returns the task's description. */
    public String getDescription() {
        return this.description;
    }

    /**
     * Marks or unmarks the task. Any {@code option} other than "mark" or
     * "unmark" is ignored and leaves the task unchanged.
     *
     * @param option "mark" to set the task as done, "unmark" to set it as not done
     */
    public void changeStatus(String option) {
        if ("mark".equals(option)) {
            this.isDone = true;
        } else if ("unmark".equals(option)) {
            this.isDone = false;
        }
    }

    /**
     * Returns this task encoded as a single line for storage on disk,
     * e.g. "T | 1 | read book". Subclasses override this to add their
     * own fields (such as a deadline date or event timing).
     */
    public String toFileFormat() {
        return "? | " + (isDone ? "1" : "0") + " | " + description;
    }
}
