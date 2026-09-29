/**
 * Entry point of the Shaheer chatbot, which manages a list of tasks
 * (todos, deadlines and events) and saves them between sessions.
 */
public class Shaheer {
    private static final String FILE_PATH = "data/shaheer.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /** Creates the chatbot, loading any tasks previously saved at {@code filePath}. */
    public Shaheer(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = loadTasks();
    }

    /** Starts the chatbot, saving tasks to data/shaheer.txt relative to the current folder. */
    public static void main(String[] args) {
        new Shaheer(FILE_PATH).run();
    }

    /** Reads and executes commands until the user exits, then saves the tasks. */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String input = ui.readCommand();
            ui.showLine();
            isExit = executeCommand(input);
            ui.showLine();
        }
        saveTasks();
    }

    // ---------------------------------------------------------------- Command execution

    /**
     * Runs the command in one line of user input and reports any input error to the user.
     *
     * @return true if the user asked to exit.
     */
    private boolean executeCommand(String input) {
        try {
            Command command = createCommand(input);
            command.execute(tasks, ui, storage);
            return command.isExit();
        } catch (ShaheerException e) {
            ui.showError(e.getMessage());
            return false;
        }
    }

    /**
     * Converts one line of user input into the matching command.
     *
     * @throws ShaheerException if the command word is unknown or its arguments are invalid.
     */
    private static Command createCommand(String input) throws ShaheerException {
        String commandWord = Parser.getCommandWord(input);
        String args = Parser.getArguments(input);
        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(Parser.parseTaskIndex(args, commandWord));
        case "unmark":
            return new UnmarkCommand(Parser.parseTaskIndex(args, commandWord));
        case "delete":
            return new DeleteCommand(Parser.parseTaskIndex(args, commandWord));
        case "find":
            return new FindCommand(Parser.parseFindKeyword(args));
        case "todo":
            return new AddCommand(Parser.parseTodo(args));
        case "deadline":
            return new AddCommand(Parser.parseDeadline(args));
        case "event":
            return new AddCommand(Parser.parseEvent(args));
        default:
            throw new ShaheerException("I'm sorry, but I don't know what that means");
        }
    }

    // ---------------------------------------------------------------- Storage

    /** Loads the saved tasks, or starts with an empty list (after telling the user) if they cannot be read. */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (ShaheerException e) {
            ui.showStorageError(e.getMessage());
            return new TaskList();
        }
    }

    private void saveTasks() {
        try {
            storage.save(tasks.getAll());
        } catch (ShaheerException e) {
            ui.showStorageError(e.getMessage());
        }
    }
}
