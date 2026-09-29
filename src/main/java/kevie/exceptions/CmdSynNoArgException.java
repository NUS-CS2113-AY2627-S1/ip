package kevie.exceptions;

import kevie.UserInterface;
import kevie.commands.Command;

/**
 * Exception thrown when command is typed without any argument required typed.
 */
public class CmdSynNoArgException extends CmdSyntaxException{
    /**
     * Creates a Command no syntax exception.
     *
     * @param command The command inputted
     * @param ui User interface to print messages in
     */
    public CmdSynNoArgException(Command command, UserInterface ui) {
        super("No arguments given", command, ui);
    }
}
