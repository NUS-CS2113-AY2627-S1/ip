package kevie.commands;

import kevie.UserInterface;

/**
 * Command that prints all commands and their syntax.
 */
public class HelpCommand extends Command{

    private CommandParser parser;

    /**
     * Creates help command.
     *
     * @param ui User interface to print messages in
     * @param parser Parser with the command list
     */
    public HelpCommand(UserInterface ui, CommandParser parser) {
        super("help", ui);
        this.parser = parser;
    }

    @Override
    public boolean execute(String arg) {
        ui.botSpeak("Here is all the list of commands and their corresponding syntax: ");
        parser.listCommands();
        return false;
    }

    @Override
    public String syntax() {
        return "help";
    }

    @Override
    public String example() {
        return "help";
    }
}
