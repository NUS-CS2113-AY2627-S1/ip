package chatbot;

import java.util.List;
import java.util.Scanner;

import chatbot.task.Task;

/** Handles console input and output for the AgentCT chatbot. */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER =
            "    _                    _    ____ _____\n"
                    + "   / \\   __ _  ___ _ __ | |_ / ___|_   _|\n"
                    + "  / _ \\ / _` |/ _ \\ '_ \\| __| |     | |\n"
                    + " / ___ \\ (_| |  __/ | | | |_  |___  | |\n"
                    + "/_/   \\_\\__, |\\___|_| |_|\\__|\\____| |_|\n"
                    + "        |___/";

    /** Displays the chatbot welcome message. */
    public void showWelcome() {
        showSeparator();
        System.out.println(BANNER);
        showSeparator();
        System.out.println("Welcome! I'm AgentCT.");
        System.out.println("How may I help you?");
        showSeparator();
    }

    /** Reads the next command from the console.
     *
     * @param scanner source of user input
     * @return the next command, or {@code null} when input is exhausted
     */
    public String readCommand(Scanner scanner) {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Displays the chatbot goodbye message. */
    public void showGoodbye() {
        showSeparator();
        System.out.println("     Goodbye! Hope you have an amazing day!");
        showSeparator();
    }

    /** Displays a separator between chatbot responses. */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /** Displays an error message with chatbot indentation. */
    public void showError(String message) {
        for (String line : message.split("\\R")) {
            System.out.println("     " + line);
        }
    }

    /** Displays all tasks in their numbered format. */
    public void showTaskList(List<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        showTasks(tasks);
    }

    /** Displays tasks matching a search keyword. */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("     Here are the matching tasks in your list:");
        showTasks(tasks);
    }

    /** Displays tasks in numbered format.
     *
     * @param tasks tasks to display
     */
    private void showTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println("     " + (i + 1) + ".[" + task.getTaskType() + "]["
                    + task.getStatusIcon() + "] " + task.getDisplayText());
        }
    }

    /** Displays confirmation after adding a task.
     *
     * @param task task that was added
     * @param taskCount number of tasks after adding
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       [" + task.getTaskType() + "][ ] " + task.getDisplayText());
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays confirmation after marking a task as done.
     *
     * @param task task that was marked as done
     */
    public void showTaskMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       [X] " + task.getDescription());
    }

    /** Displays confirmation after marking a task as not done.
     *
     * @param task task that was marked as not done
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       [ ] " + task.getDescription());
    }

    /** Displays confirmation after deleting a task.
     *
     * @param task task that was deleted
     * @param taskCount number of tasks after deletion
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       [" + task.getTaskType() + "][" + task.getStatusIcon()
                + "] " + task.getDisplayText());
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }
}
