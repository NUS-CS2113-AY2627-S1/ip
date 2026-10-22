package duke;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading input and printing
 * messages to the console.
 */
public class Ui {
    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }
    /** Reads and returns the next line of user input. */
    public String readCommand() {
        return sc.nextLine();
    }
    /** Closes the input scanner. */
    public void close() {
        sc.close();
    }
    /** Prints a greeting message to the user. */
    public void showGreeting() {
        System.out.println("Hello! I'm Pratbot.\nWhat can I do for you?");
    }
    /** Prints a farewell message to the user. */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }
    /** Prints the numbered list of tasks currently in the task list. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }
    /** Prints a message indicating that a task's done status has changed. */
    public void showStatusChange(String heading, Task t) {
        System.out.println(heading);
        System.out.println("  " + t);
    }
    /** Prints a message indicating that a task has been added. */
    public void showAddedTask(Task t, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }
    /** Prints a message indicating that a task has been deleted. */
    public void showDeletedTask(Task t, int totalTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }
    /** Prints an error message to the user. */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }
    /** Prints an error message when the saved tasks cannot be loaded. */
    public void showLoadingError() {
        System.out.println("OOPS!!! Could not load saved tasks; starting with an empty list.");
    }
    /** Prints the numbered list of matching tasks found by a search. */
    public void showMatchingTasks(ArrayList<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }
}