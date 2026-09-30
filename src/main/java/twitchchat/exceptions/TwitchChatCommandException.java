package twitchchat.exceptions;

/** Represents an invalid or unexecutable TwitchChat command. */
public class TwitchChatCommandException extends RuntimeException {
    /** Creates a command exception with the specified message.
     *
     * @param message explanation of the command error
     */
    public TwitchChatCommandException(String message) {
        super(message);
    }
}
