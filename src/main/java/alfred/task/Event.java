package alfred.task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a task that occurs over an inclusive date range.
 */
public class Event extends Task {

    private final ScheduledDateTime from;
    private final ScheduledDateTime to;

    /**
     * Creates an incomplete event with the given description and date range.
     *
     * @param description Description of the event.
     * @param from First date and optional time of the event.
     * @param to Last date and optional time of the event.
     */
    public Event(String description, ScheduledDateTime from, ScheduledDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + from.toStorageString()
                + " | " + to.toStorageString();
    }

    @Override
    public boolean occursOn(LocalDate date) {
        long daysFromStart = ChronoUnit.DAYS.between(from.getDate(), date);
        long daysUntilEnd = ChronoUnit.DAYS.between(date, to.getDate());
        return daysFromStart >= 0 && daysUntilEnd >= 0;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
