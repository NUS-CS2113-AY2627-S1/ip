import java.util.ArrayList;
import java.util.Scanner;
import task.Task;

/**
 * Handles all interactions with the user.
 * Responsible for reading input and displaying output.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER =
            " ___           _ \n"
                    + "|_ _|_ __ _ __(_)\n"
                    + " | || '__| '__| |\n"
                    + " | || |  | |  | |\n"
                    + "|___|_|  |_|  |_|\n";
    private final Scanner scanner;

    /**
     * Constructs a Ui object with a Scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of user input.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message and banner.
     */
    public void printWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hi! I'm Irri.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays the goodbye message.
     */
    public void printGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. See you!");
        System.out.println(LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message.
     */
    public void printError(String message) {
        System.out.println(LINE);
        System.out.println("Oh no! " + message);
        System.out.println(LINE);
    }

    /**
     * Displays the full task list.
     *
     * @param tasks The list of tasks to display.
     */
    public void printTaskList(ArrayList<Task> tasks) {
        System.out.println(LINE);
        if (tasks.isEmpty()) {
            System.out.println("No task added yet.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task The added task.
     * @param taskCount The total number of tasks.
     */
    public void printTaskAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task The removed task.
     * @param taskCount The remaining number of tasks.
     */
    public void printTaskDeleted(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println(" OK. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task The marked task.
     */
    public void printTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        System.out.println(LINE);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task The unmarked task.
     */
    public void printTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        System.out.println(LINE);
    }

    /**
     * Displays a warning that tasks could not be loaded from the file.
     */
    public void printLoadingError() {
        System.out.println(LINE);
        System.out.println(" Warning: Unable to load tasks from file. Starting with an empty list.");
        System.out.println(LINE);
    }

    /**
     * Displays tasks matching a search keyword.
     *
     * @param tasks The list of matching tasks.
     */
    public void printMatchingTasks(ArrayList<Task> tasks) {
        System.out.println(LINE);
        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Closes the scanner to release system resources.
     */
    public void close() {
        scanner.close();
    }
}
