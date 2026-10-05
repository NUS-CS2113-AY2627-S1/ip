package goatbot;

import goatbot.command.Parser;
import goatbot.exception.GoatBotException;
import goatbot.storage.Storage;
import goatbot.task.TaskList;
import goatbot.ui.Ui;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Coordinates Goat Bot's user interface, storage, and task list.
 */
public class GoatBot {
    private static final String INVALID_INPUT_MESSAGE =
            "Invalid input. Please try again.";
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    /**
     * Creates Goat Bot and loads its stored tasks.
     *
     * @param filePath path of the task storage file
     */
    public GoatBot(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList();

        try {
            storage.loadTasks(tasks);
        } catch (GoatBotException | IOException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Runs Goat Bot until the user enters the bye command.
     */
    public void run() {
        ui.showWelcome();
        String userInput = ui.readCommand();

        while (!userInput.equals("bye")) {
            try {
                if (userInput.equals("list")) {
                    ui.showList(tasks);
                } else if (userInput.startsWith("unmark ")) {
                    int taskNumber = Parser.parseTaskNumber(userInput, "unmark", tasks.size());
                    tasks.get(taskNumber - 1).markAsNotDone();
                    ui.showUnmarkedTask(tasks.get(taskNumber - 1));
                    storage.saveTasks(tasks);
                } else if (userInput.startsWith("mark ")) {
                    int taskNumber = Parser.parseTaskNumber(userInput, "mark", tasks.size());
                    tasks.get(taskNumber - 1).markAsDone();
                    ui.showMarkedTask(tasks.get(taskNumber - 1));
                    storage.saveTasks(tasks);
                } else if (userInput.startsWith("event ")) {
                    tasks.add(Parser.parseEvent(userInput));
                    ui.showAddedEvent(tasks.getLast(), tasks.size());
                    storage.saveTasks(tasks);
                } else if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                    tasks.add(Parser.parseTodo(userInput));
                    ui.showAddedTodo(tasks.getLast(), tasks.size());
                    storage.saveTasks(tasks);
                } else if (userInput.startsWith("deadline ")) {
                    tasks.add(Parser.parseDeadline(userInput));
                    ui.showAddedDeadline(tasks.getLast(), tasks.size());
                    storage.saveTasks(tasks);
                } else if (userInput.startsWith("delete ")) {
                    int taskNumber = Parser.parseTaskNumber(userInput, "delete", tasks.size());
                    ui.showDeletedTask(tasks.get(taskNumber - 1), tasks.size() - 1);
                    tasks.delete(taskNumber - 1);
                    storage.saveTasks(tasks);
                } else if (userInput.startsWith("on ")) {
                    LocalDate date = Parser.parseSearchDate(userInput);
                    TaskList matches = tasks.findOn(date);
                    ui.showTasksOn(date, matches);
                } else if (userInput.equals("find") || userInput.startsWith("find ")) {
                    String keyword = Parser.parseFindKeyword(userInput);
                    TaskList matches = tasks.findTasks(keyword);
                    ui.showMatchingTasks(matches);
                } else {
                    ui.showError(INVALID_INPUT_MESSAGE);
                }
            } catch (GoatBotException | IOException e) {
                ui.showError(e.getMessage());
            }
            userInput = ui.readCommand();
        }
        ui.showGoodbye();
    }

    /**
     * Starts the command loop and responds to user input.
     *
     * @param args command line arguments, currently unused
     */
    public static void main(String[] args) {
        String filePath = Path.of("data", "goatbot.txt").toString();
        new GoatBot(filePath).run();
    }
}
