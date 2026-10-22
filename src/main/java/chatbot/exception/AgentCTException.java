package chatbot.exception;

/**
 * Represents an error caused by invalid input to the AgentCT chatbot.
 */
public class AgentCTException extends Exception {
    /** Creates an exception with the given user-facing error message. */
    public AgentCTException(String message) {
        super(message);
    }
}
