package eric;

import java.nio.file.Path;

import eric.command.Command;
import eric.command.ExitCommand;
import eric.exception.EricException;
import eric.parser.Parser;
import eric.storage.Storage;
import eric.storage.StorageException;
import eric.task.TaskList;
import eric.ui.Ui;

/**
 * Eric, a command-line chatbot that manages a simple task list. It reads commands from the user
 * until the user types "bye", and keeps the tasks in a file so that they are there the next time.
 * The work is shared between {@link Ui} (talks to the user), {@link Parser} (understands commands),
 * {@link TaskList} (holds the tasks) and {@link Storage} (saves and loads the tasks).
 */
public class Eric {
    /** Where tasks are saved, relative to the folder Eric is run from. Path.of keeps it OS-independent. */
    private static final Path DATA_FILE_PATH = Path.of("data", "duke.txt");

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates Eric with an empty task list. The saved tasks are loaded when {@link #run()} starts.
     *
     * @param dataFilePath Location of the file in which the tasks are saved.
     */
    public Eric(Path dataFilePath) {
        ui = new Ui();
        storage = new Storage(dataFilePath);
        tasks = new TaskList();
    }

    /**
     * Starts Eric: greets the user, loads the saved tasks, and carries out the user's commands until
     * a command ends the program, which happens when the user types "bye" or the input ends.
     */
    public void run() {
        ui.showWelcome();

        tasks = loadTasks();

        boolean isExit = false;
        while (!isExit) {
            try {
                Command command = Parser.parse(readInput());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (EricException exception) {
                ui.showError(exception.getMessage(), exception.getFix());
            }
        }

        ui.close();
    }

    /**
     * Runs Eric, saving the tasks in the default data file.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Eric(DATA_FILE_PATH).run();
    }

    /**
     * Returns the next command typed by the user, or "bye" if the input has ended (e.g. piped
     * input without "bye"), so that the program exits normally instead of crashing.
     */
    private String readInput() {
        return ui.hasNextCommand() ? ui.readCommand() : ExitCommand.COMMAND_WORD;
    }

    /**
     * Returns the tasks saved in the data file, or an empty task list if nothing has been saved yet.
     * If the file is unusable, it is rejected as a whole: the problem is reported to the user and
     * Eric starts with an empty list instead of failing.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (StorageException exception) {
            reportRejectedDataFile("because " + exception.getMessage());
            return new TaskList();
        }
    }

    /** Tells the user that the data file was not loaded, why, and how to keep it. */
    private void reportRejectedDataFile(String reason) {
        ui.showError("I couldn't load your saved tasks from " + storage.getFilePath() + " " + reason,
                "Starting with an empty list. The file will be replaced the next time your tasks change."
                + " To keep it, close Eric, then fix or move the file.");
    }
}
