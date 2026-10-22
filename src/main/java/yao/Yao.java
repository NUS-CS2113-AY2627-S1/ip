package yao;

import yao.command.Command;

/**
 * Main class for the Yao chatbot application.
 * Coordinates user interactions, task list operations, and file storage.
 */
public class Yao {
    private static final String DEFAULT_FILE_PATH = "data/Yao.txt";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Initializes the Yao application with a specified data file path.
     *
     * @param filePath Relative or absolute path to the data storage file.
     */
    public Yao(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (YaoException e) {
            ui.showLoadingError(e.getMessage());
            this.tasks = new TaskList();
        }
    }

    /**
     * Initializes the Yao application using the default data file path.
     */
    public Yao() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Runs the main command processing loop for the Yao chatbot.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (YaoException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Main entry point for the Yao chatbot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new Yao(DEFAULT_FILE_PATH).run();
    }
}
