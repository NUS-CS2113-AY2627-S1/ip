package slay69.ui;

import java.util.List;
import java.util.Scanner;

import slay69.task.Task;

/**
 * Handles all console input and output for the Slay69 chatbot.
 */
public class Ui implements AutoCloseable {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from the standard input stream.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available from the input stream.
     *
     * @return true if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command and removes surrounding whitespace.
     *
     * @return the trimmed command text
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the chatbot logo and welcome message.
     */
    public void showGreeting() {
        String logo = " ____  _            __    ___\n"
                + "/ ___|| | __ _ _   _/ /_  / _ \\\n"
                + "\\___ \\| |/ _` | | | | '_ \\| (_) |\n"
                + " ___) | | (_| | |_| | (_) \\__, |\n"
                + "|____/|_|\\__,_|\\__, |\\___/  /_/ \n"
                + "               |___/            \n";

        System.out.println("Hello from\n" + logo);
        showLine();
        System.out.println(" Wassup! I'm slay_69");
        System.out.println(" What you want?");
        showLine();
    }

    /**
     * Displays a divider between commands and responses.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error encountered while loading saved tasks.
     *
     * @param message explanation of the loading error
     */
    public void showLoadingError(String message) {
        showLine();
        System.out.println(" Could not load tasks: " + message);
        System.out.println(" Please check data/slay69.txt and restart.");
        showLine();
    }

    /**
     * Displays an invalid-command or invalid-task error.
     *
     * @param message explanation of the invalid input
     */
    public void showError(String message) {
        System.out.println(" HUHHH!!! " + message);
    }

    /**
     * Warns that an in-memory change could not be saved.
     *
     * @param message explanation of the saving error
     */
    public void showSavingError(String message) {
        System.out.println(" Could not save tasks: " + message);
        System.out.println(" Your change is still in memory, "
                + "but may be lost when the app closes.");
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println(" Bye. Better do your work.");
    }

    /**
     * Displays every task with its one-based task number.
     *
     * @param tasks tasks in their display order
     */
    public void showTasks(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays matching tasks with their original numbers in the full list.
     *
     * @param tasks all tasks in display order
     * @param matchingNumbers one-based numbers of matching tasks
     */
    public void showMatchingTasks(List<Task> tasks, List<Integer> matchingNumbers) {
        System.out.println(" Here are the matching tasks in your list:");

        if (matchingNumbers.isEmpty()) {
            System.out.println(" No matching tasks found.");
        }

        for (int taskNumber : matchingNumbers) {
            System.out.println(" " + taskNumber + "." + tasks.get(taskNumber - 1));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount total number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Kk. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount
                + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task task that was removed
     * @param taskCount total number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task's completion status changed.
     *
     * @param task task whose status changed
     * @param isDone whether the task is now completed
     */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        if (isDone) {
            System.out.println(" Slayyyy! I've marked this task as done:");
        } else {
            System.out.println(
                    " When you want do, I've marked this task as not done yet:");
        }

        System.out.println("   " + task);
    }

    /**
     * Closes the console input reader.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
