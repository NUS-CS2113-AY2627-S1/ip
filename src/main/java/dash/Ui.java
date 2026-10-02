package dash;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import dash.task.Task;

/**
 * Handles interactions with the user, including reading input and displaying messages.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = " ____              _     \n"
            + "|  _ \\  __ _ ___| |__  \n"
            + "| | | |/ _` / __| '_ \\ \n"
            + "| |_| | (_| \\__ \\ | | |\n"
            + "|____/ \\__,_|___/_| |_|\n";

    private final Scanner scanner;

    /**
     * Constructs a new {@code Ui} instance with standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message and application banner.
     */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println(DIVIDER);
        System.out.println("Hello! I'm Dash.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Reads the next line of command input from the user.
     *
     * @return The raw command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a horizontal divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Displays an error message when tasks cannot be loaded from the data file.
     *
     * @param message The error details to display.
     */
    public void showLoadingError(String message) {
        System.out.println(" Error reading tasks from file: " + message);
    }

    /**
     * Displays an error message when tasks cannot be saved to the data file.
     *
     * @param message The error details to display.
     */
    public void showSavingError(String message) {
        System.out.println(" Error saving tasks to file: " + message);
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks that occur on the specified date.
     *
     * @param tasks The tasks that occur on the date.
     * @param date The date being queried.
     */
    public void showTasksOnDate(List<Task> tasks, LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH));
        if (tasks.isEmpty()) {
            System.out.println(" There are no tasks occurring on " + formattedDate + ".");
            return;
        }
        System.out.println(" Here are the tasks occurring on " + formattedDate + ":");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks that match the search keyword.
     *
     * @param matchingTasks The list of tasks matching the search criteria.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" There are no matching tasks in your list.");
            return;
        }
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Displays a confirmation message after a task is added.
     *
     * @param task The task that was added.
     * @param taskCount The updated total number of tasks in the list.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a confirmation message after a task is removed.
     *
     * @param task The task that was removed.
     * @param taskCount The updated total number of tasks in the list.
     */
    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a confirmation message after a task is marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays a confirmation message after a task is marked as not done yet.
     *
     * @param task The task that was marked as not done yet.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }
}
