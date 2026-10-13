package bobby.parser;

import bobby.exception.InvalidTaskException;
import bobby.exception.TaskNotFoundException;
import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Todo;

/**
 * Handles parsing of users' inputs
 * to extract commands, tasks and their information.
 */
public class Parser {

    /**
     * Parses user input to extract a command.
     *
     * @param input the user's input
     * @return a string representing the command
     */
    public static String getCommand(String input) {
        return input.strip().split(" ", 2)[0];
    }

    /**
     * Parses user input to extract the arguments following a command.
     * Returns an empty string if there are no arguments.
     *
     * @param input the user's input
     * @return a string containing the arguments
     */
    public static String getArguments(String input) {
        String[] parts = input.strip().split(" ", 2);

        if (parts.length < 2) {
            return "";
        }

        return parts[1].strip();
    }

    /**
     * Parses the given argument into an integer representing a task number.
     *
     * @param argument the argument containing the task number
     * @return an integer representing the task number
     * @throws TaskNotFoundException if argument is blank
     * @throws NumberFormatException if the argument is not a valid integer
     */
    public static int getTaskNumber(String argument) {
        if (argument.isBlank()) {
            throw new TaskNotFoundException("Task number is missing...");
        }

        return Integer.parseInt(argument.strip());
    }

    /**
     * Parses the given argument into a Todo.
     *
     * @param todo the Todo task description
     * @return a Todo task containing the given description
     * @throws InvalidTaskException if the task description is blank
     */
    public static Todo parseTodo(String todo) {
        if (todo.isBlank()) {
            throw new InvalidTaskException("Task description is missing...");
        }

        return new Todo(todo.strip());
    }

    /**
     * Parses the given argument into a Deadline.
     * The argument must be in the format: <description>/<deadline>.
     *
     * @param deadline the Deadline task information
     * @return a Deadline containing the given information
     * @throws InvalidTaskException if the argument follows an invalid format
     */
    public static Deadline parseDeadline(String deadline) {
        String[] contents = deadline.split("/", 2);

        if (contents.length != 2
                || contents[0].isBlank()
                || contents[1].isBlank()) {
            throw new InvalidTaskException("Invalid deadline format. Format: <description>/<deadline>");
        }

        return new Deadline(contents[0].strip(), contents[1].strip());
    }

    /**
     * Parses the given argument into an Event.
     * The argument must be in the format: <description>/<start>/<end>.
     *
     * @param event the Event task information
     * @return an Event containing the given information
     * @throws InvalidTaskException if the argument follows an invalid format
     */
    public static Event parseEvent(String event) {
        String[] contents = event.split("/", 3);

        if (contents.length != 3
                || contents[0].isBlank()
                || contents[1].isBlank()
                || contents[2].isBlank()) {
            throw new InvalidTaskException("Invalid event format. Format: <description>/<start>/<end>");
        }

        return new Event(contents[0].strip(), contents[1].strip(), contents[2].strip());
    }
}