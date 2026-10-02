package chre.ui;

import java.util.Scanner;
import java.util.List;
import chre.task.Task;

/**
 * Ui handles all user interface interactions.
 * Manages input reading and output display.
 */
public class Ui {
    private Scanner scanner;
    private String separator;
    private String banner;

    /**
     * Constructs a Ui object and initializes the scanner and display elements.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.separator = "____________________________________________________________";
        this.banner = " _____ _               \n"
                + "/ ____| |              \n"
                + "| |    | |__  _ __ ___ \n"
                + "| |    | '_ \\| '__/ _ \\\n"
                + "| |____| | | | | |  __/\n"
                + " \\_____|_| |_|_|  \\___|";
    }

    /**
     * Displays the welcome message and banner.
     */
    public void displayWelcome() {
        System.out.println(separator);
        System.out.println(banner);
        System.out.println();
        System.out.println("Hello! I'm Chre.");
        System.out.println("What can I do for you?");
        System.out.println(separator);
    }

    /**
     * Displays the farewell message.
     */
    public void displayFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(separator);
    }

    /**
     * Reads a command from the user.
     *
     * @return the user's input
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays all tasks in the list with 1-based numbering.
     *
     * @param tasks the list of tasks to display
     */
    public void showTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task the task that was added
     * @param totalTasks the total number of tasks in the list
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays a confirmation message when a task is added (legacy method).
     *
     * @param task the task description
     */
    public void showTaskAdded(String task) {
        System.out.println("added: " + task);
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task the task that was deleted
     * @param totalTasks the total number of remaining tasks in the list
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays the separator line.
     */
    public void showSeparator() {
        System.out.println(separator);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays tasks that match a search keyword.
     *
     * @param tasks the list of matching tasks
     * @param keyword the search keyword used
     */
    public void showFoundTasks(List<Task> tasks, String keyword) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found matching \"" + keyword + "\".");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            showTasks(tasks);
        }
    }

    /**
     * Closes the scanner resource.
     */
    public void close() {
        scanner.close();
    }
}
