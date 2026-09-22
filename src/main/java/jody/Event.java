package jody;

import java.time.LocalDateTime;

/**
 * Represents a task spanning a start and end date/time.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an event from arguments containing {@code /from} and {@code /to}.
     *
     * @param input the command arguments, excluding the command word
     * @throws JodyException if the fields are invalid or the end precedes the start
     */
    public Event(String input) throws JodyException {
        this(splitInput(input));
    }

    /**
     * Creates an event from separated command fields.
     *
     * @param parts the description, start, and end fields, in that order
     * @throws JodyException if the fields are invalid or the end precedes the start
     */
    private Event(String[] parts) throws JodyException {
        this(parts[0], parts[1], parts[2]);
    }

    // Used when loading a saved task.
    /**
     * Creates an event from separate fields, including fields loaded from storage.
     *
     * @param description the nonblank task description
     * @param from the start date/time in a supported format
     * @param to the end date/time in a supported format
     * @throws JodyException if the description is blank, a date/time is invalid,
     *         or the end precedes the start
     */
    public Event(String description, String from, String to)
            throws JodyException {
        super(description.trim());

        if (description.isBlank()) {
            throw new JodyException("An event needs a description.");
        }

        this.from = DateTimeParser.parseDateTime(from);
        this.to = DateTimeParser.parseDateTime(to);

        if (this.to.isBefore(this.from)) {
            throw new JodyException(
                    "An event cannot end before it starts.");
        }
    }

    /**
     * Splits event arguments into description, start, and end fields.
     *
     * @param input the event command arguments
     * @return fields separated by {@code /from} and {@code /to}
     * @throws JodyException if the required separators are missing or repeated
     */
    private static String[] splitInput(String input) throws JodyException {
        String[] first = input.split("/from", -1);

        if (first.length == 2) {
            String[] times = first[1].split("/to", -1);

            if (times.length == 2) {
                return new String[] {
                        first[0], times[0], times[1]
                };
            }
        }

        throw new JodyException(
                "Use: event meeting /from 2/12/2019 1400 "
                        + "/to 2/12/2019 1600");
    }

    /**
     * Returns the event's start date and time.
     *
     * @return the start date/time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event's end date and time.
     *
     * @return the end date/time
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the event's type, status, description, and formatted time range.
     *
     * @return the event's display text
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateTimeParser.format(from)
                + " to: " + DateTimeParser.format(to) + ")";
    }
}
