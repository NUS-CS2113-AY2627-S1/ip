package wenwen.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import wenwen.task.Task;
import wenwen.task.TaskList;

/**
 * Handles all console input and output for Wenwen.
 */
public class Ui {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private static final String LINE = "____________________________________________________________";
    private static final String BANNER = " __        __                                  \n"
            + " \\ \\      / /__ _ ____      _____ _ __       \n"
            + "  \\ \\ /\\ / / _ \\ '_ \\ \\ /\\ / / _ \\ '_ \\      \n"
            + "   \\ V  V /  __/ | | \\ V  V /  __/ | | |     \n"
            + "    \\_/\\_/ \\___|_| |_|\\_/\\_/ \\___|_| |_|     \n";

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Creates a user interface connected to standard input and output.
     */
    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Creates a user interface connected to the supplied streams.
     *
     * @param input stream from which commands are read
     * @param output stream to which responses are written
     */
    public Ui(InputStream input, PrintStream output) {
        this.scanner = new Scanner(input);
        this.output = output;
    }

    /**
     * Returns whether another command is available.
     *
     * @return true if another input line can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command.
     *
     * @return the next command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Shows the greeting displayed when Wenwen starts.
     */
    public void showWelcome() {
        output.println(LINE);
        output.println(BANNER);
        output.println("Hello! I'm Wenwen.");
        output.println("What can I do for you?");
        output.println(LINE);
    }

    /**
     * Shows the farewell displayed when Wenwen exits.
     */
    public void showFarewell() {
        output.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Shows a divider between a command and its response.
     */
    public void showLine() {
        output.println(LINE);
    }

    /**
     * Shows every task with a one-based list number.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        output.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Shows deadline and event tasks that occur on a date.
     *
     * @param date date that was requested
     * @param tasks tasks occurring on that date
     */
    public void showTasksOnDate(LocalDate date, TaskList tasks) {
        output.println("Here are the tasks on " + date.format(DISPLAY_DATE_FORMAT) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            output.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Shows tasks whose descriptions match a search keyword.
     *
     * @param tasks matching tasks
     */
    public void showMatchingTasks(TaskList tasks) {
        output.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount current number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        output.println("Got it. I've added this task:");
        output.println("  " + task);
        showTaskCount(taskCount);
        showLine();
    }

    /**
     * Shows confirmation that a task was removed.
     *
     * @param task task that was removed
     * @param taskCount current number of tasks
     */
    public void showTaskDeleted(Task task, int taskCount) {
        output.println("Noted. I've removed this task:");
        output.println("  " + task);
        showTaskCount(taskCount);
        showLine();
    }

    /**
     * Shows confirmation that a task was marked done or not done.
     *
     * @param task task whose status changed
     * @param isDone true when the task is now complete
     */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        if (isDone) {
            output.println("Nice! I've marked this task as done:");
        } else {
            output.println("OK, I've marked this task as not done yet:");
        }
        output.println("  " + task);
        showLine();
    }

    /**
     * Shows an input or persistence error.
     *
     * @param message explanation of the error
     */
    public void showError(String message) {
        output.println("Oops! " + message);
        showLine();
    }

    /**
     * Shows a loading error while allowing Wenwen to start with an empty list.
     *
     * @param message explanation of the loading error
     */
    public void showLoadingError(String message) {
        showError(message + " Starting with an empty list instead.");
    }

    /**
     * Shows the current task count using the correct singular or plural noun.
     *
     * @param taskCount number of tasks in the list
     */
    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        output.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
