package sandy.task;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a", Locale.ENGLISH);

    private final LocalDateTime dateTime;

    /**
     * Creates an incomplete deadline with a description and due date.
     *
     * @param description Text describing the deadline.
     * @param dateTime The date and time when the deadline is due.
     */
    public Deadline(String description, LocalDateTime dateTime) {
        super(description);
        this.dateTime = dateTime;
    }

    /**
     * Returns the deadline date and time.
     *
     * @return The deadline date and time.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Returns this deadline in the user-interface format.
     *
     * @return The task type, completion status, description, and deadline.
     */
    @Override
    public String toString() {
        String formattedDateTime = dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? dateTime.format(DISPLAY_DATE_FORMAT) : dateTime.format(DISPLAY_DATE_TIME_FORMAT);
        return "[D]" + super.toString() + " (by: " + formattedDateTime + ")";
    }
}
