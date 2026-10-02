package glados;

import glados.task.Task;

import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Runs GLaDOS, a command line chatbot that keeps a simple list of tasks.
 *
 * <p>Supports adding a todo, deadline, or event task, listing all tasks,
 * finding tasks by keyword, listing the tasks that fall on a date, marking a
 * task as done or not done, and deleting a task. The task list is loaded from disk at startup and saved
 * after every change. The conversation ends when the user enters the exit command.
 */
public class GLaDOS {

    /**
     * Where the task list is saved: data/glados.txt, relative to the folder the program runs in.
     * Path.of joins the parts with the separator of the current OS, so this works on any OS.
     */
    private static final Path DATA_FILE = Path.of("data", "glados.txt");

    /** Reads the user's commands and shows GLaDOS's replies. */
    private final Ui ui;

    /** Loads the task list at startup and saves it after every change. */
    private final Storage storage;

    /** The user's tasks, replaced by the saved ones when the conversation starts. */
    private TaskList tasks = new TaskList();

    /**
     * Creates a chatbot that saves its task list to the given file.
     *
     * @param filePath location of the data file, relative to the folder the program runs in.
     */
    public GLaDOS(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
    }

    /**
     * Runs the chatbot with the default data file.
     *
     * @param args command line arguments, not used.
     */
    public static void main(String[] args) {
        new GLaDOS(DATA_FILE).run();
    }

    /**
     * Greets the user, loads the saved tasks, then handles commands until the user
     * enters the exit command or the input ends.
     *
     * <p>Input ends when the user presses Ctrl+Z then Enter on Windows, or Ctrl+D on
     * macOS and Linux, or when commands are read from a file that has no more lines.
     */
    public void run() {
        ui.showWelcome();
        loadTasks();

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            if (Parser.isExit(input)) {
                break;
            }

            ui.showLine();
            try {
                handleCommand(input);
            } catch (GLaDOSException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }

        // Reached on the exit command, or when input ends, so GLaDOS always says goodbye.
        ui.showGoodbye();
    }

    /**
     * Replaces the task list with the saved tasks, warning the user if they could
     * not be read or some lines were skipped.
     *
     * <p>Done after the welcome rather than in the constructor, so any warning is
     * shown below the welcome message.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
        } catch (GLaDOSException e) {
            ui.showLoadingError(e.getMessage());
        }
        if (storage.getCorruptedLineCount() > 0) {
            ui.showCorruptedLineWarning(storage.getCorruptedLineCount());
        }
    }

    /**
     * Carries out one command entered by the user, saving the task list if it changed.
     *
     * @param input the full command entered by the user.
     * @throws GLaDOSException if the command is unknown, not in the expected format,
     *         or the task list could not be saved.
     */
    private void handleCommand(String input) throws GLaDOSException {
        String commandWord = Parser.parseCommandWord(input);
        if (commandWord.equals(Parser.COMMAND_LIST)) {
            ui.showTaskList(tasks);
        } else if (commandWord.equals(Parser.COMMAND_MARK)) {
            Task task = tasks.get(Parser.parseTaskIndex(input, commandWord, tasks.size()));
            task.markAsDone();
            ui.showTaskMarked(task);
            storage.save(tasks);
        } else if (commandWord.equals(Parser.COMMAND_UNMARK)) {
            Task task = tasks.get(Parser.parseTaskIndex(input, commandWord, tasks.size()));
            task.markAsNotDone();
            ui.showTaskUnmarked(task);
            storage.save(tasks);
        } else if (commandWord.equals(Parser.COMMAND_DELETE)) {
            Task task = tasks.delete(Parser.parseTaskIndex(input, commandWord, tasks.size()));
            ui.showTaskDeleted(task, tasks.size());
            storage.save(tasks);
        } else if (commandWord.equals(Parser.COMMAND_TODO)) {
            addTask(Parser.parseTodo(input));
        } else if (commandWord.equals(Parser.COMMAND_DEADLINE)) {
            addTask(Parser.parseDeadline(input));
        } else if (commandWord.equals(Parser.COMMAND_EVENT)) {
            addTask(Parser.parseEvent(input));
        } else if (commandWord.equals(Parser.COMMAND_FIND)) {
            String keyword = Parser.parseFindKeyword(input);
            ui.showMatchingTasks(keyword, tasks.find(keyword));
        } else if (commandWord.equals(Parser.COMMAND_ON)) {
            LocalDate date = Parser.parseOnDate(input);
            ui.showTasksOn(date, tasks.getTasksOn(date));
        }
    }

    /**
     * Appends the given task to the list, shows the confirmation, and saves the list.
     *
     * @throws GLaDOSException if the task list could not be saved.
     */
    private void addTask(Task task) throws GLaDOSException {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.save(tasks);
    }
}
