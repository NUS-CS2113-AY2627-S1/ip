package bobby;

import bobby.exception.InvalidTaskException;
import bobby.exception.StorageException;
import bobby.exception.TaskNotFoundException;

import bobby.ui.Ui;
import bobby.parser.Parser;
import bobby.storage.Storage;
import bobby.taskmanager.TaskManager;

import bobby.task.Deadline;
import bobby.task.Event;
import bobby.task.Task;
import bobby.task.Todo;

import java.util.ArrayList;

/**
 * Manages the Bobby application
 */
public class Bobby {
    private TaskManager taskManager;
    private Ui ui;
    private Storage storage;

    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";

    private static final String TODO_KEYWORD = "todo";
    private static final String DEADLINE_KEYWORD = "deadline";
    private static final String EVENT_KEYWORD = "event";

    /**
     * Creates a Bobby object that uses
     * the specified data file.
     *
     * @param filePath the path to the data file
     */
    public Bobby(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        try {
            taskManager = new TaskManager(storage.loadFile());
        } catch (StorageException e) {
            ui.showErrorMessage(e.getMessage());
            taskManager = new TaskManager();
        }
    }

    /**
     * Starts the application and processes user inputs.
     */
    public void run() {
        greetUser();

        boolean isRunning = true;
        while (isRunning) {
            String inputLine = ui.getInput();
            isRunning = handleInput(inputLine);
        }

        applyFileChanges();
        sayGoodbye();
    }

    /**
     * Initialises the Bobby application using the default data file.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Bobby("data/tasks.txt").run();
    }

    private void greetUser() {
        ui.showBanner();
        ui.showWelcomeMessage();
    }

    private void sayGoodbye() {
        ui.showGoodbyeMessage();
    }

    /**
     * Processes a single line of user input
     * and executes the corresponding command.
     *
     * @param inputLine the line of user input
     * @return true if the application should continue running, otherwise false
     */
    private boolean handleInput(String inputLine) {
        boolean isRunning = true;

        if (inputLine.isBlank()) {
            ui.showErrorMessage("No command received. Please enter a command.");
            return isRunning;
        }

        String command = Parser.getCommand(inputLine);
        String arguments = Parser.getArguments(inputLine);

        try {
            switch (command) {
                case EXIT_COMMAND -> isRunning = false;
                case LIST_COMMAND -> showAllTasks();
                case MARK_COMMAND -> handleTaskMarking(arguments);
                case UNMARK_COMMAND -> handleTaskUnmarking(arguments);
                case DELETE_COMMAND -> handleTaskDeletion(arguments);
                case FIND_COMMAND -> handleTaskSearch(arguments);
                case TODO_KEYWORD -> addTodo(arguments);
                case DEADLINE_KEYWORD -> addDeadline(arguments);
                case EVENT_KEYWORD -> addEvent(arguments);
                default -> ui.showErrorMessage("No such command. Try again.");
            }
        } catch (NumberFormatException e) {
            ui.showErrorMessage("Task number must be a valid number...");
        } catch (TaskNotFoundException | InvalidTaskException e) {
            ui.showErrorMessage(e.getMessage());
        }

        return isRunning;
    }

    private void showAllTasks() {
        ui.showTaskList(taskManager.getTasks());
    }

    /**
     * Marks the specified task as done and displays the task.
     *
     * @param number the task number
     */
    private void handleTaskMarking(String number) {
        int taskNumber = Parser.getTaskNumber(number);
        Task task = taskManager.markTask(taskNumber);

        ui.showMarkedTask(task);
        applyFileChanges();
    }

    /**
     * Marks the specified task as undone and displays the task.
     *
     * @param number the task number
     */
    private void handleTaskUnmarking(String number) {
        int taskNumber = Parser.getTaskNumber(number);
        Task task = taskManager.unmarkTask(taskNumber);

        ui.showUnmarkedTask(task);
        applyFileChanges();
    }

    /**
     * Deletes the specified task and displays the deleted task.
     *
     * @param number the task number
     */
    private void handleTaskDeletion(String number) {
        int taskNumber = Parser.getTaskNumber(number);
        Task deletedTask = taskManager.deleteTask(taskNumber);

        ui.showDeletedTask(deletedTask, taskManager.getTaskCount());
        applyFileChanges();
    }

    /**
     * Searches for tasks matching the given keyword and displays all matches.
     *
     * @param keyword the keyword to search for
     * @throws InvalidTaskException if the keyword is blank
     */
    private void handleTaskSearch(String keyword) {
        if (keyword.isBlank()) {
            throw new InvalidTaskException("Please provide a keyword to search for.");
        }

        ArrayList<Task> matches = taskManager.findTasks(keyword);
        ui.showMatchedTasks(matches);
    }

    /**
     * Adds a task, displays a confirmation message
     * and saves the changes to the data file.
     *
     * @param task the task to add
     */
    private void registerTask(Task task) {
        taskManager.addTask(task);
        ui.showAddedTask(task, taskManager.getTaskCount());
        applyFileChanges();
    }

    private void addTodo(String args) {
        Todo todo = Parser.parseTodo(args);
        registerTask(todo);
    }

    private void addDeadline(String args) {
        Deadline deadline = Parser.parseDeadline(args);
        registerTask(deadline);
    }


    private void addEvent(String args) {
        Event event = Parser.parseEvent(args);
        registerTask(event);
    }

    /**
     * Saves the current list of tasks to the data file.
     */
    private void applyFileChanges() {
        try {
            storage.saveFile(taskManager.getTasks());
        } catch (StorageException e) {
            ui.showErrorMessage(e.getMessage());
        }
    }
}