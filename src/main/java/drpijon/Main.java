package drpijon;

import drpijon.command.Command;
import drpijon.exception.DrPijonException;
import drpijon.parser.Parser;
import drpijon.storage.Storage;
import drpijon.task.TaskList;
import drpijon.ui.Ui;

/**
 * Runs the Dr. Pijon command-line task manager.
 */
public class Main {
    /** Default relative path used for persisted tasks. */
    private static final String TASK_FILE_PATH = "data/drpijon.txt";
    /** Console interface used to interact with the user. */
    private final Ui ui;
    /** File-backed storage used to load and save tasks. */
    private final Storage storage;
    /** In-memory task list used by the running application. */
    private final TaskList tasks;

    /**
     * Creates the application and loads its saved tasks.
     *
     * @param filePath path to the task data file
     */
    public Main(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = storage.load();
        } catch (DrPijonException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Starts the Dr. Pijon application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Main(TASK_FILE_PATH).run();
    }

    /**
     * Runs the application until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        runCommandLoop();
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private void runCommandLoop() {
        boolean isExit = false;
        while (!isExit && ui.hasNextLine()) {
            try {
                Command command = Parser.parse(ui.readLine());
                isExit = command.isExit();
                command.execute(tasks, ui, storage);
            } catch (DrPijonException e) {
                ui.showError(e.getMessage());
            } finally {
                if (!isExit) {
                    ui.showLineSeparator();
                }
            }
        }
    }
}
