package dook.exception;

/**
 * Represents the exception when attempting to undo an empty command log.
 */
public class EmptyCommandLogException extends DookException {
    public EmptyCommandLogException() {
        super("There is no past for you to fix. History starts here.");
    }
}
