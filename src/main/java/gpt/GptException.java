package gpt;

/**
 * Represents errors caused by invalid user input in the GPT chatbot.
 */
public class GptException extends Exception {
    /**
     * Creates a GPT exception with the given error message.
     */
    public GptException(String message) {
        super(message);
    }
}
