package twitchchat.exceptions;

/** Represents a command that is not supported by TwitchChat. */
public class TwitchChatInvalidCommandException extends TwitchChatCommandException {
    /** Creates an exception with the specified message.
     *
     * @param message explanation of the command error
     */
    public TwitchChatInvalidCommandException(String message) {
        super(message);
    }
}
