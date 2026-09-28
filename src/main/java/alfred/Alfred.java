package alfred;

import java.io.IOException;
import java.nio.file.Path;

import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.Task;
import alfred.task.Todo;

/**
 * Runs the Alfred command-line chatbot.
 */
public class Alfred {

    private static TaskList tasks = new TaskList();
    private static final Storage storage = new Storage(Path.of("data", "alfred.txt"));
    private static final Ui ui = new Ui();

    /**
     * Starts Alfred and processes commands until the user exits.
     *
     * @param args Command-line arguments. Alfred does not use them.
     */
    public static void main(String[] args) {
        try {
            tasks = new TaskList(storage.load(ui::showMessage));
        } catch (IOException exception) {
            ui.showLoadingError();
        }
        ui.showWelcomeMessage();

        try (ui) {
            while (ui.hasNextCommand()) {
                String command = ui.readCommand().trim();
                ui.showDivider();

                if (command.equals("bye")) {
                    ui.showGoodbyeMessage();
                    break;
                }
                try {
                    processCommand(command);
                } catch (AlfredException exception) {
                    ui.showMessage(exception.getMessage());
                } catch (IOException exception) {
                    ui.showSavingError();
                }

                ui.showDivider();
            }
        }
    }

    /**
     * Routes a command to the operation that handles it.
     *
     * @param command Full command entered by the user.
     */
    private static void processCommand(String command) throws AlfredException, IOException {
        if (command.equals("list")) {
            ui.showTaskList(tasks);
        } else if (command.equals("delete") || command.startsWith("delete ")) {
            deleteTask(command);
        } else if (command.equals("unmark") || command.startsWith("unmark ")) {
            updateTaskStatus(command, false);
        } else if (command.equals("mark") || command.startsWith("mark ")) {
            updateTaskStatus(command, true);
        } else if (command.equals("todo") || command.startsWith("todo ")) {
            addTodo(command);
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            addDeadline(command);
        } else if (command.equals("event") || command.startsWith("event ")) {
            addEvent(command);
        } else {
            throw new AlfredException("Pardon me Master Wayne, I did not quite get that.");
        }
    }

