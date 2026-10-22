package yao.task;

/**
 * Represents a Todo task without any date/time attached to it.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description Description of the todo item.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
