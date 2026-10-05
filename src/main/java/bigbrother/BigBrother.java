package bigbrother;

import java.nio.file.Path;

import bigbrother.exception.BigBrotherException;
import bigbrother.parser.Parser;
import bigbrother.parser.Parser.CommandType;
import bigbrother.storage.Storage;
import bigbrother.task.Task;
import bigbrother.task.TaskList;
import bigbrother.ui.Ui;

/**
 * Coordinates input, command interpretation, task changes, and storage.
 */
public class BigBrother {
    private static final Path DATA_FILE_PATH = Path.of("data", "bigbrother.txt");

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates a chatbot that stores its tasks at the given relative path.
     *
     * @param filePath the path to the task data file
     */
    public BigBrother(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();
    }

    /**
     * Starts BigBrother and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new BigBrother(DATA_FILE_PATH).run();
    }

    /**
     * Loads saved tasks and handles commands until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BigBrotherException exception) {
            ui.showError(exception.getMessage());
            ui.showLine();
        }

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            String input = ui.readCommand();
            ui.showLine();

            try {
                CommandType commandType = Parser.parseCommandType(input);
                executeCommand(input, commandType);
                if (commandType.changesTaskList()) {
                    storage.saveTasks(tasks.getTasks());
                }
                isExit = commandType == CommandType.BYE;
            } catch (BigBrotherException exception) {
                ui.showError(exception.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Carries out an already recognized command using the task list and UI.
     *
     * @param input the complete command line
     * @param commandType the command recognized by the parser
     * @throws BigBrotherException if an argument or task number is invalid
     */
    private void executeCommand(String input, CommandType commandType) throws BigBrotherException {
        switch (commandType) {
        case BYE:
            ui.showGoodbye();
            break;
        case LIST:
            ui.showTaskList(tasks.getTasks());
            break;
        case FIND:
            ui.showMatchingTasks(tasks.find(Parser.parseKeyword(input)));
            break;
        case MARK:
            ui.showTaskMarked(tasks.markAsDone(Parser.parseTaskNumber(input, commandType)));
            break;
        case UNMARK:
            ui.showTaskUnmarked(tasks.markAsUndone(Parser.parseTaskNumber(input, commandType)));
            break;
        case DELETE:
            ui.showTaskDeleted(tasks.delete(Parser.parseTaskNumber(input, commandType)), tasks.size());
            break;
        case TODO:
            Task todo = Parser.parseTodo(input);
            tasks.add(todo);
            ui.showTodoAdded(todo, tasks.size());
            break;
        case DEADLINE:
            Task deadline = Parser.parseDeadline(input);
            tasks.add(deadline);
            ui.showDeadlineAdded(deadline, tasks.size());
            break;
        case EVENT:
            Task event = Parser.parseEvent(input);
            tasks.add(event);
            ui.showEventAdded(event, tasks.size());
            break;
        default:
            throw new IllegalStateException("Unsupported command type: " + commandType);
        }
    }
}
