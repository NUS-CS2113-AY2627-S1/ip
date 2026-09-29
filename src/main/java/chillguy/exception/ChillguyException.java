package chillguy.exception;

/**
 * Represents an input or storage error that the chatbot can explain to the user.
 */
public class ChillguyException extends Exception {
    /**
     * Creates an error with a message explaining how the user can correct the input.
     *
     * @param message Explanation to display in the console.
     */
    public ChillguyException(String message) {
        super(message);
    }

    /**
     * Creates a user-facing error while retaining the underlying storage failure.
     *
     * @param message Explanation to display in the console.
     * @param cause Original exception that caused the failure.
     */
    public ChillguyException(String message, Throwable cause) {
        super(message, cause);
    }
}
