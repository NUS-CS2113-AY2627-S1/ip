package gpt;

/**
 * Represents a task stored by the chatbot.
 */
public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        if (isDone()) {
            return "[X]";
        } else {
            return "[ ]";
        }
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return getIcon() + " " + description;
    }
}