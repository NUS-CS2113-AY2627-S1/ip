package hedy.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import hedy.task.Deadline;
import hedy.task.NumberedTask;
import hedy.task.Task;

/**
 * Reads what the user types and prints Hedy's replies.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /** Creates a UI that reads from standard input. */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /** Prints the greeting shown when Hedy starts. */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Hedy");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Reads the next command.
     * A blank line comes back as an empty string. End of input is treated as {@code bye}.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return "bye";
        }
        return scanner.nextLine().trim();
    }

    /** Prints the horizontal line used around every reply. */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints a problem with a command the user just typed.
     *
     * @param message explanation already written for the user
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Prints a problem found while reading the save file, before the greeting.
     *
     * @param message explanation already written for the user
     */
    public void showLoadingError(String message) {
        System.out.println(" " + message);
    }

    /** Prints the goodbye shown for {@code bye}. */
    public void showGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
    }

    /**
     * Prints every task, numbered from 1.
     *
     * @param tasks the current list, in display order
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints the tasks that matched a search, numbered from 1 within the matches.
     *
     * @param matches tasks whose descriptions contain the keyword
     */
    public void showFound(ArrayList<Task> matches) {
        System.out.println(" Here are the matching tasks in your list:");
        if (matches.isEmpty()) {
            System.out.println(" No tasks match that keyword.");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matches.get(i));
        }
    }

    /**
     * Prints deadlines due on one date, using the same numbers as {@code list}.
     *
     * @param date the day the user asked about
     * @param matches deadlines due on that day
     */
    public void showDeadlinesOn(LocalDate date, ArrayList<NumberedTask> matches) {
        String printedDate = Deadline.formatDate(date);
        if (matches.isEmpty()) {
            System.out.println(" No deadlines are due on " + printedDate + ".");
            return;
        }
        System.out.println(" Here are the deadlines due on " + printedDate + ":");
        for (NumberedTask match : matches) {
            System.out.println(" " + match.getNumber() + "." + match.getTask());
        }
    }

    /**
     * Confirms that a task was added.
     *
     * @param task the task that was added
     * @param count how many tasks are in the list now
     */
    public void showAdded(Task task, int count) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + countText(count) + " in the list.");
    }

    /**
     * Confirms that a task was deleted.
     *
     * @param task the task that was removed
     * @param count how many tasks are left
     */
    public void showDeleted(Task task, int count) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + countText(count) + " in the list.");
    }

    /**
     * Confirms that a task was marked or unmarked.
     *
     * @param task the task after its status changed
     * @param isDone true if it was marked done
     */
    public void showMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
    }

    /** Closes the input scanner when Hedy exits. */
    public void close() {
        scanner.close();
    }

    /**
     * Returns {@code "1 task"} or {@code "2 tasks"}.
     *
     * @param count number of tasks
     */
    private String countText(int count) {
        return count + (count == 1 ? " task" : " tasks");
    }
}
