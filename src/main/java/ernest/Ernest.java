package ernest;

import ernest.command.Command;
import ernest.exception.ErnestException;
import ernest.parser.Parser;
import ernest.storage.Storage;
import ernest.task.TaskList;
import ernest.ui.Ui;

/**
 * Runs Ernest, a simple command-line task manager.
 */
public final class Ernest {
    private static final String DEFAULT_DATA_FILE_PATH = "data/ernest.txt";

    /** Storage used to load and save tasks. */
    private final Storage storage;

    /** Task list managed by this Ernest instance. */
    private final TaskList taskList;

    /** User interface used for console input and output. */
    private final Ui ui;

    /**
     * Creates an Ernest task manager backed by a specified data file.
     *
     * @param filePath path of the task data file.
     */
    public Ernest(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        Storage.LoadResult loadResult = storage.loadTasks(TaskList.getMaximumTaskCapacity());
        this.taskList = new TaskList(loadResult.tasks());
        showTaskLoadingStatus(loadResult.status());
    }

    /**
     * Starts the Ernest command-line task manager.
     *
     * @param args command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Ernest(DEFAULT_DATA_FILE_PATH).run();
    }

    /**
     * Runs the command loop until the user exits or input ends.
     */
    public void run() {
        try {
            ui.showWelcomeMessage();
            runChat();
        } finally {
            ui.close();
        }
    }

    /**
     * Reads and processes commands until the user exits or input ends.
     */
    private void runChat() {
        while (ui.hasNextCommand()) {
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(taskList, ui, storage);
                if (command.isExit()) {
                    return;
                }
            } catch (ErnestException exception) {
                ui.showErrorMessage(exception.getMessage());
            }
            ui.showHorizontalLine();
        }
    }

    /**
     * Shows any warning associated with loading tasks from storage.
     *
     * @param status outcome of loading tasks from storage.
     */
    private void showTaskLoadingStatus(Storage.LoadStatus status) {
        switch (status) {
            case SUCCESS:
                break;
            case INVALID_HEADER:
                ui.showInvalidStorageHeaderWarning();
                break;
            case PARTIAL_LOAD:
                ui.showPartialTaskLoadWarning();
                break;
            case LOAD_FAILURE:
                ui.showTaskLoadFailureWarning();
                break;
        }
    }
}
