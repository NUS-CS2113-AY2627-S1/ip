package wallis;

/**
 * Represents a task in the Wallis chatbot.
 * Tracks the description and completion status.
 */

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false; // Tasks start as not done
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // Returns "X" if done, or a blank space if not
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }
}