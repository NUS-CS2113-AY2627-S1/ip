package glados.task;

import java.time.LocalDate;

/**
 * Represents a single task in the task list.
 *
 * <p>A task holds its description and whether it has been completed.
 * Todo, Deadline, and Event are specific kinds of task and extend this class.
 */
public class Task {
    /** What the task is, e.g. "read book". */
    protected String description;

    /** Whether the task has been completed. */
    protected boolean isDone;

    /**
     * Creates a task with the given description, initially not done.
     *
     * @param description what the task is.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of this task: "X" when done, a blank otherwise.
     */
    public String getStatusIcon() {
        if (isDone) {
            return "X";
        }
        return " ";
    }

    /**
     * Returns what this task is, e.g. "read book", without its type or status.
     */
    public String getDescription() {
        return description;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns true if this task falls on the given date.
     *
     * <p>A plain task has no date, so it never does. Subclasses with dates override this.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Returns this task formatted as a line of the data file, e.g. "1 | read book".
     *
     * <p>Subclasses put their type letter in front and append their own fields.
     */
    public String toFileString() {
        String doneFlag;
        if (isDone) {
            doneFlag = "1";
        } else {
            doneFlag = "0";
        }
        return doneFlag + " | " + description;
    }

    /**
     * Returns this task formatted as shown to the user, e.g. "[X] read book".
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
