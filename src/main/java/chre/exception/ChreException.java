package chre.exception;

/**
 * ChreException is a custom exception class for errors in the Chre chatbot.
 * Used to represent application-specific errors that can be caught and handled gracefully.
 */
public class ChreException extends Exception {
    /**
     * Constructs a ChreException with the given error message.
     *
     * @param message the error message describing what went wrong
     */
    public ChreException(String message) {
        super(message);
    }
}
