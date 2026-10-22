/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {

    /** When the task falls due. */
    private final TaskDateTime deadline;

    /**
     * Constructs a deadline task with the given description and deadline.
     *
     * @param description description of the task.
     * @param deadline when the task falls due.
     */
    public Deadline(String description, TaskDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + getStatusIcon()
                + getDescription()
                + " (by: " + deadline + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + deadline.toFileFormat();
    }
}
