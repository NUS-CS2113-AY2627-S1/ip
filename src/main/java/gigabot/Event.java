package gigabot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task that starts and ends at specific dates.
 */
public class Event extends Task {
    protected LocalDate from;
    protected LocalDate to;

    /**
     * Creates a new Event task.
     *
     * @param description The text description of the event.
     * @param fromStr The start date (in yyyy-mm-dd format).
     * @param toStr The end date (in yyyy-mm-dd format).
     * @throws GigaBotException If the date formats are invalid.
     */
    public Event(String description, String fromStr, String toStr) throws GigaBotException {
        super(description);
        try {
            this.from = LocalDate.parse(fromStr);
            this.to = LocalDate.parse(toStr);
        } catch (DateTimeParseException e) {
            throw new GigaBotException("Please enter event dates in yyyy-mm-dd format (e.g., 2019-10-15).");
        }
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " to: " + to.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ")";
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from + " | " + to;
    }
}