package vani;

import java.util.List;
import java.util.Scanner;

import vani.task.Task;

/**
 * Reads user input and displays all messages for the command-line interface.
 */
public class Ui implements AutoCloseable {
    private static final String LINE_SEPARATOR = "____________________________________________________________";
    private static final String INDENT = "     ";

    private final Scanner scanner;

    /**
     * Creates an interface that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available, including when input is redirected.
     *
     * @return true if another line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command after checking that input is available.
     *
     * @return the user's input without the line ending
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the greeting when the application starts.
     */
    public void showGreeting() {
        showLine();
        System.out.println("|-------------------------|");
        System.out.println("  \\    /   /\\   |\\  |  |");
        System.out.println("   \\  /   /__\\  | \\ |  |");
        System.out.println("    \\/    |  |  |  \\|  |");
        System.out.println("|-------------------------|");
        System.out.println("Hello! I'm Vani. >.<");
        System.out.println("What can I do for you? ._.");
        showLine();
    }

    /**
     * Displays a separator around a command's response.
     */
    public void showLine() {
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println(INDENT + "uwu Bye. Hope to see you again soon!");
    }

    /**
     * Displays all tasks using task numbers starting from 1.
     *
     * @param tasks the task list to display
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.isEmpty()) {
            System.out.println(INDENT + "Yay! You have no tasks in your list. >.<");
            return;
        }

        System.out.println(INDENT + "Here are the tasks in your list: =^-^=");
        List<Task> displayedTasks = tasks.getTasks();
        for (int i = 0; i < displayedTasks.size(); i++) {
            System.out.println(INDENT + (i + 1) + "." + displayedTasks.get(i));
        }
    }

    /**
     * Displays search results numbered from 1, or a message if there are no matches.
     *
     * @param matchingTasks the tasks whose descriptions match the search keyword
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println(INDENT + "No matching tasks found. o_O");
            return;
        }

        System.out.println(INDENT + "Here are the matching tasks in your list: =^-^=");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println(INDENT + (i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Confirms that a new task has been added and saved.
     *
     * @param taskType the name of the task type, such as "todo"
     * @param task the task that was added
     * @param taskCount the number of tasks after adding it
     */
    public void showTaskAdded(String taskType, Task task, int taskCount) {
        System.out.println(INDENT + "Got it. I've added this " + taskType + ": ^-^");
        System.out.println(INDENT + "   " + task);
        System.out.println(INDENT + "Now you have " + taskCount + " tasks in the list. x_x");
    }

    /**
     * Confirms that a task has been marked as completed.
     *
     * @param taskNumber the task's displayed number
     * @param task the task that was marked
     */
    public void showTaskMarked(int taskNumber, Task task) {
        System.out.println(INDENT + "Nice! I've marked this task as done: <3");
        System.out.println(INDENT + "  " + taskNumber + "." + task);
    }

    /**
     * Confirms that a task has been marked as incomplete.
     *
     * @param taskNumber the task's displayed number
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(int taskNumber, Task task) {
        System.out.println(INDENT + "OK, I've marked this task as not done yet: -.-");
        System.out.println(INDENT + "  " + taskNumber + "." + task);
    }

    /**
     * Confirms that a task has been deleted.
     *
     * @param taskNumber the task's number before deletion
     * @param task the removed task
     */
    public void showTaskDeleted(int taskNumber, Task task) {
        System.out.println(INDENT + "OK, I've deleted this task from the list: -.-");
        System.out.println(INDENT + "  " + taskNumber + "." + task);
    }

    /**
     * Displays an input or storage error.
     *
     * @param message the explanation of the error
     */
    public void showError(String message) {
        System.out.println(INDENT + message);
    }

    /**
     * Displays a warning for each corrupted line skipped while loading tasks.
     *
     * @param skippedLineCount the number of corrupted lines
     */
    public void showLoadingWarnings(int skippedLineCount) {
        for (int i = 0; i < skippedLineCount; i++) {
            System.out.println("Warning: Skipping corrupted line.");
        }
    }

    /**
     * Closes the scanner and its input stream when the application finishes.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
