package nia.tasks;

import java.time.LocalDateTime;
import nia.parser.Parser;

/** A task with a description and a single deadline ("by") date-time. */
public class Deadline extends Task{

    protected LocalDateTime by;

    /** Creates a new, not-done deadline with the given description and due date-time. */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /** Returns the date-time this deadline is due by. */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String getTag() {
        return "D";
    }

    @Override
    public String toString() {
        return "[%s]".formatted(this.getTag()) + super.toString()
                + " (by: %s)".formatted(Parser.formatDisplay(this.by));
    }
}
