package nia.exceptions;

/** Thrown when the command word isn't one Processor recognizes. */
public class UnknownCommandException extends NiaProcessorException {
    /** Creates the exception for commandWord, the unrecognized command that was given. */
    public UnknownCommandException(String commandWord) {
        super(
                String.format(
                        "[Debug] Unrecognized command \"%s\". Valid commands: bye, list, mark, unmark, todo, deadline, event, find, help",
                        commandWord),
                "Hm? I don't know what that means.");
    }
}