package jude.exception;

/**
 * Represents exceptions specific to the Jude application.
 */
public class JudeException extends Exception {

    /**
     * Constructs a JudeException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public JudeException(String message) {
        super(message);
    }
}
