package alfred.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Represents a task that occurs over an inclusive date range.
 */
public class Event extends Task {

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT
            = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an incomplete event with the given description and date range.
     *
     * @param description Description of the event.
     * @param from First date of the event.
     * @param to Last date of the event.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toDataString() {
        return "E | " + super.toDataString() + " | " + from + " | " + to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        long daysFromStart = ChronoUnit.DAYS.between(from, date);
        long daysUntilEnd = ChronoUnit.DAYS.between(date, to);
        return daysFromStart >= 0 && daysUntilEnd >= 0;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
