package hedy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A task that must be finished on a particular date.
 * The date is stored as a {@link LocalDate}, not as the raw text the user typed.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    protected LocalDate by;

    /**
     * Creates a deadline.
     *
     * @param description what needs to be done
     * @param by the date it is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /** Returns the due date. */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Formats a date the same way deadlines are printed, for example {@code Oct 15 2019}.
     *
     * @param date the date to format
     */
    public static String formatDate(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by.format(FILE_FORMAT);
    }

    /**
     * Returns true when this deadline is still not done and its date is before today.
     * A deadline due today is not overdue.
     */
    public boolean isOverdue() {
        return !isDone && by.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        String overdue = isOverdue() ? ", overdue" : "";
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + overdue + ")";
    }
}
