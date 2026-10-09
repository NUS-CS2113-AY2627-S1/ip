package friday;

/**
 * Signals that Friday could not understand or process a user command,
 * e.g. an unrecognised command word or a command missing required details.
 */
public class FridayException extends Exception {
    /**
     * Creates an exception carrying a user-facing explanation.
     *
     * @param message what went wrong, shown to the user as-is
     */
    public FridayException(String message) {
        super(message);
    }
}
