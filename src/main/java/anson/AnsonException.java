package anson;

/**
 * Represents an error specific to the Anson chatbot, such as invalid
 * or malformed user input.
 */
public class AnsonException extends Exception {

    /**
     * Creates an AnsonException with the given error message.
     *
     * @param message Description of what went wrong, shown to the user.
     */
    public AnsonException(String message) {
        super(message);
    }
}