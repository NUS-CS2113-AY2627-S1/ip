package luke.task;

import java.time.LocalDate;

import luke.util.DateTimeUtil;

/**
 * Represents a task that must be completed by a specific date or time.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description Description of the deadline.
     * @param by Due date of the deadline.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTimeUtil.formatDate(by) + ")";
    }

    @Override
    public String toDataString() {
        String status = isDone() ? "1" : "0";

        return "D | " + status + " | " + getDescription() + " | " + by;
    }
}
