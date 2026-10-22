import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * A moment a task refers to, such as when a deadline falls due.
 * <p>
 * The user may give a date on its own, {@code 2019-12-02}, or a date and an
 * hour, {@code 2019-12-02 1800}. Both are kept here as a single
 * {@link LocalDateTime}, alongside a note of whether an hour was actually
 * given: a deadline of "the 2nd of December" is not the same statement as "the
 * 2nd of December at midnight", and only the second should be shown with a
 * time.
 * <p>
 * Reading, displaying and storing a moment are all here, so that the three
 * formats involved -- the one the user types, the one the user reads, and the
 * one the data file holds -- sit side by side where they can be compared.
 */
public class TaskDateTime {

    /** How a moment without an hour is shown to the user. */
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** How a moment with an hour is shown to the user. */
    private static final DateTimeFormatter DISPLAY_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma", Locale.ENGLISH);

    /**
     * How a date and an hour are typed, and stored.
     * <p>
     * The data file keeps whatever the user typed, in this same shape, so that
     * a saved task can be read back by the very same parsing below, and so the
     * file stays legible to anyone who opens it.
     */
    private static final DateTimeFormatter TYPED_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm", Locale.ENGLISH);

    /** The moment itself; midnight stands in for an unspecified hour. */
    private final LocalDateTime dateTime;

    /** Whether the user gave an hour, rather than a date alone. */
    private final boolean hasTime;

    /**
     * Constructs a moment. Private: the way in is {@link #parse(String)}, which
     * is the only thing that decides whether an hour was given.
     *
     * @param dateTime the moment.
     * @param hasTime whether an hour was given.
     */
    private TaskDateTime(LocalDateTime dateTime, boolean hasTime) {
        this.dateTime = dateTime;
        this.hasTime = hasTime;
    }

    /**
     * Reads a moment from text the user typed, or that the data file holds.
     * <p>
     * A date with an hour is tried first, since {@code 2019-12-02 1800} also
     * begins with something that looks like a date on its own.
     *
     * @param text the date, with or without an hour.
     * @return the moment that text describes.
     * @throws CortisolException if the text is not a date this bot can read.
     */
    public static TaskDateTime parse(String text) throws CortisolException {
        String trimmedText = text.trim();

        try {
            return new TaskDateTime(LocalDateTime.parse(trimmedText, TYPED_DATE_TIME), true);
        } catch (DateTimeParseException e) {
            // Not a date with an hour. Fall through and try a date on its own.
        }

        try {
            return new TaskDateTime(LocalDate.parse(trimmedText).atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            throw new CortisolException("I cannot make out '" + trimmedText
                    + "' as a date, sir/madam.\n"
                    + " Do try: 2019-12-02, or 2019-12-02 1800 if an hour matters.");
        }
    }

    /**
     * Returns this moment as it should be written to the data file, in the same
     * shape the user typed it.
     *
     * @return the date, followed by the hour if one was given.
     */
    public String toFileFormat() {
        return hasTime ? dateTime.format(TYPED_DATE_TIME) : dateTime.toLocalDate().toString();
    }

    @Override
    public String toString() {
        if (!hasTime) {
            return dateTime.format(DISPLAY_DATE);
        }

        // The formatter yields "6:00PM", and the butler does not shout. Only the
        // date and hour are formatted here, so no description can be touched.
        return dateTime.format(DISPLAY_DATE_TIME).replace("AM", "am").replace("PM", "pm");
    }
}
