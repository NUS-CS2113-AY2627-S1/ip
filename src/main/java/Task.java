/**
 * Represents a task with a description and a done/not-done status.
 */



public class Task {

    protected String body;
    protected boolean isDone;

    /**
     * Creates a new task with the given description.
     * The task is not done by default.
     *
     * @param body Description of the task.
     */
    public Task(String body) {
        this.body = body;
        this.isDone = false;
    }

    /**
     * Sets the done status of this task.
     *
     * @param isDone True if the task is done, false otherwise.
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns the done status of this task.
     *
     * @return True if the task is done, false otherwise.
     */
    public boolean getDone() {
        return this.isDone;
    }

    /** Returns whether this task body contains the given text, ignoring case. */
    public boolean contains(String searchTerm) {
        return body.toLowerCase().contains(searchTerm.toLowerCase());
    }
    /**
     * Returns the status icon of this task.
     *
     * @return 'T' for todo, 'D' for deadline, 'E' for event.
     */
    public char getStatusIcon() {
        return ' ';
    }

    @Override
    public String toString() {
        return body;
    }
}
