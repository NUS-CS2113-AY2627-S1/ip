package king;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import king.exception.KingException;
import king.storage.Storage;
import king.task.Deadline;
import king.task.Event;
import king.task.Task;
import king.task.ToDo;
import king.ui.Ui;

/**
 * Runs the King chatbot, which stores todos, deadlines, and events.
 * Commands can list or find tasks and change their completion status. Invalid commands
 * display an explanation and leave the task list unchanged.
 */
public class King {

    /** Expected syntax for a deadline command. */
    private static final String USAGE_DEADLINE = "deadline <description> /by <yyyy-MM-dd>";

    /** Expected syntax for an event command. */
    private static final String USAGE_EVENT = "event <description> /from <start> /to <end>";

    /**
     * Reads commands until the user enters {@code bye} or input ends.
     * Reports command errors without ending the conversation.
     *
     * @param args Command-line arguments; not used.
     */
    public static void main(String[] args) {
        run(Path.of("data", "king.txt"));
    }

    /**
     * Runs a session using the supplied save path, allowing tests to use isolated files.
     * Stops on load failure to avoid overwriting data that could not be recovered.
     *
     * @param filePath Path to the task save file.
     */
    public static void run(Path filePath) {
        try (Ui ui = new Ui()) {
            List<Task> tasks;
            Storage storage = new Storage(filePath);
            try {
                tasks = new ArrayList<>(storage.load());
            } catch (KingException exception) {
                ui.showLine();
                ui.showError(exception.getMessage());
                ui.showLine();
                return;
            }

            ui.showWelcome();

            while (ui.hasNextCommand()) {
                String command = ui.readCommand().trim();
                String[] parts = command.split("\\s+", 2);
                String firstWord = parts[0];
                String arguments = parts.length > 1 ? parts[1].trim() : "";
                ui.showLine();

                try {
                    requireText(command, "please enter a command, such as todo read a book or list.");
                    if (firstWord.equalsIgnoreCase("bye")) {
                        requireNoArguments(arguments, "bye");
                        ui.showGoodbye();
                        ui.showLine();
                        break;
                    } else if (firstWord.equalsIgnoreCase("list")) {
                        requireNoArguments(arguments, "list");
                        ui.showTasks(tasks);
                    } else if (firstWord.equalsIgnoreCase("find")) {
                        ui.showMatchingTasks(findTasks(tasks, arguments));
                    } else if (firstWord.equalsIgnoreCase("mark") || firstWord.equalsIgnoreCase("unmark")) {
                        ui.showStatusChanged(updateTaskStatus(tasks, arguments, firstWord.equalsIgnoreCase("mark")));
                        storage.save(tasks);
                    } else if (firstWord.equalsIgnoreCase("delete")) {
                        ui.showDeletedTask(deleteTask(tasks, arguments), tasks.size());
                        storage.save(tasks);
                    } else {
                        Task task = parseTask(firstWord, arguments);
                        tasks.add(task);
                        ui.showAddedTask(task, tasks.size());
                        storage.save(tasks);
                    }
                } catch (KingException exception) {
                    ui.showError(exception.getMessage());
                }
                ui.showLine();
            }
        }
    }

