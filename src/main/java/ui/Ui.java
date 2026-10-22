package ui;

import java.util.Scanner;

/**
 * Handles console input and application-level messages for ATHENA.
 */
public class Ui implements AutoCloseable {
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    private final Scanner input;

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
        input = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available to read.
     *
     * @return {@code true} if another command is available; otherwise {@code false}.
     */
    public boolean hasNextCommand() {
        return input.hasNextLine();
    }

    /**
     * Returns the next command entered by the user.
     *
     * @return Next command from standard input.
     */
    public String readCommand() {
        return input.nextLine();
    }

    /**
     * Displays the greeting banner and instructions.
     */
    public void showGreeting() {
        showHorizontalLine();
        String banner = " █████╗ ████████╗██╗  ██╗███████╗███╗   ██╗ █████╗\n"
                + "██╔══██╗╚══██╔══╝██║  ██║██╔════╝████╗  ██║██╔══██╗\n"
                + "███████║   ██║   ███████║█████╗  ██╔██╗ ██║███████║\n"
                + "██╔══██║   ██║   ██╔══██║██╔══╝  ██║╚██╗██║██╔══██║\n"
                + "██║  ██║   ██║   ██║  ██║███████╗██║ ╚████║██║  ██║\n"
                + "╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝\n";
        System.out.println(banner);
        System.out.println("Hello! I'm ATHENA.");
        System.out.println("What can I do for you?");
        showHorizontalLine();
    }

    /**
     * Displays a separator between commands and responses.
     */
    public void showHorizontalLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays the farewell message without surrounding separators.
     */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Stops reading commands and releases the input resource.
     */
    @Override
    public void close() {
        input.close();
    }
}
