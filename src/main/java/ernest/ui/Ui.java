package ernest.ui;

import ernest.task.Task;

import java.util.List;
import java.util.Scanner;

/**
 * Handles console input and output for Ernest.
 */
public final class Ui implements AutoCloseable {
    /** Line used to separate sections of the command-line interface. */
    private static final String HORIZONTAL_LINE = "______________________________________";

    /** ASCII-art banner displayed when Ernest starts. */
    private static final String BANNER = " _____ ____  _     _  ____  ____ _____\n"
            + "| ____|  _ \\| \\   | | ____|/ ___|_   _|\n"
            + "|  _| | |_) |  \\  | |  _|  \\___\\  | |\n"
            + "| |___|  _ /| | \\ | | |___ ___) | | |\n"
            + "|_____|_| \\ |_|  \\|_|_____||____/ |_|\n";

    /** Name displayed by the chatbot. */
    private static final String CHATBOT_NAME = "Ernest";

    /** Reads commands entered through standard input. */
    private final Scanner scanner;

    /**
     * Creates a user interface connected to standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available to read.
     *
     * @return true if another command is available; otherwise false.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return the next command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows Ernest's welcome message and exit instruction.
     */
    public void showWelcomeMessage() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(BANNER);
        System.out.printf("Hi! I'm %s.%n", CHATBOT_NAME);
        System.out.println("How can I help you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println("(Type \"bye\" to exit the chat)");
    }

    /**
     * Shows Ernest's farewell message.
     */
    public void showGoodbyeMessage() {
        System.out.println("Bye. See you again soon!");
        showHorizontalLine();
    }

    /**
     * Shows a message explaining that the command is invalid.
     */
    public void showInvalidCommandMessage() {
        System.out.println("Sorry, please insert a valid command.");
    }

    /**
     * Shows an expected error to the user.
     *
     * @param message explanation of the error.
     */
    public void showErrorMessage(String message) {
        System.out.println(message);
    }

    /**
     * Shows a message explaining that a task number is outside the task list.
     */
    public void showInvalidTaskNumberMessage() {
        System.out.println("Invalid task number. Please refer to the task list and try again.");
    }

    /**
     * Shows all tasks and their completion status.
     *
     * @param tasks tasks to display in list order.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Your to-do list is:");

        showNumberedTasks(tasks);
    }

    /**
     * Shows tasks that match a find command.
     *
     * @param tasks matching tasks in list order.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");

        showNumberedTasks(tasks);
    }

    /**
     * Shows tasks as a one-based numbered list.
     *
     * @param tasks tasks to display in list order.
     */
    private void showNumberedTasks(List<Task> tasks) {

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task);
        }
    }

    /**
     * Shows confirmation that all tasks were removed.
     */
    public void showTaskListClearedMessage() {
        System.out.println("Task list cleared.");
    }

    /**
     * Shows a warning that task changes could not be saved.
     */
    public void showSaveErrorMessage() {
        System.out.println("Warning: Task changes could not be saved.");
    }

    /**
     * Shows a warning that the saved-data header is invalid.
     */
    public void showInvalidStorageHeaderWarning() {
        System.out.println("Warning: Saved tasks could not be loaded because the file header is invalid.");
    }

    /**
     * Shows a warning that some saved tasks could not be loaded.
     */
    public void showPartialTaskLoadWarning() {
        System.out.println("Warning: Some saved tasks could not be loaded.");
    }

    /**
     * Shows a warning that saved tasks could not be loaded.
     */
    public void showTaskLoadFailureWarning() {
        System.out.println("Warning: Saved tasks could not be loaded.");
    }

    /**
     * Shows the task added to the list and the remaining capacity.
     *
     * @param task added task.
     * @param taskCount number of tasks in the list.
     * @param maximumTaskCount maximum number of tasks allowed in the list.
     */
    public void showTaskAddedMessage(Task task, int taskCount, int maximumTaskCount) {
        System.out.println("Ok, I've added to the task list:\n> " + task);
        System.out.println("Current list size: " + taskCount + "/" + maximumTaskCount);
    }

    /**
     * Shows a message explaining that no more tasks can be added.
     *
     * @param taskCount number of tasks in the list.
     * @param maximumTaskCount maximum number of tasks allowed in the list.
     */
    public void showTaskListFullMessage(int taskCount, int maximumTaskCount) {
        System.out.println("The list is full (" + taskCount + "/" + maximumTaskCount + ").");
    }

    /**
     * Shows the task removed from the list and the remaining capacity.
     *
     * @param task deleted task.
     * @param taskCount number of tasks remaining in the list.
     * @param maximumTaskCount maximum number of tasks allowed in the list.
     */
    public void showTaskDeletedMessage(Task task, int taskCount, int maximumTaskCount) {
        System.out.println("Ok, I've deleted this task from the task list:\n> " + task);
        System.out.println("Current list size: " + taskCount + "/" + maximumTaskCount);
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param taskNumber one-based number of the marked task.
     */
    public void showTaskMarkedMessage(int taskNumber) {
        System.out.println("Well done! Marked task " + taskNumber + " as done.");
    }

    /**
     * Shows a message explaining that a task is already done.
     *
     * @param taskNumber one-based number of the task.
     */
    public void showTaskAlreadyDoneMessage(int taskNumber) {
        System.out.println("Sorry, task " + taskNumber + " is already done.");
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param taskNumber one-based number of the unmarked task.
     */
    public void showTaskUnmarkedMessage(int taskNumber) {
        System.out.println("Ok, marked task " + taskNumber + " as not done yet.");
    }

    /**
     * Shows a message explaining that a task is already not done.
     *
     * @param taskNumber one-based number of the task.
     */
    public void showTaskAlreadyNotDoneMessage(int taskNumber) {
        System.out.println("Sorry, task " + taskNumber + " is already marked as not done yet.");
    }

    /**
     * Shows the commands supported by Ernest.
     */
    public void showHelpMessage() {
        System.out.println("Available commands:");
        System.out.println("todo DESCRIPTION");
        System.out.println("deadline DESCRIPTION /by yyyy-MM-dd [HHmm|HH:mm]");
        System.out.println("event DESCRIPTION /from yyyy-MM-dd [HHmm|HH:mm] /to yyyy-MM-dd [HHmm|HH:mm]");
        System.out.println("find KEYWORD");
        System.out.println("list, delete NUMBER, mark NUMBER, unmark NUMBER, clear, help, bye");
    }

    /**
     * Shows a horizontal line separating command responses.
     */
    public void showHorizontalLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Closes the standard-input scanner used by this interface.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
