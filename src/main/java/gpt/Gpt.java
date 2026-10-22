package gpt;

/**
 * Runs the GPT chatbot application.
 */
public class Gpt {
    private final Storage storage = new Storage();
    private final Ui ui = new Ui();
    private TaskList tasks = new TaskList();

    /**
     * Starts the chatbot.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        new Gpt().run();
    }

    /**
     * Greets the user, runs commands until the user exits, then says goodbye.
     */
    private void run() {
        loadTasks();
        ui.showGreeting();

        while (true) {
            String input = ui.readCommand();
            String commandWord = Parser.getCommandWord(input);

            ui.showLine();
            if (commandWord.equals("bye")) {
                break;
            }
            try {
                handleCommand(commandWord, input);
            } catch (GptException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }

        ui.showFarewell();
    }

    /**
     * Runs the command named by the given command word.
     * The full input line is passed on because most commands need their arguments.
     */
    private void handleCommand(String commandWord, String input) throws GptException {
        if (commandWord.equals("list")) {
            ui.showTasks(tasks);
        } else if (commandWord.equals("find")) {
            findTasks(input);
        } else if (commandWord.equals("mark")) {
            markTask(input);
        } else if (commandWord.equals("unmark")) {
            unmarkTask(input);
        } else if (commandWord.equals("delete")) {
            deleteTask(input);
        } else if (commandWord.equals("todo")
                || commandWord.equals("deadline")
                || commandWord.equals("event")) {
            addTask(Parser.parseTask(input));
        } else {
            throw new GptException("OOPS!!! I don't know what that command means.");
        }
    }

    /**
     * Adds the given task to the list and confirms the addition.
     */
    private void addTask(Task task) throws GptException {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Displays tasks with descriptions that contain the keyword in the given input.
     */
    private void findTasks(String input) throws GptException {
        String keyword = Parser.parseFindKeyword(input);
        TaskList matchingTasks = tasks.find(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Marks the task named in the given input as done.
     */
    private void markTask(String input) throws GptException {
        int taskIndex = Parser.parseTaskIndex(input, tasks.size());
        Task task = tasks.mark(taskIndex);
        saveTasks();
        ui.showTaskMarked(task);
    }

    /**
     * Deletes the task named in the given input.
     */
    private void deleteTask(String input) throws GptException {
        int taskIndex = Parser.parseTaskIndex(input, tasks.size());
        Task deletedTask = tasks.delete(taskIndex);
        saveTasks();
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /**
     * Marks the task named in the given input as not done.
     */
    private void unmarkTask(String input) throws GptException {
        int taskIndex = Parser.parseTaskIndex(input, tasks.size());
        Task task = tasks.unmark(taskIndex);
        saveTasks();
        ui.showTaskUnmarked(task);
    }

    /**
     * Loads saved tasks, starting with an empty list if the file cannot be read.
     */
    private void loadTasks() {
        try {
            tasks = storage.loadTasks();
        } catch (GptException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Saves the current task list.
     */
    private void saveTasks() throws GptException {
        storage.saveTasks(tasks);
    }
}
