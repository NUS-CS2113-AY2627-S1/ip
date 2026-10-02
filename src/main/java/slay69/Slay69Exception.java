package slay69;

/**
 * Represents invalid input supplied to the Slay69 chatbot.
 */
public class Slay69Exception extends Exception {

    /**
     * Creates an exception with a message suitable for showing to the user.
     *
     * @param message explanation of the invalid input
     */
    public Slay69Exception(String message) {
        super(message);
    }
}
