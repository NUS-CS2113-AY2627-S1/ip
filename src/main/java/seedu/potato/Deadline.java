package seedu.potato;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that must be completed by a specified date or time.
 */
public class Deadline extends Task {
    static final int DATA_FIELD_COUNT = 4;

    private static final String DEADLINE_SYMBOL = "[D]";
    private static final String DEADLINE_SEPARATOR = "\\s+/by\\s+";
    private static final int EXPECTED_PART_COUNT = 2;
    private static final String INVALID_INPUT_MESSAGE =
            "Invalid deadline input. Format: deadline [description] /by [date] [optional time]";

    private final LocalDateTime by;

    /**
     * Creates an incomplete deadline with the given description and due date.
     *
     * @param description Description of the task.
     * @param by Date and optional time by which the task must be completed.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Checks whether input contains a description and a non-blank {@code /by} value.
     *
     * @param taskInput Raw input following the deadline command word.
     * @return {@code true} if the input can be used to create a deadline.
     */
    public static boolean isValidInput(String taskInput) {
        String[] deadlineParts = splitInput(taskInput);
        if (!hasNonBlankParts(deadlineParts, EXPECTED_PART_COUNT)) {
            return false;
        }

        try {
            DateParser.parseDateTime(deadlineParts[1].trim());
            return true;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    /**
     * Creates a deadline from input that has already been validated.
     *
     * @param taskInput Raw input following the deadline command word.
     * @return Deadline represented by the input.
     * @throws IllegalArgumentException If the input is not valid for a deadline.
     */
    static Deadline createFromInput(String taskInput) {
        String[] deadlineParts = splitInput(taskInput);
        if (!hasNonBlankParts(deadlineParts, EXPECTED_PART_COUNT)) {
            throw new IllegalArgumentException(INVALID_INPUT_MESSAGE);
        }

        try {
            LocalDateTime by = DateParser.parseDateTime(deadlineParts[1].trim());
            return new Deadline(deadlineParts[0], by);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(INVALID_INPUT_MESSAGE + System.lineSeparator()
                    + DateParser.DATE_FORMAT_HINT, exception);
        }
    }

    /**
     * Returns this deadline in the format used for persistent storage.
     *
     * @return Save-file representation of this deadline.
     */
    @Override
    public String toDataString() {
        return "D | " + getDataStatus() + " | " + description + " | " + DateParser.format(by);
    }

    /**
     * Reports whether this deadline stores the given date.
     *
     * @param date Date to find in this deadline.
     * @return {@code true} if the deadline is due on the date.
     */
    @Override
    public boolean containsDate(LocalDate date) {
        return by.toLocalDate().equals(date);
    }

    /**
     * Splits raw deadline input into its description and due date.
     *
     * @param taskInput Raw input following the deadline command word.
     * @return Deadline parts, or an empty array if the input is {@code null}.
     */
    private static String[] splitInput(String taskInput) {
        if (taskInput == null) {
            return new String[0];
        }
        return taskInput.split(DEADLINE_SEPARATOR, EXPECTED_PART_COUNT);
    }

    /**
     * Reports whether an array has the expected number of non-blank parts.
     *
     * @param parts Text parts to check.
     * @param expectedPartCount Required number of parts.
     * @return {@code true} if every expected part contains text.
     */
    private static boolean hasNonBlankParts(String[] parts, int expectedPartCount) {
        if (parts.length != expectedPartCount) {
            return false;
        }

        for (String part : parts) {
            if (part.isBlank()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the deadline with its type, completion icon, and due date.
     *
     * @return Display-ready task text.
     */
    @Override
    public String toString() {
        return DEADLINE_SYMBOL + "[" + getStatusIcon() + "] " + description
                + " (by: " + DateParser.formatForDisplay(by) + ")";
    }
}
