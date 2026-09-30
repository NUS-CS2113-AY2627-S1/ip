package xxlilchatxx;

import java.util.Scanner;

/**
 * Handles all interactions with the user: reading input and displaying output.
 */
public class Ui {

    private static final String LINE = "____________________________________________________________";

    private final Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of user input.
     *
     * @return The raw input entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the welcome banner and greeting.
     */
    public void showWelcome() {
        String banner =
                "██╗  ██╗██╗  ██╗██╗     ██╗██╗      ██████╗██╗  ██╗ █████╗ ████████╗██╗  ██╗██╗  ██╗\n"
                        + "╚██╗██╔╝╚██╗██╔╝██║     ██║██║     ██╔════╝██║  ██║██╔══██╗╚══██╔══╝╚██╗██╔╝╚██╗██╔╝\n"
                        + " ╚███╔╝  ╚███╔╝ ██║     ██║██║     ██║     ███████║███████║   ██║    ╚███╔╝  ╚███╔╝ \n"
                        + " ██╔██╗  ██╔██╗ ██║     ██║██║     ██║     ██╔══██║██╔══██║   ██║    ██╔██╗  ██╔██╗ \n"
                        + "██╔╝ ██╗██╔╝ ██╗███████╗██║███████╗╚██████╗██║  ██║██║  ██║   ██║   ██╔╝ ██╗██╔╝ ██╗\n"
                        + "╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝\n";
        showLine();
        System.out.println(banner);
        System.out.println("Hello! I'm XxLilChatxX.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Prints the goodbye message.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Prints an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
    }
}
