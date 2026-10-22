package vani;

import vani.exception.VaniException;
import vani.task.Task;

/**
 * Coordinates the UI, parser, task list, and storage for the Vani task manager.
 */
public class Vani {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Creates an application that uses the specified task data file.
     * Tasks are loaded when the interactive session starts.
     *
     * @param filePath the path to the task data file
     */
    public Vani(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        parser = new Parser();
        tasks = new TaskList();
    }

    /**
     * Runs the application until the user enters bye or input ends.
     */
    public void run() {
        try (ui) {
            ui.showGreeting();
            loadTasks();

            while (ui.hasNextCommand()) {
                String input = ui.readCommand();
                ui.showLine();

                boolean shouldExit = false;
                try {
                    Command command = parser.parse(input);
                    shouldExit = execute(command);
                } catch (VaniException e) {
                    ui.showError(e.getMessage());
                }

                ui.showLine();
                if (shouldExit) {
                    break;
                }
            }
        }
    }

    /**
     * Loads saved tasks, falling back to an empty list if the file cannot be read.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
            ui.showLoadingWarnings(storage.getSkippedLineCount());
        } catch (VaniException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Applies a parsed command and saves task changes before confirming them.
     *
     * @param command the operation to execute
     * @return true if the application should exit
     * @throws VaniException if the task number is invalid or saving fails
     */
    private boolean execute(Command command) throws VaniException {
        switch (command.getType()) {
            case BYE:
                ui.showGoodbye();
                return true;
            case LIST:
                ui.showTaskList(tasks);
                break;
            case FIND:
                ui.showMatchingTasks(tasks.findTasks(command.getSearchKeyword()));
                break;
            case MARK:
                Task markedTask = tasks.markTask(command.getTaskNumber());
                storage.save(tasks.getTasks());
                ui.showTaskMarked(command.getTaskNumber(), markedTask);
                break;
            case UNMARK:
                Task unmarkedTask = tasks.unmarkTask(command.getTaskNumber());
                storage.save(tasks.getTasks());
                ui.showTaskUnmarked(command.getTaskNumber(), unmarkedTask);
                break;
            case DELETE:
                Task deletedTask = tasks.deleteTask(command.getTaskNumber());
                storage.save(tasks.getTasks());
                ui.showTaskDeleted(command.getTaskNumber(), deletedTask);
                break;
            case TODO, DEADLINE, EVENT:
                tasks.addTask(command.getTask());
                storage.save(tasks.getTasks());
                ui.showTaskAdded(command.getType().getKeyword(), command.getTask(), tasks.size());
                break;
            default:
                throw new VaniException("I'm sorry, I don't understand that command. T-T");
        }
        return false;
    }

    /**
     * Starts Vani using its default task data file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Vani("data/vani.txt").run();
    }
}
