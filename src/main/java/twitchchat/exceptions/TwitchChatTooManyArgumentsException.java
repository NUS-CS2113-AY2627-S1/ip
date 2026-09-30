package twitchchat.exceptions;

/** Represents a command containing more arguments than expected. */
public class TwitchChatTooManyArgumentsException extends TwitchChatCommandException {
    /** Creates an exception with the specified message.
     *
     * @param message explanation of the command error
     */
    public TwitchChatTooManyArgumentsException(String message) {
        super(message);
    }
}
