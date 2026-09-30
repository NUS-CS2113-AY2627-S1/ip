package jude;

import java.util.Scanner;

/**
 * Handles interactions with the user, including reading input and displaying messages.
 */
public class Ui {
    private Scanner in;
    private static final String DIVIDER = "____________________________________________________________";

    /**
     * Constructs a Ui object.
     */
    public Ui() {
        this.in = new Scanner(System.in);
    }

    /**
     * Displays the welcome message.
     */
    public void showWelcome() {
        String banner = """
                     _           _
                    | |_   _  __| | ___
                 _  | | | | |/ _` |/ _ \\
                | |_| | |_| | (_| |  __/
                 \\___/ \\__,_|\\__,_|\\___|
                """;
        System.out.println(banner);
        showLine();
        System.out.println("Hello! I'm Jude\nWhat can I do for you?");
        showLine();
        System.out.println();
    }

    /**
     * Reads a command entered by the user.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return in.nextLine();
    }

    /**
     * Displays a horizontal line divider.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays a message to the user, wrapped in line dividers.
     *
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
        System.out.println();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Closes the scanner used for reading input.
     */
    public void close() {
        in.close();
    }
}