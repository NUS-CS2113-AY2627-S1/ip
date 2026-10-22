package anson;

/**
 * Represents a task with no date or time attached to it.
 */
public class Todo extends Task {
    /**
     * Creates a todo task with the given description.
     *
     * @param description Description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of this todo task.
     *
     * @return Formatted string in the form "[T][status] description".
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the save-file line for this todo task.
     *
     * @return String in the form "T | isDone | description".
     */
    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }
}