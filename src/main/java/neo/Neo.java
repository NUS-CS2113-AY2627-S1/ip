package neo;

import java.util.ArrayList;
import neo.exception.NeoException;
import neo.task.Task;

/**
 * Represents the main entry point for the Neo task management application.
 * Initializes the user interface, storage, and task list components, and
 * runs the main application loop.
 */
public class Neo {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Neo application instance.
     * Initializes the UI and Storage, and attempts to load existing tasks.
     *
     * @param filePath The file path where tasks are saved and loaded from.
     */
    public Neo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (NeoException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays the welcome message and continuously reads and processes user
     * commands until the exit command is given or the input stream ends.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand == null) {
                    ui.showFarewell();
                    break;
                }
                isExit = processCommand(fullCommand);
            } catch (NeoException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Processes a single user command and executes the corresponding action.
     *
     * @param command The full string input provided by the user.
     * @return {@code true} if the application should exit, {@code false} otherwise.
     * @throws NeoException If the command format is invalid or the command is unrecognized.
     */
    private boolean processCommand(String command) throws NeoException {
        if (command.equals(Parser.EXIT_COMMAND)) {
            ui.showFarewell();
            return true;
        }

        if (command.equals(Parser.LIST_COMMAND)) {
            ui.showTaskList(tasks.getTasks());
            return false;
        }

        if (command.startsWith(Parser.MARK_PREFIX)) {
            int index = Parser.parseIndex(command, Parser.MARK_PREFIX, tasks.getSize());
            Task t = tasks.getTask(index);
            t.markAsDone();
            ui.showMessage("Nice! I've marked this task as done:\n       " + t);
            storage.save(tasks.getTasks());
            return false;
        }

        if (command.startsWith(Parser.UNMARK_PREFIX)) {
            int index = Parser.parseIndex(command, Parser.UNMARK_PREFIX, tasks.getSize());
            Task t = tasks.getTask(index);
            t.unmarkAsDone();
            ui.showMessage("OK, I've marked this task as not done yet:\n       " + t);
            storage.save(tasks.getTasks());
            return false;
        }

        if (command.startsWith(Parser.DELETE_PREFIX)) {
            int index = Parser.parseIndex(command, Parser.DELETE_PREFIX, tasks.getSize());
            Task t = tasks.deleteTask(index);
            ui.showMessage("Noted. I've removed this task:\n       " + t
                    + "\n     Now you have " + tasks.getSize() + " tasks in the list.");
            storage.save(tasks.getTasks());
            return false;
        }

        if (command.startsWith(Parser.FIND_PREFIX)) {
            String keyword = Parser.parseFind(command);
            ArrayList<Task> matches = tasks.findTasks(keyword);
            ui.showFoundTasks(matches);
            return false;
        }

        if (command.startsWith(Parser.TODO_PREFIX)) {
            addTask(Parser.parseTodo(command));
            return false;
        }

        if (command.startsWith(Parser.DEADLINE_PREFIX)) {
            addTask(Parser.parseDeadline(command));
            return false;
        }

        if (command.startsWith(Parser.EVENT_PREFIX)) {
            addTask(Parser.parseEvent(command));
            return false;
        }

        throw new NeoException("Unrecognized command. Try typing: todo <description>, "
                + "deadline <description> <date>, event <description> <date>, list, "
                + "mark <index>, unmark <index>, delete <index>, find <keyword> or bye.");
    }

    /**
     * Adds a task, displays the confirmation, and saves the updated task list.
     *
     * @param task The parsed task to add.
     */
    private void addTask(Task task) {
        tasks.addTask(task);
        ui.showMessage("Got it. I've added this task:\n       " + task
                + "\n     Now you have " + tasks.getSize() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * The main method that starts the Neo application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Neo("./data/neo.txt").run();
    }
}
