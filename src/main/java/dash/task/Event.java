package dash.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a specified start and end date or time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with its description, start, and end times.
     *
     * @param description The text describing the task.
     * @param from The start date or time.
     * @param to The end date or time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        try {
            LocalDate fromDate = LocalDate.parse(from);
            try {
                LocalDate toDate = LocalDate.parse(to);
                return !date.isBefore(fromDate) && !date.isAfter(toDate);
            } catch (DateTimeParseException exception) {
                return fromDate.equals(date);
            }
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
