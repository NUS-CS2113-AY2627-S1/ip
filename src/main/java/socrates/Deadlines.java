package socrates;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that must be completed by a given date.
 */
public class Deadlines extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDate deadline;

    public Deadlines(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    public String getStatusLine() {
        return String.format("[D]" + super.getStatusLine() + " (by: " + this.deadline.format(DISPLAY_FORMAT) + ")");
    }

    public String toSaveFormat() {
        return ("D | " + super.toSaveFormat() + " | " + this.deadline);
    }
}
