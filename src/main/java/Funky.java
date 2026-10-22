public class Funky {
    private static final String FILE_PATH = "data/duke.txt";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Funky(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (FunkyException e) {
            ui.showError(e.getMessage());
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    public void run() {
        ui.showWelcome();
        ui.showLoadWarnings(storage.getLoadWarnings());
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            ui.showLine();
            try {
                String commandWord = Parser.getCommandWord(fullCommand);
                String arguments = Parser.getArguments(fullCommand);
                switch (commandWord) {
                case "bye":
                    ui.showBye();
                    isExit = true;
                    break;
                case "list":
                    ui.showTaskList(tasks.asList());
                    break;
                case "mark":
                    markTask(Parser.parseIndex(arguments, commandWord), true);
                    break;
                case "unmark":
                    markTask(Parser.parseIndex(arguments, commandWord), false);
                    break;
                case "delete":
                    deleteTask(Parser.parseIndex(arguments, commandWord));
                    break;
                case "find":
                    ui.showMatchingTasks(tasks.find(Parser.parseFindKeyword(arguments)));
                    break;
                case "todo":
                    addTask(Parser.parseTodo(arguments));
                    break;
                case "deadline":
                    addTask(Parser.parseDeadline(arguments));
                    break;
                case "event":
                    addTask(Parser.parseEvent(arguments));
                    break;
                default:
                    throw new FunkyException("Sorry, I don't understand that command.");
                }
            } catch (FunkyException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    private void addTask(Task task) throws FunkyException {
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }

    private void deleteTask(int index) throws FunkyException {
        Task removed = tasks.delete(index);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removed, tasks.size());
    }

    private void markTask(int index, boolean isDone) throws FunkyException {
        Task task = tasks.get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.asList());
        ui.showTaskMarked(task, isDone);
    }

    public static void main(String[] args) {
        new Funky(FILE_PATH).run();
    }
}