package noah.task;

import java.util.Objects;

/**
 * Represents a task with an optional due date, time, or free-text label.
 */
public class Deadline extends Task {
    private final TaskDateTime by;

    /**
     * Creates a task with the given description and no due date.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     */
    public Deadline(String description) {
        this(description, null);
    }

    /**
     * Creates a task with the given description and due date.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     * @param by The due date of the task, or null if no date was supplied.
     */
    public Deadline(String description, TaskDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Compares the description and due value while ignoring completion status.
     * Equivalent numeric date/time formats match; free-text labels must match exactly.
     * An omitted due value only matches another omitted due value.
     *
     * @param other Task to compare, or null.
     * @return Whether the other task is a deadline with identical details.
     */
    @Override
    public boolean hasSameDetails(Task other) {
        if (!super.hasSameDetails(other)) {
            return false;
        }
        Deadline deadline = (Deadline) other;
        return Objects.equals(by, deadline.by);
    }

    /**
     * Returns this deadline with an ISO date/time or preserved date text.
     *
     * @return Serialized deadline data.
     */
    @Override
    public String toDataString() {
        String data = "D | " + (isDone ? "1" : "0") + " | " + description;
        return by == null ? data : data + " | " + by.toDataString();
    }

    /**
     * Returns the task with a formatted due date/time or preserved date text.
     *
     * @return Task status followed by its description.
     */
    @Override
    public String toString() {
        if (by != null) {
            return "[D]" + super.toString() + " (by: " + by + ")";
        }
        return "[D]" + super.toString();
    }
}
