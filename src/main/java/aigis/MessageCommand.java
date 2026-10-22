package aigis;

/**
 * Represents a command that reports a parser or validation error.
 */
public final class MessageCommand extends Command {
    private final String message;

    /**
     * Creates a message command.
     *
     * @param message The error message to report.
     */
    public MessageCommand(String message) {
        this.message = message;
    }

    /**
     * Reports the stored error message to the application loop.
     *
     * @param tasks The task list, which is unchanged.
     * @param ui The UI, which is used by the application loop after the error
     *           is caught.
     * @param storage The storage service, which is not needed for errors.
     * @throws AigisException Always, with the stored error message.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws AigisException {
        throw new AigisException(message);
    }
}
