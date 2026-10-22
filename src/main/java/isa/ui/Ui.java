package isa.ui;

import isa.task.Task;
import isa.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Handles console input and messages shown to the user.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prints the greeting shown when Isa starts.
     */
    public void showGreeting() {
        System.out.println("Helloo! I'm Isa");
        System.out.println("How can I help you?");
        showDivider();
    }

    /**
     * Reads the next command from the console.
     *
     * @return Command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the divider between responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints a command error.
     *
     * @param message Error message to show.
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Prints the farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope you have a nice day!");
    }

    /**
     * Prints tasks in their current order.
     *
     * @param tasks Tasks to display.
     */
    public void showTasks(TaskList tasks) {
        System.out.println(" Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints matching tasks with their numbers in the complete task list.
     *
     * @param tasks Full task list.
     * @param matches Zero-based indices of matching tasks.
     */
    public void showMatchingTasks(TaskList tasks, List<Integer> matches) {
        System.out.println(" Here are the matching tasks in your list:");

        if (matches.isEmpty()) {
            System.out.println(" No matching tasks found.");
        }

        for (int index : matches) {
            System.out.println(" " + (index + 1) + "." + tasks.get(index));
        }
    }

    /**
     * Prints the heading for deadlines and events occurring on a date.
     *
     * @param date Date being listed.
     */
    public void showOnDateHeader(LocalDate date) {
        System.out.println(" Tasks on " + date.format(DATE_FORMAT) + ":");
    }

    /**
     * Prints a matching task with its number in the complete task list.
     *
     * @param taskNumber One-based task number.
     * @param task Task occurring on the date.
     */
    public void showTaskOnDate(int taskNumber, Task task) {
        System.out.println(" " + taskNumber + "." + task);
    }

    /**
     * Reports when no deadlines or events occur on the selected date.
     */
    public void showNoTasksOnDate() {
        System.out.println(" No deadlines or events on this date.");
    }

    /**
     * Acknowledges a task marked as done.
     *
     * @param task Task that was marked.
     */
    public void showMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Acknowledges a task marked as not done.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Acknowledges an added task and the new list size.
     *
     * @param task Added task.
     * @param taskCount Number of tasks after the addition.
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Acknowledges a deleted task and the new list size.
     *
     * @param task Deleted task.
     * @param taskCount Number of tasks after deletion.
     */
    public void showDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Reports a saved task that could not be loaded.
     *
     * @param warning Location and reason for the skipped task.
     */
    public void showLoadWarning(String warning) {
        System.out.println(" Warning: skipped saved task on " + warning);
    }

    /**
     * Reports a failure to load the task list.
     *
     * @param message Storage error message.
     */
    public void showLoadError(String message) {
        System.out.println(" Warning: " + message + ".");
        System.out.println(" Starting with an empty task list.");
    }

    /**
     * Reports a failure to save the task list.
     *
     * @param message Storage error message.
     */
    public void showSaveWarning(String message) {
        System.out.println(" Warning: " + message + ".");
    }

    /**
     * Closes the console scanner.
     */
    public void close() {
        scanner.close();
    }
}
