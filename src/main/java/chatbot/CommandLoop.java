package chatbot;

import java.util.Scanner;

import chatbot.exception.AgentCTException;

/** Coordinates parsing and execution of commands until the session ends. */
public class CommandLoop {
    private final TaskList tasks;
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;

    /** Creates a command loop with the services needed to execute commands.
     *
     * @param tasks task list to modify
     * @param ui user interaction handler
     * @param storage task persistence handler
     * @param parser command parser
     */
    public CommandLoop(TaskList tasks, Ui ui, Storage storage, Parser parser) {
        this.tasks = tasks;
        this.ui = ui;
        this.storage = storage;
        this.parser = parser;
    }

    /** Processes commands from the given input until exit or end of input.
     *
     * @param scanner source of user commands
     */
    public void run(Scanner scanner) {
        String command;
        while ((command = ui.readCommand(scanner)) != null) {
            Command executableCommand = null;
            try {
                executableCommand = parser.parseCommand(command);
                if (!executableCommand.isExit()) {
                    ui.showSeparator();
                }
                executableCommand.execute(tasks, ui, storage);
            } catch (AgentCTException exception) {
                ui.showSeparator();
                ui.showError(exception.getMessage());
            } finally {
                if (executableCommand == null || !executableCommand.isExit()) {
                    ui.showSeparator();
                }
            }
            if (executableCommand != null && executableCommand.isExit()) {
                break;
            }
        }
    }
}
