package yao;

import java.util.ArrayList;
import java.util.Scanner;
import yao.task.Task;

/**
 * Handles all user interactions for the Yao application.
 * Responsible for reading user input commands and displaying messages,
 * greetings, errors, and task status updates.
 */
public class Ui {
    private static final String BORDER_LINE = "____________________________________________________________";
    private static final String BANNER = " __   __            \n"
            + " \\ \\ / /_ _  ___   \n"
            + "  \\ V / _` |/ _ \\  \n"
            + "   | | (_| | (_) | \n"
            + "   |_|\\__,_|\\___/  \n";

    private final Scanner scanner;

    /**
     * Constructs a new Ui instance with standard input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of command input from the user.
     *
     * @return The raw command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the horizontal divider line.
     */
    public void showLine() {
        System.out.println(BORDER_LINE);
    }

    /**
     * Displays the welcome greeting and banner to the user.
     */
    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        System.out.println("Hello! I'm Yao.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the farewell message when exiting the application.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays the list of tasks currently recorded.
     *
     * @param tasks The TaskList to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays the list of tasks currently recorded.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        showTaskList(new TaskList(tasks));
    }

    /**
     * Displays the matching tasks found from a keyword search.
     *
     * @param matchingTasks The list of tasks matching the keyword.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Displays tasks occurring on a specified date.
     *
     * @param tasks The list of matching tasks.
     * @param date The date filtered by.
     */
    public void showTasksOnDate(ArrayList<Task> tasks, java.time.LocalDate date) {
        String formattedDate = DateTimeUtil.formatDate(date);
        if (tasks.isEmpty()) {
            System.out.println("No deadlines or events found on " + formattedDate + ".");
            return;
        }
        System.out.println("Here are the tasks occurring on " + formattedDate + ":");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task The task that was marked done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task The task that was marked not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was deleted from the list.
     *
     * @param task The task that was removed.
     * @param taskCount The updated total number of tasks remaining.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a new task was added to the list.
     *
     * @param task The newly added task.
     * @param taskCount The updated total number of tasks in the list.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays an error message formatted with an indentation.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Displays a loading error warning message when reading from file storage fails.
     *
     * @param message The error detail message.
     */
    public void showLoadingError(String message) {
        System.out.println("Warning: Unable to read data file: " + message);
    }
}
