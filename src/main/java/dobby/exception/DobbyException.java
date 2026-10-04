package dobby.exception;

/**
 * Represents an input error that Dobby can explain to the user.
 */
public class DobbyException extends Exception {

    /**
     * Creates an input error with the supplied user-facing message.
     *
     * @param message the explanation to display to the user.
     */
    public DobbyException(String message) {
        super(message);
    }
}
