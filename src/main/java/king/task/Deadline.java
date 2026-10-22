package king.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Represents a task due on a calendar date, displayed with an English month name.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates an incomplete task with a deadline parsed from an ISO date.
     *
     * @param description The text of the task.
     * @param by The deadline in yyyy-MM-dd format.
     * @throws IllegalArgumentException If the deadline is not a valid ISO date.
     */
    public Deadline(String description, String by) {
        super(description);
        try {
            this.by = LocalDate.parse(by);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(
                    "a deadline needs a valid date in yyyy-MM-dd format, for example: 2019-10-15.", exception);
        }
    }

    /**
     * Returns the calendar date on which this task is due.
     *
     * @return Immutable deadline date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the task description with a formatted deadline, e.g. Oct 15 2019.
     *
     * @return Description formatted for display.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the single-character icon identifying this task type.
     *
     * @return Task type icon.
     */
    @Override
    public String getTaskType() {
        return "D";
    }
}
