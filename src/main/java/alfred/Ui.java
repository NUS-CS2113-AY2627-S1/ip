package alfred;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import alfred.task.Task;

/**
 * Handles console input and output for Alfred.
 */
public class Ui implements AutoCloseable {

    private static final String DIVIDER = "____________________________________________________________";
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT
            = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
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

    private final Scanner scanner;

    /**
     * Creates a console user interface using standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return True if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from the user.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Alfred's banner and greeting.
     */
    public void showWelcomeMessage() {
        System.out.println(DIVIDER + "\n");
        System.out.println(BANNER);
        System.out.println(DIVIDER + "\n");
        System.out.println("Welcome home Master Wayne.\nWhat can I do for you?");
    }

    /**
     * Displays Alfred's farewell message and logo.
     */
    public void showGoodbyeMessage() {
        System.out.println("Good luck Master Wayne.\n");
        System.out.println(BAT_LOGO);
    }

    /**
     * Displays a divider between responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays a message to the user.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error encountered while loading saved tasks.
     */
    public void showLoadingError() {
        showMessage("I could not load your saved tasks, Master Wayne.");
        showMessage("Starting with an empty task list.");
    }

    /**
     * Displays an error encountered while saving tasks.
     */
    public void showSavingError() {
        showMessage("I could not save that, Master Wayne.");
    }

    /**
     * Displays all tasks in their insertion order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            showMessage("You have no tasks, Master Wayne.");
            return;
        }

        showMessage("These are your tasks Master Wayne:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays scheduled tasks that occur on the specified date.
     *
     * @param tasks Scheduled tasks to display.
     * @param date Date represented by the task list.
     */
    public void showSchedule(TaskList tasks, LocalDate date) {
        String formattedDate = date.format(DISPLAY_DATE_FORMAT);
        if (tasks.isEmpty()) {
            showMessage("There are no deadlines or events on " + formattedDate + ", Master Wayne.");
            return;
        }

        showMessage("These are your deadlines and events on " + formattedDate + ":");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks whose descriptions match a search.
     *
     * @param tasks Matching tasks to display.
     */
    public void showFindResults(TaskList tasks) {
        if (tasks.isEmpty()) {
            showMessage("There are no matching tasks in your list, Master Wayne.");
            return;
        }

        showMessage("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays a newly added task and the updated task count.
     *
     * @param task Added task.
     * @param taskCount Updated number of tasks.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Understood Master Wayne, I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a deleted task and the updated task count.
     *
     * @param task Deleted task.
     * @param taskCount Updated number of tasks.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Will do Master Wayne. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays the heading for a task status change.
     *
     * @param isDone Whether the task was marked as done.
     */
    public void showTaskStatusChanged(boolean isDone) {
        if (isDone) {
            showMessage("Excellent work Master Wayne! I've marked this task as done:");
        } else {
            showMessage("Alright Master Wayne, I have unmarked this task as requested:");
        }
    }

    /**
     * Displays a task with indentation.
     *
     * @param task Task to display.
     */
    public void showTask(Task task) {
        showMessage("  " + task);
    }

    @Override
    public void close() {
        scanner.close();
    }
}
