package moistbot.ui;

import moistbot.task.Task;
import moistbot.task.TaskManager;
import moistbot.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles console input, output, and formatting for the task manager application.
 * Provides methods to read commands and display task lists and confirmations
 * with formatted output using visual dividers for better readability.
 */
public final class UserInterface implements AutoCloseable {
    private static final String BANNER = " __  __   ___   ___ ____ _____ ____   ___ _____\n"
            + "|  \\/  | / _ \\ |_ _|/ ___|_   _| __ ) / _ \\|_   _|\n"
            + "| |\\/| || | | | | | \\___ \\ | | |  _ \\| | | | | |\n"
            + "| |  | || |_| | | |  ___) || | | |_) | |_| | | |\n"
            + "|_|  |_| \\___/ |___||____/ |_| |____/ \\___/  |_|";
    private static final String DIVIDER = "____________________________________________________________";

    /** Reads commands from the console for the lifetime of this user interface. */
    private final Scanner inputScanner;

    /**
     * Creates a user interface connected to the process console.
     */
    public UserInterface() {
        inputScanner = new Scanner(System.in);
    }

    /**
     * Returns whether another console command is available to read.
     *
     * @return True if another command can be read
     */
    public boolean hasNextCommand() {
        return inputScanner.hasNextLine();
    }

    /**
     * Reads the next command entered through the console.
     *
     * @return The next command line
     */
    public String readCommand() {
        return inputScanner.nextLine();
    }

    /**
     * Displays the welcome message with the application banner and greeting.
     * Called when the application starts to introduce the user to MoistBot.
     */
    public void printWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Good day. I am MoistBot, at your service.");
        System.out.println("How may I assist you today?");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the exit message when the user quits the application.
     */
    public void printExit() {
        printMessage("Thank you for using MoistBot. Have a pleasant day.");
    }

    /**
     * Displays a confirmation message when a task is successfully added to the list.
     * Shows the added task and the updated task count.
     *
     * @param task The task that was added
     * @param size The updated total number of tasks in the list
     */
    public void printAddTask(Task task, int size) {
        String header = "Certainly. I have added this task:";
        printMessage(header + "\n" + formatModifyTask(task, size));
    }

    /**
     * Displays a confirmation message when a task is marked as complete.
     *
     * @param task The task that was marked as complete
     */
    public void printMarkTask(Task task) {
        String header = "Certainly. I have marked this task as complete:";
        printMessage(header + "\n" + formatTaskDetails(task));
    }

    /**
     * Displays a confirmation message when a task is marked as incomplete.
     *
     * @param task The task that was marked as incomplete
     */
    public void printUnmarkTask(Task task) {
        String header = "Certainly. I have marked this task as incomplete:";
        printMessage(header + "\n" + formatTaskDetails(task));
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task The task that was deleted
     * @param size The updated total number of tasks in the list
     */
    public void printDeleteTask(Task task, int size) {
        String header = "Certainly. I have deleted this task:";
        printMessage(header + "\n" + formatModifyTask(task, size));
    }

    /**
     * Formats the output for task modification operations.
     * Combines the formatted task details with the updated task count.
     *
     * @param task The task that was modified
     * @param size The updated total number of tasks in the list
     * @return A formatted string with task details and task count
     */
    public String formatModifyTask(Task task, int size) {
        String taskNoun = size == 1 ? "task" : "tasks";
        String ending = "Your list now contains " + size + " " + taskNoun + ".";
        return formatTaskDetails(task) + "\n" + ending;
    }

    /**
     * Displays a message surrounded by visual dividers for better readability.
     *
     * @param message The message to display
     */
    public void printMessage(String message) {
        System.out.println(DIVIDER);
        System.out.println(message);
        System.out.println(DIVIDER);
    }

    /**
     * Displays all tasks in the list with 1-based indexing.
     * Retrieves tasks individually from the supplied {@link TaskManager} so that the manager's
     * underlying storage remains encapsulated.
     *
     * @param taskManager The task list to display
     */
    public void printTasks(TaskManager taskManager) {
        System.out.println(DIVIDER);
        System.out.println("Certainly. Here is your task list:");

        int size = taskManager.getSize();
        if (size == 0) {
            System.out.println("Your task list is presently empty. You may use: bye, list, todo, deadline, event, "
                    + "find, mark, unmark, or delete.");
        }
        for (int i = 0; i < size; i++) {
            System.out.print((i + 1) + ".");
            System.out.println(formatTaskDetails(taskManager.getTask(i + 1)));
        }

        System.out.println(DIVIDER);
    }

    /**
     * Displays tasks whose descriptions contain the supplied search term.
     * Matching tasks retain their original list numbers so that follow-up task commands target the same tasks.
     *
     * @param taskManager The task list to search
     * @param searchTerm The text that matching descriptions must contain
     */
    public void printMatchingTasks(TaskManager taskManager, String searchTerm) {
        System.out.println(DIVIDER);
        System.out.println("Certainly. Here are the matching tasks in your list:");

        boolean hasMatchingTask = false;
        for (int taskNumber = 1; taskNumber <= taskManager.getSize(); taskNumber++) {
            Task task = taskManager.getTask(taskNumber);
            if (task.getDescription().contains(searchTerm)) {
                hasMatchingTask = true;
                System.out.println(taskNumber + "." + formatTaskDetails(task));
            }
        }
        if (!hasMatchingTask) {
            System.out.println("My apologies, but no task descriptions contain '" + searchTerm
                    + "'. Please try another search term.");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Displays deadlines due and events starting on or before an inclusive date.
     * Original task numbers are retained so subsequent task commands remain intuitive.
     *
     * @param taskManager The task list to filter and display
     * @param cutoffDate The inclusive date limit
     */
    public void printTasksOnOrBefore(TaskManager taskManager, LocalDate cutoffDate) {
        String formattedDate = DateTimeUtil.formatForDisplay(cutoffDate, null);
        System.out.println(DIVIDER);
        System.out.println("Certainly. Here are your deadlines and events on or before " + formattedDate + ":");

        boolean hasMatchingTask = false;
        for (int taskNumber = 1; taskNumber <= taskManager.getSize(); taskNumber++) {
            Task task = taskManager.getTask(taskNumber);
            if (task.isDatedOnOrBefore(cutoffDate)) {
                System.out.println(taskNumber + "." + formatTaskDetails(task));
                hasMatchingTask = true;
            }
        }
        if (!hasMatchingTask) {
            System.out.println("There are no deadlines or events on or before " + formattedDate
                    + ". Please enter another date or use 'list' to view all tasks.");
        }
        System.out.println(DIVIDER);
    }

    /**
     * Formats task details for display.
     * Shows the task type in [T/D/E], completion status as [X] for complete or [ ] for incomplete,
     * followed by the task description (which may include additional time information for deadlines and events).
     *
     * @param task The task to format
     * @return A formatted string representation of the task
     */
    public String formatTaskDetails(Task task) {
        char cross = task.isCompleted() ? 'X' : ' ';
        char taskType = task.getTaskType();
        return "[" + taskType + "][" + cross + "] " + task;
    }

    /**
     * Releases the console input scanner when the application finishes.
     */
    @Override
    public void close() {
        inputScanner.close();
    }
}
