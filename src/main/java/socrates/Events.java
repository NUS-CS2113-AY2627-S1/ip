package socrates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that spans a start and end date.
 */
public class Events extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate start;
    private final LocalDate end;

    public Events(String description, LocalDate start, LocalDate end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public String getStatusLine() {
        return String.format("[E]" +
                super.getStatusLine() +
                " (from: " +
                this.start.format(DISPLAY_FORMAT) +
                " to: " +
                this.end.format(DISPLAY_FORMAT) + ")");
    }

    public String toSaveFormat() {
        return ("E | " + super.toSaveFormat() + " | " + this.start + " | " + this.end);
    }
}
