package kevie.commands;

import kevie.UserInterface;
import kevie.exceptions.CmdNoExistException;
import kevie.exceptions.KevieException;

/**
 * Parses command keywords in text and executes them.
 * Contains a list of all commands for parsing.
 */
public class CommandParser {
    private final int MAX_COMMAND_NO = 100;
    private Command[] commands;
    private int commands_length;

    private UserInterface ui;

    /**
     * Creates a parser
     *
     * @param ui User interface to print messages in
     */
    public CommandParser(UserInterface ui){
        commands = new Command[MAX_COMMAND_NO];
        commands_length = 0;
        this.ui = ui;
    }

    /**
     * Register a command into the parser
     *
     * @param command Command to be registered
     */
    public void register(Command command){
        commands[commands_length] = command;
        commands_length++;
    }

    /**
     * Parse and executes a command input string
     *
     * @param input The command string inputted by the user
     * @return Whether Kevie would be terminated once the command finished executing.
     * @throws KevieException Thrown if command execution failed.
     */
    public boolean parseAndExecute(String input) throws KevieException {
        String[] inputArgs = input.trim().split(" ", 2);
        for (int i = 0; i < commands_length; i++){
            if (inputArgs[0].toLowerCase().equals(commands[i].getId())){
                String cmdInputArg = inputArgs.length < 2? null : inputArgs[1];
                return commands[i].execute(cmdInputArg);
            }
        }
        throw new CmdNoExistException(inputArgs[0].toLowerCase(), ui);
    }

    /**
     * Prints all commands in a list.
     */
    public void listCommands(){
        for (int i = 0; i < commands_length; i++){
            ui.botSpeak((i+1) + ". " + commands[i].getId() + " (" + commands[i].syntax() + ")", true);
        }
    }
}
