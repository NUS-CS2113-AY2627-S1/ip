package nico;

import nico.tasks.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Handles all interaction with the user: reading commands and printing responses.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " _   _  ___ ____ ___  \n"
            + "| \\ | |/ _ \\___ \\__ \\ \n"
            + "|  \\| | | |__) | ) |\n"
            + "| |\\  | |_| / __/ / / \n"
            + "|_| \\_|\\___/_____|___|\n";
    private static final String ERROR_PREFIX = "OOPS!!! ";
    private static final String EXIT_COMMAND = "bye";
    private static final DateTimeFormatter HEADING_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private final Scanner scanner;

    /**
     * Creates a Ui that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command typed by the user.
     * If there is no more input (e.g. input was piped in and has ended),
     * the exit command is returned so the program can close cleanly.
     *
     * @return The command entered by the user.
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return EXIT_COMMAND;
        }
        return scanner.nextLine();
    }

    /**
     * Prints the welcome banner and greeting.
     */
    public void showGreeting() {
        showDivider();
        System.out.println(BANNER);
        System.out.println("Hello! I'm NICO.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /**
     * Prints the goodbye message.
     */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints each given line of text on its own line.
     *
     * @param lines The lines to print.
     */
    public void showMessage(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }

    /**
     * Prints an error message, prefixed so the user can tell it apart from normal output.
     *
     * @param message The error message to show.
     */
    public void showError(String message) {
        System.out.println(ERROR_PREFIX + message);
    }

    /**
     * Prints every task in the list, numbered from 1.
     *
     * @param taskList The tasks to print.
     */
    public void showTaskList(TaskList taskList) {
        if (taskList.isEmpty()) {
            System.out.println("Your task list is empty.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        printNumbered(taskList.getAllTasks());
    }

    /**
     * Prints the tasks that matched a search, numbered from 1.
     *
     * @param matchingTasks The tasks that matched the search.
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        printNumbered(matchingTasks);
    }

    /**
     * Prints the deadlines and events on a given date, numbered from 1.
     *
     * @param date The date that was looked up.
     * @param tasksOnDate The tasks that fall on that date.
     */
    public void showTasksOn(LocalDate date, List<Task> tasksOnDate) {
        String dateText = date.format(HEADING_DATE_FORMAT);
        if (tasksOnDate.isEmpty()) {
            System.out.println("Nothing is due or happening on " + dateText + ".");
            return;
        }
        System.out.println("Here are your deadlines and events on " + dateText + ":");
        printNumbered(tasksOnDate);
    }

    private void printNumbered(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }
}