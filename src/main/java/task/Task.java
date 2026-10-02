package task;

/** Base class for todos, deadlines, and events tracked by the application. */
public abstract class Task {

    /** Text shown as the task description. */
    private String name;
    /** Whether the task has been completed. */
    private boolean isDone;
    
    /** Creates an incomplete task with the supplied description.
     * @param name task description
     */
    public Task(String name) {
        this.name = name;
        this.isDone = false;
    }

    /** Formats the task for display in lists.
     * @return task identifier, completion marker, and description
     */
    public String toString() {
        char isDone = (this.isDone()) ? 'X' : ' ';
        return String.format("[%c][%c] %s", this.getIdentifier(), isDone, this.getName());
    }

    /** Returns the identifier used for a plain task.
     * @return a blank identifier; subclasses override this value
     */
    public char getIdentifier() {
        return ' ';
    }

    /** Returns the task description.
     * @return description stored for the task
     */
    public String getName() {
        return name;
    }

    /** Replaces the task description.
     * @param name new task description
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Reports whether the task is complete.
     * @return true when the task is marked done
     */
    public boolean isDone() {
        return isDone;
    }

    /** Marks this task complete; called by the mark command.
     */
    public void markDone() {
        this.isDone = true;
    }

    /** Marks this task incomplete; called by the unmark command.
     */
    public void unmarkDone() {
        this.isDone = false;
    }
}
