package nova;

import nova.task.Task;

/**
 * Represents Nova, a command-line task list assistant with a sarcastic,
 * theatrical personality. Nova manages a list of todos, deadlines and events
 * that can be added, marked, unmarked, deleted, and listed through simple
 * text commands.
 *
 * This class only wires the other components together and runs the
 * command loop
 */
public class Nova {

    private static final String DEFAULT_FILE_PATH = "./data/nova.txt";

    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates a Nova instance that stores its tasks at the given file path.
     * If saved tasks cannot be loaded, Nova starts with an empty list.
     */
    public Nova(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (NovaException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs Nova's command loop until the user enters "bye".
     * Tasks are saved after every command so nothing is lost on a crash.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();
            try {
                isRunning = executeCommand(input);
            } catch (NovaException e) {
                ui.showError(e.getMessage());
            }
            save();
        }

        ui.showGoodbye();
    }

    /**
     * Carries out a single line of user input.
     *
     * false if the user asked to exit, true otherwise.
     * throws NovaException if the input is invalid.
     */
    private boolean executeCommand(String input) throws NovaException {
        String commandWord = Parser.getCommandWord(input);

        switch (commandWord) {
            case "bye":
                return false;
            case "list":
                ui.showTaskList(tasks.getAll());
                break;
            case "mark":
                markTask(Parser.parseIndex(input));
                break;
            case "unmark":
                unmarkTask(Parser.parseIndex(input));
                break;
            case "delete":
                Task removed = tasks.delete(Parser.parseIndex(input));
                ui.showDeleted(removed, tasks.size());
                break;
            case "todo":
                addTask(Parser.parseTodo(input));
                break;
            case "find":
                ui.showFoundTasks(tasks.find(Parser.parseKeyword(input)));
                break;
            case "deadline":
                addTask(Parser.parseDeadline(input));
                break;
            case "event":
                addTask(Parser.parseEvent(input));
                break;
            case "":
                break; // Blank line: ignore it quietly.
            default:
                throw new NovaException(
                        "I don't recognize that command. Even I have my limits.");
        }
        return true;
    }

    /**
     * Adds a task to the list and confirms it to the user.
     */
    private void addTask(Task task) {
        tasks.add(task);
        ui.showAdded(task, tasks.size());
    }

    /**
     * Marks the task at the given index as done.
     */
    private void markTask(int index) throws NovaException {
        Task task = tasks.get(index);
        if (task.isDone()) {
            ui.showAlreadyMarked();
            return;
        }
        task.markAsDone();
        ui.showMarked(index, task);
    }

    /**
     * Marks the task at the given index as not done.
     */
    private void unmarkTask(int index) throws NovaException {
        Task task = tasks.get(index);
        if (!task.isDone()) {
            ui.showAlreadyUnmarked();
            return;
        }
        task.markAsNotDone();
        ui.showUnmarked(index, task);
    }

    /**
     * Writes the current tasks to disk, warning the user if that fails.
     */
    private void save() {
        try {
            storage.save(tasks);
        } catch (NovaException e) {
            ui.showSavingError();
        }
    }

    /**
     * Starts Nova using the default save file.
     */
    public static void main(String[] args) {
        new Nova(DEFAULT_FILE_PATH).run();
    }
}