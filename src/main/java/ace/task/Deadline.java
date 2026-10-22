package ace.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by deadline date
     */
    public Deadline(String description, LocalDate by) {
        this(description, by, false);
    }

    /**
     * Creates a deadline task with its saved completion status.
     *
     * @param description description of the task
     * @param by deadline date
     * @param isDone whether the task has been completed
     */
    public Deadline(String description, LocalDate by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    /**
     * Returns the deadline date.
     *
     * @return the date by which the task is due
     */
    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        String status = isDone() ? "X" : " ";
        return "[D][" + status + "] " + getLabel()
                + " (by: " + by.format(DISPLAY_DATE) + ")";
    }
}
