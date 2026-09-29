package zack.task;

/** Represents a task without a date or time constraint. */
public class Todo extends Task {
    /**
     * Creates an initially incomplete task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the text used to display this task.
     *
     * @return description with completion status and any type-specific details
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
