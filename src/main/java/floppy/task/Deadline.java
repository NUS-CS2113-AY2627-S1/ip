package floppy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be finished before a given date,
 * for example "return book (by: Oct 15 2019)".
 */
public class Deadline extends Task {

    /** Type code written at the start of a deadline's line in the data file. */
    public static final String FILE_TYPE_CODE = "D";

    /** Type tag shown at the start of a deadline's display form. */
    private static final String TYPE_ICON = "[D]";

    /**
     * How a due date is shown to the user, for example "Oct 15 2019". The locale is fixed
     * so the month name reads the same on every computer.
     */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** The date the task is due. */
    protected LocalDate by;

    /**
     * Constructs a deadline that starts out not done.
     *
     * @param description what the user asked Floppy to remember.
     * @param by          the date the task is due.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the date this task is due.
     *
     * @return the due date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns this deadline tagged with its type and due date, for example
     * {@code [D][ ] return book (by: Oct 15 2019)}.
     *
     * @return the display form of this deadline.
     */
    @Override
    public String toString() {
        return TYPE_ICON + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns this deadline as one line of the data file, for example
     * {@code D | 0 | return book | 2019-10-15}.
     *
     * @return the line to write to the data file.
     */
    @Override
    public String toFileFormat() {
        // LocalDate.toString() writes yyyy-MM-dd, which LocalDate.parse reads back exactly.
        return FILE_TYPE_CODE + FILE_FIELD_SEPARATOR + super.toFileFormat()
                + FILE_FIELD_SEPARATOR + by;
    }
}
