package goatbot.exception;

/**
 * Represents an error caused by invalid user input in Goat Bot.
 */
public class GoatBotException extends Exception {

    /**
     * Creates an exception with a message that can be shown to the user.
     *
     * @param message explanation of the input error
     */
    public GoatBotException(String message) {
        super(message);
    }
}
