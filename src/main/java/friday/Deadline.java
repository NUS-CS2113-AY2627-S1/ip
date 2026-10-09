package friday;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** A task that must be done by a given date. */
public class Deadline extends Task {

    /** Human-friendly format used when showing the date to the user, e.g. "Dec 02 2019". */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate by;

    /**
     * Creates a deadline that is initially not done.
     *
     * @param description what needs to be done
     * @param by          the date it is due
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /** Returns the display form, e.g. {@code [D][ ] return book (by: Dec 02 2019)}. */
    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] "
                + description + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Stores {@code by} in LocalDate's default ISO format (yyyy-MM-dd)
     * rather than the display format above, so it can always be parsed
     * back unambiguously with {@link LocalDate#parse(CharSequence)}.
     */
    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}