package clanky;

/**
 * Represents an error specific to Clanky, such as invalid user input,
 * a malformed command, or a corrupted save file.
 * <p>
 * ClankyException is a checked exception, so callers are required to either
 * catch it or declare it, ensuring invalid input is always handled rather
 * than allowed to propagate as an unhandled runtime error.
 */
public class ClankyException extends Exception{

    /**
     * Creates a new ClankyException with the given message.
     *
     * @param message a description of what went wrong, suitable for
     *                displaying directly to the user.
     */
    public ClankyException(String message) {
        super(message);
    }

}
