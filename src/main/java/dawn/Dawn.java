package dawn;

import dawn.command.Command;
import dawn.exception.DawnException;
import dawn.exception.StorageException;
import dawn.parser.Parser;
import dawn.storage.Storage;
import dawn.task.TaskList;
import dawn.ui.DawnUi;

/**
 * Starts Dawn and coordinates the console application's lifecycle.
 */
public class Dawn {
    private final DawnUi ui;
    private final Storage storage;
    private TaskList tasks;
    /**
     * Stays true after any storage problem, even if a later save succeeds.
     */
    private boolean hadStorageProblem;

    /**
     * Initializes Dawn with persistent storage at the given file path.
     *
     * @param filePath the path to the tasks file.
     */
    public Dawn(String filePath) {
        this(new Storage(filePath), new DawnUi());
    }

    /**
     * Accepts a storage service and UI so sessions can be checked with simulated failures.
     */
    Dawn(Storage storage, DawnUi ui) {
        this.ui = ui;
        this.storage = storage;
        try {
            this.tasks = this.storage.load();
        } catch (DawnException e) {
            hadStorageProblem = true;
            ui.showError(e.getMessage());
            this.tasks = new TaskList();
        }
        for (String warning : storage.getLoadWarnings()) {
            hadStorageProblem = true;
            ui.showStorageWarning(warning);
        }
    }

    /**
     * Initializes Dawn with the default storage file path.
     */
    public Dawn() {
        this("data/dawn.txt");
    }

    /**
     * Runs the main application event loop until the exit command is received.
     */
    public void run() {
        ui.showIntro();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand == null) {
                break;
            }
            ui.showLine();
            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (DawnException e) {
                if (e instanceof StorageException) {
                    hadStorageProblem = true;
                }
                ui.showError(e.getMessage());
            } finally {
                if (!isExit) {
                    ui.showLine();
                }
            }
        }
        if (isExit) {
            ui.showBye(hadStorageProblem);
        }
    }

    /**
     * Entry point for the Dawn application.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Dawn().run();
    }
}
