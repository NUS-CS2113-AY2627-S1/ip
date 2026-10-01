package Bastion;

/** Represents an error caused by an invalid Bastion command or command argument. */
public class BastionException extends Exception {
    /**
     * Creates an exception containing a message suitable for the user.
     *
     * @param message explanation of the command error
     */
    public BastionException(String message) {
        super(message);
    }
}
