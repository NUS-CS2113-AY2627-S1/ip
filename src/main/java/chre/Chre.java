package chre;

import chre.ui.Ui;
import chre.parser.Parser;
import chre.data.TaskList;
import chre.storage.Storage;
import chre.exception.ChreException;
import chre.command.Command;

/**
 * Chre is a simple chatbot that manages tasks.
 * Orchestrates the Ui, Parser, TaskList, and Storage components.
 */
public class Chre {
    /**
     * Main entry point for the Chre chatbot application.
     * Coordinates user input, task management, storage, and output.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
        Storage storage = new Storage();
        TaskList taskList;

        try {
            taskList = new TaskList(storage.load());
        } catch (ChreException e) {
            ui.showError(e.getMessage());
            taskList = new TaskList();
        }

        ui.displayWelcome();

        boolean isExit = false;
        while (!isExit) {
            try {
                String userInput = ui.readCommand();
                ui.showSeparator();

                Command command = parser.parse(userInput);
                command.execute(taskList, ui, storage);
                isExit = command.isExit();

                ui.showSeparator();
            } catch (ChreException e) {
                ui.showError(e.getMessage());
                ui.showSeparator();
            }
        }

        ui.displayFarewell();
        ui.close();
    }
}
