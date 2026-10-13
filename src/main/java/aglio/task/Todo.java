package aglio.task;

/** Represents a basic task with no date or time attached. */
public class Todo extends Task {

    /**
     * Creates a Todo task with the given description.
     *
     * @param description what the task is about
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
