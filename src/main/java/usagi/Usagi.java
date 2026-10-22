package usagi;

import java.util.List;

/**
 * Entry point for the Usagi chatbot.
 *
 * Greets the user, then repeatedly reads a command and responds to it, until
 * the user types "bye". Usagi can list tasks, mark or unmark them as done,
 * delete and search them, and record three kinds of tasks: todos, deadlines and events.
 *
 * This class only coordinates the work: {@link Ui} talks to the user,
 * {@link Parser} makes sense of commands, {@link TaskList} holds the tasks and
 * {@link Storage} saves them to disk.
 */
public class Usagi {
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";

    /**
     * Tasks recorded so far, in the order they were added.
     *
     * Starts empty, and is replaced by the saved tasks once they are loaded.
     */
    private TaskList tasks = new TaskList();

    /** Reads the user's commands and prints Usagi's responses. */
    private final Ui ui;

    /** Saves the tasks to, and loads them from, the hard disk. */
    private final Storage storage;

    /**
     * Creates a Usagi chatbot that saves its tasks to the given file.
     *
     * @param filePath Location of the save file.
     */
    public Usagi(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
    }

    /**
     * Starts the Usagi chatbot.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        new Usagi("./data/tasks.txt").run();
    }

    /**
     * Runs Usagi's greet-read-respond loop until the user types "bye".
     */
    public void run() {
        ui.showWelcome();
        loadTasks();
        runCommandLoop();
        ui.showFarewell();
    }

    /**
     * Restores the tasks saved by the previous run.
     *
     * A failure to load is reported and then ignored, so that a missing or
     * unreadable save file leaves the user with an empty list rather than no
     * program at all.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
        } catch (UsagiException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Writes the current tasks to the hard disk.
     *
     * Called after every change to the list, so that the saved file always
     * matches what the user sees and nothing is lost if the program stops
     * without reaching the "bye" command.
     */
    private void saveTasks() {
        try {
            storage.save(tasks.getAll());
        } catch (UsagiException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Reads and executes commands until the exit command is entered.
     *
     * A command that the user got wrong is reported and then forgotten, so a
     * mistake never ends the session. This is the one place where problems
     * raised anywhere inside a command are turned into a printed response.
     */
    private void runCommandLoop() {
        boolean isExitRequested = false;
        while (!isExitRequested) {
            String input = ui.readCommand();
            try {
                isExitRequested = executeCommand(input);
            } catch (UsagiException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Executes a single user command and prints its response.
     *
     * @param input Full line of input entered by the user.
     * @return True if the user asked to exit, false otherwise.
     * @throws UsagiException If the command is not recognised, or its arguments
     *                       are missing or cannot be used.
     */
    private boolean executeCommand(String input) throws UsagiException {
        if (input.isEmpty()) {
            throw new UsagiException("HAAAAAA->(I didn't catch that. Type a command, or \"bye\" to leave.)");
        }

        String commandWord = Parser.getCommandWord(input);
        String arguments = Parser.getCommandArguments(input);

        switch (commandWord) {
        case COMMAND_BYE:
            return true;
        case COMMAND_LIST:
            printTaskList();
            break;
        case COMMAND_MARK:
            setTaskDoneStatus(arguments, true);
            break;
        case COMMAND_UNMARK:
            setTaskDoneStatus(arguments, false);
            break;
        case COMMAND_TODO:
            addTask(Parser.parseTodo(arguments));
            break;
        case COMMAND_DEADLINE:
            addTask(Parser.parseDeadline(arguments));
            break;
        case COMMAND_EVENT:
            addTask(Parser.parseEvent(arguments));
            break;
        case COMMAND_DELETE:
            deleteTask(arguments);
            break;
        case COMMAND_FIND:
            findTasks(arguments);
            break;
        default:
            throw new UsagiException("Una Yahaha->(Sorry, I don't know what \"" + commandWord + "\" means.) "
                    + "Yaha Una yahauna->(I understand: todo, deadline, event, list, mark, unmark, "
                    + "delete, find and bye.)");
        }
        return false;
    }

    /**
     * Stores a task and confirms it to the user.
     *
     * @param task Task to store.
     */
    private void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        ui.showResponse("Puru Yaha->(Got it. I've added this task:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in the list.)");
    }

    /**
     * Removes the task at the given position from the list, and reports it.
     *
     * The removed task is read before it is removed so that it can still be
     * shown to the user in the confirmation message.
     *
     * @param arguments Text following the command word, holding the task number
     *                  as shown by the "list" command (starting from 1).
     * @throws UsagiException If no task number was given, if it is not a number,
     *                       or if no task has that number.
     */
    private void deleteTask(String arguments) throws UsagiException {
        int taskIndex = Parser.parseTaskIndex(arguments);
        Task removedTask = tasks.delete(taskIndex);
        saveTasks();
        ui.showResponse("HaAAA->(Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.)");
    }

    /**
     * Marks the task at the given position as done or not done, and reports it.
     *
     * @param arguments Text following the command word, holding the task number
     *                  as shown by the "list" command (starting from 1).
     * @param isDone True to mark the task as done, false to mark it as not done.
     * @throws UsagiException If no task number was given, if it is not a number,
     *                       or if no task has that number.
     */
    private void setTaskDoneStatus(String arguments, boolean isDone) throws UsagiException {
        int taskIndex = Parser.parseTaskIndex(arguments);
        Task task = tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            saveTasks();
            ui.showResponse("Nice! I've marked this task as done:", "  " + task);
        } else {
            task.markAsNotDone();
            saveTasks();
            ui.showResponse("OK, I've marked this task as not done yet:", "  " + task);
        }
    }

    /**
     * Prints all stored tasks, numbered from 1.
     */
    private void printTaskList() {
        printNumberedTasks("Here are the tasks in your list:", tasks.getAll());
    }

    /**
     * Prints the tasks whose description contains a keyword, numbered from 1.
     *
     * @param arguments Text following the "find" command word, holding the
     *                  keyword to search for.
     * @throws UsagiException If no keyword was given.
     */
    private void findTasks(String arguments) throws UsagiException {
        String keyword = Parser.parseFindKeyword(arguments);
        List<Task> matchingTasks = tasks.find(keyword);
        if (matchingTasks.isEmpty()) {
            ui.showResponse("Hunaaa->(No tasks match \"" + keyword + "\".)");
            return;
        }
        printNumberedTasks("Here are the matching tasks in your list:", matchingTasks);
    }

    /**
     * Prints a heading followed by the given tasks, one per line, numbered
     * from 1.
     *
     * @param heading Line printed above the tasks.
     * @param tasksToShow Tasks to print, in order.
     */
    private void printNumberedTasks(String heading, List<Task> tasksToShow) {
        String[] lines = new String[tasksToShow.size() + 1];
        lines[0] = heading;
        for (int i = 0; i < tasksToShow.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasksToShow.get(i);
        }
        ui.showResponse(lines);
    }
}
