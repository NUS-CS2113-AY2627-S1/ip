package kevie.commands;

import kevie.Kevie;
import kevie.UserInterface;
import kevie.exceptions.*;
import kevie.tasks.TaskList;

/**
 * Represents a command.
 */
public abstract class Command {
    private String id;
    protected UserInterface ui;

    /**
     * Creates a command.
     *
     * @param id Command keyword
     * @param ui User interface to print messages in.
     */
    public Command(String id, UserInterface ui){
        this.id = id;
        this.ui = ui;
    }

    /**
     * Executes the command.
     *
     * @param arg The string of all arguments after the command keyword.
     * @return true if the command terminates Kevie right after it's execution, false if not
     * @throws KevieException If there are syntax error or command parsing errors.
     */
    public abstract boolean execute(String arg) throws KevieException;

    /**
     * Returns the syntax of the command.
     *
     * @return The syntax of the command.
     */
    public abstract String syntax();

    /**
     * Returns an example of use of the command.
     *
     * @return An example of use of the command.
     */
    public abstract String example();

    /**
     * Prints the correct syntax and a sample use of the command.
     */
    public void help() {
        ui.botSpeak("Correct syntax should be: \"" + syntax() + "\".", true);
        ui.botSpeak("For example: \"" + example() + "\".", true);
    }

    /**
     * Returns the keyword of the command.
     *
     * @return Keyword of the command.
     */
    public String getId() {
        return id;
    }
}
