package gigabot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a deadline task that needs to be completed before a specific date.
 */
public class Deadline extends Task {
    protected LocalDate dueDateTime;

    /**
     * Creates a new Deadline task.
     *
     * @param description The text description of the deadline.
     * @param dueDateTimeStr The date the task must be completed by (in yyyy-mm-dd format).
     * @throws GigaBotException If the date format is invalid.
     */
    public Deadline(String description, String dueDateTimeStr) throws GigaBotException {
        super(description);
        try {
            this.dueDateTime = LocalDate.parse(dueDateTimeStr);
        } catch (DateTimeParseException e) {
            throw new GigaBotException("Please enter the deadline date in yyyy-mm-dd format (e.g., 2019-10-15).");
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + dueDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

    @Override
    public String toSaveFormat() {
        // Saves in yyyy-mm-dd format so Storage can easily load it back
        return "D | " + super.toSaveFormat() + " | " + dueDateTime;
    }
}