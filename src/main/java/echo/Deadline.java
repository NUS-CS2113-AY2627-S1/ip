package echo;

import java.time.LocalDate;

/**
 * Represents a deadline: a task that must be done by a certain date.
 */
public class Deadline extends Task {
    /** The date the task must be done by. */
    private final LocalDate by;

    /**
     * Creates a deadline that is initially not done.
     *
     * @param description what needs to be done.
     * @param by the date it must be done by.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline as {@code [D]<base task string> (by: <date>)},
     * with the date shown as e.g. {@code Oct 15 2019}.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Dates.format(by) + ")";
    }

    /** Returns this deadline's save-file line, e.g. {@code D | 0 | return book | 2019-10-15}. */
    @Override
    protected String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by;
    }

    /**
     * Returns whether this deadline is due on the given date.
     *
     * @param date the date to check.
     */
    @Override
    public boolean isOn(LocalDate date) {
        return by.equals(date);
    }
}
