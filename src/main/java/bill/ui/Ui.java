package bill.ui;

import bill.task.Task;
import bill.task.TaskList;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Reads the user's commands and presents Bill's responses in the console.
 */
public class Ui implements AutoCloseable {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";
    private static final String BANNER = " ____  _ _ _ \n"
            + "| __ )(_) | |\n"
            + "|  _ \\| | | |\n"
            + "| |_) | | | |\n"
            + "|____/|_|_|_|\n";

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Uses standard input and output for the interactive application.
     */
    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Uses the supplied streams, which also makes console behaviour testable.
     *
     * @param input Stream containing user commands.
     * @param output Stream receiving Bill's replies.
     */
    public Ui(InputStream input, PrintStream output) {
        scanner = new Scanner(input);
        this.output = output;
    }

    /**
     * Checks for another command so end-of-file exits cleanly.
     *
     * @return True if another input line is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next complete command line.
     *
     * @return The command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints Bill's banner and greeting.
     */
    public void showWelcome() {
        showLine();
        output.print(BANNER);
        output.println("Hello! I'm Bill.");
        output.println("What can I do for you?");
        output.println("Type 'help' if you'd like a tour of my commands.");
        showLine();
    }

    /**
     * Prints Bill's farewell.
     */
    public void showGoodbye() {
        output.println("Bye. Have a good day mate!");
        showLine();
    }

    /**
     * Prints a divider between command responses.
     */
    public void showLine() {
        output.println(HORIZONTAL_LINE);
    }

    /**
     * Prints a friendly explanation of an input or storage error.
     *
     * @param message Explanation to show.
     */
    public void showError(String message) {
        output.println("Hmm, I couldn't do that:");
        output.println("  " + message);
    }

    /**
     * Explains that unreadable saved tasks will not be used in this session.
     */
    public void showLoadingFallback() {
        output.println("I'll start with an empty task list for this session.");
    }

    /**
     * Displays all tasks with their one-based numbers and completion states.
     *
     * @param tasks Task list to display.
     */
    public void showTasks(TaskList tasks) {
        if (tasks.isEmpty()) {
            output.println("Your task list is empty. Add something whenever you're ready.");
            return;
        }
        output.println("Here are the tasks in your list:");
        List<Task> entries = tasks.getTasks();
        for (int i = 0; i < entries.size(); i++) {
            output.println((i + 1) + "." + entries.get(i));
        }
    }

    /**
     * Displays Bill's supported commands.
     */
    public void showHelp() {
        output.println("Here are the commands I understand:");
        output.println("  list          - show every task");
        output.println("  find KEYWORD  - search task descriptions");
        output.println("  mark NUMBER   - mark a task as done");
        output.println("  unmark NUMBER - mark a task as not done");
        output.println("  delete NUMBER - remove a task");
        output.println("  todo TASK     - add a task without a date or time");
        output.println("  deadline TASK /by DATE - add a deadline (e.g. 2026-10-02)");
        output.println("  event TASK /from START /to END - add an event");
        output.println("  stats         - show your progress");
        output.println("  bye           - exit Bill");
        output.println("  Use todo, deadline, or event to add a task.");
        output.println("  ISO dates such as 2026-10-02 display as Oct 02 2026.");
    }

    /**
     * Displays the number of completed and remaining tasks.
     *
     * @param tasks Task list to summarize.
     */
    public void showStats(TaskList tasks) {
        int completedCount = tasks.countCompleted();
        int remainingCount = tasks.size() - completedCount;
        output.println("Task stats: " + tasks.size() + " total, "
                + completedCount + " done, " + remainingCount + " remaining.");
    }

    /**
     * Displays tasks matching a search, numbered within the results only.
     *
     * @param matches Tasks whose descriptions contain the keyword.
     * @param keyword Search text to mention when nothing matches.
     */
    public void showFound(List<Task> matches, String keyword) {
        if (matches.isEmpty()) {
            output.println("No tasks match '" + keyword + "'.");
            return;
        }
        output.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            output.println((i + 1) + "." + matches.get(i));
        }
    }

    /**
     * Confirms an added task and shows the new total.
     *
     * @param task Added task.
     * @param taskCount Number of tasks after adding it.
     */
    public void showAdded(Task task, int taskCount) {
        output.println("Got it. I've added this task:");
        output.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Confirms a task was marked done.
     *
     * @param task Updated task.
     */
    public void showMarked(Task task) {
        output.println("Nice! I've marked this task as done:");
        output.println("  " + task);
    }

    /**
     * Confirms a task was marked incomplete.
     *
     * @param task Updated task.
     */
    public void showUnmarked(Task task) {
        output.println("OK, I've marked this task as not done yet:");
        output.println("  " + task);
    }

    /**
     * Confirms a deleted task and shows the new total.
     *
     * @param task Removed task.
     * @param taskCount Number of tasks after removing it.
     */
    public void showDeleted(Task task, int taskCount) {
        output.println("Noted. I've removed this task:");
        output.println("  " + task);
        showTaskCount(taskCount);
    }

    private void showTaskCount(int taskCount) {
        String taskLabel = taskCount == 1 ? "task" : "tasks";
        output.println("Now you have " + taskCount + " " + taskLabel + " in the list.");
    }

    /**
     * Releases the input scanner without closing the output stream.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
