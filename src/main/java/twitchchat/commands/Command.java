package twitchchat.commands;

/** Represents a parsed user command and its arguments. */
public class Command {
    private final CommandType type;
    private final String[] arguments;
    private final int id;

    /** Creates a command with text arguments.
     *
     * @param type command type
     * @param arguments command arguments
     */
    public Command(CommandType type, String[] arguments) {
        this.type = type;
        this.arguments = arguments;
        this.id = 0;
    }

    /** Creates a command with a task ID argument.
     *
     * @param type command type
     * @param id task ID argument
     */
    public Command(CommandType type, int id) {
        this.type = type;
        this.arguments = new String[0];
        this.id = id;
    }

    /** Returns the command type.
     *
     * @return command type
     */
    public CommandType getType() {
        return this.type;
    }

    /** Returns a copy of the command's text arguments.
     *
     * @return command arguments
     */
    public String[] getArguments() {
        return arguments.clone();
    }

    /** Returns the command's task ID argument.
     *
     * @return task ID, or zero when the command has text arguments
     */
    public int getId() {
        return id;
    }

}
