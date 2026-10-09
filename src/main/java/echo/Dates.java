package echo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Reads and writes the dates used by tasks. Users type dates as
 * {@code yyyy-mm-dd} (e.g. {@code 2019-10-15}); they are shown as
 * {@code MMM dd yyyy} (e.g. {@code Oct 15 2019}).
 */
public class Dates {
    /** English month names regardless of the computer's language setting. */
    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    /** Prevents instantiation: this class only has static methods. */
    private Dates() {
    }

    /**
     * Parses a date typed by the user.
     *
     * @param text the date in {@code yyyy-mm-dd} form, e.g. {@code "2019-10-15"}.
     * @return the parsed date.
     * @throws EchoException if the text is not a valid {@code yyyy-mm-dd} date.
     */
    public static LocalDate parse(String text) throws EchoException {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new EchoException("\"" + text + "\" is not a valid date. Use yyyy-mm-dd, e.g. 2019-10-15.");
        }
    }

    /**
     * Formats a date for display.
     *
     * @param date the date to show.
     * @return the date as e.g. {@code Oct 15 2019}.
     */
    public static String format(LocalDate date) {
        return date.format(DISPLAY_FORMAT);
    }
}
