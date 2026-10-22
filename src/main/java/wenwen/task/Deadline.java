package wenwen.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline that is initially not done.
     *
     * @param description The text describing the deadline.
     * @param by date by which the task should be completed
     */
    public Deadline(String description, LocalDate by) {
        this(description, by, false);
    }

    /**
     * Creates a deadline with its saved completion state.
     *
     * @param description The text describing the deadline.
     * @param by date by which the task should be completed
     * @param isDone Whether the deadline is completed.
     */
    public Deadline(String description, LocalDate by, boolean isDone) {
        super(description, isDone);
        this.by = by;
    }

    /**
     * Returns the deadline's due date.
     *
     * @return due date
     */
    public LocalDate getBy() {
        return by;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return by.equals(date);
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
