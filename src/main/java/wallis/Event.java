package wallis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected String from;
    protected String to;
    protected LocalDate fromDate;
    protected LocalDate toDate;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
        this.fromDate = parseDate(from);
        this.toDate = parseDate(to);
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException ex) {
                return null;
            }
        }
    }

    @Override
    public String toString() {
        String fromString = (fromDate != null) 
            ? fromDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) 
            : from;
        String toString = (toDate != null) 
            ? toDate.format(DateTimeFormatter.ofPattern("MMM d yyyy")) 
            : to;
        return "[E]" + super.toString() + " (from: " + fromString + " to: " + toString + ")";
    }
}