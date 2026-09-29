package zack;

import java.util.Scanner;

/** Reads commands from standard input and displays messages to the user. */
public class Ui {
    private static final String HORIZONTAL_LINE =
            "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Checks whether another input line is available; may wait for input.
     *
     * @return true if another line is available; false at end of input
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next input line and removes leading and trailing whitespace.
     *
     * @return trimmed command text
     * @throws java.util.NoSuchElementException if no input line is available
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints a message followed by a line break.
     *
     * @param message message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Prints the horizontal separator used between responses. */
    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }
}