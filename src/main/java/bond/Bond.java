package bond;

import java.nio.file.Path;

import bond.command.Command;
import bond.exception.BondException;
import bond.exception.StorageException;
import bond.parser.Parser;
import bond.storage.Storage;
import bond.task.TaskList;
import bond.ui.Ui;

/**
 * Coordinates Bond's user interface, command parsing, and task operations.
 */
public class Bond {

    private static final Path DATA_FILE_PATH = Path.of("data", "bond.txt");

    private final Storage storage;
    private final Ui ui;
    /** Tasks loaded at the start of run, before any command is processed. */
    private TaskList taskList;

    /**
     * Creates an application that stores its tasks at the specified relative path.
     *
     * @param filePath Relative path of the task data file.
     */
    public Bond(Path filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
    }

    /**
     * Starts Bond and processes user commands until the user enters "bye" or input ends.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Bond(DATA_FILE_PATH).run();
    }

    /**
     * Loads tasks and runs one console session, closing input when the session ends.
     * Exits without processing commands if the task archive cannot be loaded.
     */
    public void run() {
        ui.showWelcomeMessage();

        try {
            taskList = storage.loadTasks();
            processCommands();
        } catch (StorageException e) {
            ui.showError(e.getMessage(), e.getCorrection());
            ui.showDivider();
        } finally {
            ui.close();
        }
    }

    /**
     * Reads and executes commands until the user exits Bond or input ends.
     */
    private void processCommands() {
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String command = ui.readCommand();

            ui.showDivider();

            try {
                Command parsedCommand = Parser.parse(command, taskList.getSize());
                parsedCommand.execute(taskList, ui, storage);
                isExit = parsedCommand.isExit();
            } catch (BondException e) {
                ui.showError(e.getMessage(), e.getCorrection());
            } finally {
                ui.showDivider();
            }
        }
    }
}
