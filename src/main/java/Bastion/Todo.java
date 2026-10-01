package Bastion;

/** Represents a task without a date, time, or duration. */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns this todo in the format used in the user interface. */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
    
}