    /**
     * Deletes the task identified by a delete command.
     *
     * @param command Full command entered by the user.
     */
    private static void deleteTask(String command) throws AlfredException, IOException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length != 2) {
            throw new AlfredException("Please provide a task number to delete.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new AlfredException("Invalid task number.");
            }

            int taskIndex = taskNumber - 1;
            Task deletedTask = tasks.delete(taskIndex);
            storage.save(tasks);

            ui.showTaskDeleted(deletedTask, tasks.size());
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Marks or unmarks the task identified by a status command.
     *
     * @param command Full command entered by the user.
     * @param isMarkedAsDone Whether the task should be marked as done.
     */
    private static void updateTaskStatus(String command, boolean isMarkedAsDone)
            throws AlfredException, IOException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length < 2) {
            if (isMarkedAsDone) {
                throw new AlfredException("Please provide a task number to mark as done.");
            }
            throw new AlfredException("Please provide a task number to mark as not done.");
        }
        if (parts.length > 2) {
            if (isMarkedAsDone) {
                throw new AlfredException("Please provide only one task number to mark as done.");
            }
            throw new AlfredException("Please provide only one task number to mark as not done.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new AlfredException("Invalid task number.");
            }

            Task task = tasks.get(taskNumber - 1);
            if (isMarkedAsDone) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
            }
            ui.showTaskStatusChanged(isMarkedAsDone);
            storage.save(tasks);
            ui.showTask(task);
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Parses and adds a todo command if it contains a description.
     *
     * @param command Full command entered by the user.
     */
    private static void addTodo(String command) throws AlfredException, IOException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new AlfredException("The description of a todo cannot be empty.");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw new AlfredException("The description of a todo cannot contain \"" + Storage.DELIMITER + "\", "
                    + "as it is reserved for saving tasks to disk.");
        }

        addTask(new Todo(description));
    }

    /**
     * Parses and adds a deadline command containing a description and
     * {@code /by} value. The {@code /by} marker must appear as a standalone
     * token (surrounded by whitespace or the ends of the text) exactly once.
     *
     * @param command Full command entered by the user.
     */
    private static void addDeadline(String command) throws AlfredException, IOException {
        String arguments = command.substring("deadline".length()).trim();
        int byPosition = findMarker(arguments, "/by", 0);
        if (byPosition < 0) {
            throw createDeadlineException("the /by marker is missing");
        }
        if (findMarker(arguments, "/by", byPosition + "/by".length()) >= 0) {
            throw createDeadlineException("the /by marker can only be used once");
        }

        String description = arguments.substring(0, byPosition).trim();
        String deadline = arguments.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw createDeadlineException("the task description is missing");
        }
        if (deadline.isEmpty()) {
            throw createDeadlineException("the deadline date or time is missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (deadline.contains(Storage.DELIMITER)) {
            throw createDeadlineException("the deadline date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }

        addTask(new Deadline(description, deadline));
    }

    /**
     * Finds the next standalone occurrence of a marker (such as {@code /by}) in
     * a piece of text, starting the search at the given index. A marker only
     * counts as standalone when it is preceded and followed by whitespace or
     * the ends of the text, so it is not mistaken for a marker embedded inside
     * a longer word (for example {@code /byte} does not match {@code /by}).
     *
     * @param text Text to search.
     * @param marker Marker to look for, such as {@code "/by"}.
     * @param fromIndex Index to start searching from.
     * @return Index of the next standalone occurrence, or {@code -1} if none is
     * found.
     */
    private static int findMarker(String text, String marker, int fromIndex) {
        int searchFrom = fromIndex;
        while (true) {
            int index = text.indexOf(marker, searchFrom);
            if (index < 0) {
                return -1;
            }

            boolean isPrecededByBoundary = index == 0 || Character.isWhitespace(text.charAt(index - 1));
            int afterMarkerIndex = index + marker.length();
            boolean isFollowedByBoundary = afterMarkerIndex == text.length()
                    || Character.isWhitespace(text.charAt(afterMarkerIndex));
            if (isPrecededByBoundary && isFollowedByBoundary) {
                return index;
            }
            searchFrom = index + 1;
        }
    }

    /**
     * Creates a detailed deadline error with the required command format.
     *
     * @param reason Explanation of why the deadline command is invalid.
     * @return Exception containing the error and correction instructions.
     */
    private static AlfredException createDeadlineException(String reason) {
        return new AlfredException("Unable to add the deadline: " + reason + ".\n"
                + "Use this format: deadline <description> /by <date or time>\n"
                + "Example: deadline return book /by tomorrow");
    }

    /**
     * Parses and adds an event command containing a description, start, and end
     * value. The {@code /from} and {@code /to} markers must each appear as a
     * standalone token exactly once, with {@code /from} before {@code /to}.
     *
     * @param command Full command entered by the user.
     */
    private static void addEvent(String command) throws AlfredException, IOException {
        String arguments = command.substring("event".length()).trim();
        int fromPosition = findMarker(arguments, "/from", 0);
        if (fromPosition < 0) {
            throw createEventException("the /from marker is missing");
        }
        if (findMarker(arguments, "/from", fromPosition + "/from".length()) >= 0) {
            throw createEventException("the /from marker can only be used once");
        }

        int toPosition = findMarker(arguments, "/to", fromPosition + "/from".length());
        if (toPosition < 0) {
            if (findMarker(arguments, "/to", 0) >= 0) {
                throw createEventException("the /to marker must come after the /from marker");
            }
            throw createEventException("the /to marker is missing");
        }
        if (findMarker(arguments, "/to", toPosition + "/to".length()) >= 0) {
            throw createEventException("the /to marker can only be used once");
        }

        String description = arguments.substring(0, fromPosition).trim();
        String start = arguments.substring(fromPosition + "/from".length(), toPosition).trim();
        String end = arguments.substring(toPosition + "/to".length()).trim();
        if (description.isEmpty()) {
            throw createEventException("the task description is missing");
        }
        if (start.isEmpty()) {
            throw createEventException("the start date or time is missing");
        }
        if (end.isEmpty()) {
            throw createEventException("the end date or time is missing");
        }
        if (description.contains(Storage.DELIMITER)) {
            throw createEventException("the task description cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (start.contains(Storage.DELIMITER)) {
            throw createEventException("the start date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }
        if (end.contains(Storage.DELIMITER)) {
            throw createEventException("the end date or time cannot contain \"" + Storage.DELIMITER + "\"");
        }

        addTask(new Event(description, start, end));
    }

    /**
     * Creates a detailed event error with the required command format.
     *
     * @param reason Explanation of why the event command is invalid.
     * @return Exception containing the error and correction instructions.
     */
    private static AlfredException createEventException(String reason) {
        return new AlfredException("Unable to add the event: " + reason + ".\n"
                + "Use this format: event <description> /from <start> /to <end>\n"
                + "Example: event project meeting /from Mon 2pm /to 4pm");
    }

    /**
     * Adds a task to the task list and displays the updated task count.
     *
     * @param task Task to add.
     */
    private static void addTask(Task task) throws IOException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

}
