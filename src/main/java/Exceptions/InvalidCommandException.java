package Exceptions;

/** Signals invalid command input with a message that can be shown to the user. */
public class InvalidCommandException extends RuntimeException {

    /** Creates an input error with a message suitable for display to the user. */
    public InvalidCommandException(String message) {
        super(message);
    }
}
