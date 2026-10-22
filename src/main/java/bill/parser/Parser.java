package bill.parser;

import bill.command.AddCommand;
import bill.command.Command;
import bill.command.DeleteCommand;
import bill.command.ExitCommand;
import bill.command.FindCommand;
import bill.command.HelpCommand;
import bill.command.ListCommand;
import bill.command.MarkCommand;
import bill.command.StatsCommand;
import bill.command.UnmarkCommand;
import bill.exception.BillException;
import bill.task.Deadline;
import bill.task.Event;
import bill.task.Task;
import bill.task.Todo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interprets a full user input line as a command, including task details and numbers.
 */
public final class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String TODO_PREFIX = TODO_COMMAND + " ";
    private static final String DEADLINE_PREFIX = DEADLINE_COMMAND + " ";
    private static final String EVENT_PREFIX = EVENT_COMMAND + " ";
    private static final Pattern DEADLINE_SEPARATOR = Pattern.compile("(?:^|\\s)/by(?:\\s|$)");
    private static final Pattern EVENT_FROM_SEPARATOR = Pattern.compile("(?:^|\\s)/from(?:\\s|$)");
    private static final Pattern EVENT_TO_SEPARATOR = Pattern.compile("(?:^|\\s)/to(?:\\s|$)");

    private Parser() {
    }

    /**
     * Converts one user input line into the command that should be executed.
     *
     * @param userInput Full command entered by the user.
     * @return Parsed command object.
     * @throws BillException If the command or its arguments are invalid.
     */
    public static Command parse(String userInput) throws BillException {
        String trimmedInput = userInput.strip();
        String normalizedInput = trimmedInput.toLowerCase(Locale.ROOT);

        if (normalizedInput.equals("bye")) {
            return new ExitCommand();
        }
        if (normalizedInput.equals("list")) {
            return new ListCommand();
        }
        if (normalizedInput.equals("help")) {
            return new HelpCommand();
        }
        if (normalizedInput.equals("stats")) {
            return new StatsCommand();
        }
        if (isCommand(normalizedInput, "find", "find ")) {
            String keyword = trimmedInput.substring("find".length()).strip();
            if (keyword.isEmpty()) {
                throw new BillException("Tell me what to find. Try: find book");
            }
            return new FindCommand(keyword);
        }
        if (isCommand(normalizedInput, "mark", "mark ")) {
            return new MarkCommand(parseTaskNumber(trimmedInput, "mark"));
        }
        if (isCommand(normalizedInput, "unmark", "unmark ")) {
            return new UnmarkCommand(parseTaskNumber(trimmedInput, "unmark"));
        }
        if (isCommand(normalizedInput, "delete", "delete ")) {
            return new DeleteCommand(parseTaskNumber(trimmedInput, "delete"));
        }
        return new AddCommand(parseTask(trimmedInput));
    }

    /**
     * Parses a one-based task number without checking whether that task exists.
     * TaskList owns the task count and performs that second check.
     */
    private static int parseTaskNumber(String userInput, String command) throws BillException {
        String taskNumberText = userInput.substring(command.length()).strip();
        if (taskNumberText.isEmpty()) {
            throw new BillException("Tell me which task number to " + command + ". Try: " + command + " 1");
        }
        try {
            return Integer.parseInt(taskNumberText);
        } catch (NumberFormatException exception) {
            throw new BillException("'" + taskNumberText + "' is not a valid task number. Try: "
                    + command + " 1");
        }
    }

    /**
     * Converts user input into a todo, deadline, or event.
     *
     * @param userInput Task command entered by the user.
     * @return Task represented by the command.
     * @throws BillException If the command is unknown or its details are incomplete.
     */
    public static Task parseTask(String userInput) throws BillException {
        String trimmedInput = userInput.strip();
        String normalizedInput = trimmedInput.toLowerCase(Locale.ROOT);
        if (isCommand(normalizedInput, TODO_COMMAND, TODO_PREFIX)) {
            return parseTodo(trimmedInput);
        } else if (isCommand(normalizedInput, DEADLINE_COMMAND, DEADLINE_PREFIX)) {
            return parseDeadline(trimmedInput);
        } else if (isCommand(normalizedInput, EVENT_COMMAND, EVENT_PREFIX)) {
            return parseEvent(trimmedInput);
        }
        if (normalizedInput.isEmpty()) {
            throw new BillException("Please enter a command. Type 'help' to see what I understand.");
        }
        throw new BillException("I don't recognise that command. Type 'help' to see what I understand.");
    }

    private static boolean isCommand(String input, String command, String commandPrefix) {
        return input.equals(command) || input.startsWith(commandPrefix);
    }

    private static Todo parseTodo(String userInput) throws BillException {
        String description = userInput.substring(TODO_COMMAND.length()).strip();
        if (description.isEmpty()) {
            throw new BillException("A todo needs a description. Try: todo read a book");
        }
        return new Todo(description);
    }

    private static Deadline parseDeadline(String userInput) throws BillException {
        String deadlineDetails = userInput.substring(DEADLINE_COMMAND.length()).strip();
        String normalizedDetails = deadlineDetails.toLowerCase(Locale.ROOT);
        Matcher separator = DEADLINE_SEPARATOR.matcher(normalizedDetails);
        if (!separator.find()) {
            throw new BillException("A deadline needs '/by'. Try: deadline submit report /by Friday");
        }
        String description = deadlineDetails.substring(0, separator.start()).strip();
        String by = deadlineDetails.substring(separator.end()).strip();
        if (description.isEmpty()) {
            throw new BillException("A deadline needs a description before '/by'.");
        }
        if (by.isEmpty()) {
            throw new BillException("A deadline needs a date or time after '/by'.");
        }
        if (by.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                return new Deadline(description, LocalDate.parse(by));
            } catch (DateTimeParseException exception) {
                throw new BillException("That isn't a valid date. Use yyyy-MM-dd, e.g. 2026-10-02.");
            }
        }
        return new Deadline(description, by);
    }

    private static Event parseEvent(String userInput) throws BillException {
        String eventDetails = userInput.substring(EVENT_COMMAND.length()).strip();
        String normalizedDetails = eventDetails.toLowerCase(Locale.ROOT);
        Matcher fromSeparator = EVENT_FROM_SEPARATOR.matcher(normalizedDetails);
        if (!fromSeparator.find()) {
            throw new BillException("An event needs '/from'. Try: event lecture /from 4pm /to 6pm");
        }
        Matcher toSeparator = EVENT_TO_SEPARATOR.matcher(normalizedDetails);
        if (!toSeparator.find(fromSeparator.end())) {
            throw new BillException("An event needs '/to' after its starting time.");
        }
        String description = eventDetails.substring(0, fromSeparator.start()).strip();
        String from = eventDetails.substring(fromSeparator.end(), toSeparator.start()).strip();
        String to = eventDetails.substring(toSeparator.end()).strip();
        if (description.isEmpty()) {
            throw new BillException("An event needs a description before '/from'.");
        }
        if (from.isEmpty()) {
            throw new BillException("An event needs a starting time after '/from'.");
        }
        if (to.isEmpty()) {
            throw new BillException("An event needs an ending time after '/to'.");
        }
        return new Event(description, from, to);
    }
}
