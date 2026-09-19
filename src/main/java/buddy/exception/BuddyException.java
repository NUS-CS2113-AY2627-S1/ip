package buddy.exception;

/**
 * Signals that Buddy could not understand or carry out a user command.
 * The message is written to be shown directly to the user, so it should
 * explain what went wrong in plain language.
 */
public class BuddyException extends Exception {

    /**
     * Creates an exception with a user-facing explanation of the error.
     *
     * @param message explanation shown to the user
     */
    public BuddyException(String message) {
        super(message);
    }
}
