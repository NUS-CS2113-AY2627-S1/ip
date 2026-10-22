import java.util.Scanner;

/**
 * Text UI of the application, responsible for reading user input and displaying output.
 */
public class Ui {
    private static final String DIVIDER = "__________________________________";
    private static final String BANNER =
            "_    _ ______ _      _      ____   __          __  {_} _____  _      _____  \n"
                    + "| |  | |  ____| |    | |    / __ \\  \\ \\        / / | |  __ \\| |    |  __ \\ \n"
                    + "| |__| | |__  | |    | |   | |  | |  \\ \\  /\\  / /  | | |__) | |    | |  | |\n"
                    + "|  __  |  __| | |    | |   | |  | |   \\ \\/  \\/ /   | |  _  /| |    | |  | |\n"
                    + "| |  | | |____| |____| |____| |__| |    \\  /\\  /    | | | \\ \\| |____| |__| |\n"
                    + "|_|  |_|______|______|______|\\____/      \\/  \\/     |_| |_| \\_\\______|_____/ ";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return input command text
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Checks whether another input command is available.
     *
     * @return true when another command can be read
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Prints a divider line between messages.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays the chatbot's welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("\nHello! I'm CS2113_Bot.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays the chatbot's farewell message.
     */
    public void showExit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message for the user.
     *
     * @param message explanation of the error
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays every task in a numbered list.
     *
     * @param taskList tasks to display
     */
    public void showTaskList(TaskList taskList) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + "." + taskList.get(i));
        }
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task added task
     * @param totalTasks number of tasks after the addition
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task was removed.
     *
     * @param task removed task
     * @param remainingTasks number of tasks after removal
     */
    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + remainingTasks + " tasks in the list.");
    }

    /**
     * Displays confirmation that a task's completion status changed.
     *
     * @param task updated task
     * @param isDone new completion status
     */
    public void showMarked(Task task, boolean isDone) {
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Closes the input scanner after command processing finishes.
     */
    public void close() {
        scanner.close();
    }

    public void showFoundTasks(TaskList foundList) {
        if (foundList.size() == 0) {
            System.out.println("No matching tasks found in your list.");
            return;
        }
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < foundList.size(); i++) {
            System.out.println((i + 1) + "." + foundList.get(i));
        }
    }

}
