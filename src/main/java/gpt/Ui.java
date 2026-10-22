package gpt;

import java.util.Scanner;

/**
 * Handles interactions between the chatbot and the user.
 */
public class Ui {
    private static final String BANNER = "  ____ ____ _____ \n"
            + " / ___|  _ \\_   _|\n"
            + "| |  _| |_) || |  \n"
            + "| |_| |  __/ | |  \n"
            + " \\____|_|    |_|  \n";
    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next command entered by the user.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the greeting shown when GPT starts.
     */
    public void showGreeting() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Beep Boop, I'm GPT.");
        System.out.println("What's cooking, I'm gonna use all your tokens today.");
        System.out.println(LINE);
        System.out.println();
    }

    /**
     * Displays a separator line.
     */
    public void showLine() {
        System.out.println(LINE);
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
     * Displays all tasks, numbered from 1.
     *
     * @param tasks tasks to display
     */
    public void showTasks(TaskList tasks) {
        System.out.println("Here are the tasks in your list: ");
        showNumberedTasks(tasks);
    }

    /**
     * Displays tasks that match a search keyword, numbered from 1.
     *
     * @param tasks matching tasks to display
     */
    public void showMatchingTasks(TaskList tasks) {
        System.out.println("Here are the matching tasks in your list:");
        showNumberedTasks(tasks);
    }

    private void showNumberedTasks(TaskList tasks) {
        int taskNumber = 1;
        for (Task task : tasks) {
            System.out.println(taskNumber + "." + task);
            taskNumber++;
        }
    }

    /**
     * Confirms that a task was added.
     *
     * @param task added task
     * @param taskCount number of tasks after the addition
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was deleted.
     *
     * @param task deleted task
     * @param taskCount number of tasks after the deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Got it. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task marked task
     */
    public void showTaskMarked(Task task) {
        System.out.println("Beep boop, task has been marked.");
        System.out.println("  " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task unmarked task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Beep boop, task has been unmarked.");
        System.out.println("  " + task);
    }

    /**
     * Displays the farewell shown when GPT exits.
     */
    public void showFarewell() {
        System.out.println("Bye. Make sure to buy more tokens");
        System.out.println(LINE);
    }
}
