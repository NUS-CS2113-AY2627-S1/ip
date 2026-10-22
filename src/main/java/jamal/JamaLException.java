package jamal;

/**
 * Exception used for every error JamaL reports back to the user.
 *
 * The message carried by this exception is the exact text shown to the user,
 * so the place that detects the problem decides the wording, and the main loop
 * decides the formatting. This keeps error wording next to the check that
 * produced it while printing stays in one place.
 */
public class JamaLException extends Exception {
    /**
     * Creates an exception carrying the message to show the user.
     *
     * @param message Text shown to the user inside the reply block.
     */
    public JamaLException(String message) {
        super(message);
    }
}
