package nova;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import nova.task.Deadline;
import nova.task.Event;
import nova.task.Task;
import nova.task.Todo;

/**
 * Parses input, that is, which command was requested, etc
 * by extracting its arguments (task index, description, dates).
 */
public class Parser {

    /**
     * Returns the command word from input in lower case, e.g. "deadline"
     */
    public static String getCommandWord(String input) {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return trimmed.split("\\s+", 2)[0].toLowerCase();
    }

    /**
     * Parses the task number from commands like "mark 2" and converts it
     * to a zero-based index. Also throws NovaException if index is missing or isn't an int
     */
    public static int parseIndex(String input) throws NovaException {
        String[] tokens = input.trim().split("\\s+");

        if (tokens.length < 2) {
            throw new NovaException("An actual number would help. Try again.");
        }

        try {
            return Integer.parseInt(tokens[1]) - 1;
        } catch (NumberFormatException e) {
            throw new NovaException(
                    "'" + tokens[1] + "' is not a number, last I checked.");
        }
    }

    /**
     * Returns the keyword from input of the form "find KEYWORD".
     * Throws NovaException if given keyword is missing.
     */
    public static String parseKeyword(String input) throws NovaException {
        String keyword = getArguments(input);

        if (keyword.isEmpty()) {
            throw new NovaException("Find what, exactly? I'm not a mind reader.");
        }

        return keyword;
    }

    /** Builds todo from the given input when it's of the form "todo DESCRIPTION"
     * it throws NovaException if the description is missing.
     */
    public static Task parseTodo(String input) throws NovaException {
        String description = getArguments(input);

        if (description.isEmpty()) {
            throw new NovaException("No description? How very... you.");
        }

        return new Todo(description);
    }

    /**
     * Builds a Deadline from input of the form "deadline DESCRIPTION /by DATE",
     * where DATE is in yyyy-mm-dd format (e.g. 2026-10-15).
     * Throws NovaException if the description or /by part is missing,
     * or if the date is not in the expected format.
     */
    public static Task parseDeadline(String input) throws NovaException {
        String[] parts = getArguments(input).split("/by", 2);

        if (parts.length < 2
                || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty()) {
            throw new NovaException(
                    "A deadline needs both a description and a '/by'. "
                            + "Do try again.");
        }

        String byText = parts[1].trim();
        LocalDate by;

        try {
            by = LocalDate.parse(byText);
        } catch (DateTimeParseException e) {
            throw new NovaException("'" + byText + "' is not a date, genius. "
                    + "Use yyyy-mm-dd, e.g. 2026-10-15.");
        }

        return new Deadline(parts[0].trim(), by);
    }

    /**
     * Builds an Event from input of the form "event DESCRIPTION /from START /to END".
     * throws NovaException if the description, /from or /to part is missing.
     */
    public static Task parseEvent(String input) throws NovaException {
        String[] eventParts = getArguments(input).split("/from", 2);

        if (eventParts.length < 2 || eventParts[0].trim().isEmpty()) {
            throw new NovaException(
                    "An event needs a description and a '/from'. Honestly.");
        }

        String[] timeParts = eventParts[1].split("/to", 2);

        if (timeParts.length < 2
                || timeParts[0].trim().isEmpty()
                || timeParts[1].trim().isEmpty()) {
            throw new NovaException(
                    "An event needs a '/to' as well. Do finish your thought.");
        }

        return new Event(
                eventParts[0].trim(),
                timeParts[0].trim(),
                timeParts[1].trim());
    }

    /**
     * Returns everything after the command word, with trailing spaces removed
     */
    private static String getArguments(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        return parts.length < 2 ? "" : parts[1].trim();
    }
}