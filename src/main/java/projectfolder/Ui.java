package projectfolder;

import java.util.Scanner;

/**
 * Handles interactions between DINO and the user.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a user interface for DINO.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays DINO's greeting.
     */
    public void showGreeting() {
        System.out.println(" ____    ___   _   _    ___  ");
        System.out.println("|  _ \\  |_ _| | \\ | |  / _ \\ ");
        System.out.println("| | | |  | |  |  \\| | | | | |");
        System.out.println("| |_| |  | |  | |\\  | | |_| |");
        System.out.println("|____/  |___| |_| \\_|  \\___/ ");
        System.out.println();
        System.out.println("Hello! I'm DINO");
        System.out.println("What can I do for you?");
        System.out.println();
    }

    /**
     * Displays DINO's goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the tasks in the task list.
     *
     * @param tasks task list to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays a newly added task.
     *
     * @param task added task
     * @param taskCount number of tasks after adding the task
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    /**
     * Displays a task that has been marked as done.
     *
     * @param task marked task
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Displays a task that has been marked as not done.
     *
     * @param task unmarked task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Displays a deleted task and the remaining number of tasks.
     *
     * @param task deleted task
     * @param taskCount number of remaining tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println(
                "Now you have " + taskCount + " tasks in the list."
        );
    }

    /**
     * Closes the user interface.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays tasks that match a search keyword.
     *
     * @param tasks matching tasks to display
     */
    public void showMatchingTasks(TaskList tasks) {
        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }
}