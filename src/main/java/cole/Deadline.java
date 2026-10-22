package cole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be done by a certain date.
 */
public class Deadline extends Task {

    private LocalDate by;

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description what has to be done
     * @param by the date the task must be done by
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline as shown to the user,
     * e.g. "[D][ ] return book (by: Oct 15 2019)".
     *
     * @return the deadline with its due date in "MMM dd yyyy" format
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the deadline in data file format,
     * e.g. "D | 0 | return book | 2019-10-15".
     * The date is saved as yyyy-mm-dd so it can be read back with {@link LocalDate#parse}.
     *
     * @return one line of the data file
     */
    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by;
    }

}
