package buddy.ui;

import java.util.List;

import buddy.task.Task;
import buddy.task.TaskList;

/**
 * Handles all console output shown to the user, so the rest of Buddy
 * doesn't need to know about formatting or {@code System.out} directly.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    /**
     * Prints a message surrounded by horizontal divider lines.
     *
     * @param message the message to display
     */
    public void showMessage(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation that a task was added, including the updated
     * task count.
     *
     * @param task the task that was just added
     * @param totalTasks how many tasks are now stored
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints all tasks in the given list, numbered from 1, between divider
     * lines.
     *
     * @param taskList the tasks to display
     */
    public void showTaskList(TaskList taskList) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        printNumberedTasks(taskList.asList());
        System.out.println(LINE);
    }

    /**
     * Prints the tasks that matched a search keyword, numbered from 1,
     * between divider lines.
     *
     * @param matches the matching tasks to display
     */
    public void showMatchingTasks(List<Task> matches) {
        System.out.println(LINE);
        if (matches.isEmpty()) {
            System.out.println(" Buddy couldnt find any tasks with that keyword.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            printNumberedTasks(matches);
        }
        System.out.println(LINE);
    }

    /**
     * Prints each task in the given list on its own line, numbered from 1.
     *
     * @param tasks the tasks to print
     */
    private void printNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints a confirmation that a task's done/not-done status changed.
     *
     * @param task the task whose status changed
     * @param isDone true if the task was marked done, false if marked not done
     */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        System.out.println(LINE);
        String confirmation = isDone
                ? " Nice! I've marked this task as done:"
                : " OK, I've marked this task as not done yet:";
        System.out.println(confirmation);
        System.out.println("   " + task);
        System.out.println(LINE);
    }

    /**
     * Prints a confirmation that a task was removed, including the updated
     * task count.
     *
     * @param task the task that was removed
     * @param totalTasks how many tasks remain
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints Buddy's startup banner and greeting.
     */
    public void showGreeting() {
        String banner = " ____   _   _  ____   ____  __   __\n"
                + "| __ ) | | | ||  _ \\ |  _ \\ \\ \\ / /\n"
                + "|  _ \\ | | | || | | || | | | \\ V / \n"
                + "| |_) || |_| || |_| || |_| |  | |  \n"
                + "|____/  \\___/ |____/ |____/   |_|  \n";
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println(" Hello! I'm Buddy.");
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }
}
