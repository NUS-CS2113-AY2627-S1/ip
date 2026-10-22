package wenwen.exception;

/**
 * Represents an error caused by invalid user input in the Wenwen chatbot.
 */
public class WenwenException extends Exception {
    /**
     * Creates an exception with a message that can be shown to the user.
     *
     * @param message The explanation of what went wrong.
     */
    public WenwenException(String message) {
        super(message);
    }
}
