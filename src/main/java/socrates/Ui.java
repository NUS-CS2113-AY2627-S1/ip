package socrates;

import java.util.Scanner;

/**
 * Handles all console input and output: reading commands and printing
 * boxed, divider-framed messages to the user.
 */
public class Ui {
    private static final String DIVIDER = "\t____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the startup banner and greeting.
     */
    public void printBanner() {
        String banner = "   _____                                          \n" +
                "  / ____|                                         \n" +
                " | (___   ___   ___ _ __ __ _| |_ ___  ___\n" +
                "  \\___ \\ / _ \\ / __| '__/ _` | __/ _ \\/ __|\n" +
                "  ____) | (_) | (__| | | (_| | ||  __/\\__ \\\n" +
                "  |____/ \\___/ \\___|_|  \\__,_|\\__\\___||___/\n" +
                "Hello! I'm Socrates.\n" +
                "What shall we examine together today? (say 'help' if you're unsure) \n" +
                "____________________________________________________________";
        System.out.println(banner);
    }

    /**
     * Prints a message framed by divider lines, as a single response.
     */
    public void printMessage(String s) {
        System.out.println(DIVIDER);
        System.out.println("\t " + s.strip());
        System.out.println(DIVIDER);
    }

    /**
     * Reads one line of user input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }
}
