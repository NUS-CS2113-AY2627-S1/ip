package bobby.task;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a Deadline task with uncompleted status.
     *
     * @param description the description of the task
     * @param by the deadline of the task
     */
    public Deadline(String description, String by) {
        this(description, false, by);
    }

    /**
     * Creates a Deadline task with a given status.
     *
     * @param description the description of the task
     * @param isDone the status of task's completion
     * @param by the deadline for the task
     */
    public Deadline(String description, boolean isDone, String by) {
        super(description, isDone);
        this.by = by;
    }

    /**
     * Returns the deadline of this task.
     *
     * @return the deadline of the task
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns a string representation of this Deadline task.
     *
     * @return a string containing the task description and deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " -> by: " + by;
    }
}
