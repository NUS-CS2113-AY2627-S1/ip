package twitchchat.exceptions;

/** Represents a command containing an invalid task ID. */
public class TwitchChatInvalidTaskIdException extends TwitchChatCommandException {
    /** Creates an exception with the specified message.
     *
     * @param message explanation of the command error
     */
    public TwitchChatInvalidTaskIdException(String message) {
        super(message);
    }
}
