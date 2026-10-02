package slay69.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import slay69.Slay69Exception;

/**
 * A task due on a calendar date, or a deadline loaded from older free-text data.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private final LocalDate dueDate;
    private final String legacyDueText;

    /**
     * Creates a deadline with a date that can be compared and formatted.
     *
     * @param description text describing the task
     * @param dueDate due date parsed from an ISO date entered by the user
     */
    public Deadline(String description, LocalDate dueDate) {
        super(description);
        this.dueDate = dueDate;
        this.legacyDueText = null;
    }

    /**
     * Keeps a deadline saved before dates had a structured format.
     */
    private Deadline(String description, String legacyDueText) {
        super(description);
        this.dueDate = null;
        this.legacyDueText = legacyDueText;
    }

    /**
     * Loads an ISO date or preserves free-text deadlines saved by older versions.
     *
     * @param description text describing the task
     * @param savedDueDate date field from the saved task
     * @return a date-backed or legacy deadline task
     * @throws Slay69Exception if an ISO-shaped saved date is invalid
     */
    public static Deadline fromStorage(String description, String savedDueDate)
            throws Slay69Exception {
        if (!savedDueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return new Deadline(description, savedDueDate);
        }

        try {
            return new Deadline(description, LocalDate.parse(savedDueDate));
        } catch (DateTimeParseException e) {
            throw new Slay69Exception("Invalid saved deadline date: " + savedDueDate);
        }
    }

    /**
     * Returns the displayed task text with a formatted date or legacy text.
     *
     * @return the displayed deadline
     */
    @Override
    public String toString() {
        String displayedDate = dueDate == null
                ? legacyDueText : dueDate.format(DISPLAY_FORMAT);
        return "[D]" + super.toString() + " (by: " + displayedDate + ")";
    }

    /**
     * Returns the saved task fields using ISO dates for date-backed deadlines.
     *
     * @return the serialized deadline
     */
    @Override
    public String toStorageString() {
        String savedDate = dueDate == null ? legacyDueText : dueDate.toString();
        return "D|" + super.toStorageString() + "|" + savedDate;
    }
}
