package clanky;

import clanky.task.Task;
import clanky.task.TaskList;

import java.util.ArrayList;

/**
 * Handles all user-facing console output for Clanky,
 * keeping display formatting centralized in one place.
 */
public class UI {

    private static final String BANNER = "  ____   _          _      _   _   _  __ __   __\n"
            + " / ___| | |        / \\    | \\ | | | |/ / \\ \\ / /\n"
            + "| |     | |       / _ \\   |  \\| | | ' /   \\ V / \n"
            + "| |___  | |___   / ___ \\  | |\\  | | . \\    | |  \n"
            + " \\____| |_____| /_/   \\_\\ |_| \\_| |_|\\_\\   |_|";

    private static final String DIVIDER = "=================================================";

    /** Prints the welcome banner and greeting shown at startup. */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println(DIVIDER);
        System.out.println("Hello! I'm Clanky.");
        System.out.println("What can I do for you ?");
        System.out.println(DIVIDER);
    }

    /** Prints the farewell message shown when the user exits. */
    public void showGoodbye() {
        printBlock("Bye. Hope to see you again soon!");
    }

    /**
     * Prints an error message to the user.
     *
     * @param message the error message to display.
     */
    public void showError(String message) {
        printBlock(message);
    }

    /**
     * Prints a message indicating saved tasks failed to load.
     *
     * @param message the underlying error message.
     */
    public void showLoadError(String message) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\tCouldn't load saved tasks: " + message);
        System.out.println("\tStarting with an empty list.");
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints the full task list.
     *
     * @param tasks the tasks to display.
     */
    public void showList(TaskList tasks) {
        System.out.println("\t" + DIVIDER);
        if (tasks.isEmpty()) {
            System.out.println("\tNo Tasks Yet");
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints confirmation that a task was added.
     *
     * @param task     the task that was added.
     * @param listSize the new total number of tasks.
     */
    public void showTaskAdded(Task task, int listSize) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\tGot it. I've added this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + listSize + " task" + (listSize == 1 ? "" : "s") + " in the list.");
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints confirmation that a task was deleted.
     *
     * @param task     the task that was removed.
     * @param listSize the new total number of tasks.
     */
    public void showTaskDeleted(Task task, int listSize) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\tNoted. I've removed this task:");
        System.out.println("\t  " + task);
        System.out.println("\tNow you have " + listSize + " task" + (listSize == 1 ? "" : "s") + " in the list.");
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints confirmation that a task was marked done.
     *
     * @param task the task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\tNice! I've marked this task as done:");
        System.out.println("\t" + task);
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints confirmation that a task was marked not done.
     *
     * @param task the task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\tOK, I've marked this task as not done yet:");
        System.out.println("\t" + task);
        System.out.println("\t" + DIVIDER);
    }

    /**
     * Prints results of a find/search command.
     *
     * @param matches the tasks that matched the search keyword.
     */
    public void showFindResults(ArrayList<Task> matches) {
        System.out.println("\t" + DIVIDER);
        if (matches.isEmpty()) {
            System.out.println("\tNo matching tasks found.");
        } else {
            System.out.println("\tHere are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println("\t" + (i + 1) + ". " + matches.get(i));
            }
        }
        System.out.println("\t" + DIVIDER);
    }

    private void printBlock(String message) {
        System.out.println("\t" + DIVIDER);
        System.out.println("\t" + message);
        System.out.println("\t" + DIVIDER);
    }

}
