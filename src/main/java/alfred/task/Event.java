package alfred.task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a task that occurs over an inclusive date range.
 */
public class Event extends Task {

    private final ScheduledDateTime eventStart;
    private final ScheduledDateTime eventEnd;

    /**
     * Creates an incomplete event with the given description and date range.
     *
     * @param description Description of the event.
     * @param eventStart First date and optional time of the event.
     * @param eventEnd Last date and optional time of the event.
     */
    public Event(String description, ScheduledDateTime eventStart, ScheduledDateTime eventEnd) {
        super(description);
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + eventStart.toStorageString()
                + " | " + eventEnd.toStorageString();
    }

    @Override
    public boolean occursOn(LocalDate date) {
        long daysFromStart = ChronoUnit.DAYS.between(eventStart.getDate(), date);
        long daysUntilEnd = ChronoUnit.DAYS.between(date, eventEnd.getDate());
        return daysFromStart >= 0 && daysUntilEnd >= 0;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + eventStart + " to: " + eventEnd + ")";
    }
}
