package alfred;

import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.Task;
import alfred.task.Todo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Alfred command-line chatbot.
 */
public class Alfred {

    private static final Path DATA_FILE = Path.of("data", "alfred.txt");
    private static final String DIVIDER = "____________________________________________________________";
    private static final String DELIMITER = " | ";
    private static final String BANNER
            = "         █████╗ ██╗     ███████╗██████╗ ███████╗██████╗\n"
            + "        ██╔══██╗██║     ██╔════╝██╔══██╗██╔════╝██╔══██╗\n"
            + "        ███████║██║     █████╗  ██████╔╝█████╗  ██║  ██║\n"
            + "        ██╔══██║██║     ██╔══╝  ██╔══██╗██╔══╝  ██║  ██║\n"
            + "        ██║  ██║███████╗██║     ██║  ██║███████╗██████╔╝\n"
            + "        ╚═╝  ╚═╝╚══════╝╚═╝     ╚═╝  ╚═╝╚══════╝╚═════╝ ";
    private static final String BAT_LOGO
            = "              *         *      *         *               \n"
            + "          ***          **********          ***           \n"
            + "       *****           **********           *****        \n"
            + "     *******           **********           *******      \n"
            + "   **********         ************         **********    \n"
            + "  ****************************************************   \n"
            + " ******************************************************  \n"
            + "******************************************************** \n"
            + "******************************************************** \n"
            + "******************************************************** \n"
            + " ******************************************************  \n"
            + "  ********      ************************      ********   \n"
            + "   *******       *     *********      *       *******    \n"
            + "     ******             *******              ******      \n"
            + "       *****             *****              *****        \n"
            + "          ***             ***              ***           \n"
            + "            **             *              **             \n";
    private static final ArrayList<Task> tasks = new ArrayList<>(); //initialise task list array

