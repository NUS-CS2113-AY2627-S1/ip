package neo;

import java.util.ArrayList;
import java.util.Scanner;
import neo.task.Task;

/**
 * Handles all user interactions for the Neo application.
 * Responsible for reading user input and displaying formatted messages,
 * errors, and task lists to the console.
 */
public class Ui {
    private static final String BANNER = " _   _\n"
            + "| \\ | | ___  ___\n"
            + "|  \\| |/ _ \\/ _ \\\n"
            + "| |\\  |  __/ (_) |\n"
            + "|_| \\_|\\___|\\___/\n";
    private static final String SEPARATOR = "    " + "_".repeat(60);

    private Scanner scanner;

    /**
     * Constructs a new Ui instance.
     * Initializes the scanner to read input from the standard system console.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the initial greeting and Neo logo when the application starts.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.print(BANNER);
        System.out.println("     Hello! I'm Neo.");
        System.out.println("     What can I do for you?");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Displays the farewell message when the user exits the application.
     */
    public void showFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints a horizontal separator line for visual formatting.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays a formatted error message to the user.
     *
     * @param message The specific error detail to display.
     */
    public void showError(String message) {
        System.out.println(SEPARATOR);
        System.out.println("    Error: " + message);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Displays an error message when the storage file fails to load on startup.
     */
    public void showLoadingError() {
        System.out.println(SEPARATOR);
        System.out.println("    Error: Could not load tasks from file. "
                + "Starting with an empty list.");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Reads the next line of input provided by the user.
     *
     * @return The full string command entered by the user, or {@code null} when input ends.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Displays the full list of current tasks with their respective numerical indices.
     *
     * @param tasks An {@code ArrayList<Task>} containing the current tasks to display.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Prints a general message to the user, wrapped in visual separators.
     *
     * @param message The content to be displayed to the console.
     */
    public void showMessage(String message) {
        System.out.println(SEPARATOR);
        System.out.println("     " + message);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Prints the list of tasks matching the user's search keyword.
     *
     * @param matchingTasks The list of tasks found.
     */
    public void showFoundTasks(ArrayList<Task> matchingTasks) {
        System.out.println(SEPARATOR);
        if (matchingTasks.isEmpty()) {
            System.out.println("     No matching tasks found.");
            System.out.println();
            return;
        }

        System.out.println("     Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + matchingTasks.get(i).toString());
        }
        System.out.println(SEPARATOR);
        System.out.println();
    }
}
