package seedu.joey;

import seedu.joey.task.Task;

/**
 * Entry point of the Joey chatbot.
 * Wires the UI, storage and task list together and runs the
 * main command loop until the user types "bye".
 */
public class Joey {
    private static Ui ui = new Ui();
    private static TaskList tasks = new TaskList();

    /**
     * Launches the chatbot: loads any saved tasks, prints the welcome
     * banner and reads user commands until the user types "bye".
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        tasks = new TaskList(Storage.load());
        ui.showWelcome();

        while (true) {
            String command = ui.readCommand();
            if (command.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;
            }
            handleCommand(command);
        }
    }

    /**
     * Dispatches a single user command to the matching handler.
     *
     * @param command The full command line entered by the user.
     */
    private static void handleCommand(String command) {
        String keyword = Parser.getKeyword(command);

        try {
            switch (keyword) {
            case "list":
                ui.showTaskList(tasks);
                break;
            case "mark":
                markTask(Parser.parseTaskIndex(command, tasks.size()));
                break;
            case "unmark":
                unmarkTask(Parser.parseTaskIndex(command, tasks.size()));
                break;
            case "delete":
                deleteTask(Parser.parseTaskIndex(command, tasks.size()));
                break;
            case "todo":
            case "deadline":
            case "event":
                addTypedTask(command);
                break;
            case "find":
                ui.showFindResults(tasks.find(Parser.parseKeyword(command)));
                break;
            default:
                throw new JoeyException("Sorry, I don't recognise that command.");
            }
        } catch (JoeyException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Marks the task at the given index as done and shows the confirmation.
     *
     * @param index Zero-based index of the task to mark as done.
     */
    private static void markTask(int index) {
        tasks.get(index).markAsDone();
        ui.showMarked(tasks.get(index));
    }

    /**
     * Marks the task at the given index as not done and shows the confirmation.
     *
     * @param index Zero-based index of the task to mark as not done.
     */
    private static void unmarkTask(int index) {
        tasks.get(index).markAsNotDone();
        ui.showUnmarked(tasks.get(index));
    }

    /**
     * Removes the task at the given index and reports what was removed
     * @param index Zero-based index of the task to delete
     */
    private static void deleteTask(int index) {
        Task removed = tasks.delete(index);
        Storage.save(tasks.getTasks());
        ui.showRemoved(removed, tasks.size());
    }

    /**
     * Adds a typed task (todo/deadline/event) parsed from the command,
     * then prints the standard confirmation block.
     */
    private static void addTypedTask(String command) throws JoeyException {
        Task task = Parser.parseTask(command);
        tasks.add(task);
        Storage.save(tasks.getTasks());
        ui.showAdded(task, tasks.size());
    }   
}