package kevie.commands;

import kevie.Kevie;
import kevie.UserInterface;
import kevie.tasks.TaskList;

/**
 * Command that terminates Kevie.
 */
public class ByeCommand extends Command {

    /**
     * Creates the bye command.
     *
     * @param ui User interface to print messages in.
     */
    public ByeCommand(UserInterface ui) {
        super("bye", ui);
    }

    @Override
    public boolean execute(String arg) {
        ui.botSpeak("Bye bye! See you later!");
        return true;
    }

    @Override
    public String syntax() {
        return "bye";
    }

    @Override
    public String example() {
        return "bye";
    }

}
