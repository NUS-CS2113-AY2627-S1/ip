/**
 * Main entry point for the CS2113 Bot task management application.
 */
public class CS2113_Bot {
    private final Storage storage;
    private final TaskList taskList;
    private final Ui ui;

    /**
     * Creates the chatbot and loads any tasks stored at the given path.
     *
     * @param filePath location of the task data file
     */
    public CS2113_Bot(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (CS2113BotException e) {
            ui.showError(e.getMessage());
            loadedTasks = new TaskList();
        }
        this.taskList = loadedTasks;
    }

    /**
     * Starts the command loop and continues until the user enters {@code bye}.
     */
    public void run() {
        ui.showWelcome();

        while (ui.hasNextLine()) {
            String fullCommand = ui.readCommand().trim();
            if (fullCommand.isEmpty()) {
                continue;
            }

            if (fullCommand.equalsIgnoreCase("bye")) {
                ui.showLine();
                ui.showExit();
                ui.showLine();
                break;
            }

            ui.showLine();
            try {
                handleCommand(fullCommand);
            } catch (CS2113BotException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    /**
     * Parses and performs one complete user command.
     *
     * @param fullCommand command text entered by the user
     * @throws CS2113BotException if the command is invalid
     */
    private void handleCommand(String fullCommand) throws CS2113BotException {
        String command = Parser.getCommandWord(fullCommand);
        String arguments = Parser.getArguments(fullCommand);

        switch (command) {
            case "list":
                ui.showTaskList(taskList);
                break;
            case "mark":
                handleMark(arguments, true);
                break;
            case "unmark":
                handleMark(arguments, false);
                break;
            case "todo":
                handleTodo(arguments);
                break;
            case "deadline":
                handleDeadline(arguments);
                break;
            case "event":
                handleEvent(arguments);
                break;
            case "delete":
                handleDelete(arguments);
                break;
            case "find":
                handleFind(arguments);
                break;
            default:
                System.out.println("Idk what you typed: " + fullCommand);
                break;
        }
    }

    /**
     * Changes the completion status of a task and saves the result.
     *
     * @param arguments task number supplied by the user
     * @param isDone whether the task should be marked as complete
     * @throws CS2113BotException if the task number is invalid or saving fails
     */
    private void handleMark(String arguments, boolean isDone) throws CS2113BotException {
        String commandName = isDone ? "mark" : "unmark";
        int taskNumber = Parser.parseTaskNumber(arguments, commandName);

        if (taskNumber < 1 || taskNumber > taskList.size()) {
            throw new CS2113BotException("There is no task numbered " + taskNumber + ".");
        }

        int taskIndex = taskNumber - 1;
        Task task = taskList.get(taskIndex);
        boolean previousStatus = task.isDone;
        task.setDone(isDone);

        try {
            storage.save(taskList.getAllTasks());
        } catch (CS2113BotException e) {
            task.setDone(previousStatus);
            throw e;
        }

        ui.showMarked(task, isDone);
    }

    /**
     * Adds a todo task with the supplied description and saves it.
     *
     * @param arguments description of the todo task
     * @throws CS2113BotException if the description is invalid or saving fails
     */
    private void handleTodo(String arguments) throws CS2113BotException {
        if (arguments.isEmpty()) {
            throw new CS2113BotException("Todo description cannot be empty! -.-");
        }
        Storage.validateTaskField(arguments, "Todo description");

        Todo newTodo = new Todo(arguments);
        taskList.add(newTodo);

        try {
            storage.save(taskList.getAllTasks());
        } catch (CS2113BotException e) {
            taskList.remove(taskList.size() - 1);
            throw e;
        }
        ui.showTaskAdded(newTodo, taskList.size());
    }

    /**
     * Adds a deadline task using the {@code /by} separator and saves it.
     *
     * @param arguments deadline description and date
     * @throws CS2113BotException if the command format is invalid or saving fails
     */
    private void handleDeadline(String arguments) throws CS2113BotException {
        String[] parts = arguments.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            if (!arguments.contains(" /by ")) {
                throw new CS2113BotException("Missing '/by' flag! Format: deadline <desc> /by <date>");
            }
            throw new CS2113BotException("Deadline description or date cannot be empty!");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();
        Storage.validateTaskField(description, "Deadline description");
        Storage.validateTaskField(by, "Deadline date");

        Deadline newDeadline = new Deadline(description, by);
        taskList.add(newDeadline);

        try {
            storage.save(taskList.getAllTasks());
        } catch (CS2113BotException e) {
            taskList.remove(taskList.size() - 1);
            throw e;
        }
        ui.showTaskAdded(newDeadline, taskList.size());
    }

    /**
     *  Adds an event task using the {@code /from} and {@code /to} separators.
     *
     * @param arguments event description, start time, and end time
     * @throws CS2113BotException if the command format is invalid or saving fails
     */
    private void handleEvent(String arguments) throws CS2113BotException {
        String[] parts = arguments.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new CS2113BotException("Event format invalid! Format: event <desc> /from <start> /to <end>");
        }

        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        Storage.validateTaskField(description, "Event description");
        Storage.validateTaskField(from, "Event start time");
        Storage.validateTaskField(to, "Event end time");

        Event newEvent = new Event(description, from, to);
        taskList.add(newEvent);

        try {
            storage.save(taskList.getAllTasks());
        } catch (CS2113BotException e) {
            taskList.remove(taskList.size() - 1);
            throw e;
        }
        ui.showTaskAdded(newEvent, taskList.size());
    }

    /**
     * Removes the selected task and saves the updated task list.
     *
     * @param arguments task number supplied by the user
     * @throws CS2113BotException if the task number is invalid or saving fails
     */
    private void handleDelete(String arguments) throws CS2113BotException {
        int taskNumber = Parser.parseTaskNumber(arguments, "delete");

        if (taskNumber < 1 || taskNumber > taskList.size()) {
            throw new CS2113BotException("There is no task numbered " + taskNumber + ".");
        }

        int taskIndex = taskNumber - 1;
        Task deletedTask = taskList.remove(taskIndex);

        try {
            storage.save(taskList.getAllTasks());
        } catch (CS2113BotException e) {
            taskList.add(taskIndex, deletedTask);
            throw e;
        }
        ui.showTaskDeleted(deletedTask, taskList.size());
    }

    private void handleFind(String arguments) throws CS2113BotException {
        if (arguments.isEmpty()) {
            throw new CS2113BotException("Please provide a search keyword: find <keyword>");
        }
        TaskList matches = taskList.findMatchingTasks(arguments);
        ui.showFoundTasks(matches);
    }

    /**
     * Launches the chatbot using its default storage location.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new CS2113_Bot("data/duke.txt").run();
    }
}
