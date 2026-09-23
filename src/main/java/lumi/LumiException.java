package lumi;

/**
 * Signals that the user's input could not be understood by Lumi.
 */
public class LumiException extends Exception {
    /**
     * Creates an exception carrying a message written for the user.
     *
     * @param message What went wrong, phrased so the user can act on it.
     */
    public LumiException(String message) {
        super(message);
    }
}
