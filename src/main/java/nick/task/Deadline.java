package nick.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a deadline: a task that needs to be done before a specific date or time.
 * If the "by" value is given as an ISO date (yyyy-MM-dd) it is stored as a
 * {@link LocalDate} and displayed in a friendlier format; otherwise it is kept as
 * free text.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final String by;
    private final LocalDate byDate;

    /**
     * Creates a deadline with the given description and due time.
     *
     * @param description Description of the deadline.
     * @param by The date or time the task is due by, ideally as yyyy-MM-dd.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        this.byDate = tryParseDate(by);
    }

    /**
     * Parses the given text as an ISO date, returning null if it is not one.
     *
     * @param text The text to parse.
     * @return The parsed date, or null if the text is not an ISO date.
     */
    private static LocalDate tryParseDate(String text) {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Returns the "by" value formatted for display: a friendly date if it was a
     * valid ISO date, otherwise the original text.
     *
     * @return The display form of the due time.
     */
    private String byForDisplay() {
        return byDate != null ? byDate.format(DISPLAY_FORMAT) : by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toDisplayString() {
        return super.toDisplayString() + " (by: " + byForDisplay() + ")";
    }

    @Override
    public String toSaveFormat() {
        return super.toSaveFormat() + " | " + by;
    }
}
