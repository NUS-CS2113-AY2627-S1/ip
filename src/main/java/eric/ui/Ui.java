package eric.ui;

import java.util.List;
import java.util.Scanner;

import eric.task.Task;

/**
 * Deals with all interaction with the user: reading the commands that the user types, and showing
 * messages on the console. Every message is shown between two horizontal divider lines.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = "███████╗██████╗ ██╗ ██████╗\n"
            + "██╔════╝██╔══██╗██║██╔════╝\n"
            + "█████╗  ██████╔╝██║██║     \n"
            + "██╔══╝  ██╔══██╗██║██║     \n"
            + "███████╗██║  ██║██║╚██████╗\n"
            + "╚══════╝╚═╝  ╚═╝╚═╝ ╚═════╝\n";

    private final Scanner in;

    /** Creates a Ui that reads the user's commands from the standard input. */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /** Returns true if the user has another command to read, i.e. the input has not ended. */
    public boolean hasNextCommand() {
        return in.hasNextLine();
    }

    /** Returns the next line that the user typed, without surrounding spaces. */
    public String readCommand() {
        return in.nextLine().trim();
    }

    /** Stops reading the user's input. */
    public void close() {
        in.close();
    }

    /** Shows the startup banner and greeting. */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Eric.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Shows the farewell message. */
    public void showGoodbye() {
        showMessage(" Bye. Hope to see you again soon!");
    }

    /** Shows every task in {@code tasks}, numbered from 1. */
    public void showTaskList(List<Task> tasks) {
        System.out.println(DIVIDER);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Shows the tasks that matched a search, numbered from 1 in the order of the results.
     *
     * @param matchingTasks The tasks that matched, which may be empty.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            showMessage(" There are no matching tasks in your list.");
            return;
        }
        System.out.println(DIVIDER);
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Confirms that a task was added.
     *
     * @param task The task that was added.
     * @param taskCount Number of tasks in the list after the task was added.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage(" Got it. I've added this task:\n   " + task
                + "\n Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task The task that was removed.
     * @param taskCount Number of tasks in the list after the task was removed.
     */
    public void showTaskRemoved(Task task, int taskCount) {
        showMessage(" Noted. I've removed this task:\n   " + task
                + "\n Now you have " + taskCount + " tasks in the list.");
    }

    /** Confirms that {@code task} was marked as done. */
    public void showTaskMarked(Task task) {
        showMessage(" Nice! I've marked this task as done:\n   " + task);
    }

    /** Confirms that {@code task} was marked as not done. */
    public void showTaskUnmarked(Task task) {
        showMessage(" OK, I've marked this task as not done yet:\n   " + task);
    }

    /**
     * Shows an error, stating what went wrong and how to correct it.
     *
     * @param problem Why the command could not be carried out.
     * @param fix What the user should do or type instead.
     */
    public void showError(String problem, String fix) {
        showMessage(" OOPS!!! " + problem + "\n " + fix);
    }

    /** Shows {@code message} between two divider lines. */
    private void showMessage(String message) {
        System.out.println(DIVIDER);
        System.out.println(message);
        System.out.println(DIVIDER);
    }
}
