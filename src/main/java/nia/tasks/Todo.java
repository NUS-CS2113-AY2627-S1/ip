package nia.tasks;

/** A task with just a description - no date or time attached. */
public class Todo extends Task{
    /** Creates a new, not-done todo with the given description. */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getTag() {
        return "T";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString();
    }
}