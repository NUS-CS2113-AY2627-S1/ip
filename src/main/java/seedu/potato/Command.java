package seedu.potato;

import java.util.List;
import java.util.Scanner;

/**
 * Handles command input and displays responses to the user.
 */
public class Command {
    public static final String SEPARATOR = "____________________________________________________________";

    private final Scanner scanner;

    /**
     * Creates a command input/output handler.
     *
     * @param scanner Scanner connected to the input source.
     */
    public Command(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reports whether another command is available.
     *
     * @return {@code true} if another command can be read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next command exactly as entered.
     *
     * @return The next user command.
     */
    public String getCommand() {
        return scanner.nextLine();
    }

    /**
     * Confirms that a task was added and displays the new task count.
     *
     * @param task Task that was added.
     * @param taskCount Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int taskCount) {
        String taskNoun = taskCount == 1 ? "task" : "tasks";

        System.out.println(SEPARATOR);
        System.out.println(" Got it. I've added this task:");
        System.out.printf("   %s%n", task);
        System.out.printf(" Now you have %d %s in the list.%n", taskCount, taskNoun);
        System.out.println(SEPARATOR);
    }

    /**
     * Confirms that a task was deleted and displays the remaining task count.
     *
     * @param task Task that was deleted.
     * @param taskCount Number of tasks after the deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        String taskNoun = taskCount == 1 ? "task" : "tasks";

        System.out.println(SEPARATOR);
        System.out.println(" Noted. I've removed this task:");
        System.out.printf("   %s%n", task);
        System.out.printf(" Now you have %d %s in the list.%n", taskCount, taskNoun);
        System.out.println(SEPARATOR);
    }

    /**
     * Explains that the entered command does not have a valid structure.
     */
    public void showInvalidCommand() {
        System.out.println(SEPARATOR);
        System.out.println(" Invalid command. Please check the task description and date/time fields.");
        System.out.println(SEPARATOR);
    }

    /**
     * Displays all stored tasks with one-based numbering.
     *
     * @param tasks Tasks to display in input order.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(SEPARATOR);
        System.out.println(" Here are the tasks in your list:");
        if (tasks.size() <= 0) {
            System.out.println(" [List is currently empty]");
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf(" %d.%s%n", i + 1, tasks.get(i));
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Displays tasks found by a search with one-based numbering.
     *
     * @param foundTasks Matching tasks in their original list order.
     */
    public void showFoundTasks(List<Task> foundTasks) {
        System.out.println(SEPARATOR);
        System.out.println(" Here are the matching tasks in your list:");
        if (foundTasks.isEmpty()) {
            System.out.println(" [No matching tasks found]");
        }
        for (int i = 0; i < foundTasks.size(); i++) {
            System.out.printf(" %d.%s%n", i + 1, foundTasks.get(i));
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Confirms that a task has been marked as done.
     *
     * @param task Task that was completed.
     */
    public void showTaskMarkedAsDone(Task task) {
        System.out.println(SEPARATOR);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.printf("   %s%n", task);
        System.out.println(SEPARATOR);
    }

    /**
     * Confirms that a task has been marked as not done.
     *
     * @param task Task whose completion was reversed.
     */
    public void showTaskMarkedAsNotDone(Task task) {
        System.out.println(SEPARATOR);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.printf("   %s%n", task);
        System.out.println(SEPARATOR);
    }

    /**
     * Explains that a task command did not identify a task in the list.
     */
    public void showInvalidTaskNumber() {
        System.out.println(SEPARATOR);
        System.out.println(" Please enter the number of a task in your list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the application greeting.
     *
     * @param banner Application banner.
     */
    public void showGreeting(String banner) {
        String greeting = SEPARATOR + "\n"
                + banner
                + "Hello! I'm Potato.\n"
                + "What can I do for you?\n"
                + SEPARATOR + "\n";
        System.out.print(greeting);
    }

    /**
     * Displays the application farewell.
     */
    public void showFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(SEPARATOR);
    }
}
