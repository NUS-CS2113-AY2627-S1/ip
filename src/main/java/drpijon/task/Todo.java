package drpijon.task;

/**
 * Represents a task that can be marked as done or not done.
 */
public class Todo extends Task {
    /**
     * Creates a not-done task with the specified description.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a text representation including the done status.
     *
     * @return todo task representation
     */
    @Override
    public String toString() {
        String status = this.isDone() ? "Yes" : "No";
        return super.toString() + System.lineSeparator() + "is done? " + status;
    }

    /**
     * Returns the marker used for todo tasks.
     *
     * @return todo task marker
     */
    @Override
    public char getTaskType() {
        return 'T';
    }
}
