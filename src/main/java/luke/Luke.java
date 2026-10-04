package luke;

import java.io.IOException;
import java.nio.file.Path;

import luke.command.Command;
import luke.exception.LukeException;
import luke.parser.Parser;
import luke.storage.Storage;
import luke.task.TaskList;
import luke.ui.Ui;

/**
 * Runs the Luke chatbot's command-line interface.
 */
public class Luke {
    private static final String DEFAULT_DATA_PATH = Path.of("data", "luke.txt").toString();

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Luke using the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Luke(String filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList();
        ui = new Ui();
    }

    /**
     * Starts Luke and processes commands until the user exits.
     *
     * @param args Command-line arguments; not used.
     */
    public static void main(String[] args) {
        new Luke(DEFAULT_DATA_PATH).run();
    }

    /**
     * Loads saved tasks and processes commands until the user exits.
     */
    public void run() {

        try {
            storage.load(tasks);
        } catch (IOException e) {
            ui.showError("Unable to load tasks.");
        }

        ui.showWelcome();

        boolean isExit = false;

        while (!isExit) {
            String input = ui.readCommand();
            boolean isSeparatorShown = false;

            try {
                Command command = Parser.parse(input);
                isExit = command.isExit();

                if (!isExit) {
                    ui.showHorizontalLine();
                    isSeparatorShown = true;
                    command.execute(tasks, ui, storage);
                }
            } catch (LukeException e) {
                if (!isSeparatorShown) {
                    ui.showHorizontalLine();
                }
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("Unable to save tasks.");
            }

            if (!isExit) {
                ui.showHorizontalLine();
            }
        }

        ui.showGoodbye();
    }
}
