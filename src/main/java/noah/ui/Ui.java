package noah.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import noah.task.Task;

/**
 * Reads console commands and displays messages to the user.
 */
public class Ui {
    private static final String SEPARATOR =
            "\n===============================================================================\n";

    // ASCII-art banner generated using http://www.network-science.de/ascii/
    private static final String BANNER = """
            NNNNNNNN        NNNNNNNN                                  hhhhhhh
            N:::::::N       N::::::N                                  h:::::h
            N::::::::N      N::::::N                                  h:::::h
            N:::::::::N     N::::::N                                  h:::::h
            N::::::::::N    N::::::N   ooooooooooo     aaaaaaaaaaaaa   h::::h hhhhh
            N:::::::::::N   N::::::N oo:::::::::::oo   a::::::::::::a  h::::hh:::::hhh
            N:::::::N::::N  N::::::No:::::::::::::::o  aaaaaaaaa:::::a h::::::::::::::hh
            N::::::N N::::N N::::::No:::::ooooo:::::o           a::::a h:::::::hhh::::::h
            N::::::N  N::::N:::::::No::::o     o::::o    aaaaaaa:::::a h::::::h   h::::::h
            N::::::N   N:::::::::::No::::o     o::::o  aa::::::::::::a h:::::h     h:::::h
            N::::::N    N::::::::::No::::o     o::::o a::::aaaa::::::a h:::::h     h:::::h
            N::::::N     N:::::::::No::::o     o::::oa::::a    a:::::a h:::::h     h:::::h
            N::::::N      N::::::::No:::::ooooo:::::oa::::a    a:::::a h:::::h     h:::::h
            N::::::N       N:::::::No:::::::::::::::oa:::::aaaa::::::a h:::::h     h:::::h
            N::::::N        N::::::N oo:::::::::::oo  a::::::::::aa:::ah:::::h     h:::::h
            NNNNNNNN         NNNNNNN   ooooooooooo     aaaaaaaaaa  aaaahhhhhhh     hhhhhhh
            """;

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the banner and welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        showLine();
        // Greeting wording refined with OpenAI Codex.
        System.out.println("Ad astra abyssosque!");
        System.out.println("Hey, Traveler! I'm Noah, your trusted companion.");
        System.out.println("Ready for an adventure?");
        showLine();
    }

    /**
     * Reads the next line of user input without interpreting it.
     * Treats the end of input as bye so a pending confirmation can be cancelled safely.
     *
     * @return Command exactly as entered by the user, or bye if input has ended.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : "bye";
    }

    /**
     * Displays a separator between console messages.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("Farewell, Traveler!");
        System.out.println("Time to recharge. See you on the next quest!");
    }

    /**
     * Displays all tasks in order, or explains how to add the first task.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty. No quests on the board!");
            System.out.println("Ready for one? Try: todo read book");
            return;
        }
        System.out.println("Quest board, coming right up! Here are your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays search results using the same task numbers as list.
     * Shows a helpful message when no description matches the search.
     *
     * @param tasks Matching task numbers and tasks in their original order.
     */
    public void showMatchingTasks(Map<Integer, Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No matching quests this time. Try another keyword!");
            return;
        }
        System.out.println("Quest search complete! Here are your matching tasks:");
        System.out.println("Task numbers are the same as in list.");
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            System.out.println(entry.getKey() + "." + entry.getValue());
        }
    }

    /**
     * Displays the added task and the updated task count.
     *
     * @param task Task that was added.
     * @param count Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int count) {
        System.out.println("On the board! I've added this task:");
        System.out.println("  " + task);
        showTaskCount(count);
    }

    /**
     * Shows an existing duplicate and asks whether to add another copy.
     *
     * @param taskNumber One-based number of the existing task.
     * @param task Existing task, including its current completion status.
     */
    public void showDuplicateTask(int taskNumber, Task task) {
        System.out.println("Deja vu! A task with the same details is already on the board:");
        System.out.println(taskNumber + "." + task);
        System.out.println("Add another copy? Type yes/no (y/n), or use another command to cancel.");
    }

    /**
     * Confirms that the pending duplicate was not added.
     */
    public void showAdditionCancelled() {
        System.out.println("Copy cancelled. Your quest board stays as it is!");
    }

    /**
     * Reports how many tasks were cleared, or explains that the list was already empty.
     *
     * @param count Number of tasks removed by clear.
     */
    public void showTasksCleared(int count) {
        if (count == 0) {
            System.out.println("Your task list is already empty. Nothing to clear!");
            return;
        }
        String taskLabel = count == 1 ? "task" : "tasks";
        System.out.println("A fresh start! I've cleared " + count + " " + taskLabel + " from the board.");
        showTaskCount(0);
    }

    /**
     * Displays the deleted task and the updated task count.
     *
     * @param task Task that was deleted.
     * @param count Number of tasks after the deletion.
     */
    public void showTaskDeleted(Task task, int count) {
        System.out.println("Off the board! I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(count);
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task Task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("One down! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task Task that was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Back on the board! I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays an error from command processing or storage.
     *
     * @param message Explanation of the error.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Closes the input scanner and its underlying standard input stream.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays the task count with the appropriate singular or plural noun.
     *
     * @param count Number of tasks currently stored.
     */
    private void showTaskCount(int count) {
        String taskLabel = count == 1 ? "task" : "tasks";
        System.out.println("Now you have " + count + " " + taskLabel + " in the list.");
    }
}
