package bryte;

import java.util.Scanner;

/**
 * Handles interactions with the user.
 * This includes reading commands and printing messages.
 */
public class Ui {
    private static final String BANNER = "██████╗ ██████╗ ██╗   ██╗████████╗███████╗\n"
            + "██╔══██╗██╔══██╗╚██╗ ██╔╝╚══██╔══╝██╔════╝\n"
            + "██████╔╝██████╔╝ ╚████╔╝    ██║   █████╗  \n"
            + "██╔══██╗██╔══██╗  ╚██╔╝     ██║   ██╔══╝  \n"
            + "██████╔╝██║  ██║   ██║      ██║   ███████╗\n"
            + "╚═════╝ ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚══════╝\n";
    private static final String DIVIDER = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Constructs a new {@code Ui} object and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm BRYTE.");
        System.out.println("What can I do for you?");
    }

    /**
     * Prints a divider line.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Reads the next command from the user.
     *
     * @return The user's command as a string.
     */
    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "bye";
    }

    /**
     * Prints a message to the user.
     *
     * @param message The message to print.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error message to the user.
     *
     * @param message The error message to print.
     */
    public void showError(String message) {
        System.out.println(" OOPS!!! " + message);
    }

    /**
     * Prints the goodbye message when the application exits.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }
}
