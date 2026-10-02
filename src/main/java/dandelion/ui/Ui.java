package dandelion.ui;

import dandelion.task.Task;
import dandelion.task.TaskList;

import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and session-level messages for Dandelion.
 */
public class Ui implements AutoCloseable {
    /** Text displayed when the application starts. */
    private static final String BANNER = """
                 .
              \\  |  /
            ――  (✻)  ――
                 |
            D A N D E L I O N

            """;

    /** Separator used around matching tasks. */
    private static final String FIND_SEPARATOR = "    " + "_".repeat(60);

    /** Reads commands entered through the console. */
    private final Scanner scanner;

    /**
     * Creates a console user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the application welcome message.
     */
    public void showWelcomeMessage() {
        System.out.print(BANNER);
        System.out.println("  Welcome, User.");
        System.out.println("  Type anything...");
        System.out.println();
    }

    /**
     * Reads and trims the next command from the console.
     *
     * @return Trimmed command, or {@code null} when the input ends.
     */
    public String readCommand() {
        System.out.print("  you  › ");
        System.out.flush();

        if (!scanner.hasNextLine()) {
            System.out.println();
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Displays the application goodbye message.
     */
    public void showGoodbyeMessage() {
        System.out.println("  bot  › Bye, User.");
    }

    /**
     * Displays all tasks in their insertion order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            showNoTasksMessage();
            return;
        }

        int taskNumber = 1;
        for (Task task : tasks) {
            String linePrefix = taskNumber == 1 ? "  bot  › " : "         ";
            System.out.println(linePrefix + taskNumber + "." + task);
            taskNumber++;
        }
    }

    /**
     * Displays that no tasks are available.
     */
    public void showNoTasksMessage() {
        showBotMessage("No tasks added yet.");
    }

    /**
     * Displays the number of tasks currently stored.
     *
     * @param taskCount Number of tasks.
     */
    public void showTaskCount(int taskCount) {
        System.out.println("         Number of tasks: " + taskCount);
    }

    /**
     * Displays an invalid task-number message.
     *
     * @param taskCount Largest valid task number.
     */
    public void showInvalidTaskNumber(int taskCount) {
        showBotMessage("Please enter a task number from 1 to " + taskCount + ".");
    }

    /**
     * Displays a missing task-number message.
     */
    public void showMissingTaskNumber() {
        showBotMessage("Please enter a task number after the command.");
    }

    /**
     * Displays a task after updating its completion status.
     *
     * @param task Task whose status was updated.
     * @param isDone Whether the task is now marked as done.
     */
    public void showTaskStatusUpdated(Task task, boolean isDone) {
        String message = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        showBotMessage(message);
        System.out.println("           " + task);
    }

    /**
     * Displays a task-creation confirmation.
     *
     * @param task Task that was added.
     */
    public void showTaskAdded(Task task) {
        showBotMessage("added: " + task);
    }

    /**
     * Displays a task-deletion confirmation.
     *
     * @param task Task that was deleted.
     */
    public void showTaskDeleted(Task task) {
        showBotMessage("deleted: " + task);
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param matchingTasks Tasks to display.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            showBotMessage("No matching tasks found.");
            return;
        }

        System.out.println(FIND_SEPARATOR);
        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
        }
        System.out.println(FIND_SEPARATOR);
    }

    /**
     * Displays a missing-search-keyword message.
     */
    public void showMissingSearchKeyword() {
        showBotMessage("Please enter a keyword after the command.");
    }

    /**
     * Displays an error message returned by command processing.
     *
     * @param message Error message.
     */
    public void showError(String message) {
        showBotMessage(message);
    }

    /**
     * Displays an unknown-command message.
     */
    public void showUnknownCommand() {
        showBotMessage("Unknown command.");
    }

    /**
     * Displays the loading status.
     */
    public void showLoading() {
        System.out.println("Loading...");
    }

    /**
     * Displays the number of tasks loaded from storage.
     *
     * @param taskCount Number of loaded tasks.
     */
    public void showLoadedTaskCount(int taskCount) {
        System.out.println(taskCount + " task(s) successfully loaded.");
    }

    /**
     * Displays that no saved task data exists.
     */
    public void showNoSaveData() {
        System.out.println("No save data exists.");
    }

    /**
     * Displays a saved-task loading error.
     *
     * @param message Error message.
     */
    public void showLoadingError(String message) {
        showBotMessage("Unable to load saved tasks: " + message);
    }

    /**
     * Displays a task-saving error.
     *
     * @param message Error message.
     */
    public void showSavingError(String message) {
        showBotMessage("Unable to save tasks: " + message);
    }

    /**
     * Displays spacing between command responses.
     */
    public void showCommandSeparator() {
        System.out.println();
    }

    /**
     * Closes the console input source.
     */
    @Override
    public void close() {
        scanner.close();
    }

    /**
     * Displays a bot message with the standard output prefix.
     *
     * @param message Message to display.
     */
    private void showBotMessage(String message) {
        System.out.println("  bot  › " + message);
    }
}
