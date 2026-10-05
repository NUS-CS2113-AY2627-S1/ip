package yanny.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Converts supported deadline date formats into typed deadline tasks.
 */
public final class DeadlineDateParser {
    private static final DateTimeFormatter SLASH_DATE_TIME =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm", Locale.ROOT)
                    .withResolverStyle(ResolverStyle.STRICT);

    private DeadlineDateParser() {
    }

    /**
     * Parses one of the deadline date formats accepted from a command.
     *
     * @param description the task description.
     * @param value the deadline value to parse.
     * @return a deadline containing a Java date or date and time.
     * @throws IllegalArgumentException if the value is not a supported valid date.
     */
    public static Deadline parse(String description, String value) {
        try {
            return new Deadline(description, LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException exception) {
            // Try the supported date and time formats next.
        }
        try {
            return new Deadline(description, LocalDateTime.parse(value, SLASH_DATE_TIME));
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Invalid deadline date or time.", exception);
        }
    }

    /**
     * Parses a deadline from a task file, including canonical ISO date-times.
     *
     * @param description the task description.
     * @param value the stored deadline value.
     * @return a deadline containing a Java date or date and time.
     * @throws IllegalArgumentException if the value is not a supported valid date.
     */
    public static Deadline parseStored(String description, String value) {
        try {
            return new Deadline(description, LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } catch (DateTimeParseException exception) {
            return parse(description, value);
        }
    }
}
