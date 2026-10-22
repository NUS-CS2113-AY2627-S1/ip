package duke;

import java.io.File;

/**
 * The main Duke chatbot: coordinates the UI, storage, task list, and parser.
 */
public class Duke {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Duke instance, loading any previously saved tasks.
     *
     * @param filePath Path to the data file to load from and save to.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }
    /** Runs the main command loop until the user types "bye". */
    public void run() {
        ui.showGreeting();
        String input = ui.readCommand();

        while (!input.equals("bye")) {
            try {
                runCommand(input);
                storage.save(tasks.getAll());
            } catch (DukeExceptions e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please provide a valid task number.");
            } catch (IndexOutOfBoundsException e) {
                ui.showError("That task number doesn't exist.");
            }
            input = ui.readCommand();
        }
        ui.showFarewell();
        ui.close();
    }

    private void runCommand(String input) throws DukeExceptions {
        if (input.equals("list")) {
            ui.showTaskList(tasks);
        } else if (input.startsWith("mark ")) {
            Task t = tasks.get(Parser.parseTaskNumber(input, "mark "));
            t.markAsDone();
            ui.showStatusChange("Nice! I've marked this task as done:", t);
        } else if (input.startsWith("unmark ")) {
            Task t = tasks.get(Parser.parseTaskNumber(input, "unmark "));
            t.unmarkAsDone();
            ui.showStatusChange("OK, I've marked this task as not done yet:", t);
        } else if (input.startsWith("delete ")) {
            Task t = tasks.delete(Parser.parseTaskNumber(input, "delete "));
            ui.showDeletedTask(t, tasks.size());
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            Task t = Parser.parseTodo(input);
            tasks.add(t);
            ui.showAddedTask(t, tasks.size());
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            Task t = Parser.parseDeadline(input);
            tasks.add(t);
            ui.showAddedTask(t, tasks.size());
        } else if (input.equals("event") || input.startsWith("event ")) {
            Task t = Parser.parseEvent(input);
            tasks.add(t);
            ui.showAddedTask(t, tasks.size());
        } else if (input.startsWith("find ")) {
            String keyword = input.substring(5).trim();
            ui.showMatchingTasks(tasks.find(keyword));
        } else {
            throw new DukeExceptions("I'm sorry, but I don't know what that means :-(");
        }
    }
    /**
     * Starts the application.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        new Duke("data" + File.separator + "duke.txt").run();
    }
}