package gizmo;

import gizmo.Parser.Command;
import gizmo.Parser.CommandType;
import gizmo.task.Task;

/**
 * Coordinates Gizmo's user interface, command parser, task list, and storage.
 */
public class Gizmo {
    private static final String DEFAULT_FILE_PATH = "data/gizmo.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;
    private final Parser parser;

    /**
     * Creates Gizmo and loads its saved tasks.
     *
     * @param filePath path to the file used for saving and loading tasks
     */
    public Gizmo(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (GizmoException e) {
            ui.showLoadingError(e);
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /** Creates Gizmo using the default data file. */
    public Gizmo() {
        this(DEFAULT_FILE_PATH);
    }

    /** Starts the command-line interaction loop. */
    public void run() {
        ui.showWelcome();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            try {
                Command command = parser.parse(input);
                if (command.type() == CommandType.BYE) {
                    break;
                }
                execute(command);
            } catch (GizmoException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
    }

    /** Executes a command that has already been validated by the parser. */
    private void execute(Command command) throws GizmoException {
        switch (command.type()) {
        case LIST:
            ui.showTaskList(tasks);
            break;
        case ADD:
            addTask(command.task());
            break;
        case MARK:
            markTask(command.taskNumber());
            break;
        case UNMARK:
            unmarkTask(command.taskNumber());
            break;
        case FIND:
            findTasks(command.keyword());
            break;
        case DELETE:
            deleteTask(command.taskNumber());
            break;
        default:
            throw new GizmoException("Unsupported command.");
        }
    }

    /** Adds a task, saves the updated list, and reports the result. */
    private void addTask(Task task) {
        tasks.addTask(task);
        saveTasks();
        ui.showTaskAdded(task);
    }

    /** Marks a task as done and reports whether it changed state. */
    private void markTask(int taskNumber) throws GizmoException {
        Task task = tasks.getTask(taskNumber);
        if (task.isDone()) {
            ui.showAlreadyMarked();
            return;
        }
        task.markAsDone();
        saveTasks();
        ui.showTaskMarked(task);
    }

    /** Marks a task as incomplete and reports whether it changed state. */
    private void unmarkTask(int taskNumber) throws GizmoException {
        Task task = tasks.getTask(taskNumber);
        if (!task.isDone()) {
            ui.showAlreadyUnmarked();
            return;
        }

        task.unmark();
        saveTasks();
        ui.showTaskUnmarked(task);
    }

    /** Deletes a task, saves the updated list, and reports the result. */
    private void deleteTask(int taskNumber) throws GizmoException {
        Task removedTask = tasks.deleteTask(taskNumber);
        saveTasks();
        ui.showTaskDeleted(removedTask, tasks.getSize());
    }

    /** Saves the current tasks while keeping storage errors non-fatal. */
    private void saveTasks() {
        try {
            storage.save(tasks.getTasks());
        } catch (GizmoException e) {
            ui.showStorageWarning(e);
        }
    }

    /** Standard Java entry point. */
    public static void main(String[] args) {
        String filePath = (args.length > 0) ? args[0] : DEFAULT_FILE_PATH;
        new Gizmo(filePath).run();
    }

    /** Convenience entry point supported by some IDE run configurations. */
    public static void main() {
        main(new String[0]);
    }

    /**
     * Finds and displays tasks containing the supplied keyword.
     *
     * @param keyword the keyword to search for.
     */
    private void findTasks(String keyword) {
        ui.showMatchingTasks(tasks.findTasks(keyword));
    }
}
