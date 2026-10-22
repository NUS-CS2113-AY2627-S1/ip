package yuwei;

import yuwei.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands and showing messages.
 *
 * <p>Keeping every {@code System.out.println} in this one class means the look of the
 * chatbot can be changed in one place, and the other classes never need to know
 * whether they are talking to a terminal or (later) a GUI.
 */
public class Ui {
    private static final String BOT_NAME = "YuWei";
    private static final String DIVIDER =
            "    ____________________________________________________________";
    /** Indent for message lines, so that they line up inside the dividers. */
    private static final String INDENT = "     ";
    /** Deeper indent used when a single task is shown under a message. */
    private static final String TASK_INDENT = "       ";

    private final Scanner in = new Scanner(System.in);

    /** Returns the next line typed by the user. */
    public String readCommand() {
        return in.nextLine();
    }

    /** Shows the greeting printed when the chatbot starts. */
    public void showWelcome() {
        showLine();
        showMessage("Hello! I'm " + BOT_NAME);
        showMessage("What can I do for you?");
        showLine();
        showBlankLine();
    }

    /** Shows the farewell printed when the chatbot exits. */
    public void showGoodbye() {
        showLine();
        showMessage("Bye. Hope to see you again soon!");
        showLine();
    }

    /** Shows the horizontal divider that frames each response. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Shows an empty line, used to separate one response from the next command. */
    public void showBlankLine() {
        System.out.println();
    }

    /** Shows one line of text, indented to line up inside the dividers. */
    public void showMessage(String message) {
        System.out.println(INDENT + message);
    }

    /** Shows an error message, e.g. the message of a {@link YuWeiException}. */
    public void showError(String message) {
        showMessage(message);
    }

    /** Shows every task in {@code tasks}, numbered from 1. */
    public void showTaskList(List<Task> tasks) {
        showMessage("Here are the tasks in your list:");
        showNumberedTasks(tasks);
    }

    /** Shows the tasks found by a search, numbered from 1, or says that nothing matched. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            showMessage("There are no matching tasks in your list.");
            return;
        }
        showMessage("Here are the matching tasks in your list:");
        showNumberedTasks(matchingTasks);
    }

    /** Shows the confirmation for a newly added task. */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:");
        showTask(task);
        showTaskCount(taskCount);
    }

    /** Shows the confirmation for a deleted task. */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:");
        showTask(task);
        showTaskCount(taskCount);
    }

    /** Shows the confirmation for a task that was marked as done. */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:");
        showTask(task);
    }

    /** Shows the confirmation for a task that was marked as not done. */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:");
        showTask(task);
    }

    private void showTask(Task task) {
        System.out.println(TASK_INDENT + task);
    }

    private void showTaskCount(int taskCount) {
        showMessage("Now you have " + taskCount + " tasks in the list.");
    }

    private void showNumberedTasks(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            showMessage((i + 1) + ". " + tasks.get(i));
        }
    }
}
