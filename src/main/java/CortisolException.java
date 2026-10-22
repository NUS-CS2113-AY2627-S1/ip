/**
 * Represents an error specific to CortisolBot, such as an unrecognized command
 * or a command that is missing information.
 * <p>
 * The message carried by this exception is written in the bot's own voice and
 * is shown directly to the user, so throwing sites should phrase the message as
 * something the butler would say.
 */
public class CortisolException extends Exception {

    /**
     * Constructs an exception carrying a user-facing explanation of the error.
     *
     * @param message explanation shown to the user.
     */
    public CortisolException(String message) {
        super(message);
    }
}
