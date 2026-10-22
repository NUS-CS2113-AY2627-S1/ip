package ace.ui;

import java.util.Scanner;

/**
 * Handles console input and output for Ace.
 */
public class Ui {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this(new Scanner(System.in));
    }

    /**
     * Creates a user interface using the supplied input source.
     *
     * @param scanner input source for user commands
     */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Returns whether another input line is available.
     *
     * @return true if another command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the next input line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a message with separator lines.
     *
     * @param message message to display
     */
    public void printAceMessage(Object message) {
        printAceMessage(message, true);
    }

    /**
     * Prints a message, optionally surrounded by separator lines.
     *
     * @param message message to display
     * @param withSeparation whether to print separator lines
     */
    public void printAceMessage(Object message, boolean withSeparation) {
        if (withSeparation) {
            printAceSeparation();
        }
        System.out.print("\t" + message + "\n");
        if (withSeparation) {
            printAceSeparation();
        }
    }

    /**
     * Prints the separator used between messages.
     */
    public void printAceSeparation() {
        System.out.println("\t" + Messages.HL);
    }

    /**
     * Displays an error message using Ace's error formatting.
     *
     * @param error error message to display
     */
    public void showError(String error) {
        printAceSeparation();
        System.out.println("\t" + RED + error + RESET);
        printAceSeparation();
    }
}
