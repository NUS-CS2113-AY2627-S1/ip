package dook;

import java.util.Scanner;

/**
 * Handles user input and displays messages and task information to the user.
 */
public class Ui {
    private static final String BANNER = "\t ____   ___   ___  _  __\n"
                                        + "\t|  _ \\ / _ \\ / _ \\| |/ /\n"
                                        + "\t| | | | | | | | | | ' / \n"
                                        + "\t| |_| | |_| | |_| | . \\ \n"
                                        + "\t|____/ \\___/ \\___/|_|\\_\\\n";
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a Ui instance for reading input from the standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the line entered by the user
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Displays the greeting message when Dook starts.
     */
    public void showGreetingMessage() {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println(BANNER);
        System.out.println("\tHello! I'm Dook.");
        System.out.println("\tWhat can I do for you?");
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays the exit message when the user quits Dook.
     */
    public void showExitMessage() {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tBye. Hope to see you again soon!");
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays all tasks currently in the task list.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tHere are the tasks in your list:");
        int taskNumber = 1;
        for (Task task : tasks) {
            // Display task numbers starting from 1 for the user.
            System.out.println("\t" + taskNumber + "." + task);
            taskNumber++;
        }
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays a confirmation message after a task is marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showMarkConfirmation(Task task) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tNice! I've marked this task as done:");
        System.out.println("\t  " + task);
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays a confirmation message after a task is marked as not done.
     *
     * @param task the task that was marked as not done
     */
    public void showUnmarkConfirmation(Task task) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tOK, I've marked this task as not done yet:");
        System.out.println("\t  " + task);
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays a confirmation message after a task is added.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks currently in the list
     */
    public void showAddTaskConfirmation(Task task, int taskCount) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + taskCount + " tasks in the list.");
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays a confirmation message after a task is deleted.
     *
     * @param deletedTask the task that was deleted
     * @param taskCount the number of tasks remaining in the list
     */
    public void showDeleteConfirmation(Task deletedTask, int taskCount) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tNoted. I've removed this task:");
        System.out.println("\t  " + deletedTask);
        System.out.println("\tNow you have " + taskCount + " tasks in the list.");
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays the tasks that match a search keyword.
     *
     * @param matchingTasks the task list containing the matching tasks
     */
    public void showMatchingTaskList(TaskList matchingTasks) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tHere are the matching tasks in your list:");
        int taskNumber = 1;
        for (Task task : matchingTasks) {
            // Display matching task numbers starting from 1 for the user.
            System.out.println("\t" + taskNumber + "." + task);
            taskNumber++;
        }
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }

    /**
     * Displays an error message to the user.
     *
     * @param e the exception containing the error message
     */
    public void showErrorMessage(DookException e) {
        System.out.println("\t" + LINE_SEPARATOR);
        System.out.println("\tOOPS!!! " + e.getMessage());
        System.out.println("\t" + LINE_SEPARATOR + "\n");
    }
}
