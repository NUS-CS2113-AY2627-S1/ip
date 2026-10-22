package cracker;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all console interactions with the user.
 */
public class Ui {
    /** Divider printed between chatbot responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Banner printed when the chatbot starts. */
    private static final String BANNER = " ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗ \n"
            + "██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗\n"
            + "██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝\n"
            + "██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗\n"
            + "╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║\n"
            + " ╚═════╝╚═╝  ╚═╝╚═╝  ╚════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═════╝\n";

    /** Scanner used to read commands entered in the console. */
    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from the standard input stream.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Shows the chatbot greeting. */
    public void showGreeting() {
        showDivider();
        System.out.print(BANNER);
        System.out.println("Hello! I'm Cracker.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /** Shows a divider between chatbot responses. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Reads the next complete command entered by the user.
     *
     * @return command entered by the user, or {@code bye} when the input stream ends
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return "bye";
        }
        return scanner.nextLine();
    }

    /**
     * Shows a user-facing explanation for invalid input.
     *
     * @param message explanation and correction for the invalid input
     */
    public void showError(String message) {
        System.out.println(" Error: " + message);
    }

    /**
     * Shows every task currently in the task list.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Shows tasks whose descriptions match a search keyword.
     *
     * @param matchingTasks tasks to display
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount number of tasks now in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was marked complete.
     *
     * @param task task that was marked complete
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    /**
     * Shows confirmation that a task was marked incomplete.
     *
     * @param task task that was marked incomplete
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    /**
     * Shows confirmation that a task was deleted.
     *
     * @param task task that was deleted
     * @param taskCount number of tasks now in the list
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /** Shows the chatbot farewell. */
    public void showFarewell() {
        System.out.println(" Bye. Hope to see you again soon!");
    }
}
