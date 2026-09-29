package nia.tasks;

import java.time.LocalDateTime;
import nia.parser.Parser;

/** A task with a description and a "from"/"to" date-time span. */
public class Event extends Task{

    protected LocalDateTime from;
    protected LocalDateTime to;

    /** Creates a new, not-done event with the given description and start/end date-times. */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the date-time this event starts. */
    public LocalDateTime getFrom() {
        return from;
    }

    /** Returns the date-time this event ends. */
    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String getTag() {
        return "E";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString()
                + " (from: %s; to: %s)".formatted(Parser.formatDisplay(this.from), Parser.formatDisplay(this.to));
    }
}
