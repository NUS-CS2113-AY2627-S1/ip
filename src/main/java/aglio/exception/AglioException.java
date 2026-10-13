package aglio.exception;

/**
 * Represents an error specific to the Aglio chatbot,
 * such as invalid user input or exceeding the task limit.
 */
public class AglioException extends Exception {
    /**
     * Creates an AglioException with the given error message.
     *
     * @param message a description of the error
     */
    public AglioException(String message) {
        super(message);
    }
}
