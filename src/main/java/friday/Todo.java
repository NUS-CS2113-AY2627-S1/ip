package friday;

/** A task with only a description and no date or time attached. */
public class Todo extends Task {

    /**
     * Creates a todo that is initially not done.
     *
     * @param description what needs to be done
     */
    public Todo(String description) {
        super(description);
    }

    /** Returns the display form, e.g. {@code [T][ ] read book}. */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + description;
    }

    /** Returns the save-file line, e.g. {@code T | 0 | read book}. */
    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}

