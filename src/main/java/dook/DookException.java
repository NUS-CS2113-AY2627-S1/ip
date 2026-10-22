package dook;

/**
 * Represents an exception caused by an invalid or unexpected Dook operation.
 */
public class DookException extends Exception {

    /**
     * Creates a DookException with the specified error message.
     *
     * @param message the error message describing the problem
     */
    public DookException(String message) {
        super(message);
    }
}
