package dog.ui;

import java.util.List;
import java.util.Scanner;

import dog.task.Task;

/**
 * Reads console commands and displays chatbot responses.
 */
public class Ui implements AutoCloseable {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a console interface using standard input and output.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Prompts for the next command, treating end of input as an exit request.
     *
     * @return Trimmed command, or {@code bye} when input has ended.
     */
    public String readCommand() {
        System.out.print("> ");
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "bye";
    }

    /**
     * Displays the goodbye message.
     */
    public void printGoodbye() {
        System.out.println("Woof! See you again!");
    }

    /**
     * Releases the scanner and its underlying input stream.
     */
    @Override
    public void close() {
        scanner.close();
    }

    /**
     * Displays the Dog banner and welcome message.
     */
    public void printGreeting() {
        String banner = " ____              \n"
                + "|  _ \\  ___   __ _ \n"
                + "| | | |/ _ \\ / _` |\n"
                + "| |_| | (_) | (_| |\n"
                + "|____/ \\___/ \\__, |\n"
                + "             |___/ \n";
        System.out.println(banner);
        System.out.println("Woof! What can I do for you today?");
    }

    /**
     * Displays all tasks with one-based numbers, or an empty-list message.
     *
     * @param tasks Tasks in their stored order.
     */
    public void printTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty :(");
            return;
        }
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays search results numbered independently of the full task list.
     *
     * @param matches Matching tasks in their stored order.
     */
    public void printMatchingTasks(List<Task> matches) {
        System.out.println(DIVIDER);
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation of a task's updated completion status.
     *
     * @param task Updated task.
     * @param taskNumber One-based position in the full task list.
     * @param isDone Whether the task was marked as done.
     */
    public void printTaskStatus(Task task, int taskNumber, boolean isDone) {
        String action = isDone ? "marked as done" : "marked as not done";
        System.out.println("Task " + taskNumber + " has been " + action + ":");
        System.out.println(task);
    }

    /**
     * Displays the added task and the resulting task count.
     *
     * @param task Newly added task.
     * @param taskCount Number of tasks after addition.
     */
    public void printAddedTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Woof! I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the removed task and the remaining task count.
     *
     * @param task Removed task.
     * @param taskCount Number of tasks after deletion.
     */
    public void printDeletedTask(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays an error message between divider lines.
     *
     * @param message Error text to show.
     */
    public void printError(String message) {
        System.out.println(DIVIDER);
        System.out.println(message);
        System.out.println(DIVIDER);
    }

}
