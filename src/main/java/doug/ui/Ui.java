package doug.ui;

import java.util.ArrayList;
import java.util.Scanner;

import doug.task.Task;

/**
 * Handles console input and presents messages and task information to the user.
 */
public class Ui {
    /** Scanner used to read commands from standard input. */
    private Scanner in;

    /** Display name of the application. */
    private final String name = "Doug";
    /** Horizontal divider printed between sections of console output. */
    private final String line = "_______________________________________________________________________________________";
    /** Text-art banner displayed when the application starts. */
    private final String banner = " ____\n"
            + "|  _ \\  ___  _   _  __ _\n"
            + "| | | |/ _ \\| | | |/ _` |\n"
            + "| |_| | (_) | |_| | (_| |\n"
            + "|____/ \\___/ \\__,_|\\__, |\n"
            + "                   |___/\n";

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the complete line entered by the user
     */
    public String readInput() {
        System.out.println();
        String input = in.nextLine();
        printLine();
        return input;
    }

    /**
     * Prints indented text, indenting each line in a multiline message.
     *
     * @param text text to print
     */
    public void print(String text) {
        System.out.println("\t" + text.replace("\n", "\n\t"));
    }

    /**
     * Prints the standard horizontal divider.
     */
    public void printLine() {
        print(line);
    }

    /**
     * Prints the application banner and welcome message.
     */
    public void printWelcome() {
        printLine();
        print(banner);
        print(String.format(
                "Hey, this is %s%s Where we solve problems no one has.\n\nWhat can I do for you? Try out 'help'!",
                name, name));
        printLine();
    }

    /**
     * Prints the farewell message and a horizontal divider.
     */
    public void printGoodbye() {
        print("Peace.");
        printLine();
    }

    /**
     * Prints the supported commands and their expected formats.
     */
    public void printHelp() {
        print("Here are the list of commands");

        print("\n===== List Tasks =====");
        print("list");

        print("\n===== Adding Tasks =====");
        print(String.format("%-10s <description>", "todo"));
        print(String.format("%-10s <description> /by <date>", "deadline"));
        print(String.format("%-10s <description> /from <date> /to <date>", "event"));

        print("\n===== Marking Tasks =====");
        print(String.format("%-10s <index>", "mark"));
        print(String.format("%-10s <index>", "unmark"));

        print("\n===== Deleting Tasks =====");
        print(String.format("%-10s <index>", "delete"));

        print("\n===== Finding Tasks =====");
        print(String.format("%-10s <keyword>", "find"));

        print("\n===== Termination =====");
        print("bye");
    }

    /**
     * Prints every task with a one-based display number, or an empty-list message.
     *
     * @param taskList tasks to display
     */
    public void printTaskList(ArrayList<Task> taskList) {
        if (taskList.size() <= 0) {
            print("You haven't added anything yet. Get to it.");
        } else {
            print("Here's what's on your list:");
            for (int i = 0; i < taskList.size(); ++i) {
                print(String.format("%d.%s", i + 1, taskList.get(i)));
            }
        }
    }

    /**
     * Prints confirmation that a task was added and reports the new list size.
     *
     * @param task task that was added
     * @param taskListSize number of tasks after the addition
     */
    public void printTaskAdded(Task task, int taskListSize) {
        print(String.format("Alright your %s is in the list:\n%d.%s\nYou now have %d total tasks.",
                task.getTaskType().toString(), taskListSize, task, taskListSize));
    }

    /**
     * Prints confirmation that a task was deleted and reports the supplied task count.
     *
     * @param task task that was deleted
     * @param taskListSize task count to include in the confirmation message
     */
    public void printTaskDeleted(Task task, int taskListSize) {
        print(String.format("Sweet that's gone:\n%d.%s\nYou have %d remaining tasks.",
                taskListSize, task, taskListSize));
        if (taskListSize == 0)
            print("I guess that's it.");
    }

    /**
     * Prints confirmation that a task was marked or unmarked.
     *
     * @param task task whose completion state changed
     * @param index one-based task number to display
     * @param isMarked {@code true} if the task was marked done; {@code false} if it was unmarked
     */
    public void printTaskMarked(Task task, int index, boolean isMarked) {
        if (isMarked)
            print("I guess that's done:");
        else
            print("Oh wait I guess this isn't done yet.");
        print(String.format("%d.%s", index, task));
    }

    /**
     * Prints tasks found by a search, or a message when there are no matches.
     *
     * @param foundTasks tasks whose descriptions matched the search keyword
     */
    public void printFoundTasks(ArrayList<Task> foundTasks) {
        if (foundTasks.isEmpty()) {
            print("No tasks match that keyword.");
        } else {
            print("Here are the matching tasks in your list:");
            for (int i = 0; i < foundTasks.size(); ++i) {
                // Notice there is no \n here, respecting your previous bug fix!
                print(String.format("%d.%s", i + 1, foundTasks.get(i)));
            }
        }
    }

    /**
     * Prints an error message with the application's error prefix.
     *
     * @param errorMessage description of the error to display
     */
    public void printError(String errorMessage) {
        print("ERROR: " + errorMessage);
    }
}
