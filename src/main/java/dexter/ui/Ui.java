package dexter.ui;

import dexter.tasks.Task;

import java.util.List;

/** Handles messages displayed to the user by the Dexter application. */
public class Ui {

    /** Displays the application banner and welcome message. */
    public void showWelcome() {
        String banner = "DDDD   EEEEE  XX XX  TTTTT  EEEEE  RRRR\n"
                + "D   D  E       X X     T    E      R   R\n"
                + "D   D  EEEE     X      T    EEEE   RRRR\n"
                + "D   D  E        X      T    E      R R\n"
                + "DDDD   EEEEE   X X     T    EEEEE  R  RR\n";
        System.out.println(banner);
        System.out.println("Welcome my fellow big-brainer! What question do you have in mind?");
    }

    /** Displays the message shown when the user exits the application. */
    public void showFarewell() {
        System.out.println("See you again soon!");
    }

    /**
     * Displays a confirmation after a task is added.
     *
     * @param task the task that was added
     * @param itemCount the number of tasks currently in the list
     */
    public void showTaskAdded(Task task, int itemCount) {
        System.out.println("Alright! Added:");
        System.out.println(task);
        System.out.println("Now you have " + itemCount + " items");
    }

    /**
     * Displays a confirmation after a task is deleted.
     *
     * @param task the task that was deleted
     * @param itemCount the number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int itemCount) {
        System.out.println("Ok! Removed:");
        System.out.println(task);
        System.out.println("Now you have " + itemCount + " items");
    }

    /**
     * Displays every task in the given list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Sure! Here is your list.");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    /** Displays search results numbered from one, or a message if none match. */
    public void showMatchingTasks(List<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        if (matches.isEmpty()) {
            System.out.println("No matching tasks found.");
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }
}
