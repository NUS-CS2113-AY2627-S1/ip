package maple.ui;

import java.util.List;
import java.util.Scanner;

import maple.task.Task;

/**
 * Handles Maple's user-facing input and output.
 */
public class Ui {
    private static final String SEPARATOR = "_".repeat(60);
    private static final String BANNER = "   __  ___          __   \n"
            + "  /  |/  /__ ____  / /__ \n"
            + " / /|_/ / _ `/ _ \\/ / -_)\n"
            + "/_/  /_/\\_,_/ .__/_/\\__/ \n"
            + "           /_/           ";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the welcome message with the Maple banner.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println(" Hello! I'm Maple.");
        System.out.println(" What can I do for you?");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Reads the next line of user input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Returns whether another command can be read from the input.
     *
     * @return true if more input is available, false at end of input.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Confirms that a task has been added.
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Confirms that a task has been deleted.
     */
    public void showDeleted(Task task, int taskCount) {
        System.out.println(SEPARATOR);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Displays the list of tasks.
     */
    public void showTasks(List<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Displays tasks that match a find command.
     *
     * @param tasks the matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Confirms that a task has been marked as done.
     */
    public void showMarked(Task task) {
        System.out.println(SEPARATOR);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Confirms that a task has been marked as not done.
     */
    public void showUnmarked(Task task) {
        System.out.println(SEPARATOR);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Displays an error caused by an invalid command.
     *
     * @param message the message that explains the error.
     */
    public void showError(String message) {
        System.out.println(SEPARATOR);
        System.out.println(" Oops! " + message);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * Prints the farewell message.
     */
    public void showExit() {
        System.out.println(SEPARATOR);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
