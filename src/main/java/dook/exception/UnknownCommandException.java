package dook.exception;

/**
 * Represents the exception when failing to parse user input into command.
 */
public class UnknownCommandException extends DookException {
    public UnknownCommandException() {
        super("You are not supposed to be here!");
    }
}
