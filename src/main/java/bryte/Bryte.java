package bryte;

import bryte.command.Command;
import bryte.exception.BryteException;

/**
 * Main entry point for the Bryte task management application.
 * Initializes the required components and runs the main event loop.
 */
public class Bryte {
    private static final String DATA_DIR = "data";
    private static final String FILE_NAME = "bryte.txt";

    private TaskList tasks;
    private Ui ui;
    private Storage storage;

    /**
     * Constructs a new Bryte instance.
     *
     * @param dirPath The directory path for data storage.
     * @param filePath The file name for data storage.
     */
    public Bryte(String dirPath, String filePath) {
        ui = new Ui();
        storage = new Storage(dirPath, filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (BryteException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Main method to start the Bryte application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Bryte(DATA_DIR, FILE_NAME).run();
    }

    /**
     * Runs the main loop of the Bryte application.
     */
    public void run() {
        boolean isRunning = true;

        ui.showWelcome();

        while (isRunning) {
            String fullCommand = ui.readCommand();
            if (fullCommand.isEmpty()) {
                continue;
            }

            try {
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isRunning = !c.isExit();
            } catch (BryteException e) {
                ui.showDivider();
                ui.showError(e.getMessage());
                ui.showDivider();
            }
        }

        ui.showDivider();
        ui.showGoodbye();
        ui.showDivider();
    }
}
