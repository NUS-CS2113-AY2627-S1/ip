package bigbrother.ui;

import java.util.List;
import java.util.Scanner;

import bigbrother.task.Task;

/**
 * Handles console input and output for BigBrother.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "______ _      ______           _   _\n"
            + "| ___ (_)     | ___ \\         | | | |\n"
            + "| |_/ /_  __ _| |_/ /_ __ ___ | |_| |__   ___ _ __\n"
            + "| ___ \\ |/ _` | ___ \\ '__/ _ \\| __| '_ \\ / _ \\ '__|\n"
            + "| |_/ / | (_| | |_/ / | | (_) | |_| | | |  __/ |\n"
            + "\\____/|_|\\__, \\____/|_|  \\___/ \\__|_| |_|\\___|_|\n"
            + "          __/ |\n"
            + "         |___/\n";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Checks whether another command is available to read.
     *
     * @return true if another command is available
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the complete command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome banner and greeting.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hello! I'm BigBrother watching your every move.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays a separator between responses.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays an error message.
     *
     * @param message explanation of the error
     */
    public void showError(String message) {
        System.out.println("     ERROR!!! " + message);
    }

    /**
     * Displays every task in its numbered list position.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("     Displaying list of tasks:");
        showNumberedTasks(tasks);
    }

    /**
     * Displays the tasks that match a search keyword.
     *
     * @param matchingTasks matching tasks to display
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println("     Displaying matched tasks in your list:");
        showNumberedTasks(matchingTasks);
    }

    /**
     * Displays tasks numbered according to their positions in the supplied list.
     *
     * @param tasks tasks to display
     */
    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("     I've marked this task as not done:");
        System.out.println("       " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task task that was deleted
     * @param taskCount number of remaining tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("     Understood. I've removed this task:");
        System.out.println("       " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays confirmation that a todo task was added.
     *
     * @param task task that was added
     * @param taskCount number of current tasks
     */
    public void showTodoAdded(Task task, int taskCount) {
        showTaskAdded("     Understood, Creating Task:", task, taskCount);
    }

    /**
     * Displays confirmation that a deadline task was added.
     *
     * @param task task that was added
     * @param taskCount number of current tasks
     */
    public void showDeadlineAdded(Task task, int taskCount) {
        showTaskAdded("     Understood, Creating Task with Deadline:", task, taskCount);
    }

    /**
     * Displays confirmation that an event task was added.
     *
     * @param task task that was added
     * @param taskCount number of current tasks
     */
    public void showEventAdded(Task task, int taskCount) {
        showTaskAdded("     Understood, Created Event task:", task, taskCount);
    }

    /**
     * Displays a common task-added response.
     *
     * @param heading message describing the kind of task added
     * @param task task that was added
     * @param taskCount number of current tasks
     */
    private void showTaskAdded(String heading, Task task, int taskCount) {
        System.out.println(heading);
        System.out.println("       " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays the current number of tasks.
     *
     * @param taskCount number of current tasks
     */
    private void showTaskCount(int taskCount) {
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }
}
