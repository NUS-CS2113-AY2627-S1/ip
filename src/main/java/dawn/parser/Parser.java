package dawn.parser;

import dawn.command.AddCommand;
import dawn.command.Command;
import dawn.command.DeleteCommand;
import dawn.command.ExitCommand;
import dawn.command.FindCommand;
import dawn.command.HelpCommand;
import dawn.command.ListCommand;
import dawn.command.SetCompletionCommand;
import dawn.command.ViewCommand;
import dawn.exception.DawnException;
import dawn.task.Deadline;
import dawn.task.Event;
import dawn.task.TaskDateTime;
import dawn.task.ToDo;

import java.util.Locale;

/**
 * Parses user input and validates command arguments.
 */
public class Parser {
    private static final String DEADLINE_MARKER = "/by";
    private static final String EVENT_START_MARKER = "/from";
    private static final String EVENT_END_MARKER = "/to";

    /**
     * Parses a raw user input string into an executable {@link Command}.
     *
     * @param input the raw user input line.
     * @return the corresponding executable {@link Command}.
     * @throws DawnException if the input is malformed or unrecognized.
     */
    public static Command parse(String input) throws DawnException {
        String[] parsed = parseCommand(input);
        String command = parsed[0];
        String arguments = parsed[1];
        CommandWord commandWord = CommandWord.fromKeyword(command);
        if (commandWord == null) {
            throw new DawnException(unknownCommandMessage(command));
        }

        switch (commandWord) {
            case BYE:
                requireNoArguments(command, arguments);
                return new ExitCommand();
            case LIST:
                requireNoArguments(command, arguments);
                return new ListCommand();
            case HELP:
                requireNoArguments(command, arguments);
                return new HelpCommand();
            case MARK:
                return new SetCompletionCommand(parseTaskNumber(arguments, CommandWord.MARK.usage()), true);
            case UNMARK:
                return new SetCompletionCommand(parseTaskNumber(arguments, CommandWord.UNMARK.usage()), false);
            case DELETE:
                return new DeleteCommand(parseTaskNumber(arguments, CommandWord.DELETE.usage()));
            case TODO:
                return new AddCommand(new ToDo(parseTodoArgs(arguments)));
            case DEADLINE:
                return new AddCommand(parseDeadlineArgs(arguments));
            case EVENT:
                return new AddCommand(parseEventArgs(arguments));
            case VIEW:
                return parseViewArgs(arguments);
            case FIND:
                return parseFindArgs(arguments);
            default:
                throw new DawnException(unknownCommandMessage(command));
        }
    }

