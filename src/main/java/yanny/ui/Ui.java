package yanny.ui;

import java.util.List;
import java.util.Scanner;

import yanny.task.Task;

/**
 * Reads terminal commands and displays Yanny's messages.
 */
public class Ui {
    private static final String BORDER = "+------------------------------------------+";
    private static final String COMMAND_REJECTED = "| YANNY_OS :: COMMAND REJECTED";
    private static final String ERROR_PREFIX = "| ERROR > ";
    private static final String BANNER = "____    ____  ___      .__   __. .__   __. ____    ____\n"
            + "\\   \\  /   / /   \\     |  \\ |  | |  \\ |  | \\   \\  /   /\n"
            + " \\   \\/   / /  ^  \\    |   \\|  | |   \\|  |  \\   \\/   /\n"
            + "  \\_    _/ /  /_\\  \\   |  . `  | |  . `  |   \\_    _/\n"
            + "    |  |  /  _____  \\  |  |\\   | |  |\\   |     |  |\n"
            + "    |__| /__/     \\__\\ |__| \\__| |__| \\__|     |__|";

    private final Scanner scanner;

    /** Creates a terminal interface using standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command can be read from standard input.
     *
     * @return true if another line is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command from standard input.
     *
     * @return the command line entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the startup message for Yanny. */
    public void showStartupScreen() {
        System.out.println(BORDER);
        System.out.println("| YANNY_OS :: BOOT SEQUENCE COMPLETE");
        System.out.println(BANNER);
        System.out.println();
        System.out.println("| GREETINGS I'M YANNY.");
        System.out.println("| SYSTEM READY. AWAITING COMMAND...");
        System.out.println(BORDER);
    }

    /** Displays a border between command responses. */
    public void showBorder() {
        System.out.println(BORDER);
    }

    /**
     * Displays a formatted error for invalid user input.
     *
     * @param message the actionable error message.
     */
    public void showCommandError(String message) {
        System.out.println(COMMAND_REJECTED);
        System.out.println(ERROR_PREFIX + message);
    }

    /**
     * Displays the current tasks and their completion status.
     *
     * @param tasks the tasks to display in list order.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("| YANNY_OS :: TASK LIST");
        if (tasks.isEmpty()) {
            System.out.println("| OUTPUT > NO TASKS STORED");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("| " + (i + 1) + ". " + tasks.get(i));
            }
        }
    }

    /**
     * Displays matching tasks with their positions in the complete task list.
     *
     * @param tasks all stored tasks in list order.
     * @param matchingIndices zero-based positions of matching tasks.
     */
    public void showSearchResults(List<Task> tasks, List<Integer> matchingIndices) {
        System.out.println("| YANNY_OS :: SEARCH RESULTS");
        if (matchingIndices.isEmpty()) {
            System.out.println("| OUTPUT > NO MATCHING TASKS FOUND");
        } else {
            System.out.println("| OUTPUT > HERE ARE THE MATCHING TASKS IN YOUR LIST:");
            for (int index : matchingIndices) {
                System.out.println("| " + (index + 1) + ". " + tasks.get(index));
            }
        }
    }

    /**
     * Displays a task after it has been marked done.
     *
     * @param task the marked task.
     */
    public void showMarkedTask(Task task) {
        System.out.println("| YANNY_OS :: MARKED TASK SUCCESSFULLY");
        System.out.println("| OUTPUT > [X] " + task.getDescription());
    }

    /**
     * Displays a task after it has been marked not done.
     *
     * @param task the unmarked task.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("| YANNY_OS :: UNMARKED TASK SUCCESFULLY");
        System.out.println("| OUTPUT > [ ] " + task.getDescription());
    }

    /**
     * Displays a task after it has been deleted.
     *
     * @param task the deleted task.
     */
    public void showDeletedTask(Task task) {
        System.out.println("| YANNY_OS :: DELETED TASK SUCCESSFULLY");
        System.out.println("| OUTPUT > " + task);
    }

    /**
     * Displays a received task creation command before it is parsed.
     *
     * @param command the original command text.
     */
    public void showCommandReceived(String command) {
        System.out.println("| YANNY_OS :: COMMAND RECEIVED");
        String inputDisplay = command.isBlank() ? "" : " " + command;
        System.out.println("| INPUT  >" + inputDisplay);
    }

    /**
     * Displays the task and count after an addition succeeds.
     *
     * @param task the added task.
     * @param taskCount the current number of tasks.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("| OUTPUT > ADDED: " + task);
        System.out.println("| OUTPUT > CURRENT TASK COUNT: " + taskCount);
    }

    /** Displays the shutdown message for Yanny. */
    public void showShutdownMessage() {
        System.out.println("| YANNY_OS :: SHUTDOWN INITIATED");
        System.out.println("| OUTPUT > Bye. Hope to see you again!");
        System.out.println(BORDER);
    }
}
