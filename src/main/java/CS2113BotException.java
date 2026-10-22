/**
 * Represents an error caused by invalid chatbot input or task data.
 */
public class CS2113BotException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates an exception with a message suitable for showing to the user.
     *
     * @param message explanation of the problem
     */
    public CS2113BotException(String message) {
        super(message);
    }
}
