package goatbot.task;

/**
 * Represents a task without a deadline or event time.
 */
public class Todo extends Task {

    /**
     * Creates a todo task with the given description.
     *
     * @param description text that describes the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this task with its todo type indicator.
     *
     * @return display representation of this todo
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns this todo in the pipe-separated storage format.
     *
     * @return stored representation of this todo
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }
}
