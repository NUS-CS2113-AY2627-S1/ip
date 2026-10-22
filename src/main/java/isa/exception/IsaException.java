package isa.exception;

/**
 * Represents an invalid command entered by the user.
 */
public class IsaException extends Exception {

    /**
     * Creates an Isa exception with the specified error message.
     *
     * @param message Error message to display.
     */
    public IsaException(String message) {
        super(message);
    }
}