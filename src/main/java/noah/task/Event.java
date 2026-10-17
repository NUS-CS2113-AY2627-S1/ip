package noah.task;

import java.util.Objects;

/**
 * Represents a task with optional start and end time labels stored as text.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates a task with the given description.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     */
    public Event(String description) {
        super(description);
    }

    /**
     * Creates a task with the given description and period.
     * New tasks are not done by default.
     *
     * @param description Description of the task.
     * @param from Start time.
     * @param to End time.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Compares the description and exact start/end labels, ignoring completion status.
     * A missing start or end label only matches another missing label.
     *
     * @param other Task to compare, or null.
     * @return Whether the other task is an event with identical details.
     */
    @Override
    public boolean hasSameDetails(Task other) {
        if (!super.hasSameDetails(other)) {
            return false;
        }
        Event event = (Event) other;
        return Objects.equals(from, event.from) && Objects.equals(to, event.to);
    }

    /**
     * Returns this event in the format used by the data file.
     *
     * @return Serialized event data.
     */
    @Override
    public String toDataString() {
        String data = "E | " + (isDone ? "1" : "0") + " | " + description;
        return from == null || to == null ? data : data + " | " + from + " | " + to;
    }

    /**
     * Returns the task in the format used when displaying the task list.
     *
     * @return Task status followed by its description.
     */
    @Override
    public String toString() {
        if (from != null && to != null) {
            return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
        }
        return "[E]" + super.toString();
    }
}
