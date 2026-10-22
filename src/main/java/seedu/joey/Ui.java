package seedu.joey;

import java.util.Scanner;
import seedu.joey.task.Task;
/**
 * Handles all interaction with the user: reading input and printing output
 */
public class Ui {
    private static final String LINE = "----------------------------------------";
    private static final String BANNER = "     _  ___  _______   __\n"
            + "    | |/ _ \\| ____\\ \\ / /\n"
            + " _  | | | | |  _|  \\ V / \n"
            + "| |_| | |_| | |___  | |  \n"
            + " \\___/ \\___/|_____| |_|  \n";
    
    private final Scanner in;

    /**
     * Creates a Ui that reads user input from standard input.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Reads the next command line entered by the user, trimmed of
     * surrounding whitespace.
     *
     * @return The user's command line.
     */
    public String readCommand() {
        return in.nextLine().trim();
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine(){
        System.out.println(LINE);
    }

    /**
     * Prints the welcome banner and greeting shown at startup.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Joey");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the farewell message shown when the user types "bye".
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Prints an error message inside the standard separator block.
     *
     * @param message The message to display.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints the entire task list, one task per line, numbered from 1.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks){
        System.out.println(LINE);
        System.out.println("To Do List:");
        for(int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + " . " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints the confirmation message shown after marking a task as done.
     *
     * @param task The task that was just marked.
     */
    public void showMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Tasks marked as done:");
        System.out.println(" " + task);
        System.out.println(LINE);
    }

    /**
     * Prints the confirmation message shown after marking a task as not done.
     *
     * @param task The task that was just unmarked.
     */
    public void showUnmarked(Task task){
        System.out.println(LINE);
        System.out.println("Tasks marked as undone:");
        System.out.println(" " + task);
        System.out.println(LINE);
    }

    /**
     * Prints the confirmation message shown after adding a task.
     *
     * @param task  The task that was just added.
     * @param total The new total number of tasks in the list.
     */
    public void showAdded(Task task, int total) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println(" " + task);
        System.out.println("now you have " + total + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints the confirmation message shown after deleting a task.
     *
     * @param task  The task that was just removed.
     * @param total The new total number of tasks in the list.
     */
    public void showRemoved(Task task, int total) {
        System.out.println(LINE);
        System.out.println("Okay, I've removed this task");
        System.out.println("Now you have " + total + " tasks in the list");
        System.out.println(LINE);
    }

    /**
     * Prints the tasks matched by a `find` command, or a friendly
     * message when there are no matches.
     *
     * @param results The task list containing matched tasks.
     */
    public void showFindResults(TaskList results) {
        System.out.println(LINE);
        if (results.size() == 0) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list");
            for (int i = 0; i < results.size(); i++) {
                System.out.println(" " + (i + 1) + "." + results.get(i));
            }
        }
        System.out.println(LINE);
    }
    
}
