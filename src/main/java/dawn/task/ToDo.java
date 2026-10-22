package dawn.task;

/**
 * Subclass ToDo: tasks without any date/time attached to them (e.g. visit new theme park)
 */
public class ToDo extends Task {
    /**
     * Constructs a new ToDo task with the given description.
     *
     * @param description the details of this background task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Constructs a ToDo task with a specific completion status.
     *
     * @param description the details of this background task.
     * @param isDone {@code true} if the task is already completed.
     */
    public ToDo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Returns the string representation of this ToDo task for UI display.
     *
     * @return the formatted task string.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
