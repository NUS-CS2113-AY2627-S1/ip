package goober.task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter WHOLE_HOUR_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("ha", Locale.ENGLISH);
    private static final DateTimeFormatter MINUTE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

    private final LocalDateTime by;

    /**
     * Creates a deadline task.
     *
     * @param description Description of the task.
     * @param by          Date and time by which the task should be completed.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the deadline date and time.
     *
     * @return Deadline date and time.
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    protected String getTaskType() {
        return "D";
    }

    @Override
    public String toString() {
        String formattedDate = by.format(DATE_DISPLAY_FORMAT);
        DateTimeFormatter timeFormat = by.getMinute() == 0
                ? WHOLE_HOUR_DISPLAY_FORMAT
                : MINUTE_DISPLAY_FORMAT;
        String formattedTime = by.format(timeFormat).toLowerCase(Locale.ENGLISH);
        String status = by.isBefore(LocalDateTime.now(ZoneId.of("Asia/Singapore")))
                ? " [OVERDUE]"
                : "";
        return super.toString() + " (by: " + formattedDate + " " + formattedTime + ")" + status;
    }

}
