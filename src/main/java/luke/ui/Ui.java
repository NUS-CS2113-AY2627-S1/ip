package luke.ui;

import java.util.Scanner;

import luke.task.Task;

/**
 * Handles the display of messages to the user.
 */
public class Ui {
    private static final int HORIZONTAL_LINE_LENGTH = 60;
    private static final String HORIZONTAL_LINE = createHorizontalLine();
    private final Scanner input;

    /**
     * Creates a user interface that reads from the standard input stream.
     */
    public Ui() {
        input = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return User command.
     */
    public String readCommand() {
        return input.nextLine();
    }

    /**
     * Returns Luke's text logo.
     *
     * @return Multi-line text logo.
     */
    private static String getLogo() {
        return " _          _        \n"
                + "| |   _   _| | _____ \n"
                + "| |  | | | | |/ / _ \\\n"
                + "| |__| |_| |   <  __/\n"
                + "|_____\\__,_|_|\\_\\___|\n";
    }

    /**
     * Shows the welcome message and logo.
     */
    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Hello! I'm Luke\n" + getLogo());
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Shows the goodbye message.
     */
    public void showGoodbye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Shows all tasks currently stored in the task list.
     *
     * @param tasks Tasks to display.
     * @param taskCount Number of stored tasks.
     */
    public void showTaskList(Task[] tasks, int taskCount) {
        if (taskCount == 0) {
            System.out.println("There are no tasks in your list.");
            return;
        }

        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }

    /**
     * Shows tasks that match a search keyword.
     *
     * @param matchingTasks Tasks that match the keyword.
     */
    public void showFindResults(Task[] matchingTasks) {
        if (matchingTasks.length == 0) {
            System.out.println("There are no matching tasks in your list.");
            return;
        }

        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < matchingTasks.length; i++) {
            System.out.println((i + 1) + ". " + matchingTasks[i]);
        }
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task      Added task.
     * @param taskCount Updated number of stored tasks.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task Marked task.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task Unmarked task.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Shows the task that was deleted and the remaining task count.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);

        if (taskCount == 1) {
            System.out.println("Now you have 1 task in the list.");
        } else {
            System.out.println("Now you have " + taskCount + " tasks in the list.");
        }
    }

    /**
     * Shows a horizontal line separating output sections.
     */
    public void showHorizontalLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    private static String createHorizontalLine() {
        return "_".repeat(HORIZONTAL_LINE_LENGTH);
    }

    /**
     * Shows an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println("[WARNING] " + message);
    }

}
