package gigabot;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all user interface interactions, including reading inputs and displaying outputs.
 */
public class Ui {
    private Scanner in;
    private static final String BANNER = "   _____ _             ____        _    \n"
            + "  / ____(_)           |  _ \\      | |   \n"
            + " | |  __ _  __ _  __ _| |_) | ___ | |_  \n"
            + " | | |_ | |/ _` |/ _` |  _ < / _ \\| __| \n"
            + " | |__| | | (_| | (_| | |_) | (_) | |_  \n"
            + "  \\_____|_|\\__, |\\__,_|____/ \\___/ \\__| \n"
            + "            __/ |                       \n"
            + "           |___/                        \n";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    /**
     * Initializes the Ui and the input scanner.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Prints the welcome banner and greeting.
     */
    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm GigaBot!");
        System.out.println("What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The trimmed string command.
     */
    public String readCommand() {
        return in.nextLine().trim();
    }

    /**
     * Prints a standard horizontal divider line.
     */
    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints an error message to the user.
     *
     * @param message The error description.
     */
    public void showError(String message) {
        System.out.println("[GigaBot] >> " + message);
    }

    /**
     * Prints a critical error if the hard disk save file fails to load.
     */
    public void showLoadingError() {
        System.out.println("[GigaBot] >> SYSTEM ERROR: Could not load existing data. Starting fresh.");
    }

    /**
     * Prints a standard system message.
     *
     * @param message The text to display.
     */
    public void showMessage(String message) {
        System.out.println("[GigaBot] >> " + message);
    }

    /**
     * Prints the confirmation sequence when a task is successfully added.
     *
     * @param task The newly added task.
     * @param currentCount The updated total number of tasks.
     */
    public void showTaskAdded(Task task, int currentCount) {
        System.out.println("[GigaBot] >> Got it. I've added this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
    }

    /**
     * Prints the confirmation sequence when a task is successfully deleted.
     *
     * @param task The deleted task.
     * @param currentCount The updated total number of tasks.
     */
    public void showTaskDeleted(Task task, int currentCount) {
        System.out.println("[GigaBot] >> Noted. I've removed this task:");
        System.out.println("[GigaBot] >>   " + task.toString());
        System.out.println("[GigaBot] >> Now you have " + currentCount + " tasks in the list.");
    }

    /**
     * Prints a list of tasks that match the user's search keyword.
     *
     * @param matchingTasks An ArrayList containing the matching Task objects.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        System.out.println("[GigaBot] >> Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            System.out.println("[GigaBot] >> No matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println("[GigaBot] >> " + (i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
    }
}