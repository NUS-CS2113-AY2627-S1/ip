package alfred.task;

import java.time.LocalDate;

/**
 * Represents a task that must be completed by a specified date and optional time.
 */
public class Deadline extends Task {

    private final ScheduledDateTime dueDateTime;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description Description of the deadline.
     * @param dueDateTime Date and optional time by which the task must be completed.
     */
    public Deadline(String description, ScheduledDateTime dueDateTime) {
        super(description);
        this.dueDateTime = dueDateTime;
    }

    @Override
    public String toDataString() {
        return "D | " + super.toDataString() + " | " + dueDateTime.toStorageString();
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return dueDateTime.occursOn(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDateTime + ")";
    }
}
