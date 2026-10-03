package clammy.exception;

/**
 * Indicates that a user command does not follow Clammy's command grammar.
 */
public class ParseException extends ClammyException {
    /**
     * Creates a parsing error with a user-friendly explanation.
     *
     * @param message Explanation of the invalid input.
     */
    public ParseException(String message) {
        super(message);
    }
}
