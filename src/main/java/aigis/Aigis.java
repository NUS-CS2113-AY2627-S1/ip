package aigis;

/**
 * A simple command-line task manager.
 */
public class Aigis implements AutoCloseable {
    private static final String DEFAULT_FILE_PATH = "data/aigis.txt";

    /** The persistence component used by the application. */
    private final Storage storage;

    /** The task collection used by the application. */
    private final TaskList tasks;

    /** The console interaction component used by the application. */
    private final Ui ui;

    /** The command parser used by the application. */
    private final Parser parser;

    /**
     * Creates an Aigis application using the specified data file.
     *
     * @param filePath The path of the file used to load and save tasks.
     */
    public Aigis(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        tasks = storage.load();
        parser = new Parser();
    }

    /**
     * Runs the command-line task manager until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        try {
            runCommandLoop();
        } finally {
            storage.save(tasks);
        }
        ui.showGoodbye();
    }

    /**
     * Starts the Aigis command-line task manager.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        try (Aigis aigis = new Aigis(DEFAULT_FILE_PATH)) {
            aigis.run();
        }
    }

    /**
     * Closes the application's console input.
     */
    @Override
    public void close() {
        ui.close();
    }

    /**
     * Reads and processes commands until the user exits or input is exhausted.
     */
    private void runCommandLoop() {
        String input;
        while ((input = ui.readCommand()) != null) {
            ui.showLine();
            try {
                Command command = parser.parse(input);
                command.execute(tasks, ui, storage);
                if (command.isExit()) {
                    break;
                }
            } catch (AigisException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }
}
