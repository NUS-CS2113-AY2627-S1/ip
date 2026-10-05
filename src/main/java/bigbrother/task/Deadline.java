package bigbrother.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

import bigbrother.exception.BigBrotherException;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    private static final String INVALID_DATE_MESSAGE = "Use a valid yyyy-MM-dd date "
            + "(month 01-12, day valid for that month) and optional HHmm time "
            + "(hours 00-23, minutes 00-59).";
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter STORAGE_TIME = DateTimeFormatter.ofPattern("HHmm");

    private final LocalDate dueDate;
    private final LocalTime dueTime;
    private final String legacyBy;

    /**
     * Creates an incomplete deadline. ISO dates become date objects; older free-form dates remain readable.
     *
     * @param description the text that describes the deadline
     * @param by the due date in yyyy-MM-dd or yyyy-MM-dd HHmm form, or legacy free-form text
     * @throws BigBrotherException if a date-like value is not a valid date or time
     */
    public Deadline(String description, String by) throws BigBrotherException {
        super(description);

        try {
            if (by.matches("\\d{4}-\\d{2}-\\d{2}")) {
                dueDate = LocalDate.parse(by);
                dueTime = null;
                legacyBy = null;
            } else if (by.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                LocalDateTime dateTime = LocalDateTime.parse(by, INPUT_DATE_TIME);
                dueDate = dateTime.toLocalDate();
                dueTime = dateTime.toLocalTime();
                legacyBy = null;
            } else if (by.matches("\\d{4}-.*")) {
                throw new BigBrotherException(INVALID_DATE_MESSAGE);
            } else {
                dueDate = null;
                dueTime = null;
                legacyBy = by;
            }
        } catch (DateTimeParseException exception) {
            throw new BigBrotherException(INVALID_DATE_MESSAGE);
        }
    }

    /**
     * Returns the original or ISO-formatted due value for saving to the data file.
     *
     * @return the due value in a form that can be loaded again
     */
    public String getBy() {
        if (dueDate == null) {
            return legacyBy;
        }
        if (dueTime == null) {
            return dueDate.toString();
        }
        return dueDate + " " + dueTime.format(STORAGE_TIME);
    }

    /**
     * Returns the formatted representation of this deadline.
     *
     * @return the deadline type, status, description, and readable due date
     */
    @Override
    public String toString() {
        String displayedBy = dueDate == null ? legacyBy : dueDate.format(DISPLAY_DATE);
        if (dueTime != null) {
            displayedBy += " " + dueTime.format(DISPLAY_TIME);
        }
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + displayedBy + ")";
    }
}
