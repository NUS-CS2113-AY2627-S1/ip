package clammy.exception;

/**
 * Represents an error caused by a command that Clammy cannot complete.
 */
public class ClammyException extends Exception {
    /**
     * Creates a Clammy-specific error with a user-friendly explanation.
     *
     * @param message Explanation of the error and how the user can correct it.
     */
    public ClammyException(String message) {
        super(message);
    }
}
