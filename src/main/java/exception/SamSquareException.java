package exception;

/**
 * Reports a recoverable command error that the console displays to the user.
 */
public class SamSquareException extends Exception {

    /**
     * Creates an error explaining why a command cannot be completed.
     *
     * @param message User-facing explanation of the invalid command or task number.
     */
    public SamSquareException(String message) {
        super(message);
    }
}
