package command;

/**
 * Defines the task operations that commands can invoke.
 */
public interface CommandExecutor {
    /**
     * Adds a task described by the given command.
     *
     * @param fullCommand Complete add command.
     */
    void add(String fullCommand);

    /**
     * Deletes the task selected by the given command.
     *
     * @param fullCommand Complete delete command.
     */
    void delete(String fullCommand);

    /**
     * Finds tasks whose descriptions contain the keyword in the given command.
     *
     * @param fullCommand Complete find command.
     */
    void find(String fullCommand);

    /**
     * Lists tasks after validating the given command.
     *
     * @param fullCommand Complete list command.
     */
    void list(String fullCommand);

    /**
     * Marks the task selected by the given command as done.
     *
     * @param fullCommand Complete mark command.
     */
    void mark(String fullCommand);

    /**
     * Marks the task selected by the given command as not done.
     *
     * @param fullCommand Complete unmark command.
     */
    void unmark(String fullCommand);
}
