package bigbrother.exception;

/**
 * Represents an error caused by an invalid command given to BigBrother.
 */
public class BigBrotherException extends Exception {
    /**
     * Creates an exception with a message explaining how to correct the command.
     *
     * @param message explanation of the command error
     */
    public BigBrotherException(String message) {
        super(message);
    }
}
