package bill.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents a task that should be completed by a specified time.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final String by;
    private final LocalDate byDate;

    /**
     * Creates an incomplete deadline with its description and due time.
     *
     * @param description Description of the deadline.
     * @param by Due date or time as entered by the user.
     */
    public Deadline(String description, String by) {
        this(description, by, false);
    }

    /**
     * Creates a deadline with its description, due time, and completion state.
     *
     * @param description Description of the deadline.
     * @param by Due date or time as entered by the user.
     * @param isDone Whether the deadline is completed.
     */
    public Deadline(String description, String by, boolean isDone) {
        super(description, isDone);
        this.by = by;
        byDate = parseStoredDate(by);
    }

    /**
     * Creates an incomplete deadline with a real calendar date.
     *
     * @param description Description of the deadline.
     * @param byDate Due date parsed from the user's ISO date.
     */
    public Deadline(String description, LocalDate byDate) {
        super(description);
        this.byDate = byDate;
        by = byDate.toString();
    }

    /**
     * Returns the deadline's due date or time.
     *
     * @return Due date or time as entered by the user.
     */
    public String getBy() {
        return by;
    }

    /**
     * Returns the parsed due date when one is available. Older free-form deadlines
     * remain usable but do not have a calendar date.
     *
     * @return Parsed due date, or empty for legacy free-form text.
     */
    public Optional<LocalDate> getByDate() {
        return Optional.ofNullable(byDate);
    }

    /**
     * Recognizes ISO dates when loading saved tasks while retaining older text values.
     */
    private static LocalDate parseStoredDate(String by) {
        try {
            return LocalDate.parse(by);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    /**
     * Identifies this task as a deadline in task listings.
     *
     * @return The deadline type icon.
     */
    @Override
    protected String getTypeIcon() {
        return "D";
    }

    /**
     * Formats a calendar due date for display, or keeps a legacy free-form due time unchanged.
     *
     * @return The due-date suffix shown after the description.
     */
    @Override
    protected String getDetails() {
        String displayBy = byDate == null ? by : byDate.format(DISPLAY_DATE);
        return " (by: " + displayBy + ")";
    }
}
