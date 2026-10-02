package nova.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    /** Format used when showing the date to the user, e.g. "Oct 15 2026". */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    protected LocalDate by;

    /**
     * Creates a new Deadline with the given description and due date.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the type icon for a Deadline task.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the display representation, with the date in a readable format.
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns this deadline as a save-file line. The date is saved in
     * yyyy-mm-dd format so it can be read back with LocalDate.parse.
     */
    @Override
    public String toSaveString() {
        return super.toSaveString() + " | " + by;
    }
}