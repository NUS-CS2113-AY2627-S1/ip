package slay69.task;

/**
 * A task without a deadline or scheduled time.
 */
public class Todo extends Task {

    /**
     * Creates an incomplete todo.
     *
     * @param description text describing the todo
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the displayed task text prefixed with the todo type marker.
     *
     * @return the displayed todo
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the saved task fields prefixed with the todo type marker.
     *
     * @return the serialized todo
     */
    @Override
    public String toStorageString() {
        return "T|" + super.toStorageString();
    }
}
