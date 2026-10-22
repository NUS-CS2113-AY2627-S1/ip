package ducky.chore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Represents a deadline: a chore that must be done by a certain date.
 * The due date is stored as a {@link LocalDate} rather than as free text,
 * so Ducky understands it as a real calendar date.
 */
public class Deadline extends Chore {
    /**
     * Format of a due date typed by the user, e.g. "15-10-2019" for
     * 15 October 2019. The single-letter "d" and "M" accept the day and month
     * with or without a leading zero, so "5-1-2019" and "05-01-2019" both work.
     * "uuuu" (not "yyyy") is needed for STRICT resolving, which rejects
     * impossible dates like 30-02-2019 instead of silently adjusting them.
     */
    public static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d-M-uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Format of a due date shown to the user (and written to the save file),
     * e.g. "15 Oct 2019". The locale is fixed to English so month names look
     * the same on every machine, regardless of its language settings.
     */
    public static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    /** Date by which the chore is due. */
    protected LocalDate by;

    /**
     * Creates a deadline with the given description and due date.
     *
     * @param description Text describing the chore.
     * @param by Date by which the chore is due.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    /**
     * Returns the description followed by the due date in parentheses,
     * with the date shown in {@link #DISPLAY_DATE_FORMAT}, e.g. "(by: 15 Oct 2019)".
     *
     * @return The chore description with its due date appended.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
