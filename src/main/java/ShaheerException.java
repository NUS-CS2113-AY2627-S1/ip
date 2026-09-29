/**
 * Signals a problem that Shaheer (the chatbot) knows how to explain to the
 * user in a friendly way, e.g. a missing task description, an unrecognised
 * command, or a save file that cannot be read or written.
 */
public class ShaheerException extends Exception {
    /** Creates an exception whose {@code message} is shown to the user as-is. */
    public ShaheerException(String message) {
        super(message);
    }
}
