package ui;

import commands.Command;
import exception.SamSquareException;
import parser.Parser;
import storage.Storage;
import tasks.TaskList;

/**
 * Coordinates command handling, task changes, storage, and console interaction.
 */
public class SamSquare {
    private final Ui ui = new Ui();

    /**
     * Starts the console task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new SamSquare().run();
    }

    /**
     * Shows the greeting and processes commands until the user exits.
     */
    public void run() {
        try (ui) {
            ui.showGreeting();
            runTaskManager();
        }
    }

    /**
     * Loads saved tasks and dispatches commands, saving successful changes.
     */
    private void runTaskManager() {
        TaskList tasks = new TaskList(Storage.load());

        while (true) {
            try {
                Command command = new Parser(ui.readCommand()).parseCommand();
                command.execute(tasks, ui);
                if (command.isExit()) {
                    return;
                }
            } catch (SamSquareException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
