package eric.parser;

import java.util.List;

import eric.command.AddCommand;
import eric.command.Command;
import eric.command.DeleteCommand;
import eric.command.ExitCommand;
import eric.command.FindCommand;
import eric.command.ListCommand;
import eric.command.MarkCommand;
import eric.exception.EricException;
import eric.task.Deadline;
import eric.task.Event;
import eric.task.Task;
import eric.task.Todo;

/**
 * Makes sense of the commands typed by the user: it recognizes the command word, and turns the
 * arguments of a command into tasks and task numbers, to create the {@link Command} that the user
 * asked for. Anything that cannot be understood is reported with an {@link EricException} that
 * explains the problem and how to fix it.
 */
public final class Parser {
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";

    private static final List<String> COMMANDS_WITH_ARGUMENTS = List.of(
            FindCommand.COMMAND_WORD, MarkCommand.COMMAND_WORD_MARK, MarkCommand.COMMAND_WORD_UNMARK,
            DeleteCommand.COMMAND_WORD, COMMAND_TODO, COMMAND_DEADLINE, COMMAND_EVENT);

    private static final String MARKER_BY = "/by";
    private static final String MARKER_FROM = "/from";
    private static final String MARKER_TO = "/to";

    private static final int NOT_FOUND = -1;

    private static final String MESSAGE_COMMAND_LIST =
            "Available commands: todo, deadline, event, list, find, mark, unmark, delete, bye.";
    private static final String EXAMPLE_DEADLINE = "deadline return book /by Sunday";
    private static final String EXAMPLE_EVENT = "event project meeting /from Mon 2pm /to 4pm";

    private Parser() {
    }

    /**
     * Returns the command that the user asked for.
     *
     * @param fullCommand Line typed by the user.
     * @return The command, ready to be executed.
     * @throws EricException If the line is not a valid command, saying what is wrong and how to fix it.
     */
    public static Command parse(String fullCommand) throws EricException {
        String input = fullCommand.trim();
        String commandWord = parseCommandWord(input);
        String arguments = getArguments(input, commandWord);
        return switch (commandWord) {
        case ListCommand.COMMAND_WORD -> new ListCommand();
        case ExitCommand.COMMAND_WORD -> new ExitCommand();
        case FindCommand.COMMAND_WORD -> new FindCommand(parseKeyword(arguments));
        case MarkCommand.COMMAND_WORD_MARK -> new MarkCommand(parseTaskNumber(arguments, commandWord), true);
        case MarkCommand.COMMAND_WORD_UNMARK ->
                new MarkCommand(parseTaskNumber(arguments, commandWord), false);
        case DeleteCommand.COMMAND_WORD -> new DeleteCommand(parseTaskNumber(arguments, commandWord));
        case COMMAND_TODO -> new AddCommand(parseTodo(arguments));
        case COMMAND_DEADLINE -> new AddCommand(parseDeadline(arguments));
        case COMMAND_EVENT -> new AddCommand(parseEvent(arguments));
        default -> throw new IllegalStateException("Unhandled command: " + commandWord);
        };
    }

    /**
     * Returns the command word that {@code input} starts with. "list" and "bye" must be typed on
     * their own; the other commands may be followed by arguments.
     *
     * @param input Line typed by the user, without surrounding spaces.
     * @return The command word that {@code input} starts with.
     * @throws EricException If {@code input} is empty or does not start with a known command.
     */
    private static String parseCommandWord(String input) throws EricException {
        if (input.isEmpty()) {
            throw new EricException("You didn't type a command.", MESSAGE_COMMAND_LIST);
        }
        if (input.equals(ListCommand.COMMAND_WORD)) {
            return ListCommand.COMMAND_WORD;
        }
        if (input.equals(ExitCommand.COMMAND_WORD)) {
            return ExitCommand.COMMAND_WORD;
        }
        for (String command : COMMANDS_WITH_ARGUMENTS) {
            if (isCommand(input, command)) {
                return command;
            }
        }
        throw new EricException("I don't know the command \"" + input + "\".", MESSAGE_COMMAND_LIST);
    }

    /**
     * Returns the arguments that follow {@code command} in {@code input}, without surrounding spaces.
     *
     * @param input Line typed by the user, which starts with {@code command}.
     * @param command Command word of {@code input}.
     */
    private static String getArguments(String input, String command) {
        return input.substring(command.length()).trim();
    }

    /**
     * Creates the task described by the arguments of "todo DESCRIPTION".
     *
     * @param arguments Everything typed after "todo".
     * @throws EricException If the description is empty or cannot be saved.
     */
    private static Task parseTodo(String arguments) throws EricException {
        if (arguments.isEmpty()) {
            throw new EricException("The description of a todo is empty.",
                    "Type a description after \"todo\", e.g. " + AddCommand.EXAMPLE_TODO);
        }
        requireNoFileSeparator(arguments);
        return new Todo(arguments);
    }

    /**
     * Creates the task described by the arguments of "deadline DESCRIPTION /by DATE".
     *
     * @param arguments Everything typed after "deadline".
     * @throws EricException If /by, the description or the date is missing, or cannot be saved.
     */
    private static Task parseDeadline(String arguments) throws EricException {
        int byIndex = findMarker(arguments, MARKER_BY);
        if (byIndex == NOT_FOUND) {
            throw new EricException("A deadline needs a /by date, but I couldn't find one.",
                    "Use the format: deadline DESCRIPTION /by DATE, e.g. " + EXAMPLE_DEADLINE);
        }

        String description = arguments.substring(0, byIndex).trim();
        String by = arguments.substring(byIndex + MARKER_BY.length()).trim();
        if (description.isEmpty()) {
            throw new EricException("The description of a deadline is empty.",
                    "Type a description before /by, e.g. " + EXAMPLE_DEADLINE);
        }
        if (by.isEmpty()) {
            throw new EricException("The date after /by is empty.",
                    "Type when the task is due after /by, e.g. " + EXAMPLE_DEADLINE);
        }
        requireNoFileSeparator(description, by);
        return new Deadline(description, by);
    }

