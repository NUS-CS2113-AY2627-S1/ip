package noah.task;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import noah.exception.NoahException;

/**
 * Stores a deadline's calendar date and optional clock time.
 * Preserves free-text date labels without guessing their meaning.
 */
public final class TaskDateTime {
    private static final Pattern NUMERIC_DATE_PREFIX = Pattern.compile("^\\d{4}-|^\\d{1,2}/\\d{1,2}/");
    private static final Pattern INPUT_FORMAT = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2}|\\d{1,2}/\\d{1,2}/\\d{4})(?:\\s+(\\d{2}:?\\d{2}))?$");
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final String INVALID_DATE_MESSAGE =
            "That date needs a second look! Use yyyy-MM-dd or d/M/yyyy.\n"
                    + "Time is optional: add HH:mm or HHmm, like 2019-12-02 18:00.";

    private final LocalDate date;
    private final LocalTime time;

    /**
     * Original wording when a deadline uses a free-text label instead of a calendar date.
     */
    private final String legacyText;

    /**
     * Creates a parsed date/time or a preserved text value.
     *
     * @param date Calendar date, or null for a text value.
     * @param time Optional clock time.
     * @param legacyText Original wording, or null for a parsed date.
     */
    private TaskDateTime(LocalDate date, LocalTime time, String legacyText) {
        this.date = date;
        this.time = time;
        this.legacyText = legacyText;
    }

    /**
     * Parses numeric dates with an optional HH:mm or HHmm time and preserves other labels.
     * Input starting with a four-digit year and hyphen, or day/month/, opts into date validation.
     * Other nonblank input retains the original free-text behavior, including Sunday and 2pm.
     *
     * @param input Numeric date/time or free-text label entered by the user.
     * @return Validated date/time or unchanged free-text label.
     * @throws NoahException If the input is blank or an explicit numeric date/time is invalid.
     */
    public static TaskDateTime parse(String input) throws NoahException {
        String value = input.trim();
        if (value.isEmpty()) {
            throw new NoahException(INVALID_DATE_MESSAGE);
        }
        if (!NUMERIC_DATE_PREFIX.matcher(value).find()) {
            return new TaskDateTime(null, null, input);
        }
        Matcher match = INPUT_FORMAT.matcher(value);
        if (!match.matches()) {
            throw new NoahException(INVALID_DATE_MESSAGE);
        }
        try {
            String dateText = match.group(1);
            boolean isDayFirst = dateText.contains("/");
            String[] parts = dateText.split(isDayFirst ? "/" : "-");
            int year = Integer.parseInt(parts[isDayFirst ? 2 : 0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[isDayFirst ? 0 : 2]);
            if (year == 0) {
                throw new DateTimeException("The year must be at least 1");
            }
            LocalDate date = LocalDate.of(year, month, day);
            LocalTime time = null;
            if (match.group(2) != null) {
                String digits = match.group(2).replace(":", "");
                time = LocalTime.of(Integer.parseInt(digits.substring(0, 2)),
                        Integer.parseInt(digits.substring(2)));
            }
            return new TaskDateTime(date, time, null);
        } catch (DateTimeException | NumberFormatException e) {
            throw new NoahException(INVALID_DATE_MESSAGE);
        }
    }

    /**
     * Reads saved dates while retaining all text accepted by earlier Noah versions.
     * Loading also preserves old invalid numeric dates so existing tasks are not discarded.
     *
     * @param input Nonblank date field from an existing saved task.
     * @return Parsed date/time, or the original saved text if it cannot be parsed.
     */
    public static TaskDateTime fromStorage(String input) {
        try {
            return parse(input);
        } catch (NoahException e) {
            return new TaskDateTime(null, null, input);
        }
    }

    /**
     * Compares parsed date/time values or exact preserved text labels.
     * Equivalent numeric input formats match, but an omitted time differs from midnight.
     * A text label is not interpreted as a date during comparison.
     *
     * @param other Object to compare, or null.
     * @return Whether both objects represent the same date/time or text value.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TaskDateTime otherValue)) {
            return false;
        }
        return Objects.equals(date, otherValue.date)
                && Objects.equals(time, otherValue.time)
                && Objects.equals(legacyText, otherValue.legacyText);
    }

    /**
     * Returns a hash code consistent with date/time and text-value equality.
     *
     * @return Hash code of the stored values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(date, time, legacyText);
    }

    /**
     * Returns a stable storage value, preserving legacy text where needed.
     *
     * @return ISO date with optional HH:mm time, or unchanged legacy text.
     */
    public String toDataString() {
        if (date == null) {
            return legacyText;
        }
        return date + (time == null ? "" : " " + time.format(TIME_FORMAT));
    }

    /**
     * Displays parsed dates using English month names without adding an unspecified time.
     *
     * @return Formatted date/time, or unchanged legacy text.
     */
    @Override
    public String toString() {
        if (date == null) {
            return legacyText;
        }
        return date.format(DISPLAY_DATE_FORMAT) + (time == null ? "" : " " + time.format(TIME_FORMAT));
    }
}
