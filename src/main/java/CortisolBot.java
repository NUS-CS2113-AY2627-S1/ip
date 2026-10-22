/**
 * Talks to the user and keeps the task list.
 * <p>
 * One object holds the three things a session needs: a {@link Ui} for the
 * console, a {@link Storage} for the file, and the tasks themselves. Nothing
 * here prints or reads directly -- every word the user sees goes through the
 * Ui -- so this class is left with the one job of deciding what each command
 * means.
 */
public class CortisolBot {

    /** Where the task list is kept between sessions. */
    private static final String DATA_FILE_PATH = "data/cortisolbot.txt";

    /** Everything the user types and sees. */
    private final Ui ui;

    /** The file the task list is read from and written to. */
    private final Storage storage;

    /**
     * Tasks held for this session.
     * <p>
     * A TaskList grows as needed, so there is no fixed ceiling on the number of
     * tasks the bot can hold, and it is what refuses a task number that refers
     * to nothing.
     */
    private final TaskList tasks;

    /**
     * Constructs a bot that keeps its task list in the named file.
     * <p>
     * Nothing is read or printed yet; the session proper begins with
     * {@link #run()}.
     *
     * @param filePath path to the data file.
     */
    public CortisolBot(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList();
    }

    /**
     * Greets the user, then reads and carries out commands until told to stop.
     */
    public void run() {
        ui.showWelcome();
        loadTasks();

        boolean isExiting = false;
        while (!isExiting) {
            String userInput = ui.readCommand();
            ui.showLine();

            try {
                // The Parser works out which command was asked for and the
                // command carries itself out, so this loop never needs to know
                // which one it is holding.
                Command command = Parser.parse(userInput);
                command.execute(tasks, ui, storage);
                isExiting = command.isExit();
            } catch (CortisolException e) {
                // The exception message is already phrased for the user.
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Fills the task list from the data file and reports what was found.
     * <p>
     * A file that cannot be opened is reported and then left behind: the
     * session begins with an empty list rather than refusing to start.
     */
    private void loadTasks() {
        try {
            tasks.addAll(storage.load());
            ui.showLoadReport(tasks.size(), storage.getSkippedLineCount());
        } catch (CortisolException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Starts a session.
     *
     * @param args command line arguments, which this program does not use.
     */
    public static void main(String[] args) {
        new CortisolBot(DATA_FILE_PATH).run();
    }
}
