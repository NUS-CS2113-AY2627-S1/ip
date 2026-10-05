package bigbrother.parser;

import bigbrother.exception.BigBrotherException;
import bigbrother.task.Deadline;
import bigbrother.task.Event;
import bigbrother.task.Task;
import bigbrother.task.ToDo;

/**
 * Interprets user input and constructs the requested task or command argument.
 */
public class Parser {
    /**
     * Identifies a supported user command and whether it changes saved tasks.
     */
    public enum CommandType {
        /** Ends the chatbot session. */
        BYE("bye", false),
        /** Displays every task. */
        LIST("list", false),
        /** Searches task descriptions. */
        FIND("find", false),
        /** Marks a task as completed. */
        MARK("mark", true),
        /** Marks a task as incomplete. */
        UNMARK("unmark", true),
        /** Removes a task. */
        DELETE("delete", true),
        /** Adds a todo task. */
        TODO("todo", true),
        /** Adds a deadline task. */
        DEADLINE("deadline", true),
        /** Adds an event task. */
        EVENT("event", true);

        private final String keyword;
        private final boolean changesTaskList;

        CommandType(String keyword, boolean changesTaskList) {
            this.keyword = keyword;
            this.changesTaskList = changesTaskList;
        }

        /**
         * Checks whether the command changes tasks and must be saved.
         *
         * @return true if the task list changes
         */
        public boolean changesTaskList() {
            return changesTaskList;
        }
    }

    /**
     * Prevents instantiation because parsing does not need object state.
     */
    private Parser() {
    }

    /**
     * Recognizes the command named at the start of the user's input.
     *
     * @param input the complete command line
     * @return the recognized command type
     * @throws BigBrotherException if the command is unknown
     */
    public static CommandType parseCommandType(String input) throws BigBrotherException {
        for (CommandType type : CommandType.values()) {
            if (type == CommandType.BYE || type == CommandType.LIST) {
                if (input.equals(type.keyword)) {
                    return type;
                }
            } else if (matchesCommand(input, type.keyword)) {
                return type;
            }
        }
        throw new BigBrotherException("Invalid command. Try todo, deadline, event, list, find, mark,"
                + " unmark, delete, or bye.");
    }

    /**
     * Reads a task number from a mark, unmark, or delete command.
     *
     * @param input the complete command line
     * @param type the recognized command type
     * @return the one-based task number
     * @throws BigBrotherException if the number is missing or is not a whole number
     */
    public static int parseTaskNumber(String input, CommandType type) throws BigBrotherException {
        String taskNumberInput = input.substring(type.keyword.length()).trim();
        if (taskNumberInput.isEmpty()) {
            throw new BigBrotherException("Please provide a task number after " + type.keyword + ".");
        }

        try {
            return Integer.parseInt(taskNumberInput);
        } catch (NumberFormatException exception) {
            throw new BigBrotherException("The task number must be a whole number.");
        }
    }

    /**
     * Reads the search keyword from a find command.
     *
     * @param input the complete command line
     * @return the keyword to search for
     * @throws BigBrotherException if the keyword is missing
     */
    public static String parseKeyword(String input) throws BigBrotherException {
        String keyword = input.substring(CommandType.FIND.keyword.length()).trim();
        if (keyword.isEmpty()) {
            throw new BigBrotherException("Please provide a keyword after find.");
        }
        return keyword;
    }

    /**
     * Creates a todo from its command.
     *
     * @param input the complete command line
     * @return the new todo task
     * @throws BigBrotherException if the description is empty
     */
    public static Task parseTodo(String input) throws BigBrotherException {
        String description = input.substring(CommandType.TODO.keyword.length()).trim();
        if (description.isEmpty()) {
            throw new BigBrotherException("     ERROR - Empty Todo task.");
        }
        return new ToDo(description);
    }

    /**
     * Creates a deadline from its command.
     *
     * @param input the complete command line
     * @return the new deadline task
     * @throws BigBrotherException if the description or due time is missing
     */
    public static Task parseDeadline(String input) throws BigBrotherException {
        String details = input.substring(CommandType.DEADLINE.keyword.length()).trim();
        if (details.isEmpty() || details.equals("/by") || details.startsWith("/by ")) {
            throw new BigBrotherException("ERROR - Empty Deadline Task.");
        }
        if (details.endsWith(" /by")) {
            throw new BigBrotherException("A deadline must include a date or time after /by.");
        }

        String[] parts = details.split(" /by ", 2);
        if (parts.length < 2) {
            throw new BigBrotherException("A deadline must use: deadline <description> /by <date or time>.");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty()) {
            throw new BigBrotherException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new BigBrotherException("A deadline must include a date or time after /by.");
        }
        return new Deadline(description, by);
    }

    /**
     * Creates an event from its command.
     *
     * @param input the complete command line
     * @return the new event task
     * @throws BigBrotherException if its description or time range is missing
     */
    public static Task parseEvent(String input) throws BigBrotherException {
        String details = input.substring(CommandType.EVENT.keyword.length()).trim();
        if (details.isEmpty() || details.equals("/from") || details.startsWith("/from ")) {
            throw new BigBrotherException("The description of an event cannot be empty.");
        }
        if (details.endsWith(" /from")) {
            throw new BigBrotherException("An event must include a start time after /from.");
        }

        String[] descriptionAndTime = details.split(" /from ", 2);
        if (descriptionAndTime.length < 2) {
            throw new BigBrotherException("An event must use: event <description> /from <start> /to <end>.");
        }

        String description = descriptionAndTime[0].trim();
        String timeInput = descriptionAndTime[1].trim();
        if (description.isEmpty()) {
            throw new BigBrotherException("The description of an event cannot be empty.");
        }
        if (timeInput.isEmpty() || timeInput.equals("/to") || timeInput.startsWith("/to ")) {
            throw new BigBrotherException("An event must include a start time after /from.");
        }
        if (timeInput.endsWith(" /to")) {
            throw new BigBrotherException("An event must include an end time after /to.");
        }

        String[] timeRange = timeInput.split(" /to ", 2);
        if (timeRange.length < 2) {
            throw new BigBrotherException("An event must use: event <description> /from <start> /to <end>.");
        }

        String from = timeRange[0].trim();
        String to = timeRange[1].trim();
        if (from.isEmpty()) {
            throw new BigBrotherException("An event must include a start time after /from.");
        }
        if (to.isEmpty()) {
            throw new BigBrotherException("An event must include an end time after /to.");
        }
        return new Event(description, from, to);
    }

    /**
     * Checks for a command name followed by either nothing or a space and arguments.
     *
     * @param input the complete command line
     * @param keyword the command name to match
     * @return true if the command name matches
     */
    private static boolean matchesCommand(String input, String keyword) {
        return input.equals(keyword) || input.startsWith(keyword + " ");
    }
}
