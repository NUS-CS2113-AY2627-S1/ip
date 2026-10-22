package parser;

/**
 * Represents an error caused by invalid command input.
 */
public class ParserException extends RuntimeException {
    /**
     * Creates a parser exception with the given error message.
     *
     * @param message Explanation of the parsing error.
     */
    public ParserException(String message) {
        super(message);
    }
}
