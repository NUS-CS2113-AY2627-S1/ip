package alfred;

import java.io.IOException;
import java.nio.file.Path;

import alfred.command.Command;
import alfred.exception.AlfredException;

/**
 * Runs the Alfred command-line chatbot.
 */
public class Alfred {

    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates Alfred and loads tasks from the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Alfred(String filePath) {
        this.parser = new Parser();
        this.storage = new Storage(Path.of(filePath));
        this.ui = new Ui();

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load(ui::showMessage));
        } catch (IOException exception) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Starts Alfred and processes commands until the user exits.
     */
    public void run() {
        ui.showWelcomeMessage();
        boolean isExit = false;

        try (ui) {
            while (!isExit && ui.hasNextCommand()) {
                String fullCommand = ui.readCommand();
                ui.showDivider();

                try {
                    Command command = parser.parse(fullCommand);
                    command.execute(tasks, ui, storage);
                    isExit = command.isExit();
                } catch (AlfredException exception) {
                    ui.showMessage(exception.getMessage());
                    ui.showHelpPrompt();
                } catch (IOException exception) {
                    ui.showSavingError();
                } finally {
                    ui.showDivider();
                }
            }
        }
    }

    /**
     * Starts Alfred using the default data file.
     *
     * @param args Command-line arguments. Alfred does not use them.
     */
    public static void main(String[] args) {
        new Alfred("data/alfred.txt").run();
    }
}
