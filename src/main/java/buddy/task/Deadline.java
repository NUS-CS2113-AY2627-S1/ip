package buddy.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that needs to be done before a specific date and time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a", Locale.ENGLISH);

    /** The date and time by which this task should be completed. */
    protected LocalDateTime by;

    /**
     * Creates a new deadline with the given description and due date/time.
     *
     * @param description the text describing this task
     * @param by the date and time by which this task should be done
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + this.by;
    }
}
