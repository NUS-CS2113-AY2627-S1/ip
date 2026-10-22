package slowbro;

/** Represents a command that does not follow the expected format. */
public class InvalidCommandException extends Exception {
    private final String usageMessage;

    /**
     * Creates an exception with a user-facing usage message.
     *
     * @param usageMessage the message explaining the expected command format
     */
    public InvalidCommandException(String usageMessage) {
        this.usageMessage = usageMessage;
    }

    /**
     * Returns the user-facing usage message.
     *
     * @return the usage message
     */
    public String getUsageMessage() {
        return usageMessage;
    }
}
