package chillguy.ui;

import java.util.List;
import java.util.Scanner;

import chillguy.task.Task;

/**
 * Reads console input and presents the chatbot's messages and task lists.
 */
public class Ui {
    private static final int SEPARATOR_LENGTH = 60;
    private static final String SEPARATOR = "_".repeat(SEPARATOR_LENGTH);
    private static final String BANNER = """
               _____ _   _ ___ _     _      _____ _   _ __   __
              / ____| | | |_ _| |   | |    / ____| | | |\\ \\ / /
             | |    | |_| || || |   | |   | |  __| | | | \\ V /
             | |___ |  _  || || |___| |___| | |_ | |_| |  | |
              \\____||_| |_|___|_____|______\\_____|____/   |_|\
            """;

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Returns whether another command is available, including when input is redirected.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command line for the parser to interpret.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the boundary between command responses.
     */
    public void showSeparator() {
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the banner and welcome message.
     */
    public void showGreeting() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Chillguy.");
        System.out.println("What can I do for you?");
        System.out.println(SEPARATOR);
    }

    /**
     * Prints the farewell and closes its response boundary.
     */
    public void showExitMessage() {
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }

    /**
     * Displays tasks in their stored order with one-based numbers.
     */
    public void showTasks(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /**
     * Displays search results numbered from one, or explains that no tasks match.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        showNumberedTasks(tasks);
    }

    /**
     * Prints tasks in the supplied order with consecutive numbers starting at one.
     */
    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Confirms a successfully saved addition and displays the new task count.
     */
    public void showAddedTask(Task task, int count) {
        System.out.println("Got it. I've added this task:");
        showTaskAndCount(task, count);
    }

    /**
     * Confirms a successfully saved deletion and displays the remaining task count.
     */
    public void showDeletedTask(Task task, int count) {
        System.out.println("Noted. I've removed this task:");
        showTaskAndCount(task, count);
    }

    /**
     * Prints the affected task and the total task count, choosing the singular or plural label.
     */
    private void showTaskAndCount(Task task, int count) {
        System.out.println("  " + task);
        String countLabel = count == 1 ? "1 task" : count + " tasks";
        System.out.println("Now you have " + countLabel + " in the list.");
    }

    /**
     * Confirms the requested completion status after the task operation succeeds.
     */
    public void showTaskStatus(Task task) {
        System.out.println(task.isDone() ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Explains why startup stopped and reassures the user that saved data is untouched.
     */
    public void showLoadingError(String message) {
        showError(message);
        System.out.println("Your saved file has not been changed.");
        showSeparator();
    }

    /**
     * Displays a recoverable input or storage error.
     */
    public void showError(String message) {
        System.out.println(message);
    }
}