    /**
     * Starts Alfred and processes commands until the user exits.
     *
     * @param args Command-line arguments. Alfred does not use them.
     */
    public static void main(String[] args) {
        try {
            loadTasks();
        } catch (IOException exception) {
            System.out.println("I could not load your saved tasks, Master Wayne.");
            System.out.println("Starting with an empty task list.");
        }
        printWelcomeMessage();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                System.out.println(DIVIDER);

                if (command.equals("bye")) {
                    printGoodbyeMessage();
                    break;
                }
                try {
                    processCommand(command);
                } catch (AlfredException exception) {
                    System.out.println(exception.getMessage());
                } catch (IOException exception) {
                    System.out.println("I could not save that, Master Wayne.");
                }

                System.out.println(DIVIDER);
            }
        }
    }

    /**
     * Displays Alfred's banner and greeting.
     */
    private static void printWelcomeMessage() {
        System.out.println(DIVIDER + "\n");
        System.out.println(BANNER);
        System.out.println(DIVIDER + "\n");
        System.out.println("Welcome home Master Wayne.\nWhat can I do for you?");
    }

    /**
     * Displays Alfred's farewell message and logo.
     */
    private static void printGoodbyeMessage() {
        System.out.println("Good luck Master Wayne.\n");
        System.out.println(BAT_LOGO);
        System.out.println(DIVIDER);
    }

    /**
     * Routes a command to the operation that handles it.
     *
     * @param command Full command entered by the user.
     */
    private static void processCommand(String command) throws AlfredException, IOException {
        if (command.equals("list")) {
            printTaskList();
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
     * Displays all tasks in their insertion order.
     */
    private static void printTaskList() {
        if (tasks.isEmpty()) {
            System.out.println("You have no tasks, Master Wayne.");
            return;
        }
        System.out.println("These are your tasks Master Wayne:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Deletes the task identified by a delete command.
     *
     * @param command Full command entered by the user.
     */
    private static void deleteTask(String command) throws AlfredException {
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
            Task deletedTask = tasks.remove(taskIndex);

            System.out.println("Will do Master Wayne. I've removed this task:");
            System.out.println("  " + deletedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Marks or unmarks the task identified by a status command.
     *
     * @param command Full command entered by the user.
     * @param isMarkAsDone Whether the task should be marked as done.
     */
    private static void updateTaskStatus(String command, boolean isMarkAsDone)
            throws AlfredException, IOException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length < 2) {
            if (isMarkAsDone) {
                throw new AlfredException("Please provide a task number to mark as done.");
            }
            throw new AlfredException("Please provide a task number to mark as not done.");
        }
        if (parts.length > 2) {
            if (isMarkAsDone) {
                throw new AlfredException("Please provide only one task number to mark as done.");
            }
            throw new AlfredException("Please provide only one task number to mark as not done.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                if (taskNumber < 1 || taskNumber > tasks.size()) {
                    throw new AlfredException("Invalid task number.");
                }

                Task task = tasks.get(taskNumber - 1);
                Task task = tasks.get(taskNumber - 1);
                if (isMarkAsDone) {
                    task.markAsDone();
                    System.out.println("Excellent work Master Wayne! I've marked this task as done:");
                } else {
                    task.markAsNotDone();
                    System.out.println("Alright Master Wayne, I have unmarked this task as requested:");
                }
                saveTasks();
                System.out.println("  " + task);
            }catch (NumberFormatException exception) {
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
        if (description.contains(DELIMITER)) {
            throw new AlfredException("The description of a todo cannot contain \"" + DELIMITER + "\", "
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
        if (description.contains(DELIMITER)) {
            throw createDeadlineException("the task description cannot contain \"" + DELIMITER + "\"");
        }
        if (deadline.contains(DELIMITER)) {
            throw createDeadlineException("the deadline date or time cannot contain \"" + DELIMITER + "\"");
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
        if (description.contains(DELIMITER)) {
            throw createEventException("the task description cannot contain \"" + DELIMITER + "\"");
        }
        if (start.contains(DELIMITER)) {
            throw createEventException("the start date or time cannot contain \"" + DELIMITER + "\"");
        }
        if (end.contains(DELIMITER)) {
            throw createEventException("the end date or time cannot contain \"" + DELIMITER + "\"");
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
    private static void addTask(Task task) throws AlfredException, IOException {
        tasks.add(task);
        saveTasks();
        System.out.println("Understood Master Wayne, I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Writes the current task list to the data file.
     */
    private static void saveTasks() throws IOException {
        Files.createDirectories(DATA_FILE.getParent());

        List<String> taskDataLines = new ArrayList<>();
        for (Task task : tasks) {
            taskDataLines.add(task.toDataString());
        }
        Files.write(DATA_FILE, taskDataLines, StandardCharsets.UTF_8);
    }

    /**
     * Loads tasks from the data file when it exists. Lines that cannot be
     * parsed are reported and skipped so that the rest of the file still loads.
     */
    private static void loadTasks() throws IOException {
        if (Files.notExists(DATA_FILE)) {
            return;
        }

        List<String> taskDataLines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
        for (String taskLine : taskDataLines) {
            try {
                tasks.add(createTaskFromData(taskLine));
            } catch (AlfredException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    /**
     * Recreates a task from one line of stored task data.
     *
     * @param taskData Pipe-delimited task data.
     * @return Task represented by the stored data.
     * @throws AlfredException If the line is missing fields or has an
     * unrecognized task type.
     */
    private static Task createTaskFromData(String taskData) throws AlfredException {
        String[] fields = taskData.split(" \\| ", -1);
        if (fields.length < 2) {
            throw new AlfredException("I could not make sense of this line, Master Wayne: "
                    + "it does not match the expected \"type | status | details\" format.\n"
                    + "Offending line: \"" + taskData + "\"");
        }

        String taskType = fields[0];
        boolean isDone = fields[1].equals("1");
        Task task;

        if (taskType.equals("T")) {
            if (fields.length < 3) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "a todo is missing its description.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Todo(fields[2]);
        } else if (taskType.equals("D")) {
            if (fields.length < 4) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "a deadline is missing its description or /by date.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Deadline(fields[2], fields[3]);
        } else if (taskType.equals("E")) {
            if (fields.length < 5) {
                throw new AlfredException("I could not make sense of this line, Master Wayne: "
                        + "an event is missing its description, /from, or /to value.\n"
                        + "Offending line: \"" + taskData + "\"");
            }
            task = new Event(fields[2], fields[3], fields[4]);
        } else {
            throw new AlfredException("I could not make sense of this line, Master Wayne: "
                    + "unknown task type \"" + taskType + "\".\n"
                    + "Offending line: \"" + taskData + "\"");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
