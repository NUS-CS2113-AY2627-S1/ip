package socrates;

/**
 * A single task with a description and a done/not-done status.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a task, initially marked not done.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    private String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Returns this task's display line, e.g. {@code "[X] read book"}.
     */
    public String getStatusLine() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /**
     * Returns this task encoded for saving to disk, e.g. {@code "1 | read book"}.
     */
    public String toSaveFormat() {
        return String.format("%s | %s", this.isDone ? "1" : "0", this.description);
    }
}
