package kevie.exceptions;

import kevie.Kevie;
import kevie.UserInterface;

/**
 * Exception thrown when command keyword typed cannot be parsed to a command.
 */
public class CmdNoExistException extends KevieException{

    /**
     * Creates a Command non-existence exception.
     *
     * @param attempt The command keyword typed
     * @param ui User interface to print messages in
     */
    public CmdNoExistException(String attempt, UserInterface ui) {
        super("Did not understand", "\"" + attempt + "\" is not a valid command", ui);
    }

    @Override
    public void printMessage() {
        super.printMessage();
        ui.botSpeak("Say \"help\" if you need a list of commands.", true);
    }
}
