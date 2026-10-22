package dawn.task;

import java.time.LocalDate;

/**
 * Subclass Deadline: A task with a due date and description.
 */
public class Deadline extends Task {
    private final TaskDateTime dueDate;

    /**
     * Constructs a new Deadline task with the specified description and due date.
     *
     * @param description the description of the task.
     * @param dueDate the due date of the task.
     */
    public Deadline(String description, TaskDateTime dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    /**
     * Constructs a Deadline task with a specific completion status.
     *
     * @param description the description of the task.
     * @param dueDate the due date of the task.
     * @param isDone {@code true} if the task is already completed.
     */
    public Deadline(String description, TaskDateTime dueDate, boolean isDone) {
        super(description, isDone);
        this.dueDate = dueDate;
    }

    /**
     * Retrieves the due date of this deadline.
     *
     * @return the due date value.
     */
    public TaskDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Checks if this deadline is due on the specified calendar date.
     *
     * @param date the date to check against.
     * @return {@code true} if the deadline falls on the given date, {@code false} otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate date) {
        return dueDate.isOnDate(date);
    }

    /**
     * Returns the string representation of this deadline for UI display.
     *
     * @return the formatted deadline string.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDate.toDisplayString() + ")";
    }
}
