package clanky;

import clanky.task.Deadline;
import clanky.task.Event;
import clanky.task.TaskList;
import clanky.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses raw user input into commands and validated task data.
 * Responsible for tokenizing input and converting it into usable values
 * (indices, dates, task objects), throwing {@link ClankyException} when
 * input is missing, malformed, or otherwise invalid.
 */
public class Parser {

    /**
     * Splits raw input into a command word and the remaining argument text.
     *
     * @param input the raw line of user input.
     * @return an array of length 1 or 2: [command] or [command, arguments].
     */
    public static String[] parse(String input) {
        return input.trim().split("\\s+", 2);
    }

    /**
     * Parses and validates a task index from user input.
     *
     * @param parts the split user input; here, parts[1] is expected to be the task number.
     * @param tasks the current task list, used to validate the index is in range.
     * @param action the action being performed (e.g. "mark"), used in the error message.
     * @return the validated, zero-based index into the task list.
     * @throws ClankyException if the index is missing, not a number, or out of range.
     */
    public static int parseIndex(String[] parts, TaskList tasks, String action) throws ClankyException {
        // Catch Missing Arguments
        if (parts.length < 2) {
            throw new ClankyException("Please specify a Task Number to " + action + ".");
        }

        int index;
        // Catch Invalid Task Number
        try {
            index = Integer.parseInt(parts[1]) - 1;
        } catch(NumberFormatException e) {
            throw new ClankyException("That's not a valid Task number.");
        }

        // Catch Index Out Of Array
        if (index < 0 || index >= tasks.size()) {
            throw new ClankyException("That Task Number does not exist.");
        }
        return index;
    }

    /**
     * Parses a todo command's arguments into a {@link Todo}.
     *
     * @param args the text following "todo".
     * @return the constructed Todo task.
     * @throws ClankyException if the description is missing or blank.
     */
    public static Todo parseTodoArgs(String args) throws ClankyException {
        if (args == null || args.isBlank()) {
            throw new ClankyException("Description of ToDo can't be empty.");
        }
        return new Todo(args.trim());
    }

    /**
     * Parses a deadline command's arguments into a {@link Deadline}.
     *
     * @param args the text following "deadline", expected to contain "/by".
     * @return the constructed Deadline task.
     * @throws ClankyException if the format is invalid or the date can't be parsed.
     */
    public static Deadline parseDeadlineArgs(String args) throws ClankyException {
        if (args == null || !args.contains("/by")) {
            throw new ClankyException("Please use the format: deadline <description> /by <time>");
        }
        String[] deadlineParts = args.split("/by", 2);
        String desc = deadlineParts[0].trim();
        String byText = deadlineParts[1].trim();
        if (desc.isBlank() || byText.isBlank()) {
            throw new ClankyException("Both description and /by time are required.");
        }

        LocalDate by;
        try {
            by = LocalDate.parse(byText);
        } catch (DateTimeParseException e) {
            throw new ClankyException("Use yyyy-mm-dd format for dates, e.g. 2026-11-11");
        }
        return new Deadline(desc, by);
    }

    /**
     * Parses an event command's arguments into an {@link Event}.
     *
     * @param args the text following "event", expected to contain "/from" and "/to".
     * @return the constructed Event task.
     * @throws ClankyException if the format is invalid or a date can't be parsed.
     */
    public static Event parseEventArgs(String args) throws ClankyException {
        if (args == null || !args.contains("/from") || !args.contains("/to")) {
            throw new ClankyException("Please use the format: event <description> /from <start> /to <end>");
        }
        String[] fromSplit = args.split("/from", 2);
        String desc = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split("/to", 2);
        String fromText = toSplit[0].trim();
        String toText = toSplit[1].trim();
        if (desc.isBlank() || fromText.isBlank() || toText.isBlank()) {
            throw new ClankyException("Description, /from, and /to are all required.");
        }

        LocalDate from;
        LocalDate to;
        try {
            from = LocalDate.parse(fromText);
            to = LocalDate.parse(toText);
        } catch (DateTimeParseException e) {
            throw new ClankyException("Use yyyy-mm-dd format for dates, e.g. 2026-11-11");
        }
        return new Event(desc, from, to);
    }

    /**
     * Parses a find command's arguments into a search keyword.
     *
     * @param args the text following "find".
     * @return the trimmed keyword to search for.
     * @throws ClankyException if the keyword is missing or blank.
     */
    public static String parseFindArgs(String args) throws ClankyException {
        if (args == null || args.isBlank()) {
            throw new ClankyException("Please specify a keyword to search for");
        }
        return args.trim();
    }

}
