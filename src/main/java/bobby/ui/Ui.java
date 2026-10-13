package bobby.ui;

import bobby.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles user input and displays messages to the user.
 */
public class Ui {
    private final Scanner scanner;
    private static final String BORDER_CHAR = "~";
    private static final int EXTRA_SPACING = 10;

    /**
     * Creates an Ui object for user interaction.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the user's input
     */
    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * Prints a message surrounded by a border
     * adjusted to the maximum line length.
     *
     * @param message the message to display
     */
    private void printWithBorder(String message) {
        String[] lines = message.split("\n");

        int maxLength = 0;
        for (String line : lines) {
            maxLength = Math.max(maxLength, line.length());
        }

        String border = BORDER_CHAR.repeat(maxLength + EXTRA_SPACING);

        System.out.println(border);
        System.out.println(indent(message));
        System.out.println(border);
    }

    /**
     * Adds indentation to each line of the given message.
     *
     * @param message the message to indent
     * @return the indented message
     */
    private String indent(String message) {
        return "\t" + message.replace("\n", "\n\t");
    }

    /**
     * Displays a Bobby banner.
     */
    public void showBanner() {
        System.out.println(
                  " ____        _     _           \n"
                + "| __ )  ___ | |__ | |__  _   _ \n"
                + "|  _ \\ / _ \\| '_ \\| '_ \\| | | |\n"
                + "| |_) | (_) | |_) | |_) | |_| |\n"
                + "|____/ \\___/|_.__/|_.__/ \\__, |\n"
                + "                         |___/ \n"
        );
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcomeMessage() {
        printWithBorder("Hi! I'm Bobby.\nWhat can I do for you?\n");
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbyeMessage() {
        printWithBorder("Bye Bye!");
    }

    /**
     * Displays a formatted error message.
     *
     * @param message the original error message
     */
    public void showErrorMessage(String message) {
        printWithBorder("ERROR: " + message);
    }

    /**
     * Displays a message indicating that a task was marked as done.
     *
     * @param task the task that was marked as done
     */
    public void showMarkedTask(Task task) {
        printWithBorder("Good, this task is done: " + task);
    }

    /**
     * Displays a message indicating that a task was marked as not done.
     *
     * @param task the task that was marked as not done
     */
    public void showUnmarkedTask(Task task) {
        printWithBorder("Okay, this task is not done: " + task);
    }

    /**
     * Displays a message indicating that a task was added.
     *
     * @param task the added task
     * @param taskCount the current number of tasks
     */
    public void showAddedTask(Task task, int taskCount) {
        printWithBorder("added: \n\t" + task
                + "\nYou now have " + taskCount + " pending tasks.");
    }

    /**
     * Displays a message indicating that a task was deleted.
     *
     * @param task the deleted task
     * @param taskCount the current number of tasks
     */
    public void showDeletedTask(Task task, int taskCount) {
        printWithBorder("removed: \n\t" + task
                + "\nYou now have " + taskCount + " pending tasks.");
    }

    /**
     * Displays the entire list of tasks.
     *
     * @param tasks the list of tasks
     */
    public void showTaskList(ArrayList<Task> tasks) {
        String message;
        if (tasks.isEmpty()) {
            message = "So empty...";
        } else {
            message = "Here are your tasks:\n" + convertTaskListToStringFormat(tasks);
        }

        printWithBorder(message);
    }

    /**
     * Displays tasks that matched a search keyword.
     *
     * @param tasks the list of matching tasks
     */
    public void showMatchedTasks(ArrayList<Task> tasks) {
        String message;
        if (tasks.isEmpty()) {
            message = "No similar tasks were found.";
        } else {
            message = "Here are the matched tasks:\n" + convertTaskListToStringFormat(tasks);
        }

        printWithBorder(message);
    }

    /**
     * Converts a list of tasks into a formatted string for displaying.
     *
     * @param tasks the list of tasks to format
     * @return a formatted string containing the tasks
     */
    private String convertTaskListToStringFormat(ArrayList<Task> tasks) {
        String formattedList = "";
        for (int i = 0; i < tasks.size(); i++) {
            formattedList += "\t" + (i + 1) + ". " + tasks.get(i) + "\n";
        }

        return formattedList;
    }
}
