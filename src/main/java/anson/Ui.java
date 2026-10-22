package anson;

import java.util.Scanner;

/**
 * Deals with all interactions with the user: reading input and printing output.
 */
public class Ui {
    private static final String LINE = "___________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads one line of input from the user.
     *
     * @return The raw line entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the horizontal separator line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the startup banner and greeting.
     */
    public void showWelcome() {
        String banner = "    _                              \n"
                + "   / \\    _ __   ___   ___   _ __  \n"
                + "  / _ \\  | '_ \\ / __| / _ \\ | '_ \\ \n"
                + " / ___ \\ | | | |\\__ \\| (_) || | | |\n"
                + "/_/   \\_\\|_| |_||___/ \\___/ |_| |_|\n";

        System.out.println(banner);
        System.out.println("Hey there, I am Anson!");
        System.out.println("How can I help?");
        showLine();
    }

    /**
     * Prints the farewell message followed by the separator line.
     */
    public void showGoodbye() {
        System.out.println("Bye, see you soon!");
        showLine();
    }

    /**
     * Prints an error message.
     *
     * @param message Description of what went wrong.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message saying the save file could not be read.
     */
    public void showLoadingError() {
        System.out.println("I couldn't read your saved tasks, so I'm starting with an empty list.");
        showLine();
    }

    /**
     * Prints a warning that some lines of the save file were unreadable.
     *
     * @param skippedCount Number of lines that were skipped.
     */
    public void showSkippedLines(int skippedCount) {
        System.out.println("Warning: skipped " + skippedCount + " unreadable line(s) in your save file. "
                + "They will be dropped the next time your tasks are saved.");
        showLine();
    }

    /**
     * Prints the confirmation shown after a task is added.
     *
     * @param task The task that was added.
     * @param taskCount Number of tasks now in the list.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it! I've added this task:");
        System.out.println("  " + task);
        System.out.println("Number of tasks in the list: " + taskCount);
    }

    /**
     * Prints the confirmation shown after a task is deleted.
     *
     * @param task The task that was removed.
     * @param taskCount Number of tasks now in the list.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints the confirmation shown after a task is marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("I have marked this task as done:");
        System.out.println(task);
    }

    /**
     * Prints the confirmation shown after a task is marked as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("I have marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Prints every task in the given list, in order.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty!");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints the tasks that matched a search, numbered from 1.
     *
     * @param matches Tasks that matched the search keyword.
     */
    public void showMatchingTasks(TaskList matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }
}