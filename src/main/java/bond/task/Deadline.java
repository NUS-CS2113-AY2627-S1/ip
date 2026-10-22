package bond.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline task that is initially not done.
     *
     * @param description Description of the task.
     * @param by Deadline date in yyyy-MM-dd format.
     * @throws DateTimeParseException If the deadline is not a valid date in that format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by);
    }

    /**
     * Returns this deadline task in the file storage format.
     *
     * @return Storage form of this deadline task.
     */
    @Override
    public String toDataString() {
        return "D" + DATA_FIELD_DELIMITER + super.toDataString()
                + DATA_FIELD_DELIMITER + by;
    }

    /**
     * Returns the task type, status icon, description, and deadline.
     *
     * @return Display form of this deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
