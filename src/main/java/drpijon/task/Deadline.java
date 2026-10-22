package drpijon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {
    /** Parsed deadline date and time, or null for legacy text. */
    private final LocalDateTime by;
    /** Whether the deadline input included an explicit time. */
    private final boolean hasTime;
    /** Original deadline text retained for legacy data. */
    private final String legacyBy;

    /**
     * Creates a task with a description and deadline.
     *
     * @param description task description
     * @param by deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by.atStartOfDay();
        this.hasTime = false;
        this.legacyBy = null;
    }

    /**
     * Creates a task with a description and deadline date and time.
     *
     * @param description task description
     * @param by deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
        this.hasTime = true;
        this.legacyBy = null;
    }

    /**
     * Parses a date or date-time deadline from user or storage text.
     *
     * @param description task description
     * @param by deadline date or date-time text
     * @return parsed deadline
     * @throws DateTimeParseException when the text is neither a supported date nor date-time
     */
    public static Deadline fromText(String description, String by) throws DateTimeParseException {
        return new Deadline(description, DateTimeParser.parse(by), DateTimeParser.hasTime(by), null);
    }

    /**
     * Creates a deadline from the application's older free-text format.
     *
     * @param description task description
     * @param legacyBy original deadline text
     * @return deadline that preserves the original text
     */
    public static Deadline fromLegacy(String description, String legacyBy) {
        return new Deadline(description, null, false, legacyBy);
    }

    private Deadline(String description, LocalDateTime by, boolean hasTime, String legacyBy) {
        super(description);
        this.by = by;
        this.hasTime = hasTime;
        this.legacyBy = legacyBy;
    }

    /**
     * Returns the parsed deadline date.
     *
     * @return parsed deadline date and time, or null for a legacy free-text deadline
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns whether this deadline has a parsed date.
     *
     * @return true when the deadline uses a typed date or date-time format
     */
    public boolean hasDate() {
        return by != null;
    }

    /**
     * Returns whether the deadline includes an explicit time.
     *
     * @return true when the deadline has a time component
     */
    public boolean hasTime() {
        return hasTime;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return hasDate() && by.toLocalDate().equals(date);
    }

    /**
     * Returns the value used when this deadline is saved.
     *
     * @return ISO date or date-time text, or the original legacy text
     */
    public String getByText() {
        if (!hasDate()) {
            return legacyBy;
        }
        return DateTimeParser.formatForStorage(by, hasTime);
    }

    /**
     * Returns a text representation including the deadline.
     *
     * @return deadline task representation
     */
    @Override
    public String toString() {
        return super.toString() + System.lineSeparator() + "do by: " + getByText();
    }

    /**
     * Returns the marker used for deadline tasks.
     *
     * @return deadline task marker
     */
    @Override
    public char getTaskType() {
        return 'D';
    }
}
