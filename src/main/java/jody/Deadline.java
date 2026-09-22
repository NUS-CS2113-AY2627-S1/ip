package jody;

import java.time.LocalDateTime;

/**
 * Represents a task that is due at a particular date and time.
 */
public class Deadline extends Task {
    private final LocalDateTime by;

    /**
     * Creates a deadline from a description and a {@code /by} date/time.
     *
     * @param input the command arguments, excluding the command word
     * @throws JodyException if the separator, description, or date/time is invalid
     */
    public Deadline(String input) throws JodyException {
        this(splitInput(input));
    }

    /**
     * Creates a deadline from separated command fields.
     *
     * @param parts the description and due date/time, in that order
     * @throws JodyException if the description or date/time is invalid
     */
    private Deadline(String[] parts) throws JodyException {
        this(parts[0], parts[1]);
    }

    /**
     * Creates a deadline from separate description and date/time fields.
     *
     * @param description the nonblank task description
     * @param by the due date/time in a supported format
     * @throws JodyException if the description is blank or the date/time is invalid
     */
    public Deadline(String description, String by) throws JodyException {
        super(description.trim());

        if (description.isBlank()) {
            throw new JodyException("A deadline needs a description.");
        }

        this.by = DateTimeParser.parseDateTime(by);
    }

    /**
     * Splits deadline arguments at exactly one {@code /by} separator.
     *
     * @param input the deadline command arguments
     * @return the description and due date/time fields
     * @throws JodyException if there is not exactly one separator
     */
    private static String[] splitInput(String input) throws JodyException {
        String[] parts = input.split("/by", -1);

        if (parts.length != 2) {
            throw new JodyException(
                    "Use: deadline return book /by 2/12/2019 1800");
        }

        return parts;
    }

    /**
     * Returns the deadline's due date and time.
     *
     * @return the due date/time
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the deadline's type, status, description, and formatted due time.
     *
     * @return the deadline's display text
     */
    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + DateTimeParser.format(by) + ")";
    }
}
