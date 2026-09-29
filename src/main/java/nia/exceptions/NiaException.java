package nia.exceptions;

/**
 * Root of Nia's exception hierarchy. Every failure that should reach the user
 * carries a pair of messages: a technical detail (passed to Exception's own
 * message, for stderr/debugging) and Nia's in-character voiceline (for the
 * user to actually read). Keeping them on the exception itself - rather than
 * passed in as strings at every throw site - means each failure's wording
 * lives in exactly one place.
 * NiaParserException and NiaProcessorException split this into the two
 * stages that can fail (turning input into words, and acting on those words),
 * so a caller can catch either stage specifically or NiaException to handle
 * both the same way.
 */
public abstract class NiaException extends Exception {
    private final String voiceline;

    /** Creates a NiaException carrying both a technical debugMessage and Nia's in-character voiceline. */
    NiaException(String debugMessage, String voiceline) {
        super(debugMessage);
        this.voiceline = voiceline;
    }

    /** Returns Nia's in-character line for the user to read. */
    public String getVoiceline() {
        return voiceline;
    }
}