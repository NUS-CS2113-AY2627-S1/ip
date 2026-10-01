package proton.ui;

import proton.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Reads console commands and displays Proton's messages without changing tasks.
 */
public class Ui implements AutoCloseable {
    private static final String BANNER = " ____            _              \n"
            + "|  _ \\ _ __ ___ | |_ ___  _ __ \n"
            + "| |_) | '__/ _ \\| __/ _ \\| '_ \\\n"
            + "|  __/| | | (_) | || (_) | | | |\n"
            + "|_|   |_|  \\___/ \\__\\___/|_| |_|\n";
    private static final String SEPARATOR = "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Checks whether another command is available, waiting for input if necessary.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command exactly as entered, preserving whitespace for validation.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the banner and greeting.
     */
    public void showWelcome() {
        System.out.println(BANNER);
        showSeparator();
        System.out.println("Hey there! I'm Proton, your positively charged chatbot!");
        System.out.println("I'm fired up and ready to help! What awesome thing shall we tackle today?");
        showSeparator();
    }

    /**
     * Displays a boundary around a command response.
     */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the startup failure message for the task file.
     */
    public void showLoadingError() {
        showError("Proton could not load data/proton.txt. Check the file and restart.");
    }

    /**
     * Displays an error using the same indentation as other command responses.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println(" Powering down for now, I'll see you next time!");
    }

    /**
     * Displays tasks in list order with one-based numbers.
     */
    public void showTasks(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println(" Here are the matching tasks in your list:");
        showNumberedTasks(tasks);
    }

    /**
     * Displays the supplied tasks with one-based numbers in their current order.
     */
    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation of a successfully saved completion change.
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation of a successfully saved change back to incomplete.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Displays the deleted task and the remaining count after a successful save.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays the added task and the new count after a successful save.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        showTaskCount(taskCount);
    }

    private void showTaskCount(int taskCount) {
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Closes the scanner and standard input when the console session ends.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
