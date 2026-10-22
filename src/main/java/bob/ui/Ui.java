package bob.ui;

import bob.task.Task;
import bob.task.TaskList;

import java.util.Scanner;

/**
 * Handles console input and messages for the B.O.B. task manager.
 */
public class Ui implements AutoCloseable {
    private static final String DIVIDER = "____________________________________________________________\n";
    private final Scanner input = new Scanner(System.in);

    /**
     * Returns whether another command is available, including redirected input.
     */
    public boolean hasNextCommand() {
        return input.hasNextLine();
    }

    /**
     * Reads the next command and removes surrounding whitespace.
     */
    public String readCommand() {
        return input.nextLine().trim();
    }

    /**
     * Shows the application banner and greeting.
     */
    public void showWelcome() {
        String banner = " ____     ___    ____  \n"
                + "| |_) )  / _ \\  | |_) ) \n"
                + "|  _ \\  | | | | |  _ \\ \n"
                + "| |_) | | |_| | | |_) |\n"
                + "|____/   \\___/  |____/ \n";
        System.out.println(DIVIDER + banner + "Hello! I'm B.O.B. (Best OpenAI Bot)\n"
                + "What can I do for you?\n" + DIVIDER);
    }

    /**
     * Shows the farewell message.
     */
    public void showGoodbye() {
        System.out.println(DIVIDER + "Bye. Hope to see you again soon!\n" + DIVIDER);
    }

    /**
     * Shows the occupied entries in the task list, numbered from one.
     */
    public void showTaskList(TaskList tasks) {
        showTaskList(tasks, "Here are the tasks in your list:");
    }

    /**
     * Shows matching tasks numbered from one, or an empty result under the heading.
     */
    public void showMatchingTasks(TaskList tasks) {
        showTaskList(tasks, "Here are the matching tasks in your list:");
    }

    /**
     * Displays a numbered task list between divider lines using the supplied heading.
     */
    private void showTaskList(TaskList tasks, String heading) {
        System.out.println(DIVIDER + heading);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(DIVIDER);
    }

    /**
     * Shows the added task and updated task count.
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(DIVIDER + "Got it. I've added this task:\n"
                + task + "\nNow you have " + taskCount + " tasks in the list.\n" + DIVIDER);
    }

    /**
     * Shows the task after its completion status changes.
     */
    public void showTaskStatus(Task task) {
        String status = task.isDone() ? "done" : "not done";
        System.out.println(DIVIDER + "Ok, I've marked this task as " + status + ":\n"
                + task + "\n" + DIVIDER);
    }

    /**
     * Shows the removed task and remaining task count.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(DIVIDER + "Noted. I've removed this task: " + "\n  " + task
                + "\nNow you have " + taskCount + " tasks in the list\n" + DIVIDER);
    }

    /**
     * Shows that the task list has reached its capacity.
     */
    public void showListFull() {
        System.out.println(DIVIDER + "Your task list is full.\n" + DIVIDER);
    }

    /**
     * Shows an error message supplied by a command.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Closes the input scanner and its underlying standard input stream.
     */
    @Override
    public void close() {
        input.close();
    }
}
