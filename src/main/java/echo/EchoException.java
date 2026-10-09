package echo;

/**
 * Signals that user input could not be understood or acted on.
 * The message carries a plain-English explanation shown to the user,
 * prefixed with {@code OOPS!!! }.
 */
public class EchoException extends Exception {

    /**
     * Creates an exception carrying a user-facing explanation.
     *
     * @param message what went wrong, phrased for the user.
     */
    public EchoException(String message) {
        super(message);
    }
}