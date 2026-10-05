package ted.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description The deadline description.
     * @param by The date by which the task must be completed.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the date by which this task must be completed.
     *
     * @return The due date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the display form of this deadline.
     *
     * @return The deadline type, completion status, description, and formatted due date.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}

