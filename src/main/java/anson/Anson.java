package anson;

/**
 * Entry point for the Anson task-tracking chatbot.
 */
public class Anson {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates an Anson chatbot that saves to and loads from the given file.
     * If the file cannot be read, the chatbot starts with an empty task list.
     *
     * @param filePath Path of the data file, relative to the working directory.
     */
    public Anson(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);

        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (AnsonException e) {
            ui.showLoadingError();
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    /**
     * Runs the chatbot, reading commands from standard input until
     * the user enters "bye".
     */
    public void run() {
        ui.showWelcome();

        if (storage.getSkippedLineCount() > 0) {
            ui.showSkippedLines(storage.getSkippedLineCount());
        }

        while (true) {
            String reply = ui.readCommand();
            ui.showLine();

            if (reply.trim().equals("bye")) {
                ui.showGoodbye();
                break;
            }

            handleCommand(reply);
        }
    }

    /**
     * Dispatches a single line of user input to the matching command
     * handler and prints the trailing separator line. Any AnsonException
     * raised while handling the command is caught here so the chatbot
     * never crashes mid-session.
     *
     * @param reply Raw command line entered by the user.
     */
    private void handleCommand(String reply) {
        try {
            String commandWord = Parser.parseCommandWord(reply);
            String arguments = Parser.parseArguments(reply);

            boolean isModified = true;

            switch (commandWord) {
                case "mark":
                    handleMark(arguments);
                    break;
                case "unmark":
                    handleUnmark(arguments);
                    break;
                case "delete":
                    handleDelete(arguments);
                    break;
                case "find":
                    ui.showMatchingTasks(tasks.find(Parser.parseFindKeyword(arguments)));
                    isModified = false;
                    break;
                case "list":
                    ui.showTaskList(tasks);
                    isModified = false;
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
                    throw new AnsonException("I'm sorry, but I don't know what that means :(");
            }

            if (isModified) {
                storage.save(tasks);
            }
        } catch (AnsonException e) {
            ui.showError(e.getMessage());
        }

        ui.showLine();
    }

    /**
     * Marks the task referenced by the given argument as done.
     *
     * @param arguments Text after the "mark" command word.
     * @throws AnsonException If the argument is missing, non-numeric, or out of range.
     */
    private void handleMark(String arguments) throws AnsonException {
        int taskNum = Parser.parseTaskNumber(arguments, tasks.size());

        Task task = tasks.get(taskNum - 1);
        task.markAsDone();
        ui.showTaskMarked(task);
    }

    /**
     * Marks the task referenced by the given argument as not done.
     *
     * @param arguments Text after the "unmark" command word.
     * @throws AnsonException If the argument is missing, non-numeric, or out of range.
     */
    private void handleUnmark(String arguments) throws AnsonException {
        int taskNum = Parser.parseTaskNumber(arguments, tasks.size());

        Task task = tasks.get(taskNum - 1);
        task.markAsNotDone();
        ui.showTaskUnmarked(task);
    }

    /**
     * Deletes the task referenced by the given argument from the list.
     *
     * @param arguments Text after the "delete" command word.
     * @throws AnsonException If the argument is missing, non-numeric, or out of range.
     */
    private void handleDelete(String arguments) throws AnsonException {
        int taskNum = Parser.parseTaskNumber(arguments, tasks.size());

        Task removed = tasks.remove(taskNum - 1);
        ui.showTaskDeleted(removed, tasks.size());
    }

    /**
     * Adds a task to the list and shows the confirmation message.
     *
     * @param newTask Task to add.
     */
    private void addTask(Task newTask) {
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    /**
     * Starts the Anson chatbot.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Anson("data/anson.txt").run();
    }
}