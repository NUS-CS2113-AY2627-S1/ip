package sandy;

import java.io.IOException;
import java.nio.file.Path;

import sandy.command.Command;
import sandy.exception.SandyException;
import sandy.storage.Storage;

/**
 * Starts Sandy and coordinates the application components.
 */
public class Sandy {
    private static final Path DATA_FILE_PATH = Path.of("data", "sandy.txt");

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates Sandy with the default data file.
     */
    public Sandy() {
        this(DATA_FILE_PATH);
    }

    /**
     * Creates Sandy with the given data file.
     *
     * @param dataFilePath The path used to load and save tasks.
     */
    public Sandy(Path dataFilePath) {
        ui = new Ui();
        storage = new Storage(dataFilePath);
        tasks = new TaskList();
    }

    /**
     * Starts the command loop.
     */
    public void run() {
        ui.showWelcome();
        tasks = loadTasks();
        try (ui) {
            String command;
            while ((command = ui.readCommand()) != null) {
                ui.showLine();
                boolean isExit = false;
                try {
                    Command parsedCommand = Parser.parse(command);
                    parsedCommand.execute(tasks, ui, storage);
                    isExit = parsedCommand.isExit();
                } catch (SandyException exception) {
                    ui.showError(exception.getMessage());
                } finally {
                    ui.showLine();
                }
                if (isExit) {
                    break;
                }
            }
        }
    }

    /**
     * Starts the Sandy command loop.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Sandy().run();
    }

    /**
     * Loads saved tasks, reporting an error and returning an empty list if loading fails.
     *
     * @return The loaded tasks, or an empty task list after a loading error.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (IOException | SandyException exception) {
            ui.showLoadingError(exception.getMessage());
            return new TaskList();
        }
    }

}
