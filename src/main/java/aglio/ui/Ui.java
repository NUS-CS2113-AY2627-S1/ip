package aglio.ui;

import aglio.task.Task;
import aglio.task.TaskList;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all interactions with the user: reading commands from standard
 * input and printing responses to standard output.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = "    _         _ _       \n"
            + "   / \\   __ _| (_) ___  \n"
            + "  / _ \\ / _` | | |/ _ \\ \n"
            + " / ___ \\ (_| | | | (_) |\n"
            + "/_/   \\_\\__, |_|_|\\___/ \n"
            + "        |___/           \n";

    private final Scanner scanner;

    /** Creates a Ui that reads from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Prints the welcome banner and greeting, wrapped in dividers. */
    public void showGreeting() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello, I am Aglio.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Prints the goodbye message.
     * The surrounding dividers are provided by the main command loop.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a warning that the save file could not be loaded.
     *
     * @param message the error detail from the exception
     */
    public void showLoadingError(String message) {
        System.out.println(" Warning: " + message);
        System.out.println(" Starting with an empty task list.");
    }

    /** Prints a horizontal divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints all tasks in the list with 1-based numbering.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.getTask(i));
        }
    }

    /**
     * Prints a confirmation that a task has been marked as done.
     *
     * @param task the task that was marked
     */
    public void showMarkDone(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Prints a confirmation that a task has been marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showMarkNotDone(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Prints a confirmation that a task has been deleted.
     *
     * @param task      the task that was removed
     * @param taskCount the number of tasks remaining after deletion
     */
    public void showDelete(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints a confirmation that a task has been added.
     *
     * @param task      the task that was added
     * @param taskCount the total number of tasks after adding
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints the tasks that match a find query, with 1-based numbering.
     *
     * @param matches the list of tasks that matched the keyword
     */
    public void showFindResults(ArrayList<Task> matches) {
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matches.get(i));
        }
    }

    /**
     * Prints an error message from the application.
     *
     * @param message the error detail
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Prints a warning that tasks could not be saved to disk.
     *
     * @param message the error detail from the IOException
     */
    public void showSaveError(String message) {
        System.out.println(" Warning: Could not save tasks: " + message);
    }

    /**
     * Returns true if there is another line of input available.
     *
     * @return true if the user has entered another command
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next line of input from the user.
     *
     * @return the raw command string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Closes the underlying input scanner. */
    public void close() {
        scanner.close();
    }
}
