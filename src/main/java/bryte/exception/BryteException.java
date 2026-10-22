package bryte.exception;

/**
 * Signals that an application-specific error occurred during Bryte execution.
 */
public class BryteException extends Exception {

    /**
     * Constructs a new {@code BryteException} with the specified detail message.
     *
     * @param message The detail error message.
     */
    public BryteException(String message) {
        super(message);
    }
}
