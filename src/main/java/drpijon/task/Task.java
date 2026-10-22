package drpijon.task;

import java.time.LocalDate;

/**
 * Represents a task with a textual description.
 */
public class Task {
    /** Text describing the task. */
    private final String description;
    /** Whether the task has been marked as complete. */
    private boolean isDone;

    /**
     * Creates a task with the specified description.
     *
     * @param description task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets whether this task is done.
     *
     * @param isDone whether the task is done
     */
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    /**
     * Returns whether this task is done.
     *
     * @return true when the task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns whether this task occurs on a date.
     *
     * @param date date to check
     * @return true when this task occurs on the specified date
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }
    
    /**
     * Returns a text representation of this task.
     *
     * @return task description representation
     */
    @Override
    public String toString() {
        return "description: " + description;
    }

    /**
     * Returns the marker used to identify this task type.
     *
     * @return task type marker, or a blank marker for a base task
     */
    public char getTaskType() {
        return ' ';
    }
}
