package floppy;

import java.nio.file.Path;
import java.util.List;

import floppy.command.Command;
import floppy.task.Task;

/**
 * Floppy is a command line chatbot with the personality of a 1.44 MB floppy disk:
 * it whirrs, it clicks, and it is delighted to be useful again after decades in a drawer.
 *
 * <p>Floppy tracks three kinds of task -- todos, deadlines and events -- lists them on
 * demand, records which ones are done, deletes them when asked, and saves them to disk
 * so they survive a restart. It exits on {@code bye}.
 *
 * <p>This class holds the pieces together and runs the main loop. The work itself belongs
 * to {@link Ui}, {@link Storage}, {@link Parser}, {@link TaskList} and the
 * {@link Command} classes.
 */
public class Floppy {

    /**
     * Where Floppy keeps its tasks between runs. The path is relative, so it resolves
     * against the folder Floppy is run from, and it is built from separate parts so
     * that the right folder separator is used on every operating system.
     */
    private static final Path DATA_FILE = Path.of("data", "floppy.txt");

    /** Loads and saves the tasks. */
    private final Storage storage;

    /** Reads the user's commands and prints Floppy's replies. */
    private final Ui ui;

    /** The tasks Floppy is holding. Filled in by {@link #run()} when the saved tasks load. */
    private TaskList tasks;

    /**
     * Constructs a chatbot that keeps its tasks in the given file.
     *
     * @param filePath where the tasks are saved between runs.
     */
    public Floppy(Path filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
    }

    /**
     * Starts the chatbot and runs it until the user says {@code bye} or the input runs out.
     */
    public void run() {
        ui.showGreeting();
        tasks = loadTasks();

        // hasNextCommand() is false once the input runs out, which happens when
        // input is piped in from a file rather than typed.
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String fullCommand = ui.readCommand();

            try {
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (FloppyException e) {
                ui.showProblem(e.getMessage());
            }
        }

        ui.showFarewell();
    }

    /**
     * Runs Floppy.
     *
     * @param args command line arguments, which Floppy does not use.
     */
    public static void main(String[] args) {
        new Floppy(DATA_FILE).run();
    }

    /**
     * Loads the saved tasks. Warns the user about any unreadable lines that were skipped,
     * and starts with no tasks if the data file cannot be read at all.
     *
     * @return the tasks to start with.
     */
    private TaskList loadTasks() {
        List<Task> savedTasks;
        try {
            savedTasks = storage.load();
        } catch (FloppyException e) {
            ui.showProblem(e.getMessage());
            return new TaskList();
        }

        List<Integer> skippedLineNumbers = storage.getSkippedLineNumbers();
        if (!skippedLineNumbers.isEmpty()) {
            ui.showProblem("Some of " + DATA_FILE + " was unreadable (line numbers " + skippedLineNumbers
                    + "), so I skipped those lines. The original is backed up at "
                    + storage.getBackupPath() + ".");
        }

        if (!savedTasks.isEmpty()) {
            ui.showTasksLoaded(savedTasks.size());
        }
        return new TaskList(savedTasks);
    }
}
