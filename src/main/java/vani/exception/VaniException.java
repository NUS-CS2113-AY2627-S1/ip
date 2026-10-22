package vani.exception;

/**
 * Represents an input or storage error that can be explained to the user.
 */
public class VaniException extends Exception {

    /**
     * Creates a Vani exception with the supplied error message.
     *
     * @param message the message describing the input or storage error
     */
    public VaniException(String message) {
        super(message);
    }
}
