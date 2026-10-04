package dook.service;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import dook.command.Response;

/**
 * Handles taking the user inputs and printing the responses.
 */
public class ChatInterface {
    private static final int BAR_LENGTH = 72;
    private static final String DIVIDER = "_".repeat(BAR_LENGTH) + "\n\n> ";

    private final Scanner scanner = new Scanner(System.in);
    private final Logger logger = Logger.getLogger(ChatInterface.class.getName());

    /**
     * Formats and prints the response answering user input.
     *
     * @param response The response answering user input.
     */
    public void printResponse(Response response) {
        if (response.hasTask()) {
            System.out.print("\n" + response.message() + "\n  " + response.task() + "\n" + DIVIDER);
        } else {
            System.out.print("\n" + response.message() + "\n" + DIVIDER);
        }
    }

    /**
     * Prints a generic error message and logs the given exception.
     *
     * @param e The exception to log.
     */
    public void printError(Exception e) {
        System.out.print(
            """

            A swarm of bugs emerges from nearby fallen trees.
            Check the logs.
            """
        );
        logger.log(Level.WARNING, "Unchecked exception caught.", e);
        System.out.print(DIVIDER);
    }

    /**
     * Cleans and retrieves the user input.
     *
     * @return Trimmed user input.
     */
    public String getUserQuery() {
        return scanner.nextLine().trim();
    }
}
