package floppy;

/**
 * Signals a problem Floppy can explain to the user, such as a command that is
 * missing part of its input or names a task that does not exist.
 *
 * <p>The message carried by this exception is written for the user to read, so it
 * explains what went wrong and how to correct it rather than describing the fault
 * in technical terms.
 */
public class FloppyException extends Exception {

    /**
     * Constructs an exception carrying an explanation meant for the user.
     *
     * @param message what went wrong, in Floppy's own words.
     */
    public FloppyException(String message) {
        super(message);
    }
}
