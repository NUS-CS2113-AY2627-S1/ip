package twitchchat.exceptions;

/** Represents a command missing a required argument. */
public class TwitchChatMissingArgumentException extends TwitchChatCommandException {
    /** Creates an exception with the specified message.
     *
     * @param message explanation of the command error
     */
    public TwitchChatMissingArgumentException(String message) {
        super(message);
    }
}
