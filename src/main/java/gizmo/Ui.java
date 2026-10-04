package gizmo;

import gizmo.task.Task;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all interaction between Gizmo and the command-line user.
 */
public class Ui {
    private static final String LINE_SEPARATOR =
            "    ________________________________________________________________________________________\n";

    private static final String WELCOME_BANNER =
            LINE_SEPARATOR
            + "    Hello! I'm Gizmo. \n"
            + "    How may I assist you today?\n"
            + LINE_SEPARATOR;

    private static final String BYE_MESSAGE =
            LINE_SEPARATOR
            + "    Disconnecting... I'll be here whenever you need me again.\n"
            + LINE_SEPARATOR;

    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Returns whether another command can be read from standard input. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and trims one command from standard input. */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /** Displays Gizmo's welcome message. */
    public void showWelcome() {
        System.out.println(WELCOME_BANNER);
    }

    /** Displays Gizmo's goodbye message. */
    public void showGoodbye() {
        System.out.println(BYE_MESSAGE);
    }

    /** Displays a warning that saved tasks could not be loaded. */
    public void showLoadingError(GizmoException exception) {
        showWarning(exception);
    }

    /** Displays a warning from storage. */
    public void showStorageWarning(GizmoException exception) {
        showWarning(exception);
    }

    /** Displays an error caused by an invalid command. */
    public void showError(String message) {
        System.out.println(LINE_SEPARATOR + "    " + message + "\n" + LINE_SEPARATOR);
    }

    /** Displays all tasks in their one-based display order. */
    public void showTaskList(TaskList tasks) {
        System.out.print(LINE_SEPARATOR);

        if (tasks.emptyCheck()) {
            System.out.println("    wow, Such empty!");
        } else {
            for (int i = 0; i < tasks.getSize(); i++) {
                System.out.println("    " + (i + 1) + ". " + tasks.getTasks().get(i));
            }
        }
        System.out.println(LINE_SEPARATOR);
    }

    /** Displays the result of adding a task. */
    public void showTaskAdded(Task task) {
        System.out.println(
                LINE_SEPARATOR
                + "    added: \n    " + task + "\n"
                + "    to task list\n"
                + LINE_SEPARATOR);
    }

    /** Displays the result of marking a task as done. */
    public void showTaskMarked(Task task) {
        showTaskChange("    Nice, I'll be marking this as done!:\n    ", task);
    }

    /** Displays the result of marking a task as incomplete. */
    public void showTaskUnmarked(Task task) {
        showTaskChange("    marking this as undone, be sure to get back to it later!:\n    ", task);
    }

    /** Displays a message for attempting to mark an already completed task. */
    public void showAlreadyMarked() {
        showMessage("    somehow, against all odds, you've managed to mark an already marked task!");
    }

    /** Displays a message for attempting to unmark an incomplete task. */
    public void showAlreadyUnmarked() {
        showMessage("    unmarking an unmarked task won't magically delete the task :)");
    }

    /** Displays the result of deleting a task. */
    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println(
                LINE_SEPARATOR
                + "    Done. Removed task:\n"
                + "    " + task + "\n"
                + "    " + remainingTasks + " tasks remaining\n"
                + LINE_SEPARATOR);
    }

    /** Displays a message surrounded by Gizmo's standard separator. */
    private void showMessage(String message) {
        System.out.println(LINE_SEPARATOR + message + "\n" + LINE_SEPARATOR);
    }

    /** Displays a storage warning surrounded by Gizmo's standard separator. */
    private void showWarning(GizmoException exception) {
        System.out.println(
                LINE_SEPARATOR + "    Warning: " + exception.getMessage()
                + "\n" + LINE_SEPARATOR);
    }

    /** Displays a task-changing message surrounded by Gizmo's separator. */
    private void showTaskChange(String message, Task task) {
        System.out.println(message + task + "\n" + LINE_SEPARATOR);
    }

    /**
     * Displays tasks matching a keyword.
     *
     * @param matchingTasks tasks that matched the search keyword.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.print(LINE_SEPARATOR);
        System.out.println("    Here are the tasks with the specified keyword in your list:");

        if (matchingTasks.isEmpty()) {
            System.out.println("    {There is no tasks with that keyword}");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println("    " + (i + 1) + ". "
                        + matchingTasks.get(i));
            }
        }

        System.out.println(LINE_SEPARATOR);
    }
}
