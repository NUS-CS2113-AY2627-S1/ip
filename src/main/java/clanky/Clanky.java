package clanky;

import clanky.task.Task;
import clanky.task.TaskList;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Entry point for the Clanky application.
 * Clanky is a command-line chatbot that lets users add, list, mark, unmark,
 * delete, and search tasks (todos, deadlines, and events), persisting them
 * to disk between sessions via {@link Storage}.
 * <p>
 * This class is responsible only for orchestration: reading user input,
 * dispatching to {@link Parser} for interpretation, applying changes via
 * {@link TaskList}, and reporting results via {@link UI}. It does not itself
 * contain parsing, validation, or display logic.
 */
public class Clanky {

    /**
     * Starts the Clanky application. Loads any previously saved tasks,
     * then repeatedly reads a line of user input, interprets it as a
     * command, and executes it, until the user enters "bye".
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args){

        // Instantiate UI to handle printing
        UI ui = new UI();
        ui.showWelcome();

        // Load from Storage & Populate List
        Storage storage = new Storage(
                System.getProperty("user.home") + File.separator + ".clanky" + File.separator + "clanky.txt"
        );

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (ClankyException e) {
            ui.showLoadError(e.getMessage());
            tasks = new TaskList();
        }

        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);
        String input = "";

        // Scan for Input
        label:
        while (true) {
            // Gets Input
            input = scanner.nextLine();

            // Checks empty input
            if (input.trim().isEmpty()) {
                continue;
            }

            // Splits Up Input Into Command & Arguments
            String[] parts = Parser.parse(input);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : null;

            try {
                switch (command) {
                    // Input 'bye' Command
                    case "bye":
                        break label;

                    // Input 'list' Command
                    case "list": {
                        ui.showList(tasks);
                        break;
                    }

                    // Input 'mark' Command
                    case "mark": {
                        int index = Parser.parseIndex(parts, tasks, "mark");
                        Task task = tasks.get(index);
                        task.setMark();
                        ui.showTaskMarked(task);
                        break;
                    }

                    // Input 'unmark' Command
                    case "unmark": {
                        int index = Parser.parseIndex(parts, tasks, "unmark");
                        Task task = tasks.get(index);
                        task.setUnmark();
                        ui.showTaskUnmarked(task);
                        break;
                    }

                    // Input 'to-do' command
                    case "todo": {
                        Task task = Parser.parseTodoArgs(argument);
                        tasks.add(task);
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }

                    // Input 'deadline' command
                    case "deadline": {
                        Task task = Parser.parseDeadlineArgs(argument);
                        tasks.add(task);
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }

                    // Input 'event' command
                    case "event": {
                        Task task = Parser.parseEventArgs(argument);
                        tasks.add(task);
                        ui.showTaskAdded(task, tasks.size());
                        break;
                    }

                    // Input 'delete' command
                    case "delete": {
                        int index = Parser.parseIndex(parts, tasks, "delete");
                        Task removed = tasks.remove(index);
                        ui.showTaskDeleted(removed, tasks.size());
                        break;
                    }

                    // Input 'find' command
                    case "find": {
                        String keyword = Parser.parseFindArgs(argument);
                        ArrayList<Task> matches = tasks.find(keyword);
                        ui.showFindResults(matches);
                        break;
                    }

                    // Default case
                    default: {
                        throw new ClankyException("Idk what's that command.");
                    }

                }

                // Saves to Storage
                storage.save(tasks.asArrayList());

            } catch (ClankyException e){
                ui.showError(e.getMessage());
            }

        }

        // Close Scanner
        ui.showGoodbye();
        scanner.close();
    }

}
