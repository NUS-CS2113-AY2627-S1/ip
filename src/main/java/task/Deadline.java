package task;

import java.time.LocalDateTime;

/** Represents a todo with a required deadline. */
public class Deadline extends Todo {
    /** Date and time by which the task should be completed. */
    private LocalDateTime deadline;

    /** Creates a deadline task.
     * @param description task description
     * @param deadline date and time stored for the deadline
     */
    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    /** Formats the deadline for task-list output.
     * @return task display text with the deadline appended
     */
    public String toString() {
        super.toString();
        return super.toString() + String.format(" (by: %s)", getDeadline());
    }

    /** Returns the deadline identifier used in task display.
     * @return {@code 'D'}
     */
    public char getIdentifier() {
        return 'D';
    }

    /** Returns the stored deadline.
     * @return deadline date and time
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /** Replaces the stored deadline.
     * @param by new deadline date and time
     */
    public void setDeadline(LocalDateTime by) {
        this.deadline = by;
    }
}
