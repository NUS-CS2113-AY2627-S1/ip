package yanny.ui;

import java.io.IOException;

import yanny.command.CommandProcessor;
import yanny.exception.YannyException;
import yanny.storage.Storage;
import yanny.task.TaskList;

/**
 * Starts Yanny and coordinates its command loop.
 */
public class Yanny {
    /**
     * Starts Yanny and processes commands entered by the user.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showStartupScreen();
        try {
            CommandProcessor commandProcessor = createCommandProcessor(ui);
            runCommandLoop(ui, commandProcessor);
        } catch (YannyException exception) {
            ui.showCommandError(exception.getMessage());
        }
    }

    /**
     * Loads stored tasks and connects the components used by the command loop.
     *
     * @param ui the terminal interface used for command responses.
     * @return the command processor with its storage and task list.
     * @throws YannyException if existing task data cannot be loaded.
     */
    private static CommandProcessor createCommandProcessor(Ui ui) throws YannyException {
        try {
            Storage storage = new Storage();
            TaskList tasks = new TaskList(storage.loadTasks());
            return new CommandProcessor(ui, storage, tasks);
        } catch (IOException | IllegalArgumentException | SecurityException exception) {
            throw new YannyException("TASK DATA COULD NOT BE LOADED. CHECK FILE FORMAT AND PERMISSIONS.");
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     *
     * @param ui the terminal interface for reading commands and showing messages.
     * @param commandProcessor the component that processes user commands.
     */
    private static void runCommandLoop(Ui ui, CommandProcessor commandProcessor) {
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showBorder();

            if (command.trim().equalsIgnoreCase("bye")) {
                ui.showShutdownMessage();
                break;
            }

            try {
                commandProcessor.processCommand(command);
            } catch (YannyException exception) {
                ui.showCommandError(exception.getMessage());
            }
            ui.showBorder();
        }
    }
}
