package ducky.parser;

/**
 * Signals that a command typed by the user could not be understood: for
 * example a missing description, a missing "/by", "/from" or "/to"
 * separator, or a chore rank that is not a number.
 */
public class DuckyException extends Exception {
    /**
     * Creates an exception carrying the message to show the user.
     *
     * @param message Text explaining what was wrong with the command.
     */
    public DuckyException(String message) {
        super(message);
    }
}
