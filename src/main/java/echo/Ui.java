package echo;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user: printing messages and reading input.
 * Responses to commands are printed without divider lines; the caller frames
 * each response with {@link #showLine()}.
 */
public class Ui {
    /** Divider printed above and below each response. */
    private static final String LINE = "____________________________________________________________";
    /** ASCII-art logo shown in the greeting. */
    private static final String BANNER = " _____ ____ _   _  ___  \n"
            + "| ____/ ___| | | |/ _ \\ \n"
            + "|  _|| |   | |_| | | | |\n"
            + "| |__| |___|  _  | |_| |\n"
            + "|_____\\____|_| |_|\\___/ \n";
    /** The chatbot's name, used in the greeting. */
    private static final String NAME = "Echo";

    /** Reads the user's commands from standard input. */
    private final Scanner scanner = new Scanner(System.in);

    /** Creates a Ui that reads commands from standard input and prints to standard output. */
    public Ui() {
    }

    /** Prints the greeting banner and the welcome message, framed by divider lines. */
    public void showGreeting() {
        showLine();
        showMessage(BANNER + System.lineSeparator()
                + "Hello! I'm " + NAME + "." + System.lineSeparator()
                + "What can I do for you?");
        showLine();
    }

    /** Prints a divider line. */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Tells the user, framed by divider lines, that saved tasks could not be
     * loaded and the chatbot is starting with an empty list.
     *
     * @param reason why loading failed.
     */
    public void showLoadingError(String reason) {
        showLine();
        showMessage("OOPS!!! " + reason + " Starting with an empty list.");
        showLine();
    }

    /** Prints the farewell message. */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Reads one line of user input.
     *
     * @return the line read, or {@code null} if there is no more input.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Prints an error message, prefixed with {@code OOPS!!! }.
     *
     * @param errorMessage what went wrong.
     */
    public void showError(String errorMessage) {
        showMessage("OOPS!!! " + errorMessage);
    }

    /**
     * Prints every task in the list, numbered, in response to the {@code list} command.
     *
     * @param tasks the tasks to display, in order.
     */
    public void showTaskList(List<Task> tasks) {
        showNumberedTasks("Here are the tasks in your list:", tasks);
    }

    /**
     * Prints the tasks found by the {@code find} command, numbered from 1.
     *
     * @param matches the matching tasks, in list order.
     */
    public void showMatchingTasks(List<Task> matches) {
        showNumberedTasks("Here are the matching tasks in your list:", matches);
    }

    /**
     * Prints the tasks found by the {@code on} command, numbered from 1.
     *
     * @param date  the date that was checked.
     * @param tasks the tasks falling on that date, in list order.
     */
    public void showTasksOn(LocalDate date, List<Task> tasks) {
        showNumberedTasks("Here are the tasks on " + Dates.format(date) + ":", tasks);
    }

    /**
     * Prints a heading followed by the given tasks, one per line, numbered from 1.
     *
     * @param heading the line to print above the tasks.
     * @param tasks   the tasks to print, in order.
     */
    private void showNumberedTasks(String heading, List<Task> tasks) {
        StringBuilder text = new StringBuilder(heading);
        for (int i = 0; i < tasks.size(); i++) {
            text.append(System.lineSeparator())
                    .append(i + 1).append(".").append(tasks.get(i));
        }
        showMessage(text.toString());
    }

    /**
     * Confirms that a task was added.
     *
     * @param task       the task that was added.
     * @param totalTasks how many tasks are now in the list.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        showMessage("Got it. I've added this task:" + System.lineSeparator()
                + "  " + task + System.lineSeparator()
                + "Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Confirms that a task was removed.
     *
     * @param task       the task that was removed.
     * @param totalTasks how many tasks remain in the list.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        showMessage("Noted. I've removed this task:" + System.lineSeparator()
                + "  " + task + System.lineSeparator()
                + "Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Confirms that a task's done status changed.
     *
     * @param task   the task after the change.
     * @param isDone whether it was just marked done ({@code true}) or not done ({@code false}).
     */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        String heading = isDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        showMessage(heading + System.lineSeparator() + "  " + task);
    }

    /**
     * Prints a message as-is, without divider lines.
     *
     * @param message text to display.
     */
    private void showMessage(String message) {
        System.out.println(message);
    }
}
