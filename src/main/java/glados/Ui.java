package glados;

import glados.task.Task;
import glados.task.TaskDateTime;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands and printing replies.
 *
 * <p>Every reply is indented and printed between divider lines, so keeping all
 * printing here means that formatting is decided in one place.
 */
public class Ui {

    /** Indentation placed before every line of GLaDOS's replies. */
    private static final String INDENT = "     ";

    /** Horizontal divider that wraps each block of replies. */
    private static final String DIVIDER =
            "    ____________________________________________________________";

    /** ASCII art banner shown at startup, already indented. */
    private static final String BANNER = "        ________          ____  ____  _____\n"
            + "       / ____/ /   ____ _/ __ \\/ __ \\/ ___/\n"
            + "      / / __/ /   / __ `/ / / / / / /\\__ \\ \n"
            + "     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / \n"
            + "     \\____/_____/\\__,_/_____/\\____//____/  \n";

    /** Reads the lines the user types. */
    private final Scanner in;

    /**
     * Creates a Ui that reads commands from the keyboard and prints replies to the console.
     */
    public Ui() {
        in = new Scanner(System.in);
    }

    /**
     * Shows the banner and greeting, wrapped in divider lines.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        showMessage("Hello, I'm GLaDOS nice to... Oh, it's you.");
        showMessage("State your query. I have other tests to run.");
        showLine();
    }

    /**
     * Shows the farewell message, wrapped in divider lines.
     */
    public void showGoodbye() {
        showLine();
        showMessage("Test concluded. Try not to disappoint me next time.");
        showLine();
    }

    /**
     * Returns true if there is another line of input to read.
     * Waits until the user types a line, and returns false only once the input has
     * ended, e.g. after Ctrl+D.
     */
    public boolean hasNextCommand() {
        return in.hasNextLine();
    }

    /**
     * Returns the next line the user enters.
     * Call {@link #hasNextCommand()} first, since this fails if the input has ended.
     */
    public String readCommand() {
        return in.nextLine();
    }

    /**
     * Shows the divider line that separates blocks of replies.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Shows one line of reply, indented.
     */
    public void showMessage(String message) {
        System.out.println(INDENT + message);
    }

    /**
     * Shows an error message, e.g. explaining why a command was rejected.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Shows that the saved tasks could not be loaded, wrapped in divider lines.
     *
     * @param message explanation of what went wrong.
     */
    public void showLoadingError(String message) {
        showLine();
        showError(message);
        showLine();
    }

    /**
     * Warns that some lines of the save file were unreadable and were skipped,
     * wrapped in divider lines.
     *
     * @param corruptedLineCount how many lines were skipped.
     */
    public void showCorruptedLineWarning(int corruptedLineCount) {
        showLine();
        showMessage("Your save file is damaged. I skipped "
                + corruptedLineCount + " unreadable line(s).");
        showMessage("They will be gone for good the next time I save.");
        showLine();
    }

    /**
     * Shows every task, numbered from 1.
     */
    public void showTaskList(TaskList tasks) {
        showMessage("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /**
     * Shows the tasks that matched a search, numbered from 1, or says there are none.
     *
     * @param keyword the text searched for.
     * @param matchingTasks the tasks whose description contains the keyword.
     */
    public void showMatchingTasks(String keyword, TaskList matchingTasks) {
        if (matchingTasks.size() == 0) {
            showMessage("No tasks match \"" + keyword + "\". Perhaps it never existed.");
            return;
        }
        showMessage("Here are the matching tasks in your list:");
        showNumberedTasks(matchingTasks);
    }

    /**
     * Shows the tasks that fall on a date, numbered from 1, or says there are none.
     *
     * @param date the date asked about.
     * @param tasksOnDate the tasks that fall on that date.
     */
    public void showTasksOn(LocalDate date, TaskList tasksOnDate) {
        // Wrapping the date in a TaskDateTime shows it in the same format as task dates.
        String shownDate = new TaskDateTime(date).toString();
        if (tasksOnDate.size() == 0) {
            showMessage("You have nothing on " + shownDate + ". Enjoy it while it lasts.");
            return;
        }
        showMessage("Here are the tasks on " + shownDate + ":");
        showNumberedTasks(tasksOnDate);
    }

    /**
     * Shows that a task was added, and how many tasks the list now holds.
     *
     * @param task the task added.
     * @param taskCount how many tasks are in the list after adding it.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Shows that a task was removed, and how many tasks the list now holds.
     *
     * @param task the task removed.
     * @param taskCount how many tasks are in the list after removing it.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Shows that a task was marked as done.
     */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
    }

    /**
     * Shows that a task was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
    }

    /**
     * Shows how many tasks the list holds, after a task is added or removed.
     */
    private void showTaskCount(int taskCount) {
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows each task on its own line, numbered from 1, e.g. "1.[T][ ] read book".
     * Used by every command that lists tasks, so they all look the same.
     */
    private void showNumberedTasks(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + "." + tasks.get(i));
        }
    }
}
