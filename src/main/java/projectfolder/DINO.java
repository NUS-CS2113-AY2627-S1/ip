package projectfolder;

/**
 * Runs the DINO chatbot and manages the application flow.
 */
public class DINO {
    private static final String MARK_PREFIX = "mark ";
    private static final String UNMARK_PREFIX = "unmark ";
    private static final String DELETE_PREFIX = "delete ";
    private static final String FIND_PREFIX = "find ";

    private static final String DATA_FILE_PATH = "data/dino.txt";

    /**
     * Starts DINO and processes commands entered by the user.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(DATA_FILE_PATH);
        Parser parser = new Parser();
        TaskList tasks = new TaskList(storage.load());

        ui.showGreeting();
        runCommandLoop(ui, storage, parser, tasks);
        ui.close();
    }

    private static void runCommandLoop(Ui ui, Storage storage,
                                       Parser parser, TaskList tasks) {
        while (true) {
            String input = ui.readCommand();

            if (input.equals("bye")) {
                ui.showGoodbye();
                return;
            }

            try {
                processCommand(input, tasks, storage, parser, ui);
            } catch (DinoException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void processCommand(String input, TaskList tasks,
                                       Storage storage, Parser parser,
                                       Ui ui)
            throws DinoException {
        if (input.equals("list")) {
            ui.showTaskList(tasks);
            return;
        }

        if (input.equals("find") || input.startsWith(FIND_PREFIX)) {
            handleFindCommand(tasks, input, parser, ui);
            return;
        }

        if (input.equals("mark") || input.startsWith(MARK_PREFIX)) {
            handleMarkCommand(tasks, input, storage, parser, ui);
            return;
        }

        if (input.equals("unmark") || input.startsWith(UNMARK_PREFIX)) {
            handleUnmarkCommand(tasks, input, storage, parser, ui);
            return;
        }

        if (input.equals("delete") || input.startsWith(DELETE_PREFIX)) {
            handleDeleteCommand(tasks, input, storage, parser, ui);
            return;
        }

        if (input.equals("todo") || input.startsWith("todo ")) {
            handleTodoCommand(tasks, input, storage, parser, ui);
            return;
        }

        if (input.equals("deadline") || input.startsWith("deadline ")) {
            handleDeadlineCommand(tasks, input, storage, parser, ui);
            return;
        }

        if (input.equals("event") || input.startsWith("event ")) {
            handleEventCommand(tasks, input, storage, parser, ui);
            return;
        }

        throw new DinoException(
                "OOPS!!! I don't know what that command means."
        );
    }

    private static void handleFindCommand(TaskList tasks, String input,
                                          Parser parser, Ui ui)
            throws DinoException {
        String keyword = parser.parseFindKeyword(input);
        TaskList matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    private static void handleMarkCommand(TaskList tasks, String input,
                                          Storage storage, Parser parser,
                                          Ui ui)
            throws DinoException {
        int taskIndex = parser.parseTaskIndex(
                input, MARK_PREFIX, tasks.size(), "mark");

        Task task = tasks.get(taskIndex);
        task.markAsDone();
        ui.showTaskMarked(task);

        storage.save(tasks);
    }

    private static void handleUnmarkCommand(TaskList tasks, String input,
                                            Storage storage, Parser parser,
                                            Ui ui)
            throws DinoException {
        int taskIndex = parser.parseTaskIndex(
                input, UNMARK_PREFIX, tasks.size(), "unmark");

        Task task = tasks.get(taskIndex);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);

        storage.save(tasks);
    }

    private static void handleDeleteCommand(TaskList tasks, String input,
                                            Storage storage, Parser parser,
                                            Ui ui)
            throws DinoException {
        int taskIndex = parser.parseTaskIndex(
                input, DELETE_PREFIX, tasks.size(), "delete");

        Task removedTask = tasks.delete(taskIndex);
        ui.showTaskDeleted(removedTask, tasks.size());

        storage.save(tasks);
    }

    private static void handleTodoCommand(TaskList tasks, String input,
                                          Storage storage, Parser parser,
                                          Ui ui)
            throws DinoException {
        Task task = parser.parseTodo(input);
        addTask(tasks, task, ui);
        storage.save(tasks);
    }

    private static void handleDeadlineCommand(TaskList tasks, String input,
                                              Storage storage, Parser parser,
                                              Ui ui)
            throws DinoException {
        Task task = parser.parseDeadline(input);
        addTask(tasks, task, ui);
        storage.save(tasks);
    }

    private static void handleEventCommand(TaskList tasks, String input,
                                           Storage storage, Parser parser,
                                           Ui ui)
            throws DinoException {
        Task task = parser.parseEvent(input);
        addTask(tasks, task, ui);
        storage.save(tasks);
    }

    private static void addTask(TaskList tasks, Task task, Ui ui) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }
}