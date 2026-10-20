package gigabot;

import java.util.ArrayList;

/**
 * Parses user string commands and executes the appropriate application logic.
 */
public class Parser {
    private static final String BY_MARKER = " /by ";
    private static final String FROM_MARKER = " /from ";
    private static final String TO_MARKER = " /to ";

    /**
     * Interprets a raw user command, executes the corresponding action, and returns whether the program should exit.
     *
     * @param command The raw string input from the user.
     * @param tasks The current list of tasks.
     * @param ui The user interface manager.
     * @param storage The hard disk storage manager.
     * @return true if the application should terminate, false otherwise.
     * @throws GigaBotException If the command is invalid, unrecognized, or missing required arguments.
     */
    public static boolean parseAndExecute(String command, TaskList tasks, Ui ui, Storage storage) throws GigaBotException {
        if (command.equals("bye")) {
            ui.showMessage("Shutting down. Hope to see you again soon!");
            return true;
        }

        if (command.equals("list")) {
            ui.showMessage("Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                ui.showMessage((i + 1) + "." + tasks.getTask(i).toString());
            }
        } else if (command.startsWith("mark ")) {
            int index = Integer.parseInt(command.substring("mark".length()).trim()) - 1;
            if (index < 0 || index >= tasks.getSize()) throw new GigaBotException("Task number does not exist.");
            tasks.getTask(index).markAsDone();
            storage.save(tasks);
            ui.showMessage("Nice! I've marked this task as done:\n[GigaBot] >>   " + tasks.getTask(index).toString());
        } else if (command.startsWith("unmark ")) {
            int index = Integer.parseInt(command.substring("unmark".length()).trim()) - 1;
            if (index < 0 || index >= tasks.getSize()) throw new GigaBotException("Task number does not exist.");
            tasks.getTask(index).markAsUndone();
            storage.save(tasks);
            ui.showMessage("OK, I've marked this task as not done yet:\n[GigaBot] >>   " + tasks.getTask(index).toString());
        } else if (command.startsWith("delete ")) {
            int index = Integer.parseInt(command.substring("delete".length()).trim()) - 1;
            if (index < 0 || index >= tasks.getSize()) throw new GigaBotException("Task number does not exist.");
            Task removedTask = tasks.deleteTask(index);
            storage.save(tasks);
            ui.showTaskDeleted(removedTask, tasks.getSize());
        } else if (command.startsWith("find ")) {
            String keyword = command.substring("find".length()).trim();
            if (keyword.isEmpty()) throw new GigaBotException("Please provide a keyword to search for.");
            ArrayList<Task> matchingTasks = new ArrayList<>();
            for (Task task : tasks.getTasks()) {
                if (task.getDescription().contains(keyword)) {
                    matchingTasks.add(task);
                }
            }
            ui.showMatchingTasks(matchingTasks);
        } else if (command.startsWith("todo")) {
            String desc = command.substring("todo".length()).trim();
            if (desc.isEmpty()) throw new GigaBotException("A todo task requires a description.");
            tasks.addTask(new Todo(desc));
            storage.save(tasks);
            ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
        } else if (command.startsWith("deadline")) {
            if (!command.contains(BY_MARKER)) throw new GigaBotException("A deadline requires a '" + BY_MARKER.trim() + "' marker.");
            String[] parts = command.substring("deadline".length()).trim().split(BY_MARKER);
            if (parts.length < 2 || parts[0].trim().isEmpty()) throw new GigaBotException("A deadline requires both a description and a time.");
            tasks.addTask(new Deadline(parts[0].trim(), parts[1].trim()));
            storage.save(tasks);
            ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
        } else if (command.startsWith("event")) {
            if (!command.contains(FROM_MARKER) || !command.contains(TO_MARKER)) throw new GigaBotException("An event requires both '" + FROM_MARKER.trim() + "' and '" + TO_MARKER.trim() + "' markers.");
            String[] parts = command.substring("event".length()).trim().split(FROM_MARKER);
            String[] timeParts = parts[1].split(TO_MARKER);
            tasks.addTask(new Event(parts[0].trim(), timeParts[0].trim(), timeParts[1].trim()));
            storage.save(tasks);
            ui.showTaskAdded(tasks.getTask(tasks.getSize() - 1), tasks.getSize());
        } else {
            throw new GigaBotException("SYSTEM ERROR: Command not recognized.");
        }
        return false;
    }
}