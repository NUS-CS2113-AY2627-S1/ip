package slowbro;

import java.util.Scanner;

/** Runs the Slowbro task-list application. */
public class SlowBro {

    /**
     * Starts the application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used by the application
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage();
        TaskList tasks = storage.load();

        UI.printGreeting();

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine();

            if (Parser.isBye(input)) {
                break;
            } else {
                handleCommand(input, tasks, storage);
            }
        }

        UI.printExitMessage();
        scanner.close();
    }

    /**
     * Parses and executes one user command.
     *
     * @param input the command entered by the user
     * @param tasks the current task list
     * @param storage the storage handler used to save changes
     */
    private static void handleCommand(String input, TaskList tasks, Storage storage) {
        try {
            Parser.Command command = Parser.parse(input);
            switch (command.type()) {
            case LIST -> UI.listTasks(tasks);
            case MARK -> markTask(command, tasks, storage);
            case TODO -> addAndSave(new Todo(command.description()), tasks, storage);
            case DEADLINE -> addAndSave(new Deadline(command.description(), command.firstDetail()), tasks, storage);
            case EVENT -> addAndSave(new Event(command.description(), command.firstDetail(),
                    command.secondDetail()), tasks, storage);
            case DELETE -> {
                deleteTask(tasks, command.taskIndex());
                storage.save(tasks);
            }
            case FIND -> UI.listMatchingTasks(tasks.find(command.description()));
            case BYE -> { }
            }
        } catch (InvalidCommandException e) {
            UI.printInvalidCommand(e.getUsageMessage());
        } catch (IllegalStateException e) {
            UI.printInvalidCommand(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            UI.printIndexOutOfBounds();
        } catch (NumberFormatException e) {
            UI.printInvalidNumber();
        }
    }

    /**
     * Deletes a task and displays the deletion confirmation.
     *
     * @param tasks the task list containing the task
     * @param index the zero-based index of the task to delete
     */
    private static void deleteTask(TaskList tasks, int index) {
        Task deletedTask = tasks.get(index);
        tasks.delete(index);
        UI.printDeleteTask(deletedTask, tasks.size());
    }

    /**
     * Marks or unmarks a task according to the parsed command and saves the change.
     *
     * @param command the parsed mark or unmark command
     * @param tasks the current task list
     * @param storage the storage handler used to save the change
     */
    private static void markTask(Parser.Command command, TaskList tasks, Storage storage) {

        try {
            if (command.shouldUnmark()) {
                tasks.unmark(command.taskIndex());
            } else {
                tasks.mark(command.taskIndex());
            }
            UI.printMarkTask(tasks, command.shouldUnmark(), command.taskIndex());
            storage.save(tasks);
        } catch (NumberFormatException e) {
            UI.printInvalidTaskNumber();
        } catch (IndexOutOfBoundsException e) {
            UI.printIndexOutOfBounds();
        }
    }

    /**
     * Adds a task to the task list and displays the addition confirmation.
     *
     * @param task the task to add
     * @param tasks the task list to update
     */
    private static void addTask(Task task, TaskList tasks) {
        tasks.add(task);
        UI.printAddTask(task, tasks.size() - 1);
    }

    /** Adds a task and persists the updated task list. */
    private static void addAndSave(Task task, TaskList tasks, Storage storage) {
        addTask(task, tasks);
        storage.save(tasks);
    }

}
