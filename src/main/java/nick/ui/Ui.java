package nick.ui;

import java.util.ArrayList;
import java.util.Scanner;

import nick.task.Task;
import nick.task.TaskList;

/**
 * Handles all interaction with the user: reading commands and printing messages.
 * Message methods print their content only; callers use {@link #showLine()} to
 * frame a block of output with divider lines.
 */
public class Ui {
    private static final String LINE = "    ____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next command line entered by the user.
     *
     * @return The line of input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Returns whether there is more input to read.
     *
     * @return True if another line of input is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Prints a divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows the welcome message greeting the user, framed by divider lines.
     */
    public void showWelcome() {
        showLine();
        System.out.println("     Hello! I'm Nick.");
        System.out.println("     What can I do for you?");
        showLine();
    }

    /**
     * Shows the farewell message shown when the user exits.
     */
    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    /**
     * Shows an error message to the user.
     *
     * @param message The error description.
     */
    public void showError(String message) {
        System.out.println("     OOPS!!! " + message);
    }

    /**
     * Shows the message confirming a task was added.
     *
     * @param task The task that was added.
     * @param count The number of tasks now in the list.
     */
    public void showAdded(Task task, int count) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task.toDisplayString());
        System.out.println("     Now you have " + count + " tasks in the list.");
    }

    /**
     * Shows the message confirming a task was removed.
     *
     * @param task The task that was removed.
     * @param count The number of tasks now in the list.
     */
    public void showRemoved(Task task, int count) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task.toDisplayString());
        System.out.println("     Now you have " + count + " tasks in the list.");
    }

    /**
     * Shows the message confirming a task was marked as done.
     *
     * @param task The task that was marked.
     */
    public void showMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + task.toDisplayString());
    }

    /**
     * Shows the message confirming a task was marked as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showUnmarked(Task task) {
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + task.toDisplayString());
    }

    /**
     * Shows the full list of tasks, numbered.
     *
     * @param tasks The tasks to display.
     */
    public void showList(TaskList tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i).toDisplayString());
        }
    }

    /**
     * Shows the tasks that matched a find search, numbered.
     *
     * @param matches The matching tasks to display.
     */
    public void showFound(ArrayList<Task> matches) {
        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matches.get(i).toDisplayString());
        }
    }
}
