package wenwen;

import java.nio.file.Path;
import java.util.ArrayList;

import wenwen.command.Command;
import wenwen.exception.WenwenException;
import wenwen.parser.Parser;
import wenwen.storage.Storage;
import wenwen.task.TaskList;
import wenwen.ui.Ui;

/**
 * Starts the Wenwen chatbot and coordinates its main components.
 */
public class Wenwen {
    private static final Path DATA_FILE_PATH = Path.of("data", "wenwen.txt");

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Wenwen instance that stores tasks at the given path.
     *
     * @param dataFilePath path of the task data file
     */
    public Wenwen(Path dataFilePath) {
        this(new Storage(dataFilePath), new Ui());
    }

    /**
     * Creates a Wenwen instance using the supplied storage and user interface.
     *
     * @param storage storage used to load and save tasks
     * @param ui user interface used for all input and output
     */
    public Wenwen(Storage storage, Ui ui) {
        this.storage = storage;
        this.ui = ui;
        this.tasks = loadTasks();
    }

    /**
     * Runs the chatbot until the input ends or the user enters the bye command.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (WenwenException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }

    /**
     * Launches Wenwen with its default data file.
     *
     * @param args command line arguments supplied by the runtime
     */
    public static void main(String[] args) {
        new Wenwen(DATA_FILE_PATH).run();
    }

    /**
     * Loads saved tasks and falls back to an empty list if loading fails.
     *
     * @return loaded tasks, or an empty task list after a loading error
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.loadTasks());
        } catch (WenwenException exception) {
            ui.showLoadingError(exception.getMessage());
            return new TaskList(new ArrayList<>());
        }
    }
}
