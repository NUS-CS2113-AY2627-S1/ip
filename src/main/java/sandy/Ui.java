package sandy;

import java.util.List;
import java.util.Scanner;

import sandy.task.Task;

/**
 * Handles console input and output for Sandy.
 */
public class Ui implements AutoCloseable {
    private static final String SEPARATOR = "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a user interface connected to the process console.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command from the console.
     *
     * @return The next command, or {@code null} when input ends.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Displays the application's welcome message.
     */
    public void showWelcome() {
        String banner = " ____                  _       \n"
                + "/ ___|  __ _ _ __   __| |_   _ \n"
                + "\\___ \\ / _` | '_ \\ / _` | | | |\n"
                + " ___) | (_| | | | | (_| | |_| |\n"
                + "|____/ \\__,_|_| |_|\\__,_|\\__, |\n"
                + "                         |___/ \n";
        showLine();
        System.out.print(banner);
        System.out.println("Hello! I'm Sandy.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the separator line used between interactions.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a command error.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(" Oops! " + message);
    }

    /**
     * Displays a task loading error.
     *
     * @param message The reason loading failed.
     */
    public void showLoadingError(String message) {
        showLine();
        System.out.println(" Oops! I could not load saved tasks: " + message);
        showLine();
    }

    /**
     * Displays the current tasks.
     *
     * @param tasks The tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.asList().get(i));
        }
    }

    /**
     * Displays tasks matching a search keyword.
     *
     * @param matchingTasks The tasks that matched the keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked complete.
     *
     * @param task The completed task.
     */
    public void showMarkedTask(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was marked incomplete.
     *
     * @param task The incomplete task.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task The deleted task.
     * @param remainingTaskCount The number of tasks left.
     */
    public void showDeletedTask(Task task, int remainingTaskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + remainingTaskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task The added task.
     * @param taskCount The number of tasks now in the list.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays an error encountered while saving tasks.
     */
    public void showSaveError() {
        System.out.println(" Oops! I could not save your tasks.");
    }

    /**
     * Displays the exit message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Closes the console input scanner.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