    /**
     * Splits a raw command string into the command word and raw arguments.
     *
     * @param input raw input string.
     * @return two-element array with command word and arguments.
     * @throws DawnException if input is blank.
     */
    private static String[] parseCommand(String input) throws DawnException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new DawnException(unknownCommandMessage(""));
        }
        String[] parts = trimmedInput.split("\\s+", 2);
        String command = parts[0];
        String arguments = parts.length == 2 ? parts[1].trim() : "";
        return new String[]{command, arguments};
    }

    /**
     * Parses a one-based task number and converts it to a zero-based list index.
     *
     * @param arguments raw command arguments.
     * @param usage expected command format shown when validation fails.
     * @return the corresponding zero-based task index.
     * @throws DawnException if the argument is missing or is not a positive integer.
     */
    private static int parseTaskNumber(String arguments, String usage) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("I need a task number. Use: " + usage);
        }
        if (!arguments.matches("[1-9]\\d*")) {
            throw new DawnException("I need a positive whole number for the task number. Use: " + usage);
        }
        try {
            return Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new DawnException("I need a positive whole number for the task number. Use: " + usage);
        }
    }

    /**
     * Extrapolates and validates the description for a ToDo task from the command arguments.
     *
     * @param arguments the raw command arguments.
     * @return the validated task description safely extracted.
     * @throws DawnException if the description is blank.
     */
    private static String parseTodoArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("I need a description for your todo. Use: " + CommandWord.TODO.usage());
        }
        return arguments;
    }

    /**
     * Parses the arguments for a Deadline task into a description and due date.
     *
     * @param arguments the raw command arguments containing description and /by clause.
     * @return a constructed Deadline task.
     * @throws DawnException if markers are missing or fields are blank.
     */
    private static Deadline parseDeadlineArgs(String arguments) throws DawnException {
        int byIndex = findStandaloneMarker(arguments, DEADLINE_MARKER);
        if (byIndex < 0) {
            throw new DawnException("I need the /by keyword for your deadline. Use: " + CommandWord.DEADLINE.usage());
        }
        String description = arguments.substring(0, byIndex).trim();
        String dueDateStr = arguments.substring(byIndex + DEADLINE_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("I need a description for your deadline. Use: "
                    + CommandWord.DEADLINE.usage());
        }
        if (dueDateStr.isEmpty()) {
            throw new DawnException("I need a due date. Use: " + CommandWord.DEADLINE.usage());
        }
        TaskDateTime dueDate = DateTimeParser.parseFlexible(dueDateStr);
        return new Deadline(description, dueDate);
    }

    /**
     * Parses the arguments for an Event task into a description, start date, and end date.
     *
     * @param arguments the raw command arguments containing description, /from, and /to clauses.
     * @return a constructed Event task.
     * @throws DawnException if markers are missing out of order, or fields are blank.
     */
    private static Event parseEventArgs(String arguments) throws DawnException {
        int fromIndex = findStandaloneMarker(arguments, EVENT_START_MARKER);
        int toIndex = findStandaloneMarker(arguments, EVENT_END_MARKER);
        if (fromIndex < 0) {
            throw new DawnException("I need the /from keyword for your event. Use: " + CommandWord.EVENT.usage());
        }
        if (toIndex < 0) {
            throw new DawnException("I need the /to keyword for your event. Use: " + CommandWord.EVENT.usage());
        }
        if (toIndex < fromIndex) {
            throw new DawnException("I need /from before /to. Use: " + CommandWord.EVENT.usage());
        }
        String description = arguments.substring(0, fromIndex).trim();
        String startStr = arguments.substring(fromIndex + EVENT_START_MARKER.length(), toIndex).trim();
        String endStr = arguments.substring(toIndex + EVENT_END_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new DawnException("I need a description for your event. Use: " + CommandWord.EVENT.usage());
        }
        if (startStr.isEmpty()) {
            throw new DawnException("I need a start for your event. Use: " + CommandWord.EVENT.usage());
        }
        if (endStr.isEmpty()) {
            throw new DawnException("I need an end for your event. Use: " + CommandWord.EVENT.usage());
        }
        TaskDateTime startDate = DateTimeParser.parseFlexible(startStr);
        TaskDateTime endDate = DateTimeParser.parseFlexible(endStr);
        return new Event(description, startDate, endDate);
    }

    /**
     * Parses the arguments for a view command to extract the target date.
     *
     * @param arguments the raw command arguments.
     * @return an executable ViewCommand holding the target date.
     * @throws DawnException if the date is blank or malformed.
     */
    private static Command parseViewArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("I need a date. Use: " + CommandWord.VIEW.usage());
        }
        TaskDateTime targetDateTime = DateTimeParser.parseStrict(arguments);
        return new ViewCommand(targetDateTime);
    }

    /**
     * Parses the arguments for a find command to extract the search keyword.
     *
     * @param arguments the raw command arguments.
     * @return an executable FindCommand holding the extracted keyword.
     * @throws DawnException if the keyword is blank.
     */
    private static Command parseFindArgs(String arguments) throws DawnException {
        if (arguments.isEmpty()) {
            throw new DawnException("I need a search keyword. Use: " + CommandWord.FIND.usage());
        }
        return new FindCommand(arguments);
    }

    /**
     * Asserts that no trailing arguments were supplied for a command that does not accept any.
     *
     * @param command the command word invoked.
     * @param arguments the trailing arguments string.
     * @throws DawnException if arguments is not empty.
     */
    private static void requireNoArguments(String command, String arguments) throws DawnException {
        if (!arguments.isEmpty()) {
            throw new DawnException("I don't need arguments for " + command + ". Use: " + command);
        }
    }

    /**
     * Searches for a command marker token bounded by whitespace matching standard separator rules.
     *
     * @param text the text to search within.
     * @param marker the specific marker sequence to find.
     * @return the zero-based index marking the start of the token, or -1 if no standalone match is found.
     */
    private static int findStandaloneMarker(String text, String marker) {
        int index = text.indexOf(marker);
        while (index >= 0) {
            int markerEnd = index + marker.length();
            boolean leftBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            boolean rightBoundary = markerEnd == text.length()
                    || Character.isWhitespace(text.charAt(markerEnd));
            if (leftBoundary && rightBoundary) {
                return index;
            }
            index = text.indexOf(marker, markerEnd);
        }
        return -1;
    }

    /**
     * Generates a helpful error message when an unrecognized command is typed, detecting plausible typos.
     *
     * @param commandWord the malformed command word received.
     * @return a message suggesting a likely command or directing the user to help.
     */
    private static String unknownCommandMessage(String commandWord) {
        String normalizedCommand = commandWord.toLowerCase(Locale.ROOT);
        if (normalizedCommand.contains(CommandWord.TODO.keyword())) {
            return suggestCommand(CommandWord.TODO);
        }
        if (normalizedCommand.contains(CommandWord.DEADLINE.keyword())) {
            return suggestCommand(CommandWord.DEADLINE);
        }
        if (normalizedCommand.contains(CommandWord.EVENT.keyword())) {
            return suggestCommand(CommandWord.EVENT);
        }
        if (normalizedCommand.contains(CommandWord.UNMARK.keyword())) {
            return suggestCommand(CommandWord.UNMARK);
        }
        if (normalizedCommand.contains(CommandWord.MARK.keyword())) {
            return suggestCommand(CommandWord.MARK);
        }
        if (normalizedCommand.contains(CommandWord.DELETE.keyword())) {
            return suggestCommand(CommandWord.DELETE);
        }
        if (normalizedCommand.contains(CommandWord.VIEW.keyword())) {
            return suggestCommand(CommandWord.VIEW);
        }
        if (normalizedCommand.contains(CommandWord.FIND.keyword())) {
            return suggestCommand(CommandWord.FIND);
        }
        return "I don't recognize that command. Type 'help' to see the available commands.";
    }

    /**
     * Formats a suggestion consistently for each recognized command fragment.
     */
    private static String suggestCommand(CommandWord commandWord) {
        return "I don't recognize that command.\nDid you mean: " + commandWord.usage() + "?";
    }
}
