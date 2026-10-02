package dandelion.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that has a deadline.
 */
public class Deadline extends Task {
    /** Format used to display deadline dates to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /** Date by which the task should be completed. */
    private final LocalDate by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description Description of the task.
     * @param by Deadline for completing the task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this task's deadline.
     *
     * @return Deadline for completing this task.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns this deadline task in display-ready format.
     *
     * @return Display-ready deadline representation.
     */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] "
                + getDescription() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
