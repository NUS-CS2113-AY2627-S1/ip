import java.io.IOException;

public class Exia {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Exia(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();

        TaskList loadedTasks;

        try {
            loadedTasks = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }

        tasks = loadedTasks;
    }

    public void run() {
        ui.showGreeting();
        boolean shouldExit = false;

        try {
            while (!shouldExit && ui.hasNextCommand()) {
                String input = ui.readCommand();

                try {
                    shouldExit = executeCommand(input);
                } catch (ExiaException e) {
                    ui.showError(e.getMessage());
                } catch (IOException e) {
                    ui.showError("Unable to save tasks.");
                }
            }
        } finally {
            ui.close();
        }
    }

    private boolean executeCommand(String input)
            throws ExiaException, IOException {
        Parser.CommandType commandType = Parser.getCommandType(input);

        switch (commandType) {
            case BYE:
                ui.showGoodbye();
                return true;
            case LIST:
                ui.showTaskList(tasks);
                break;
            case DONE:
                markTaskAsDone(input);
                break;
            case DELETE:
                deleteTask(input);
                break;
            case FIND:
                findTasks(input);
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                addTask(input);
                break;
            default:
                throw new ExiaException(
                        "I'm sorry, but I don't know what that means :-(");
        }

        return false;
    }

    private void markTaskAsDone(String input)
            throws ExiaException, IOException {
        int taskNumber = Parser.parseTaskNumber(input, "done");
        Task task = tasks.markAsDone(taskNumber);
        storage.saveTasks(tasks.getTasks());
        ui.showMarkedTask(task);
    }

    private void deleteTask(String input)
            throws ExiaException, IOException {
        int taskNumber = Parser.parseTaskNumber(input, "delete");
        Task task = tasks.delete(taskNumber);
        storage.saveTasks(tasks.getTasks());
        ui.showDeletedTask(task, tasks.size());
    }

    private void findTasks(String input) throws ExiaException {
        String keyword = Parser.parseFindKeyword(input);
        TaskList matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    private void addTask(String input)
            throws ExiaException, IOException {
        Task task = Parser.createTask(input);
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        ui.showAddedTask(task);
    }

    public static void main(String[] args) {
        new Exia("data/exia.txt").run();
    }
}