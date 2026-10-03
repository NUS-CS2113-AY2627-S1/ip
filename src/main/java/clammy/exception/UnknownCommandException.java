package clammy.exception;

/**
 * Indicates that a command keyword is not supported by Clammy.
 */
public class UnknownCommandException extends ClammyException {
    /**
     * Creates an error that lists the commands accepted by Clammy.
     *
     * @param validCommands Explanation containing the supported command formats.
     */
    public UnknownCommandException(String validCommands) {
        super(validCommands);
    }
}
