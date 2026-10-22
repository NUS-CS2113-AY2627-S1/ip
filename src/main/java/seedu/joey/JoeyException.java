package seedu.joey;

/**
 * Represents an error caused by invalid user input to Joey.
 * Thrown whenever the user enters something the chatbot recognises
 * as wrong (e.g. an empty todo description), so the main loop can
 * catch it, display a friendly message and continue running.
 */
public class JoeyException extends Exception {
    /**
     * Creates a JoeyException with a user-facing error message.
     *
     * @param message The message shown to the user.
     */
    public JoeyException(String message) {
        super(message);
    }
}
