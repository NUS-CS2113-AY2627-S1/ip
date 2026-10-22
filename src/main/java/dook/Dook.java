package dook;

/**
 * The main class that coordinates the user interface, command parsing,
 * task management, and task storage for Dook.
 */
public class Dook {
    private final Storage storage;
    private final Ui ui;
    private final Parser parser;
    private final TaskList tasks;

    /**
     * Creates a Dook instance and loads the saved tasks from the given file.
     *
     * @param filePath the path to the file used to store tasks
     */
    public Dook(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        parser = new Parser();
        TaskList loadedTasks;
        try {
            loadedTasks = storage.loadTasks();
        } catch (DookException e) {
            // Start with an empty task list if the saved data cannot be loaded.
            ui.showErrorMessage(e);
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /**
     * Starts the main loop that reads and processes commands from the user.
     */
    public void run() {
        ui.showGreetingMessage();
        while (true) {
            String input = ui.readLine();
            try {
                CommandType commandType = parser.parseCommand(input);
                if (commandType == CommandType.BYE) {
                    ui.showExitMessage();
                    break;
                } else if (commandType == CommandType.LIST) {
                    ui.showTaskList(tasks);
                } else if (commandType == CommandType.MARK) {
                    int taskNumber = parser.parseMarkCommand(input);
                    Task task = tasks.markTask(taskNumber);
                    storage.saveTasks(tasks);
                    ui.showMarkConfirmation(task);
                } else if (commandType == CommandType.UNMARK) {
                    int taskNumber = parser.parseUnmarkCommand(input);
                    Task task = tasks.unmarkTask(taskNumber);
                    storage.saveTasks(tasks);
                    ui.showUnmarkConfirmation(task);
                } else if (commandType == CommandType.TODO) {
                    Todo todo = parser.parseTodoCommand(input);
                    tasks.addTask(todo);
                    storage.saveTasks(tasks);
                    ui.showAddTaskConfirmation(todo, tasks.size());
                } else if (commandType == CommandType.DEADLINE) {
                    Deadline deadline = parser.parseDeadlineCommand(input);
                    tasks.addTask(deadline);
                    storage.saveTasks(tasks);
                    ui.showAddTaskConfirmation(deadline, tasks.size());
                } else if (commandType == CommandType.EVENT) {
                    Event event = parser.parseEventCommand(input);
                    tasks.addTask(event);
                    storage.saveTasks(tasks);
                    ui.showAddTaskConfirmation(event, tasks.size());
                } else if (commandType == CommandType.DELETE) {
                    int taskNumber = parser.parseDeleteCommand(input);
                    Task deletedTask = tasks.deleteTask(taskNumber);
                    storage.saveTasks(tasks);
                    ui.showDeleteConfirmation(deletedTask, tasks.size());
                } else if (commandType == CommandType.FIND) {
                    String keyword = parser.parseFindCommand(input);
                    TaskList matchingTasks = tasks.findTasks(keyword);
                    ui.showMatchingTaskList(matchingTasks);
                } else if (commandType == CommandType.UNKNOWN) {
                    throw new DookException("I'm sorry, but I don't know what that means :-(");
                } else {
                    throw new DookException("An unexpected command type was encountered.");
                }
            } catch (DookException e) {
                ui.showErrorMessage(e);
            }
        }
    }

    /**
     * Starts Dook using the default task storage file.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Dook("data/dook.txt").run();
    }
}
