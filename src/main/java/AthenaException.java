/**
 * Represents an error caused by invalid ATHENA input or an unsuccessful task operation.
 */
public class AthenaException extends RuntimeException {
    /**
     * Creates an exception with the given error message.
     *
     * @param message Explanation of the error.
     */
    public AthenaException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the given error message and underlying cause.
     *
     * @param message Explanation of the error.
     * @param cause Underlying cause of the error.
     */
    public AthenaException(String message, Throwable cause) {
        super(message, cause);
    }
}
