import java.util.Scanner;

/**
 * Reads user commands and displays messages from Exia.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Checks whether another command is available.
     *
     * @return true if another command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next user command.
     *
     * @return next command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the greeting shown when Exia starts.
     */
    public void showGreeting() {
        System.out.println(LINE);
        System.out.println("Hello! I'm Exia");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays the message shown when Exia exits.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays all tasks in a numbered list.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Displays tasks that match a search keyword.
     *
     * @param matchingTasks matching tasks to display
     */
    public void showMatchingTasks(TaskList matchingTasks) {
        System.out.println(LINE);
        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }

        System.out.println(LINE);
    }

    /**
     * Displays the task that was marked as completed.
     *
     * @param task completed task
     */
    public void showMarkedTask(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays the task that was deleted and the number of remaining tasks.
     *
     * @param task deleted task
     * @param remainingTasks number of tasks remaining
     */
    public void showDeletedTask(Task task, int remainingTasks) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println(
                "Now you have " + remainingTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays a newly added task.
     *
     * @param task task that was added
     */
    public void showAddedTask(Task task) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }
}
