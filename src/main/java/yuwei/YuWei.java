package yuwei;

import yuwei.task.Task;

import java.nio.file.Path;

/**
 * A command-line chatbot that keeps track of a list of tasks entered by the user.
 * Tasks are saved to {@code ./data/yuwei.txt} after every command and loaded again on startup.
 *
 * <p>This class only coordinates the work; the details are handled by the objects it holds:
 * {@link Ui} talks to the user, {@link Parser} makes sense of commands, {@link TaskList}
 * holds the tasks and {@link Storage} reads and writes the data file.
 *
 * <p>Supported commands: {@code list}, {@code find}, {@code mark}, {@code unmark},
 * {@code delete}, {@code todo}, {@code deadline}, {@code event} and {@code bye}.
 */
public class YuWei {
    private static final String EXIT_COMMAND = "bye";
    /** Relative path built with Path.of, so it works on any computer and any OS. */
    private static final Path DATA_FILE = Path.of("data", "yuwei.txt");

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a chatbot that saves its tasks to {@code filePath}.
     * The saved tasks are not loaded until {@link #run()} is called.
     */
    public YuWei(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();
    }

    /** Starts the chatbot, saving tasks to {@code data/yuwei.txt} in the current folder. */
    public static void main(String[] args) {
        new YuWei(DATA_FILE).run();
    }

    /** Greets the user, then keeps reading and executing commands until the user types {@code bye}. */
    public void run() {
        ui.showWelcome();
        // Loaded after the greeting, so any problems with the file are reported under it.
        loadTasks();

        String fullCommand = ui.readCommand();
        while (!fullCommand.equals(EXIT_COMMAND)) {
            ui.showLine();
            try {
                executeCommand(fullCommand);
                // Saving after every command is simplest and guarantees no change is lost.
                storage.save(tasks.getTasks());
            } catch (YuWeiException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
            ui.showBlankLine();
            fullCommand = ui.readCommand();
        }

        ui.showGoodbye();
    }

    /** Loads the saved tasks, keeping the empty list if the data file cannot be read. */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
        } catch (YuWeiException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Executes the command contained in {@code fullCommand}, updating the task list if needed.
     */
    private void executeCommand(String fullCommand) throws YuWeiException {
        String commandWord = Parser.getCommandWord(fullCommand);

        switch (commandWord) {
            case "list" -> ui.showTaskList(tasks.getTasks());
            case "find" -> ui.showMatchingTasks(tasks.find(
                    Parser.getArgument(fullCommand, "Please tell me what to search for.")));
            case "mark" -> ui.showTaskMarked(tasks.mark(Parser.parseTaskIndex(fullCommand)));
            case "unmark" -> ui.showTaskUnmarked(tasks.unmark(Parser.parseTaskIndex(fullCommand)));
            case "delete" -> {
                Task removedTask = tasks.delete(Parser.parseTaskIndex(fullCommand));
                ui.showTaskDeleted(removedTask, tasks.size());
            }
            case "todo", "deadline", "event" -> {
                String argument = Parser.getArgument(fullCommand,
                        "The description of a " + commandWord + " cannot be empty.");
                Task task = Parser.parseTask(commandWord, argument);
                tasks.add(task);
                ui.showTaskAdded(task, tasks.size());
            }
            default -> throw new YuWeiException("I'm sorry, but I don't know what that means :-(");
        }
    }
}
