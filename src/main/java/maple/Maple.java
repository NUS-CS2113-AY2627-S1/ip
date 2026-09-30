package maple;

import java.io.IOException;

import maple.exception.MapleException;
import maple.parser.ParsedCommand;
import maple.parser.Parser;
import maple.storage.Storage;
import maple.task.Task;
import maple.task.TaskList;
import maple.ui.Ui;

/**
 * Entry point for the Maple chatbot.
 */
public class Maple {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";
    private static final String DATA_FILE_PATH = "data/maple.txt";

    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs Maple using the given data file.
     *
     * @param filePath the path used to load and save tasks.
     */
    public Maple(String filePath) {
        parser = new Parser();
        storage = new Storage(filePath);
        ui = new Ui();
        tasks = loadTasks();
    }

    /**
     * Starts the Maple chatbot.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Maple(DATA_FILE_PATH).run();
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.showWelcome();
        while (ui.hasNextCommand()) {
            String command = ui.readCommand().trim();
            if (COMMAND_BYE.equals(command)) {
                break;
            }
            try {
                executeCommand(command);
            } catch (MapleException exception) {
                ui.showError(exception.getMessage());
            }
        }

        saveTasks();
        ui.showExit();
    }

    /**
     * Executes a single user command.
     *
     * @param command the command entered by the user.
     * @throws MapleException if the command is invalid.
     */
    private void executeCommand(String command) throws MapleException {
        ParsedCommand parsedCommand = parser.parseCommand(command);
        String keyword = parsedCommand.getKeyword();
        String detail = parsedCommand.getDetail();

        switch (keyword) {
        case COMMAND_LIST:
            if (!detail.isEmpty()) {
                throw new MapleException("I don't recognize that command.");
            }
            ui.showTasks(tasks.getTasks());
            break;
        case COMMAND_MARK:
            Task taskToMark = tasks.get(parser.parseTaskNumber(detail, COMMAND_MARK));
            taskToMark.markDone();
            ui.showMarked(taskToMark);
            break;
        case COMMAND_UNMARK:
            Task taskToUnmark = tasks.get(parser.parseTaskNumber(detail, COMMAND_UNMARK));
            taskToUnmark.markNotDone();
            ui.showUnmarked(taskToUnmark);
            break;
        case COMMAND_DELETE:
            int taskNumber = parser.parseTaskNumber(detail, COMMAND_DELETE);
            Task taskToDelete = tasks.delete(taskNumber);
            ui.showDeleted(taskToDelete, tasks.size());
            break;
        case COMMAND_FIND:
            String findKeyword = parser.parseFindKeyword(detail);
            ui.showMatchingTasks(tasks.find(findKeyword));
            break;
        case COMMAND_TODO:
            addTask(parser.parseTodo(detail));
            break;
        case COMMAND_DEADLINE:
            addTask(parser.parseDeadline(detail));
            break;
        case COMMAND_EVENT:
            addTask(parser.parseEvent(detail));
            break;
        default:
            throw new MapleException("I don't recognize that command.");
        }
    }

    /**
     * Adds a task to the task list and confirms it to the user.
     */
    private void addTask(Task task) {
        tasks.add(task);
        ui.showAdded(task, tasks.size());
    }

    /**
     * Loads tasks from storage, falling back to an empty list on failure.
     */
    private TaskList loadTasks() {
        try {
            return storage.load();
        } catch (IOException exception) {
            ui.showError("Could not load saved tasks; starting with an empty list.");
            return new TaskList();
        }
    }

    /**
     * Saves tasks to storage, reporting any failure to the user.
     */
    private void saveTasks() {
        try {
            storage.save(tasks);
        } catch (IOException exception) {
            ui.showError("Could not save tasks to the data file.");
        }
    }
}
