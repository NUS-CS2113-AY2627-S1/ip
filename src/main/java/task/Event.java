package task;

import java.time.LocalDateTime;

/** Represents a task with a start and end date and time. */
public class Event extends Task {
    /** Event start date and time. */
    private LocalDateTime from;
    /** Event end date and time. */
    private LocalDateTime to;

    /** Creates an event task.
     * @param name event description
     * @param from event start date and time
     * @param to event end date and time
     */
    public Event(String name, LocalDateTime from, LocalDateTime to) {
        super(name);
        this.from = from;
        this.to = to;
    }

    /** Formats the event for task-list output.
     * @return task display text with its start and end times
     */
    public String toString() {
        super.toString();
        return super.toString() + String.format(" (from: %s to: %s)", this.getFrom(), this.getTo());
    }

    /** Returns the event identifier used in task display.
     * @return {@code 'E'}
     */
    public char getIdentifier() {
        return 'E';
    }

    /** Returns the event start time.
     * @return start date and time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /** Replaces the event start time.
     * @param from new start date and time
     */
    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    /** Returns the event end time.
     * @return end date and time
     */
    public LocalDateTime getTo() {
        return to;
    }

    /** Replaces the event end time.
     * @param to new end date and time
     */
    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
