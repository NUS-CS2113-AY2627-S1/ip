package hedy;

import hedy.command.Command;
import hedy.exception.HedyException;
import hedy.parser.Parser;
import hedy.storage.Storage;
import hedy.task.TaskList;
import hedy.ui.Ui;

/**
 * Hedy is a command-line chatbot that tracks todos, deadlines, and events.
 * User interaction, command parsing, the task list, and file saving each live
 * in their own class. A bad command is reported and does not crash the program.
 */
public class Hedy {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Sets up the user interface and loads tasks from the save file.
     * If the file cannot be read, Hedy starts with an empty list.
     *
     * @param filePath save file, relative to the folder where Hedy is run
     */
    public Hedy(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            if (storage.getLoadWarning() != null) {
                ui.showLoadingError(storage.getLoadWarning());
            }
        } catch (HedyException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Greets the user and runs commands until they say bye.
     * Each command is parsed into its own object and then executed.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }
            try {
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (HedyException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    /**
     * Starts Hedy and loads tasks from {@code data/duke.txt}.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        new Hedy("data/duke.txt").run();
    }
}
