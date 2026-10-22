package neo.exception;

/**
 * Represents exceptions specific to the Neo application.
 * Thrown when user input is invalid, files cannot be loaded, or other
 * application-specific errors occur.
 */
public class NeoException extends Exception {

    /**
     * Constructs a new NeoException with the specified detailed error message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public NeoException(String message) {
        super(message);
    }
}