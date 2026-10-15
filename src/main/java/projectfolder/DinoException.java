package projectfolder;

/**
 * Represents an error specific to DINO.
 */
public class DinoException extends Exception {

    /**
     * Creates a DINO exception with the given message.
     *
     * @param message error message
     */
    public DinoException(String message) {
        super(message);
    }
}