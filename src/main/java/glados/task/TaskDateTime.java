package glados.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a date with an optional time of day, such as when a deadline is due.
 *
 * <p>Shown to the user as e.g. "Oct 15 2019", or "Dec 02 2019 6:00 PM" when a time
 * was given. Saved in the data file as e.g. "2019-10-15" or "2019-12-02 1800".
 */
public class TaskDateTime {

    /** Date part shown to the user. Locale.ENGLISH keeps month names in English on any computer. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Time part shown to the user, when there is one. */
    private static final DateTimeFormatter DISPLAY_TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    /**
     * Date format used in the data file.
     * STRICT rejects dates that do not exist, such as 2019-02-30, and needs "uuuu" for the year.
     */
    private static final DateTimeFormatter FILE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /** Date and time format used in the data file, e.g. "2019-12-02 1800". */
    private static final DateTimeFormatter FILE_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HHmm").withResolverStyle(ResolverStyle.STRICT);

    /** The date and time. When no time was given, the time is midnight and is never shown. */
    private final LocalDateTime dateTime;

    /** Whether a time of day was given, which decides whether the time is shown and saved. */
    private final boolean hasTime;

    /**
     * Creates a date with no time of day.
     */
    public TaskDateTime(LocalDate date) {
        this.dateTime = date.atStartOfDay();
        this.hasTime = false;
    }

    /**
     * Creates a date with a time of day.
     */
    public TaskDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.hasTime = true;
    }

    /**
     * Returns the date and time written in a data file line, e.g. "2019-10-15" or "2019-12-02 1800".
     *
     * @param text the date as written by {@link #toFileString()}.
     * @return the date and time.
     * @throws java.time.format.DateTimeParseException if the text is not in either format,
     *         or is not a real date or time.
     */
    public static TaskDateTime fromFileString(String text) {
        if (text.contains(" ")) {
            return new TaskDateTime(LocalDateTime.parse(text, FILE_DATE_TIME_FORMAT));
        }
        return new TaskDateTime(LocalDate.parse(text, FILE_DATE_FORMAT));
    }

    /**
     * Returns the date, without any time of day.
     */
    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    /**
     * Returns true if this is earlier than the other date and time.
     *
     * <p>When either one has no time of day, only the dates are compared, so a
     * date with no time counts as the whole day, e.g. "2019-10-15" is not before
     * "2019-10-15 1800".
     */
    public boolean isBefore(TaskDateTime other) {
        if (hasTime && other.hasTime) {
            return dateTime.isBefore(other.dateTime);
        }
        return getDate().isBefore(other.getDate());
    }

    /**
     * Returns this date and time as written in the data file, e.g. "2019-12-02 1800".
     */
    public String toFileString() {
        if (hasTime) {
            return dateTime.format(FILE_DATE_TIME_FORMAT);
        }
        return dateTime.format(FILE_DATE_FORMAT);
    }

    /**
     * Returns this date and time as shown to the user, e.g. "Dec 02 2019 6:00 PM".
     */
    @Override
    public String toString() {
        if (hasTime) {
            return dateTime.format(DISPLAY_DATE_FORMAT) + " " + dateTime.format(DISPLAY_TIME_FORMAT);
        }
        return dateTime.format(DISPLAY_DATE_FORMAT);
    }
}
