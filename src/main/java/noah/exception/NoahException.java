package noah.exception;

/**
 * Represents an error caused by invalid user input.
 */
public class NoahException extends Exception {

    /**
     * Creates an exception with an explanation.
     *
     * @param message Explanation shown to the user.
     */
    public NoahException(String message) {
        super(message);
    }
}
