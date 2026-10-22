package doug.task;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    /** Due date or time supplied for the deadline. */
    private String by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description description of the deadline
     * @param by due date or time of the deadline
     */
    public Deadline(String description, String by) {
        super(Type.DEADLINE, description);
        this.by = by;
    }

    /**
     * Reconstructs a deadline with an existing completion state.
     *
     * @param description description of the deadline
     * @param by due date or time of the deadline
     * @param isDone whether the deadline has been completed
     */
    public Deadline(String description, String by, boolean isDone) {
        super(Type.DEADLINE, isDone, description);
        this.by = by;
    }

    /**
     * Converts this deadline to its pipe-delimited storage representation.
     *
     * @return serialized deadline containing its type, completion state, description,
     *         and due value
     */
    @Override
    public String toSaveFormat() {
        // Appends the specific 'by' date to the save string
        return String.format("D|%d|%s|%s", isDone ? 1 : 0, description, by);
    }

    /**
     * Returns a display representation that includes the deadline's due value.
     *
     * @return formatted deadline for display to the user
     */
    @Override
    public String toString() {
        // Calls the parent's string output [D][ ] description, then adds the date!
        return super.toString() + " (by: " + by + ")";
    }
}
