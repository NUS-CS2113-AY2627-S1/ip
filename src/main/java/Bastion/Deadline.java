package Bastion;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** A task that has a deadline. */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu");
    private static final DateTimeFormatter DATE_TIME_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd uuuu h:mm a");
    private final LocalDateTime by;

    /**
     * Creates a deadline task with a structured date and time.
     *
     * @param description task description
     * @param by date and time by which the task is due
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /** Returns this deadline in the format used in the user interface. */
    @Override
    public String toString() {
        DateTimeFormatter formatter = by.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? DATE_DISPLAY_FORMAT : DATE_TIME_DISPLAY_FORMAT;
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }

    /** Returns this deadline in the format used for persistence. */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
