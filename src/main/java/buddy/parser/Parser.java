package buddy.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import buddy.command.AddCommand;
import buddy.command.Command;
import buddy.command.DeleteCommand;
import buddy.command.ExitCommand;
import buddy.command.FindCommand;
import buddy.command.ListCommand;
import buddy.command.MarkCommand;
import buddy.exception.BuddyException;
import buddy.task.Deadline;
import buddy.task.Event;
import buddy.task.Todo;

/**
 * Makes sense of raw lines of user input, turning each one into the
 * {@link Command} it represents. Only checks a command's syntax (e.g. is
 * a task number present and numeric); whether that task number actually
 * refers to an existing task is checked later, when the command runs.
 */
public class Parser {
    private static final DateTimeFormatter DEADLINE_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses a full line of user input into the command it represents.
     *
     * @param fullCommand the full line of user input
     * @return the command to execute
     * @throws BuddyException if the line isn't a recognized, well-formed command
     */
    public static Command parse(String fullCommand) throws BuddyException {
        String[] commandParts = fullCommand.split(" ");
        return switch (commandParts[0]) {
            case "bye" -> new ExitCommand();
            case "list" -> new ListCommand();
            case "mark" -> new MarkCommand(parseTaskIndex(commandParts, "mark"), true);
            case "unmark" -> new MarkCommand(parseTaskIndex(commandParts, "unmark"), false);
            case "todo" -> new AddCommand(parseTodo(fullCommand));
            case "deadline" -> new AddCommand(parseDeadline(fullCommand));
            case "event" -> new AddCommand(parseEvent(fullCommand));
            case "delete" -> new DeleteCommand(parseTaskIndex(commandParts, "delete"));
            case "find" -> new FindCommand(parseKeyword(fullCommand));
            default -> throw new BuddyException("Buddy there is no such command.");
        };
    }

    /**
     * Removes the leading command word (e.g. "todo") from a line of user
     * input, returning only the text after the first space.
     *
     * @param line the full line of user input, including the command word
     * @return the remainder of the line after the first space
     */
    private static String stripCommandWord(String line) {
        if (line.indexOf(' ') == -1) {
            return "";
        }
        return line.substring(line.indexOf(' ') + 1);
    }

    /**
     * Parses a todo from a "todo &lt;description&gt;" command line.
     *
     * @param line the full command line
     * @return the new todo
     */
    private static Todo parseTodo(String line) throws BuddyException {
        String description = stripCommandWord(line);
        if (description.isBlank()) {
            throw new BuddyException("Buddy you cant leave the description of a todo empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline from a
     * "deadline &lt;description&gt; /by &lt;yyyy-mm-dd HHmm&gt;" command line.
     *
     * @param line the full command line
     * @return the new deadline
     */
    private static Deadline parseDeadline(String line) throws BuddyException {
        String description = stripCommandWord(line);
        if (description.isBlank()) {
            throw new BuddyException("Buddy you cant leave the description of a deadline empty.");
        }
        String[] descriptionAndBy = description.split(" /by ");

        if (descriptionAndBy.length < 2) {
            throw new BuddyException("Buddy you need to specify the deadline using '/by'.");
        }
        try {
            LocalDateTime by = LocalDateTime.parse(descriptionAndBy[1], DEADLINE_INPUT_FORMAT);
            return new Deadline(descriptionAndBy[0], by);
        } catch (DateTimeParseException e) {
            throw new BuddyException("Buddy needs the deadline date in yyyy-mm-dd HHmm format, " +
                    "e.g. 2019-12-02 1800, not " + descriptionAndBy[1] + ".");
        }
    }

    /**
     * Parses an event from an
     * "event &lt;description&gt; /from &lt;start&gt; /to &lt;end&gt;" command line.
     *
     * @param line the full command line
     * @return the new event
     */
    private static Event parseEvent(String line) throws BuddyException {
        String description = stripCommandWord(line);
        if (description.isBlank()) {
            throw new BuddyException("Buddy you cant leave the description of an event empty.");
        }
        String[] descriptionAndFrom = description.split(" /from ");
        if (descriptionAndFrom.length < 2) {
            throw new BuddyException("Buddy you need to specify the start of an event using '/from'.");
        }
        String[] fromAndTo = descriptionAndFrom[1].split(" /to ");
        if (fromAndTo.length < 2) {
            throw new BuddyException("Buddy you need to specify the end of an event using '/to'.");
        }
        return new Event(descriptionAndFrom[0], fromAndTo[0], fromAndTo[1]);
    }

    /**
     * Parses the search keyword from a "find &lt;keyword&gt;" command line.
     *
     * @param line the full command line
     * @return the keyword to search for
     * @throws BuddyException if no keyword was given
     */
    private static String parseKeyword(String line) throws BuddyException {
        String keyword = stripCommandWord(line);
        if (keyword.isBlank()) {
            throw new BuddyException("Buddy you need to provide a keyword to search for.");
        }
        return keyword;
    }

    /**
     * Parses and validates that the task number given as the second word
     * of a mark/unmark/delete command line is present and numeric.
     *
     * @param commandParts the command line split by spaces
     * @param action the action being attempted (e.g. "mark"), used to
     *     phrase the error message if no task number was given
     * @return the parsed 1-based task number
     * @throws BuddyException if no task number was given or it isn't an integer
     */
    private static int parseTaskIndex(String[] commandParts, String action) throws BuddyException {
        if (commandParts.length < 2) {
            throw new BuddyException("Buddy you need to provide a task number to " + action + ".");
        }
        try {
            return Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException e) {
            throw new BuddyException("Buddy you need to provide an int for the task number, " +
                    "not " + commandParts[1] + ".");
        }
    }
}
