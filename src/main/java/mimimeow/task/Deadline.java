package mimimeow.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Represents a task that must be completed by a specified deadline. */
public class Deadline extends Task implements ScheduledTask {
    private final TaskDateTime by;

    /**
     * Creates a deadline task with the specified description and deadline.
     *
     * @param description description of the task
     * @param by deadline by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        if (by == null || by.isBlank()) {
            throw new InvalidTaskException("A deadline needs a date.");
        }
        this.by = new TaskDateTime(by);
    }

    /** Returns this deadline in the storage format. */
    @Override
    public String toFileString() {
        return "D | " + super.toFileString() + " | " + escapeFileField(by.toFileString());
    }

    /** Returns whether this deadline falls on the specified date. */
    @Override
    public boolean occursOn(LocalDate date) {
        return by.isOn(date);
    }

    /** Returns whether this deadline is due at or after the specified date and time. */
    @Override
    public boolean isUpcomingAt(LocalDateTime dateTime) {
        return !by.isBefore(dateTime);
    }

    /** Returns whether this deadline was due before the specified date and time. */
    @Override
    public boolean isOverdueAt(LocalDateTime dateTime) {
        return by.isBefore(dateTime);
    }

    /** Returns the deadline date and time used for chronological ordering. */
    @Override
    public TaskDateTime getScheduleTime() {
        return by;
    }

    /** Returns this deadline in the user-facing display format. */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
