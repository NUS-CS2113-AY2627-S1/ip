package foodielover;

import foodielover.command.Command;
import foodielover.parser.Parser;
import foodielover.storage.Storage;
import foodielover.task.TaskList;
import foodielover.ui.Ui;

/**
 * Coordinates application lifecycle and executes user commands for the Foodielover chatbot.
 */
public class Foodielover {
    /** Default file path for storing task data. */
    private static final String DEFAULT_FILE_PATH = "./data/foodielover.txt";

    /** Storage handler for persisting and loading task data. */
    private final Storage storage;

    /** In-memory task list. */
    private TaskList tasks;

    /** User interface handler for input and output interactions. */
    private final Ui ui;

    /**
     * Constructs a Foodielover application instance with the default file path.
     */
    public Foodielover() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Constructs a Foodielover application instance with the specified file path.
     *
     * @param filePath Relative or absolute path to the task data storage file.
     */
    public Foodielover(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(storage.load());
        for (String corruptedLine : storage.getCorruptedLines()) {
            ui.showCorruptedLineWarning(corruptedLine);
        }
    }

    /**
     * Runs the main execution loop of the Foodielover application.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit && ui.hasCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (FoodieloverException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts the Foodielover application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        new Foodielover(DEFAULT_FILE_PATH).run();
    }
}