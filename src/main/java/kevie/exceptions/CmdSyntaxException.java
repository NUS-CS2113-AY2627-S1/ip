package kevie.exceptions;

import kevie.Kevie;
import kevie.UserInterface;
import kevie.commands.Command;

/**
 * Exception thrown when the command inputted has wrong syntax.
 */
public class CmdSyntaxException extends KevieException{

    private Command command;

    /**
     * Creates a Command syntax error exception.
     *
     * @param type Reason of why syntax is incorrect
     * @param command The command inputted
     * @param ui User interface to print messages in
     */
    public CmdSyntaxException(String type, Command command, UserInterface ui){
        super("Sorry, the syntax of your command is invalid", type, ui);
        this.command = command;
    }

    @Override
    public void printMessage(){
        super.printMessage();
        command.help();
    }
}
