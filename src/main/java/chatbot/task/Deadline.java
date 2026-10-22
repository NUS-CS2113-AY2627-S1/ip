package chatbot.task;

/** Represents a task that must be completed by a specified time. */
public class Deadline extends Task {
    private final String by;

    /** Creates a pending deadline task.
     *
     * @param description task description
     * @param by deadline time
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the deadline time for persistence.
     *
     * @return the deadline time
     */
    public String getBy() {
        return by;
    }

    /** Returns the deadline task type identifier.
     *
     * @return the deadline type identifier
     */
    @Override
    public String getTaskType() {
        return "D";
    }

    /** Returns the description together with the deadline time.
     *
     * @return formatted deadline text
     */
    @Override
    public String getDisplayText() {
        return super.getDisplayText() + " (by: " + by + ")";
    }
}
