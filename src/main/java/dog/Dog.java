package dog;

import java.io.IOException;
import java.nio.file.Path;

import dog.parser.Parser;
import dog.storage.Storage;
import dog.task.Task;
import dog.task.TaskList;
import dog.ui.Ui;

/**
 * Coordinates console interaction, command parsing, task operations, and storage.
 */
public class Dog {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates a chatbot that stores tasks at the specified path.
     *
     * @param filePath Path of the task data file.
     */
    public Dog(Path filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        tasks = new TaskList();
    }

    /**
     * Starts the chatbot using a data file relative to the working directory.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Dog(Path.of("data", "dog.txt")).run();
    }

    /**
     * Loads saved tasks and processes commands until bye or end of input.
     */
    public void run() {
        try (ui) {
            ui.printGreeting();
            loadTasks();
            while (true) {
                String command = ui.readCommand();
                if (command.equalsIgnoreCase("bye")) {
                    ui.printGoodbye();
                    return;
                }
                try {
                    executeCommand(command);
                } catch (IllegalArgumentException e) {
                    ui.printError(e.getMessage());
                } catch (IOException e) {
                    ui.printError("OOPS!!! I couldn't save your tasks.");
                }
            }
        }
    }

    /**
     * Executes a command and saves the collection after a mutation.
     *
     * @param command Trimmed user command.
     * @throws IOException If saving fails.
     * @throws IllegalArgumentException If the command is invalid.
     */
    private void executeCommand(String command) throws IOException {
        if (command.equalsIgnoreCase("list")) {
            ui.printTaskList(tasks.getTasks());
            return;
        }

        String commandWord = Parser.getCommandWord(command);
        String arguments = Parser.getArguments(command);
        if (commandWord.equalsIgnoreCase("find")) {
            ui.printMatchingTasks(tasks.find(arguments));
            return;
        }
        if (commandWord.equalsIgnoreCase("mark") || commandWord.equalsIgnoreCase("unmark")) {
            int index = Parser.parseTaskIndex(commandWord, arguments, tasks.size());
            boolean isDone = commandWord.equalsIgnoreCase("mark");
            Task task = tasks.setDone(index, isDone);
            storage.saveTasks(tasks.getTasks());
            ui.printTaskStatus(task, index + 1, isDone);
            return;
        }
        if (commandWord.equalsIgnoreCase("delete")) {
            int index = Parser.parseTaskIndex(commandWord, arguments, tasks.size());
            Task removedTask = tasks.delete(index);
            storage.saveTasks(tasks.getTasks());
            ui.printDeletedTask(removedTask, tasks.size());
            return;
        }

        Task task = Parser.createTask(commandWord, arguments);
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        ui.printAddedTask(task, tasks.size());
    }

    /**
     * Loads valid tasks and reports skipped records or a read failure.
     */
    private void loadTasks() {
        try {
            tasks = new TaskList(storage.loadTasks());
            int skippedLineCount = storage.getSkippedLineCount();
            if (skippedLineCount > 0) {
                String entryLabel = skippedLineCount == 1 ? "entry" : "entries";
                ui.printError("OOPS!!! I skipped " + skippedLineCount + " corrupted data " + entryLabel + ".");
            }
        } catch (IOException e) {
            ui.printError("OOPS!!! I couldn't load your saved tasks noob.");
            tasks = new TaskList();
        }
    }
}
