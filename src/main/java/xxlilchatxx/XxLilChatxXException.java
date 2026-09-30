package xxlilchatxx;

/**
 * Represents an error specific to the XxLilChatxX chatbot, such as invalid user input.
 */
public class XxLilChatxXException extends Exception {
    /**
     * Creates a new exception with the given error message.
     *
     * @param message Description of the error, shown to the user.
     */
    public XxLilChatxXException(String message) {
        super(message);
    }
}
