package dash;

/**
 * Signals that a user command could not be processed because it is invalid.
 */
public class DashException extends Exception {
    /**
     * Creates an exception with a message that can be shown to the user.
     *
     * @param message The explanation of what went wrong.
     */
    public DashException(String message) {
        super(message);
    }
}
