package ernest.task;

/**
 * Represents a task that has a due date and an optional time.
 */
public class Deadline extends Task {
    /** Due date and optional time associated with this task. */
    private final TaskDateTime dueDateTime;

    /**
     * Creates a deadline task.
     *
     * @param description description of the task.
     * @param dueDateTime due date and optional time for the task.
     */
    public Deadline(String description, TaskDateTime dueDateTime) {
        super(description);
        this.dueDateTime = dueDateTime;
    }

    /**
     * Returns this deadline's due date and optional time.
     *
     * @return due date and optional time.
     */
    public TaskDateTime getDueDateTime() {
        return this.dueDateTime;
    }

    /**
     * Returns this task formatted with its deadline.
     *
     * @return display representation of this deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDateTime + ")";
    }
}
