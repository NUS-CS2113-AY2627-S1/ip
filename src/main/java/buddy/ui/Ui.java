package buddy.ui;

import java.util.List;
import java.util.Scanner;

import buddy.task.Task;

/**
 * Handles all interaction with the user: printing Buddy's messages to the
 * console and reading the commands the user types.
 */
public class Ui {
    private static final String NAME = "Buddy";
    private static final String LINE = "---------------------------------------------------------------";

    private final Scanner scanner;

    /**
     * Creates a Ui that reads user input from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Checks whether there is another command waiting to be read.
     *
     * @return true if {@link #readCommand()} can be called safely
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next full line the user typed.
     *
     * @return the line the user typed
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the divider line used to separate one command's output from the next.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints Buddy's startup banner and greeting.
     */
    public void showGreeting() {
        String banner = " ____            _     _       \n"
                + "| __ ) _   _  __| | __| |_   _ \n"
                + "|  _ \\| | | |/ _` |/ _` | | | |\n"
                + "| |_) | |_| | (_| | (_| | |_| |\n"
                + "|____/ \\__,_|\\__,_|\\__,_|\\__, |\n"
                + "                          |___/ \n";

        showLine();
        System.out.println(banner);
        System.out.println("Hello! I'm " + NAME);
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Prints Buddy's farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints an error message, e.g. when a command could not be understood or carried out.
     *
     * @param message explanation to show the user
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /**
     * Prints every task in the given list, numbered from 1.
     *
     * @param tasks tasks to display, in order
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        printNumberedTasks(tasks);
    }

    /**
     * Prints every task in the given list that matched a search, numbered from 1.
     *
     * @param tasks matching tasks to display, in order
     */
    public void showMatchingTaskList(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        printNumberedTasks(tasks);
    }

    private void printNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Prints confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount number of tasks now in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        printTaskCount(taskCount);
    }

    /**
     * Prints confirmation that a task was removed.
     *
     * @param task task that was removed
     * @param taskCount number of tasks remaining in the list
     */
    public void showTaskRemoved(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        printTaskCount(taskCount);
    }

    /**
     * Prints confirmation that a task was marked as done.
     *
     * @param task task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Prints confirmation that a task was marked as not done.
     *
     * @param task task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    private void printTaskCount(int taskCount) {
        String taskLabel = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskLabel + " in the list.");
    }
}
