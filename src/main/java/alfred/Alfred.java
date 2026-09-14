package alfred;

import alfred.exception.AlfredException;
import alfred.task.Deadline;
import alfred.task.Event;
import alfred.task.Task;
import alfred.task.Todo;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Alfred command-line chatbot.
 */
public class Alfred {

    private static final String DIVIDER = "____________________________________________________________";
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
        printWelcomeMessage();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(DIVIDER);

            if (command.equals("bye")) {
                printGoodbyeMessage();
                break;
            }
            try {
                processCommand(command);
            } catch (AlfredException exception) {
                System.out.println(exception.getMessage());
            }

            System.out.println(DIVIDER);
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
    private static void processCommand(String command) throws AlfredException {
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
    private static void updateTaskStatus(String command, boolean isMarkAsDone) throws AlfredException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length != 2) {
            if (isMarkAsDone) {
                throw new AlfredException("Please provide a task number to mark as done.");
            }
            throw new AlfredException("Please provide a task number to mark as not done.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > tasks.size()) {
                throw new AlfredException("Invalid task number.");
            }

            Task task = tasks.get(taskNumber - 1);
            if (isMarkAsDone) {
                task.markAsDone();
                System.out.println("Excellent work Master Wayne! I've marked this task as done:");
            } else {
                task.markAsNotDone();
                System.out.println("Alright Master Wayne, I have unmarked this task as requested:");
            }
            System.out.println("  " + task);
        } catch (NumberFormatException exception) {
            throw new AlfredException("Please provide a valid task number.");
        }
    }

    /**
     * Parses and adds a todo command if it contains a description.
     *
     * @param command Full command entered by the user.
     */
    private static void addTodo(String command) throws AlfredException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new AlfredException("The description of a todo cannot be empty.");
        }

        addTask(new Todo(description));
    }

    /**
     * Parses and adds a deadline command containing a description and
     * {@code /by} value.
     *
     * @param command Full command entered by the user.
     */
    private static void addDeadline(String command) throws AlfredException {
        String arguments = command.substring("deadline".length()).trim();
        int byPosition = arguments.indexOf("/by");
        if (byPosition < 0) {
            throw createDeadlineException("the /by marker is missing");
        }

        String description = arguments.substring(0, byPosition).trim();
        String deadline = arguments.substring(byPosition + "/by".length()).trim();
        if (description.isEmpty()) {
            throw createDeadlineException("the task description is missing");
        }
        if (deadline.isEmpty()) {
            throw createDeadlineException("the deadline date or time is missing");
        }

        addTask(new Deadline(description, deadline));
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
     * value.
     *
     * @param command Full command entered by the user.
     */
    private static void addEvent(String command) throws AlfredException {
        String arguments = command.substring("event".length()).trim();
        int fromPosition = arguments.indexOf("/from");
        if (fromPosition < 0) {
            throw createEventException("the /from marker is missing");
        }

        int toPosition = arguments.indexOf("/to", fromPosition + "/from".length());
        if (toPosition < 0) {
            throw createEventException("the /to marker is missing");
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
    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println("Understood Master Wayne, I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }
}
