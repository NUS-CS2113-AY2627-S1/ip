package nick;

import java.io.IOException;

import nick.command.Command;
import nick.parser.Parser;
import nick.storage.Storage;
import nick.task.TaskList;
import nick.ui.Ui;

/**
 * A command line chatbot that lets the user add, list, mark, unmark, and delete tasks.
 * Tasks can be todos, deadlines, or events, and are saved to disk between runs.
 */
public class Nick {
    private static final String DATA_FILE = "data/nick.txt";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates the chatbot backed by the data file at the given path, loading any
     * previously saved tasks.
     *
     * @param filePath Relative path to the data file.
     */
    public Nick(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showError("I couldn't load your saved tasks, starting with an empty list.");
            tasks = new TaskList();
        }
    }

    /**
     * Runs the chatbot, reading and executing commands until an exit command.
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
            } catch (NickException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("I couldn't save your tasks: " + e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts the chatbot.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Nick(DATA_FILE).run();
    }
}
