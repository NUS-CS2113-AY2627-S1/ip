package jody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads console commands and displays task-manager messages and results.
 */
public class Ui implements AutoCloseable {
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String BANNER = "     _           _       \n"
            + "    | | ___   __| |_   _ \n"
            + " _  | |/ _ \\ / _` | | | |\n"
            + "| |_| | (_) | (_| | |_| |\n"
            + " \\___/ \\___/ \\__,_|\\__, |\n"
            + "                   |___/ \n";

    private final Scanner input = new Scanner(System.in);

    /**
     * Checks whether another input line is available, waiting for input if necessary.
     *
     * @return {@code true} if another command line is available
     */
    public boolean hasNextCommand() {
        return input.hasNextLine();
    }

    /**
     * Reads the next command and removes leading and trailing whitespace.
     *
     * @return the trimmed command line
     * @throws java.util.NoSuchElementException if no input line is available
     */
    public String readCommand() {
        return input.nextLine().trim();
    }

    /**
     * Prints the divider used around UI messages.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints the Jody banner, greeting, and command prompt.
     */
    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        System.out.println("    Hello! I'm Jody.");
        System.out.println("    What can I do for you?");
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Prints the farewell message.
     */
    public void showGoodbye() {
        showLine();
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Prints a user-facing error message.
     *
     * @param message the explanation of the error
     */
    public void showError(String message) {
        showLine();
        System.out.println("    Oops! " + message);
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays the added task and the resulting task count.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after adding
     */
    public void showAddedTask(Task task, int taskCount) {
        showLine();
        System.out.println("    Got it. I've added this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays the deleted task and the remaining task count.
     *
     * @param task the task that was removed
     * @param taskCount the number of tasks remaining
     */
    public void showDeletedTask(Task task, int taskCount) {
        showLine();
        System.out.println("    Noted. I've removed this task:");
        System.out.println("      " + task);
        System.out.println("    Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays the requested number of tasks with the default list heading.
     *
     * @param tasks the tasks to display
     * @param taskCount the number to display, from zero to the list size
     */
    public void showTasks(List<Task> tasks, int taskCount) {
        showTasks(tasks, taskCount, true);
    }

    /**
     * Displays tasks numbered from one using a regular or search-results heading.
     *
     * @param tasks the tasks to display
     * @param taskCount the number to display, from zero to the list size
     * @param useDefaultHeader {@code true} for the regular heading,
     *         or {@code false} for the matching-tasks heading
     */
    public void showTasks(List<Task> tasks, int taskCount, boolean useDefaultHeader) {
        showLine();

        if  (useDefaultHeader) {
            System.out.println("    Here are the tasks in your list:");
        } else {
            System.out.println("    Here are the matching tasks in your list:");
        }

        for (int i = 0; i < taskCount; i++) {
            System.out.println("    " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays confirmation that a task is completed.
     *
     * @param task the completed task
     */
    public void showMarkedTask(Task task) {
        showLine();
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("      " + task);
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays confirmation that a task is incomplete.
     *
     * @param task the incomplete task
     */
    public void showUnmarkedTask(Task task) {
        showLine();
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("      " + task);
        System.out.println(DIVIDER + "\n");
    }

    /**
     * Displays selected tasks for a date using their original one-based task numbers.
     *
     * @param date the date used to select tasks
     * @param tasks the full task list
     * @param indices valid zero-based indices to display in the supplied order
     */
    public void showTasksOnDate(LocalDate date, List<Task> tasks,
                                List<Integer> indices) {
        showLine();
        System.out.println("    Tasks on " + date + ":");

        if (indices.isEmpty()) {
            System.out.println("    No deadlines or events on this date.");
        }

        for (int index : indices) {
            System.out.println("    " + (index + 1) + "." + tasks.get(index));
        }

        System.out.println(DIVIDER + "\n");
    }

    /**
     * Closes the command scanner and its underlying standard input stream.
     */
    @Override
    public void close() {
        input.close();
    }

    /**
     * Displays tasks whose descriptions contain the given case-sensitive substring.
     * An empty substring matches all tasks; results are numbered from one.
     *
     * @param description the substring to search for
     * @param taskList the tasks to search
     */
    public void findInDescription(String description, ArrayList<Task> taskList) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task: taskList){
            if (task.getDescription().contains(description)){
                tasks.add(task);
            }
        }
        showTasks(tasks, tasks.size(), false);
    }
}
