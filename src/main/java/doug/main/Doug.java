package doug.main;

import java.util.ArrayList;

import doug.command.Command;
import doug.errors.DougException;
import doug.parser.Parser;
import doug.storage.Storage;
import doug.task.TaskList;
import doug.ui.Ui;

/**
 * Coordinates the user interface, task storage, and command-processing loop for Doug.
 */
public class Doug {
    /** Storage used to load and save tasks. */
    private Storage storage;
    /** Tasks currently managed by the application. */
    private TaskList taskList;
    /** User interface used for all console input and output. */
    private Ui ui;

    /**
     * Creates a Doug application backed by the specified save file.
     * If the saved tasks cannot be loaded, the application starts with an empty task list.
     *
     * @param filePath path to the file used to load and save tasks
     */
    public Doug(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            taskList = new TaskList(storage.load());
        } catch (DougException e) {
            ui.printError("INIT ERROR: " + e.getMessage());
            taskList = new TaskList(new ArrayList<>());
        }
    }

    /**
     * Runs the application loop until the user executes an exit command.
     */
    public void run() {
        ui.printWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String input = ui.readInput();
                Command c = Parser.parseCommand(input);
                c.execute(taskList, ui, storage);
                isExit = c.isExit();
            } catch (DougException e) {
                ui.printError("REPLY ERROR: " + e.getMessage());
            } finally {
                ui.printLine();
            }
        }
    }

    /**
     * Starts Doug using the default task data file.
     *
     * @param args command-line arguments; not used by this application
     */
    public static void main(String[] args) {
        new Doug("data/task_list.txt").run();
    }
}
