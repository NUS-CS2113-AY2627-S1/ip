package nico;

/**
 * Represents an error specific to Nico, such as invalid user input
 * or a problem reading or writing the save file.
 * The message is meant to be shown directly to the user.
 */
public class NicoException extends Exception {

    /**
     * Creates an exception with a message that explains what went wrong.
     *
     * @param message A user-friendly description of the error.
     */
    public NicoException(String message) {
        super(message);
    }
}