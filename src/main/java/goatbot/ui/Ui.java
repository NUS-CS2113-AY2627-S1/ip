package goatbot.ui;

import goatbot.task.Task;
import goatbot.task.TaskList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Handles console input and output for Goat Bot.
 */
public class Ui {
    private static final String DIVIDER = "    ____________________________________________________________";
    private static final String WELCOME_BANNER = """
            ____________________________________________________________
                    2222222      3333333
                         22            33
                         22            33
                    2222222       333333
                    22                 33
                    22                 33
                    2222222      3333333
             Hello! I'm Goat Bot.
             What can I do for you?
            ____________________________________________________________
            """;

    private static final String FAREWELL = """
            ____________________________________________________________
             Bye. Hope to see you again soon! Happy hooping :)
            ____________________________________________________________
            """;

    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from the console.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome banner.
     */
    public void showWelcome() {
        System.out.println(WELCOME_BANNER);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message.
     *
     * @param message error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays the farewell message.
     */
    public void showGoodbye() {
        System.out.println(FAREWELL);
    }

    /**
     * Displays the task that was deleted and the updated number of tasks.
     *
     * @param task deleted task
     * @param taskCounter number of tasks remaining
     */
    public void showDeletedTask(Task task, int taskCounter) {
        System.out.println(DIVIDER);
        System.out.println("     OK, I've deleted this task:");
        System.out.println(task.toString());
        System.out.println("    Now you have " + taskCounter + " tasks in your list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays all tasks in their current list order.
     *
     * @param tasks tasks to display
     */
    public void showList(TaskList tasks) {
        System.out.println(DIVIDER);
        System.out.println("     Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println("     " + i + "." + tasks.get(i - 1).toString());
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation that a task was marked as done.
     *
     * @param task task that was marked
     */
    public void showMarkedTask(Task task) {
        System.out.println(DIVIDER);
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println(task.toString());
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     *
     * @param task task that was unmarked
     */
    public void showUnmarkedTask(Task task) {
        System.out.println(DIVIDER);
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println(task.toString());
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation that a todo was added.
     *
     * @param task todo that was added
     * @param taskCounter updated number of tasks
     */
    public void showAddedTodo(Task task, int taskCounter) {
        System.out.println(DIVIDER);
        System.out.println("    added todo successfully, dont forget: \n"
                + "    " + task.getDescription());
        System.out.println("    Now you have " + taskCounter + " tasks in your list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation that a deadline was added.
     *
     * @param task deadline that was added
     * @param taskCounter updated number of tasks
     */
    public void showAddedDeadline(Task task, int taskCounter) {
        System.out.println(DIVIDER);
        System.out.println("    added deadline successfully, DO ON TIME PLS: \n" + " "
                + "    " + task.getDescription());
        System.out.println("    Now you have " + taskCounter + " tasks in your list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays confirmation that an event was added.
     *
     * @param task event that was added
     * @param taskCounter updated number of tasks
     */
    public void showAddedEvent(Task task, int taskCounter) {
        System.out.println(DIVIDER);
        System.out.println("    added event successfully, better attend: \n"
                + "    " + task.getDescription());
        System.out.println("    Now you have " + taskCounter + " tasks in your list.");
        System.out.println(DIVIDER);
    }

    /**
     * Displays tasks occurring on the specified date.
     *
     * @param date searched date
     * @param tasks matching tasks
     */
    public void showTasksOn(LocalDate date, TaskList tasks) {
        System.out.println(DIVIDER);
        System.out.println("     Tasks on "
                + date.format(DATE_DISPLAY_FORMAT) + ":");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "."
                    + tasks.get(i));
        }

        System.out.println(DIVIDER);
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks matching tasks
     */
    public void showMatchingTasks(TaskList tasks) {
        System.out.println(DIVIDER);
        System.out.println("     Here are the matching tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "."
                    + tasks.get(i));
        }

        System.out.println(DIVIDER);
    }
}
