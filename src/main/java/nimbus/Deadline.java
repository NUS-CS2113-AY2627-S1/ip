package nimbus;

import java.time.LocalDateTime;

/**
 * Represents a task with a due date and time.
 * Previously saved text deadlines remain readable.
 */
public class Deadline extends Task {
    protected String by;
    private final LocalDateTime dueDateTime;

    /**
     * Creates a deadline from a date/time or legacy saved text.
     *
     * @param description Task description.
     * @param by Due date/time or legacy deadline text.
     */
    public Deadline(String description, String by) {
        super(description);

        LocalDateTime parsedDateTime;
        try {
            parsedDateTime = DateTimes.parse(by);
        } catch (IllegalArgumentException exception) {
            parsedDateTime = null;
        }

        this.dueDateTime = parsedDateTime;
        this.by = parsedDateTime == null
                ? by
                : DateTimes.toStorage(parsedDateTime);
    }

    /**
     * Returns the deadline task type icon.
     *
     * @return Deadline icon.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the task with its readable deadline.
     *
     * @return Formatted deadline task.
     */
    @Override
    public String toString() {
        String displayedDeadline = dueDateTime == null
                ? by
                : DateTimes.format(dueDateTime);

        return super.toString() + " (by: " + displayedDeadline + ")";
    }
}