package alfred.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents either a calendar date or a calendar date with a time.
 */
public final class ScheduledDateTime {

    private static final DateTimeFormatter USER_DATE_TIME_FORMAT
            = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter STORAGE_DATE_TIME_FORMAT
            = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT
            = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT
            = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a", Locale.ENGLISH);

    private final LocalDateTime value;
    private final boolean hasTime;

    private ScheduledDateTime(LocalDateTime value, boolean hasTime) {
        this.value = value;
        this.hasTime = hasTime;
    }

    /**
     * Parses a user value containing a date and an optional time.
     *
     * @param text Value using {@code yyyy-MM-dd} or {@code yyyy-MM-dd HH:mm}.
     * @return Parsed scheduled value.
     */
    public static ScheduledDateTime parseUserInput(String text) {
        if (isDateOnly(text)) {
            return fromDate(LocalDate.parse(text));
        }
        return new ScheduledDateTime(LocalDateTime.parse(text, USER_DATE_TIME_FORMAT), true);
    }

    /**
     * Parses a stored value containing a date and an optional time.
     *
     * @param text Value using {@code yyyy-MM-dd} or {@code yyyy-MM-ddTHH:mm}.
     * @return Parsed scheduled value.
     */
    public static ScheduledDateTime parseStoredValue(String text) {
        if (isDateOnly(text)) {
            return fromDate(LocalDate.parse(text));
        }
        return new ScheduledDateTime(LocalDateTime.parse(text, STORAGE_DATE_TIME_FORMAT), true);
    }

    private static boolean isDateOnly(String text) {
        return text.length() == 10;
    }

    private static ScheduledDateTime fromDate(LocalDate date) {
        return new ScheduledDateTime(date.atStartOfDay(), false);
    }

    public boolean hasTime() {
        return hasTime;
    }

    /**
     * Returns whether this value occurs before another scheduled value.
     *
     * @param other Value to compare with.
     * @return True if this value occurs earlier than the other value.
     */
    public boolean isBefore(ScheduledDateTime other) {
        return value.isBefore(other.value);
    }

    /**
     * Returns whether this value occurs on the specified calendar date.
     *
     * @param date Date to check.
     * @return True if the date component matches.
     */
    public boolean occursOn(LocalDate date) {
        return value.toLocalDate().equals(date);
    }

    public LocalDate getDate() {
        return value.toLocalDate();
    }

    /**
     * Returns the stable representation used in the data file.
     *
     * @return ISO date or ISO date-time text.
     */
    public String toStorageString() {
        if (hasTime) {
            return value.format(STORAGE_DATE_TIME_FORMAT);
        }
        return value.toLocalDate().toString();
    }

    @Override
    public String toString() {
        if (hasTime) {
            return value.format(DISPLAY_DATE_TIME_FORMAT);
        }
        return value.format(DISPLAY_DATE_FORMAT);
    }
}
