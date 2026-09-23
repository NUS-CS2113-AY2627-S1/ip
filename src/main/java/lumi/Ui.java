package lumi;

import java.util.Scanner;

/**
 * Handles everything the user sees and types: the startup banner, the bordered
 * message blocks, and reading command lines from standard input. Keeping this
 * apart from the task logic means the wording and layout can change without
 * touching the rules of the application.
 */
public class Ui {
    private static final String LOGO = " _    _   _ __  __ ___ \n"
            + "| |  | | | |  \\/  |_ _|\n"
            + "| |  | | | | |\\/| || | \n"
            + "| |__| |_| | |  | || | \n"
            + "|_____\\___/|_|  |_|___|";
    private static final String LINE = "    ____________________________________________________________";
    private static final String INDENT = "     ";

    /** Extra indent for a task shown inside a message block. */
    public static final String TASK_INDENT = "  ";

    private final Scanner scanner;

    /** Creates a display that reads its commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Prints the startup banner and the greeting. */
    public void showWelcome() {
        System.out.println("Hello from");
        System.out.println(LOGO);
        show("Hello! I'm Lumi", "What can I do for you?");
    }

    /** Returns true while the user has typed another line for Lumi to read. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Returns the next command line with its surrounding spaces removed. */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /** Prints the given messages inside a pair of horizontal lines. */
    public void show(String... messages) {
        System.out.println(LINE);
        for (String message : messages) {
            System.out.println(INDENT + message);
        }
        System.out.println(LINE);
        System.out.println();
    }

    /** Releases the input stream once no more commands will be read. */
    public void close() {
        scanner.close();
    }
}
