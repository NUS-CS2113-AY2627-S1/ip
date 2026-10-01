package Bastion;

import java.util.Scanner;
import java.util.List;

/**
 * Handles all console input and output for the Bastion application.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________________________________";
    private final Scanner scanner;

    /** Creates a UI that reads commands from the standard input stream. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Bastion's greeting. */
    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Bastion.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /** Returns whether another command is available from the user. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and trims the user's next command. */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /** Displays the farewell message. */
    public void showGoodbye() {
        System.out.println("Beep Beep! Hope to see you again soon!");
    }

    /** Displays a divider line between user-facing messages. */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /** Displays an error message between divider lines. */
    public void showError(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /** Displays all tasks, numbered from one for the user. */
    public void showTaskList(TaskList tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println("Your task list is empty.");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int index = 0; index < tasks.size(); index++) {
                System.out.println((index + 1) + "." + tasks.get(index));
            }
        }
        showLine();
    }

    /** Displays tasks whose descriptions matched a user search. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        showLine();
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int index = 0; index < matchingTasks.size(); index++) {
                System.out.println((index + 1) + "." + matchingTasks.get(index));
            }
        }
        showLine();
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println("Beep Beep! I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /** Displays confirmation that a task's completion status changed. */
    public void showTaskStatusChanged(Task task, boolean isDone) {
        showLine();
        String status = isDone ? "done:" : "not done yet:";
        System.out.println("Beep Beep! I've marked this task as " + status);
        System.out.println("  " + task);
        showLine();
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskRemoved(Task task, int taskCount) {
        showLine();
        System.out.println("Beep Beep! I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        showLine();
    }
}