    /**
     * Creates the task described by the arguments of "event DESCRIPTION /from START /to END".
     *
     * @param arguments Everything typed after "event".
     * @throws EricException If /from, /to, the description or a time is missing or misplaced, or
     *         cannot be saved.
     */
    private static Task parseEvent(String arguments) throws EricException {
        int fromIndex = findMarker(arguments, MARKER_FROM);
        int toIndex = findMarker(arguments, MARKER_TO);
        requireValidEventMarkers(fromIndex, toIndex);

        String description = arguments.substring(0, fromIndex).trim();
        String from = arguments.substring(fromIndex + MARKER_FROM.length(), toIndex).trim();
        String to = arguments.substring(toIndex + MARKER_TO.length()).trim();
        if (description.isEmpty()) {
            throw new EricException("The description of an event is empty.",
                    "Type a description before /from, e.g. " + EXAMPLE_EVENT);
        }
        if (from.isEmpty()) {
            throw new EricException("The start time after /from is empty.",
                    "Type when the event starts after /from, e.g. " + EXAMPLE_EVENT);
        }
        if (to.isEmpty()) {
            throw new EricException("The end time after /to is empty.",
                    "Type when the event ends after /to, e.g. " + EXAMPLE_EVENT);
        }
        requireNoFileSeparator(description, from, to);
        return new Event(description, from, to);
    }

    /**
     * Returns the keyword typed after "find". Everything after the command word is the keyword, so a
     * keyword can have several words.
     *
     * @param arguments Everything typed after "find".
     * @throws EricException If no keyword was typed.
     */
    private static String parseKeyword(String arguments) throws EricException {
        if (arguments.isEmpty()) {
            throw new EricException("The keyword to search for is missing.",
                    "Type a keyword after \"find\", e.g. " + FindCommand.EXAMPLE_FIND);
        }
        return arguments;
    }

    /**
     * Returns the task number typed after a command such as "mark" or "delete". Task numbers start
     * at 1; whether the number refers to an existing task is for the caller to check.
     *
     * @param arguments Everything typed after the command.
     * @param command Command word, used in the messages.
     * @throws EricException If the number is missing or is not a plain whole number.
     */
    private static int parseTaskNumber(String arguments, String command) throws EricException {
        if (arguments.isEmpty()) {
            throw new EricException("The task number is missing.",
                    "Type the number of a task after \"" + command + "\", e.g. " + command + " 2");
        }
        if (!isPlainInteger(arguments)) {
            throw new EricException("\"" + arguments + "\" is not a valid task number.",
                    "Use a plain whole number (no + sign or leading zeros), e.g. " + command
                    + " 2. Type list to see the task numbers.");
        }
        return Integer.parseInt(arguments);
    }

    /** Returns true if {@code input} is exactly {@code command} or starts with it followed by a space. */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Returns the index of {@code marker} (e.g. "/by") in {@code text}, or NOT_FOUND if absent.
     * The marker must be a whole word, so "/tomorrow" is not mistaken for "/to".
     */
    private static int findMarker(String text, String marker) {
        int index = text.indexOf(marker);
        while (index != NOT_FOUND) {
            int end = index + marker.length();
            boolean isStartOfWord = index == 0 || text.charAt(index - 1) == ' ';
            boolean isEndOfWord = end == text.length() || text.charAt(end) == ' ';
            if (isStartOfWord && isEndOfWord) {
                return index;
            }
            index = text.indexOf(marker, index + 1);
        }
        return NOT_FOUND;
    }

    /**
     * Returns true if {@code text} is an integer written in its plain form, e.g. "2" or "-1",
     * but not "+2", "02" or "-0", and small enough to fit in an int.
     */
    private static boolean isPlainInteger(String text) {
        try {
            return String.valueOf(Integer.parseInt(text)).equals(text);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Throws if any of {@code texts} contains the separator of the save file columns, because such a
     * task could not be read back from the file.
     */
    private static void requireNoFileSeparator(String... texts) throws EricException {
        for (String text : texts) {
            if (text.contains(Task.FILE_SEPARATOR)) {
                throw new EricException("A task can't contain \"" + Task.FILE_SEPARATOR + "\", because that "
                        + "separates the columns of the save file.",
                        "Remove the \"" + Task.FILE_SEPARATOR
                        + "\" from your command, e.g. use a comma instead.");
            }
        }
    }

    /** Throws, saying which marker is missing or misplaced, unless an event has /from and then /to. */
    private static void requireValidEventMarkers(int fromIndex, int toIndex) throws EricException {
        String format = "Use the format: event DESCRIPTION /from START /to END, e.g. " + EXAMPLE_EVENT;
        if (fromIndex == NOT_FOUND && toIndex == NOT_FOUND) {
            throw new EricException(
                    "An event needs a /from time and a /to time, but I couldn't find either.", format);
        }
        if (fromIndex == NOT_FOUND) {
            throw new EricException("An event needs a /from time, but I couldn't find one.", format);
        }
        if (toIndex == NOT_FOUND) {
            throw new EricException("An event needs a /to time, but I couldn't find one.", format);
        }
        if (toIndex < fromIndex) {
            throw new EricException("/to comes before /from.", "Put /from first, then /to. " + format);
        }
    }
}
