package nia.exceptions;

/**
 * Thrown by Processor when a canonical command's words are individually
 * well-formed but the command still can't be carried out - e.g. a task index
 * that isn't a number, or doesn't exist. Each concrete subclass corresponds
 * to one specific processing failure.
 */
public abstract class NiaProcessorException extends NiaException {
    /** Creates a processing-stage failure carrying debugMessage and voiceline. */
    NiaProcessorException(String debugMessage, String voiceline) {
        super(debugMessage, voiceline);
    }
}