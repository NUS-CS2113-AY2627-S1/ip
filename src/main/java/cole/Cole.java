package cole;

/**
 * Entry point of the Cole chatbot. Owns the Ui, Storage and TaskList,
 * and runs the loop that reads commands and executes them.
 */
public class Cole {

    private static final String ERROR_TASK_NOT_FOUND =
            "OOPS!!! That task number doesn't exist.";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Cole chatbot that stores its tasks in the given file.
     *
     * @param filePath path of the data file used to load and save tasks
     */
    public Cole(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Greets the user, then reads and executes commands until the user types "bye".
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand().trim();

            if (input.equalsIgnoreCase("bye")) {
                ui.showMessages("Bye. Hope to see you again soon!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                ui.showTaskList(tasks.getAll());
            } else if (Parser.isCommand(input, "mark")) {
                markTask(input, true);
            } else if (Parser.isCommand(input, "unmark")) {
                markTask(input, false);
            } else if (Parser.isCommand(input, "delete")) {
                deleteTask(input);
            } else if (Parser.isFindCommand(input)) {
                findTasks(input);
            } else if (Parser.isAddTaskCommand(input)) {
                addTask(input);
            } else {
                ui.showError(Parser.getUnknownCommandMessage());
            }
        }
        ui.close();
    }

    /**
     * Starts Cole using the default data file.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new Cole("./data/cole.txt").run();
    }

    /**
     * Marks or unmarks the task chosen by a "mark N" or "unmark N" command,
     * saves the change and tells the user. Invalid task numbers are reported as errors.
     *
     * @param input the full command typed by the user
     * @param isDone true for "mark", false for "unmark"
     */
    private void markTask(String input, boolean isDone) {
        String command = isDone ? "mark" : "unmark";
        try {
            int index = Parser.parseTaskIndex(input);
            Task task = tasks.get(index);
            if (isDone) {
                task.markAsDone();
                storage.save(tasks.getAll());
                ui.showMessages("Nice! I've marked this task as done:", " " + task);
            } else {
                task.markAsNotDone();
                storage.save(tasks.getAll());
                ui.showMessages("OK, I've marked this task as not done yet:", " " + task);
            }
        } catch (NumberFormatException e) {
            ui.showMessages(invalidTaskNumberMessage(command));
        } catch (IndexOutOfBoundsException e) {
            ui.showMessages(ERROR_TASK_NOT_FOUND);
        }
    }

    /**
     * Removes the task chosen by a "delete N" command, saves the change and
     * tells the user. Invalid task numbers are reported as errors.
     *
     * @param input the full command typed by the user
     */
    private void deleteTask(String input) {
        try {
            int index = Parser.parseTaskIndex(input);
            Task removedTask = tasks.delete(index);
            storage.save(tasks.getAll());
            ui.showMessages("Noted. I've removed this task:",
                    " " + removedTask,
                    taskCountMessage());
        } catch (NumberFormatException e) {
            ui.showMessages(invalidTaskNumberMessage("delete"));
        } catch (IndexOutOfBoundsException e) {
            ui.showMessages(ERROR_TASK_NOT_FOUND);
        }
    }

    /**
     * Creates a task from a todo, deadline or event command, adds it to the list,
     * saves the list and tells the user. Invalid input is reported as an error.
     *
     * @param input the full command typed by the user
     */
    private void addTask(String input) {
        try {
            Task newTask = Parser.parseTask(input);
            tasks.add(newTask);
            storage.save(tasks.getAll());
            ui.showMessages("Got it. I've added this task:",
                    " " + newTask,
                    taskCountMessage());
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Shows the tasks whose description contains the keyword of a "find" command.
     * A missing keyword is reported as an error.
     *
     * @param input the full command typed by the user
     */
    private void findTasks(String input) {
        try {
            String keyword = Parser.parseFindKeyword(input);
            ui.showMatchingTasks(tasks.find(keyword));
        } catch (ColeException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Returns a message with the number of tasks, using "task" or "tasks" as appropriate.
     */
    private String taskCountMessage() {
        int count = tasks.size();
        return "Now you have " + count + (count == 1 ? " task" : " tasks") + " in the list.";
    }

    private String invalidTaskNumberMessage(String command) {
        return "OOPS!!! Please provide a valid task number, e.g. \"" + command + " 2\".";
    }
}
