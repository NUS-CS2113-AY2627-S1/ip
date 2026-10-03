package clammy.ui;

import java.util.Scanner;

import clammy.task.Task;
import clammy.task.TaskList;

/**
 * Reads console input and displays Clammy's user-facing messages.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private static final String START_MESSAGE = "Hello! I'm Clammy.\nWhat can I do for you?";
    private final Scanner scanner;

    /**
     * Creates a UI connected to standard input.
     */
    public Ui() {
        this(new Scanner(System.in));
    }

    /**
     * Creates a UI with a supplied scanner, allowing controlled test input.
     */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Returns whether another command is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and returns the next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays Clammy's greeting.
     */
    public void showWelcome() {
        showMessage(START_MESSAGE);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Displays all tasks with their one-based task numbers.
     */
    public void showTaskList(TaskList tasks) {
        showTaskList(tasks, "Here are the tasks in your list:");
    }

    /**
     * Displays matching tasks numbered from one, or just the heading when there are no matches.
     */
    public void showMatchingTasks(TaskList tasks) {
        showTaskList(tasks, "Here are the matching tasks in your list:");
    }

    /**
     * Displays a numbered task list under the supplied heading.
     */
    private void showTaskList(TaskList tasks, String heading) {
        StringBuilder message = new StringBuilder(heading);
        int taskNumber = 1;
        for (Task task : tasks.asList()) {
            message.append("\n").append(taskNumber).append(".").append(task);
            taskNumber++;
        }
        showMessage(message.toString());
    }

    /**
     * Displays the added task and the new task count.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessage("Got it. I've added this task:\n" + task + "\n" + formatTaskCount(taskCount));
    }

    /**
     * Displays the removed task and the remaining task count.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showMessage("Noted. I've removed this task:\n" + task + "\n" + formatTaskCount(taskCount));
    }

    /**
     * Displays confirmation that a task has been marked as done.
     */
    public void showTaskMarked(Task task) {
        showMessage("Nice! I've marked this task as done:\n" + task);
    }

    /**
     * Displays confirmation that a task has been marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        showMessage("OK, I've marked this task as not done yet:\n" + task);
    }

    /**
     * Explains why loading failed and how to recover without overwriting saved data.
     */
    public void showLoadingError(String reason) {
        showError("Could not load data/clammy.txt. " + reason
                + "\nExisting data was left unchanged. Fix the file or folder and restart Clammy.");
    }

    /**
     * Warns that the current changes could not be saved.
     */
    public void showSavingError() {
        showError("Could not save data/clammy.txt. Changes are in memory only."
                + "\nCheck that the data folder is writable before changing another task.");
    }

    /**
     * Formats the list size using the appropriate singular or plural task label.
     */
    private String formatTaskCount(int taskCount) {
        String taskLabel = taskCount == 1 ? "task" : "tasks";
        return "Now you have " + taskCount + " " + taskLabel + " in the list.";
    }

    /**
     * Displays a message produced by executing a command.
     */
    public void showMessage(String message) {
        System.out.println(LINE + "\n" + message + "\n" + LINE + "\n");
    }

    /**
     * Displays an explanation of a command or storage error.
     */
    public void showError(String message) {
        showMessage(message);
    }
}
