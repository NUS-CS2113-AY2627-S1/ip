package yuwei.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that must be done by a certain date, e.g. {@code return book} by {@code 2019-10-15}.
 */
public class Deadline extends Task {
    /**
     * Format used to show the date to the user, e.g. {@code Oct 15 2019}.
     * The locale is fixed to English so month names look the same on every computer,
     * whatever language its operating system is set to.
     */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Stored as a LocalDate rather than a String, so the chatbot understands it as a date. */
    private final LocalDate by;

    /**
     * Creates a deadline.
     *
     * @param description what must be done
     * @param by the date by which it must be done
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getType() {
        return "[D]";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /** Saves the date as {@code yyyy-mm-dd}, the format {@link LocalDate#parse} reads back. */
    @Override
    public String toFileString() {
        return "D | " + getDoneFlag() + " | " + description + " | " + by;
    }
}
