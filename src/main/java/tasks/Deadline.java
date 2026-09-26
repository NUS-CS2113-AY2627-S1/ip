package tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * A task due on a calendar date, displayed independently of its storage format.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd uuuu", Locale.ENGLISH);

    private final LocalDate by;

    /**
     * Creates a deadline with a validated calendar date.
     *
     * @param description Task description.
     * @param by Due date, without a time or time zone.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = Objects.requireNonNull(by);
    }

    /**
     * Returns the due date for comparisons and ISO-format storage.
     *
     * @return The deadline's calendar date.
     */
    public LocalDate getBy() {
        return by;
    }

    /**
     * Returns the deadline type marker.
     *
     * @return The deadline marker D.
     */
    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Formats the due date with an English month name for the console.
     *
     * @return The description with a date such as Oct 15 2019.
     */
    @Override
    public String getFullDescription() {
        return getDescription() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}
