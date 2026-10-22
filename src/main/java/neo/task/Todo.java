package neo.task;

/** Represents a todo task without a specific date or time attached. */
public class Todo extends Task {

    /**
     * Creates an incomplete todo task with the specified description.
     *
     * @param description Text that describes the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns a string representation of this todo task for display. */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /** Returns a string representation of this todo task for saving to a file. */
    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }
}