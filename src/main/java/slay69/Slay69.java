package slay69;

import java.io.IOException;

import slay69.command.Command;
import slay69.storage.Storage;
import slay69.task.TaskList;
import slay69.ui.Ui;

/**
 * Coordinates the Slay69 chatbot's user interface, tasks, and storage.
 */
public class Slay69 {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final String loadingError;

    /**
     * Creates the chatbot and loads tasks from the specified file.
     * A loading error is retained so it can be shown after the welcome message.
     *
     * @param filePath path to the saved task file
     */
    public Slay69(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        String errorMessage = null;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (IOException | Slay69Exception e) {
            loadedTasks = new TaskList();
            errorMessage = e.getMessage();
        }
        tasks = loadedTasks;
        loadingError = errorMessage;
    }

    /**
     * Runs the command loop until the user exits or the input stream ends.
     */
    public void run() {
        try {
            ui.showGreeting();
            if (loadingError != null) {
                ui.showLoadingError(loadingError);
                return;
            }

            boolean isExit = false;
            while (!isExit && ui.hasNextCommand()) {
                String fullCommand = ui.readCommand();
                ui.showLine();

                try {
                    Command command = Parser.parse(fullCommand);
                    command.execute(tasks, ui, storage);
                    isExit = command.isExit();
                } catch (Slay69Exception e) {
                    ui.showError(e.getMessage());
                } catch (IOException e) {
                    ui.showSavingError(e.getMessage());
                } finally {
                    ui.showLine();
                }
            }
        } finally {
            ui.close();
        }
    }

    /**
     * Starts Slay69 using its default data file.
     *
     * @param args command-line arguments, which are ignored
     */
    public static void main(String[] args) {
        new Slay69("data/slay69.txt").run();
    }
}
