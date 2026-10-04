package dook.exception;

/**
 * Represents the exception when failing to parse user input into a task.
 */
public class UnknownTaskException extends DookException {
    public UnknownTaskException() {
        super("Gibberish words only draw the monster nearer.\nTry screaming `help` instead.");
    }
}
