package king.ui;

import java.util.List;
import java.util.Scanner;

import king.task.Task;

/**
 * Reads console commands and displays the chatbot's messages and task summaries.
 * Owns the input scanner for one conversation.
 */
public class Ui implements AutoCloseable {

    /** Line of underscores used to frame the chatbot's messages. */
    private static final String LINE = "____________________________________________________________\n";

    private final Scanner console;

    /**
     * Creates a user interface using standard input and output.
     */
    public Ui() {
        console = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available, allowing clean exit at end of input.
     *
     * @return Whether another input line is available.
     */
    public boolean hasNextCommand() {
        return console.hasNextLine();
    }

    /**
     * Returns the next input line; command interpretation belongs to the caller.
     *
     * @return Next input line without its line terminator.
     */
    public String readCommand() {
        return console.nextLine();
    }

    /**
     * Displays the greeting and banner between divider lines.
     */
    public void showWelcome() {
        String banner = "    __    _          \n"
                + "   / /__ (_)__  ___ _\n"
                + "  /  '_// / _ \\/ _ `/\n"
                + " /_/\\_\\/_/_//_/\\_, / \n"
                + "              /___/\n";
        showLine();
        System.out.print(banner);
        System.out.println("Hello, my subject. I am King, your faithful chatbot.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the divider between responses.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays an error with the chatbot's customary address.
     *
     * @param message Explanation to display or include in a failure.
     */
    public void showError(String message) {
        System.out.println("    My subject, " + message);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("    Bye. Hope to see you again soon!");
    }

    /**
     * Prints all stored tasks as a numbered list.
     *
     * @param tasks Tasks to display, in list order.
     */
    public void showTasks(List<Task> tasks) {
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            printTask(i + 1, tasks.get(i));
        }
    }

    /**
     * Displays matching tasks numbered from one, or an explanation when no tasks match.
     *
     * @param matchingTasks Matching tasks in their original order.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println("    Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            System.out.println("    No matching tasks found.");
        }
        for (int i = 0; i < matchingTasks.size(); i++) {
            printTask(i + 1, matchingTasks.get(i));
        }
    }

    /**
     * Prints a task with its type, done state, and description.
     * Includes a list number only when the index is positive.
     *
     * @param index Display number, or zero to omit numbering.
     * @param task Task to display or serialize.
     */
    private void printTask(int index, Task task) {
        if (index > 0) {
            System.out.printf("    %d. ", index);
        }
        System.out.printf("[%s][%s] %s%n", task.getTaskType(), task.getStatusIcon(), task.getDescription());
    }

    /**
     * Prints an added task and the updated task count.
     *
     * @param task Task to display or serialize.
     * @param taskCount Number of tasks in the list.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("    Got it. I've added this task:");
        System.out.print("      ");
        printTask(0, task);
        System.out.println("    Now you have " + taskCount + " task(s) in the list.");
    }

    /**
     * Displays a confirmation of the task's current completion state.
     *
     * @param task Task to display or serialize.
     */
    public void showStatusChanged(Task task) {
        if (task.isDone()) {
            System.out.println("    Nice! I've marked this task as done:");
        } else {
            System.out.println("    OK, I've marked this task as not done yet:");
        }
        System.out.print("      ");
        printTask(0, task);
    }

    /**
     * Displays a removed task and the remaining task count.
     *
     * @param task Task to display or serialize.
     * @param taskCount Number of tasks in the list.
     */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println("    By your command, I've removed this task:");
        System.out.print("      ");
        printTask(0, task);
        System.out.println("    Now you have " + taskCount + " task(s) in the list.");
    }

    /**
     * Closes the conversation's input scanner and its underlying input stream.
     */
    @Override
    public void close() {
        console.close();
    }
}
