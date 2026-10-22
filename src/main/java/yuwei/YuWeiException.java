package yuwei;

/**
 * Signals a problem the user should be told about, such as a command in the wrong format.
 * Its message is shown to the user as-is, so it should be written in plain language.
 */
public class YuWeiException extends Exception {
    /** Creates an exception whose {@code message} will be shown to the user. */
    public YuWeiException(String message) {
        super(message);
    }
}
