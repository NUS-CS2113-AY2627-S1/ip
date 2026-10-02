import command.Command;
import display.Ui;
import exception.AtlasException;
import list.TaskList;
import parser.Parser;
import storage.Storage;

/**
 * The main entry point for the Atlas task management application.
 * Initializes the user interface, persistent storage, and task list.
 */
public class Atlas {
    private static final String FILE_PATH = "./data/atlas.txt";
    private final Storage storage;
    private final Ui ui;
    private final TaskList list;

    /**
     * Constructs a new Atlas application instance, initializing the UI,
     * loading stored tasks from the filesystem, and preparing the task list.
     */
    public Atlas() {
        this.ui = new Ui();
        this.storage = new Storage(FILE_PATH);
        this.list = new TaskList(storage.load());
    }

    /**
     * Starts the main application loop and handles user input until exit command is issued.
     */
    public static void main(String[] args) {
        new Atlas().run();
    }

    public void run() {
        ui.displayLine();
        ui.displayWelcomeBanner();
        ui.displayLine();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readInput();
                Command c = Parser.parse(fullCommand);
                c.execute(list, ui, storage);
                isExit = c.isExit();
            } catch (AtlasException e) {
                ui.displayLine();
                System.out.println(" " + e.getMessage());
                ui.displayLine();
            }
        }
    }
}