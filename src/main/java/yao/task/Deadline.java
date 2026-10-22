package yao.task;

import java.time.LocalDate;
import java.time.LocalTime;
import yao.DateTimeUtil;

/**
 * Represents a task that needs to be done before a specific date/time or deadline limit.
 */
public class Deadline extends Task {
    /** Parsed date and time container for the deadline, or null if plain text. */
    protected DateTimeUtil.ParsedDateTime byDateTime;
    /** Raw deadline string when date/time parsing is not applicable. */
    protected String rawBy;

    /**
     * Constructs a Deadline task with description and deadline date/time string.
     * If the input matches a recognized date/time format, it is parsed and stored
     * as structured date/time data. Otherwise, it is preserved as plain text.
     *
     * @param description Description of the task.
     * @param by Due date/time string or informal deadline description.
     */
    public Deadline(String description, String by) {
        super(description);
        this.rawBy = by;
        this.byDateTime = DateTimeUtil.tryParseDateTime(by);
    }

    /**
     * Returns the date when this deadline is due, if it was parsed as a date.
     *
     * @return The LocalDate object, or null if a plain text deadline was specified.
     */
    public LocalDate getDate() {
        return byDateTime != null ? byDateTime.getDate() : null;
    }

    /**
     * Returns the time when this deadline is due, if specified.
     *
     * @return The LocalTime object, or null if no time was specified or applicable.
     */
    public LocalTime getTime() {
        return byDateTime != null ? byDateTime.getTime() : null;
    }

    /**
     * Returns the formatted date/time string or original text of the deadline.
     *
     * @return Formatted date/time string or raw description.
     */
    public String getBy() {
        return byDateTime != null ? byDateTime.toDisplayString() : rawBy;
    }

    @Override
    public boolean isOnDate(LocalDate date) {
        return byDateTime != null && byDateTime.getDate().equals(date);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + getBy() + ")";
    }

    @Override
    public String toFileFormat() {
        String byStorage = byDateTime != null ? byDateTime.toStorageString() : rawBy;
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + byStorage;
    }
}
