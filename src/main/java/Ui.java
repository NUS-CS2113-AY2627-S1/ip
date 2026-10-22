import java.util.Scanner;

/**
 * Handles all command-line input and output for the YODA application.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "__   __  ___  ____    _\n"
            + "\\ \\ / / / _ \\|  _ \\  / \\\n"
            + " \\ V / | | | | | | |/ _ \\\n"
            + "  | |  | |_| | |_| / ___ \\\n"
            + "  |_|   \\___/|____/_/   \\_\\\n";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome banner and greeting.
     */
    public void showWelcome() {
        showDivider();
        System.out.print(BANNER);
        showDivider();
        System.out.println("Hello! I'm YODA.");
        System.out.println("What can I do for you?");
        showDivider();
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return true if a command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next command entered by the user.
     *
     * @return the next command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the divider used to separate application messages.
     */
    public void showDivider() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays the farewell message before the application exits.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showDivider();
    }

    /**
     * Displays an application error message.
     *
     * @param message the explanation of the error
     */
    public void showError(String message) {
        System.out.println("OOPS! " + message);
    }

    /**
     * Displays the error message used for invalid task numbers.
     */
    public void showInvalidNumberError() {
        System.out.println("OOPS! That's not a valid number.\n"
                + "  Correct format: mark <number>  OR  unmark <number>\n"
                + "  Example:        mark 2");
    }

    /**
     * Displays the error message used when a deadline date is not ISO-formatted.
     */
    public void showInvalidDateError() {
        System.out.println("OOPS! Please use a date in yyyy-MM-dd format.\n"
                + "  Example: deadline Submit report /by 2019-10-15");
    }
}