    /**
     * Returns tasks whose raw descriptions contain the case-sensitive search text, in list order.
     * Leaves the original list unchanged and excludes displayed dates and status icons from the search.
     *
     * @param tasks Tasks to inspect or update, in list order.
     * @param keyword Case-sensitive search text.
     * @return New list containing matching task references.
     * @throws KingException If no search text is supplied.
     */
    private static List<Task> findTasks(List<Task> tasks, String keyword) throws KingException {
        requireText(keyword, "please supply a search keyword. Try: find book");
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getRawDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    /**
     * Returns a task parsed from an add command without changing the task list.
     *
     * @param command Command name or text to validate.
     * @param arguments Text following the command name.
     * @return New incomplete task.
     * @throws KingException If the command is unknown or its fields are invalid.
     */
    private static Task parseTask(String command, String arguments) throws KingException {
        if (command.equalsIgnoreCase("todo")) {
            requireText(arguments, "a todo needs a description. Try: todo read a book");
            return new ToDo(arguments);
        } else if (command.equalsIgnoreCase("deadline")) {
            String[] parts = splitClause(arguments, "/by", USAGE_DEADLINE);
            requireText(parts[0], "a deadline needs a description. Try: " + USAGE_DEADLINE);
            requireText(parts[1], "a deadline needs a date after /by. Try: " + USAGE_DEADLINE);
            try {
                return new Deadline(parts[0], parts[1]);
            } catch (IllegalArgumentException exception) {
                throw new KingException(exception.getMessage());
            }
        } else if (command.equalsIgnoreCase("event")) {
            String[] parts = splitClause(arguments, "/from", USAGE_EVENT);
            // Check the entire command for duplicate /to markers before parsing the times.
            splitClause(arguments, "/to", USAGE_EVENT);
            String[] times = splitClause(parts[1], "/to", USAGE_EVENT);
            requireText(parts[0], "an event needs a description. Try: " + USAGE_EVENT);
            requireText(times[0], "an event needs a start time after /from. Try: " + USAGE_EVENT);
            requireText(times[1], "an event needs an end time after /to. Try: " + USAGE_EVENT);
            return new Event(parts[0], times[0], times[1]);
        }
        throw new KingException("I do not recognize '" + command
                + "'. Use todo, deadline, event, list, find, mark, unmark, delete, or bye.");
    }

    /**
     * Splits text at exactly one standalone clause marker and trims both fields.
     * Preserves empty fields so callers can explain which value is missing.
     *
     * @param text Text to process.
     * @param marker Standalone clause marker to locate.
     * @param usage Syntax to include in errors.
     * @return Two trimmed fields, which may be empty.
     * @throws KingException If the marker is missing or repeated.
     */
    private static String[] splitClause(String text, String marker, String usage) throws KingException {
        String[] parts = text.split("(?<!\\S)" + marker + "(?=\\s|$)", -1);
        if (parts.length != 2) {
            throw new KingException("use exactly one " + marker + " clause. Try: " + usage);
        }
        parts[0] = parts[0].trim();
        parts[1] = parts[1].trim();
        return parts;
    }

    /**
     * Rejects an empty command or field with an explanation for the user.
     *
     * @param text Text to process.
     * @param message Explanation to display or include in a failure.
     * @throws KingException If the text is empty.
     */
    private static void requireText(String text, String message) throws KingException {
        if (text.isEmpty()) {
            throw new KingException(message);
        }
    }

    /**
     * Rejects extra arguments for commands that take none.
     *
     * @param arguments Text following the command name.
     * @param command Command name or text to validate.
     * @throws KingException If arguments were supplied.
     */
    private static void requireNoArguments(String arguments, String command) throws KingException {
        if (!arguments.isEmpty()) {
            throw new KingException("'" + command + "' takes no arguments. Type just: " + command);
        }
    }

    /**
     * Returns the zero-based index of an existing task from a user-supplied number.
     *
     * @param indexText One-based task number supplied by the user.
     * @param taskCount Number of tasks in the list.
     * @return Zero-based task index.
     * @throws KingException If the number is missing, nonnumeric, or out of range.
     */
    private static int parseTaskIndex(String indexText, int taskCount) throws KingException {
        requireText(indexText, "please supply a task number. Try: mark 1, unmark 1, or delete 1");
        int index;
        try {
            index = Integer.parseInt(indexText);
        } catch (NumberFormatException exception) {
            throw new KingException("a task number must be a whole number. Use list to see valid numbers.");
        }
        if (taskCount == 0) {
            throw new KingException("your list is empty. Add a task first, for example: todo read a book");
        }
        if (index < 1 || index > taskCount) {
            throw new KingException("there is no task " + index + ". Choose a number from 1 to "
                    + taskCount + "; use list to see your tasks.");
        }
        return index - 1;
    }

    /**
     * Updates an existing task's completion status and returns the task.
     *
     * @param tasks Tasks to inspect or update, in list order.
     * @param indexText One-based task number supplied by the user.
     * @param isDone Whether to mark the task complete.
     * @return Task whose completion state was updated.
     * @throws KingException If the task number is invalid.
     */
    private static Task updateTaskStatus(List<Task> tasks, String indexText, boolean isDone) throws KingException {
        Task task = tasks.get(parseTaskIndex(indexText, tasks.size()));
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        return task;
    }

    /**
     * Removes and returns an existing task, closing the gap in the list.
     *
     * @param tasks Tasks to inspect or update, in list order.
     * @param indexText One-based task number supplied by the user.
     * @return Removed task.
     * @throws KingException If the task number is invalid.
     */
    private static Task deleteTask(List<Task> tasks, String indexText) throws KingException {
        return tasks.remove(parseTaskIndex(indexText, tasks.size()));
    }

}
