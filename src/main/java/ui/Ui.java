package ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import tasks.Deadline;
import tasks.Event;
import tasks.Task;

/**
 * Reads console commands and displays task responses without changing task state.
 */
public class Ui implements AutoCloseable {
    private static final String LINE_SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER = "      __________\n"
            + "    /            \\\n"
            + "   /   •     •    \\\n"
            + "  |      ᴥ         |\n"
            + "   \\    _____     /\n"
            + "    \\____________/\n"
            + "      ||    ||\n"
            + "      ||____||";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next command, preserving whitespace for command validation.
     *
     * @return The user's complete input line.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome banner before tasks are loaded.
     */
    public void showGreeting() {
        System.out.println(BANNER);
        System.out.println("HELLO!! I am SamSquare :D\n"
                + "What can I do for you?");
        showLine();
    }

    /**
     * Displays tasks in their current order using one-based task numbers.
     *
     * @param tasks Tasks to display.
     */
    public void showTasks(List<Task> tasks) {
        showTaskList(tasks, " Here are the tasks in your list:");
    }

    /**
     * Displays matching tasks with consecutive result numbers starting at one.
     *
     * @param tasks Search results in their original order.
     */
    public void showMatchingTasks(List<Task> tasks) {
        showTaskList(tasks, " Here are the matching tasks in your list:");
    }

    /**
     * Displays deadlines for a date, numbering the results from one.
     *
     * @param tasks Matching deadlines in their original order.
     * @param date Calendar date used for the search heading.
     */
    public void showDeadlinesOn(List<Task> tasks, LocalDate date) {
        showTaskList(tasks, " Here are the deadlines on " + date + ":");
    }

    /**
     * Uses the same numbering and task format for full lists and search results.
     *
     * @param tasks Tasks to display, in result order.
     * @param heading Heading identifying the kind of list being displayed.
     */
    private void showTaskList(List<Task> tasks, String heading) {
        System.out.println(heading);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + formatTask(tasks.get(i)));
        }
        showLine();
    }

    /**
     * Displays a successful addition using the existing wording for each task type.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        if (task instanceof Deadline) {
            System.out.println(" Got it! I've added this task:");
        } else if (task instanceof Event) {
            System.out.println(" Got it. I've added this task:");
        } else {
            System.out.println(" Got it!! I've added this task:");
        }
        System.out.println("   " + formatTask(task));
        String ending = task instanceof Event ? "." : " :)";
        System.out.println(" Now you have " + taskCount + " tasks in the list" + ending);
        showLine();
    }

    /**
     * Displays the task after its completion status has changed.
     *
     * @param task Task with its updated status.
     */
    public void showTaskStatus(Task task) {
        if (task.isDone()) {
            System.out.println(" WELL DONE!! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + formatTask(task));
        showLine();
    }

    /**
     * Displays a successful deletion and the remaining task count.
     *
     * @param task Task that was removed.
     * @param taskCount Number of remaining tasks.
     */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(" Ahh noted! I've removed this task:");
        System.out.println("   " + formatTask(task));
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a rejected command's error message.
     *
     * @param message Explanation of the invalid input.
     */
    public void showError(String message) {
        System.out.println(" WAIT PAUSE!! " + message);
        showLine();
    }

    /**
     * Displays the farewell response.
     */
    public void showGoodbye() {
        System.out.println("Byebye hope to see you again soon!");
        showLine();
    }

    /**
     * Combines type, completion status, and details into one console task entry.
     *
     * @param task Task to format.
     * @return The task entry without indentation or a list number.
     */
    private String formatTask(Task task) {
        return "[" + task.getTypeIcon() + "][" + task.getStatusIcon() + "] "
                + task.getFullDescription();
    }

    /**
     * Prints the separator that terminates a console response.
     */
    private void showLine() {
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Closes console input when the application finishes.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
