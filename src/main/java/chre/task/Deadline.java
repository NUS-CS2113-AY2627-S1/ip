package chre.task;

import java.time.LocalDate;
import chre.util.DateUtil;

/**
 * Deadline represents a task that needs to be done by a specific date/time.
 */
public class Deadline extends Task {
    private LocalDate by;

    /**
     * Constructs a Deadline with the given name and deadline date.
     *
     * @param name the task description
     * @param by the deadline as a LocalDate
     */
    public Deadline(String name, LocalDate by) {
        super(name);
        this.by = by;
    }

    /**
     * Returns the deadline.
     *
     * @return the deadline date
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the deadline with its type icon, completion status, and deadline date.
     * Format: [D][X] name (by: MMM dd yyyy) if done, or [D][ ] name (by: MMM dd yyyy) if not done
     *
     * @return the formatted deadline string
     */
    @Override
    public String toString() {
        String formattedDate = DateUtil.formatDate(by);
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}
