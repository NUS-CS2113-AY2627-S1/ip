package friday;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Entry point of the Friday task-list chatbot. Reads commands from the
 * user in a loop, updates the task list accordingly and saves it to disk
 * after every change.
 */
public class Friday {

    /** Location of the save file, relative to where the program is run. */
    public static final String SAVE_FILE_PATH = "./data/friday.txt";

    /**
     * Runs Friday until the user types "bye" or input ends.
     * Supported commands: list, mark, unmark, delete, find, todo,
     * deadline and event.
     */
    public static void main(String[] args) {

        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(SAVE_FILE_PATH);
        ArrayList<Task> tasks = storage.load(ui);

        while (true) {

            // Handle end-of-input (e.g. piped input, Ctrl+D) gracefully
            // instead of crashing with NoSuchElementException.
            String word = ui.readCommand();
            if (word == null) {
                break;
            }

            // Exit
            if ("bye".equals(word)) {
                break;
            }

            // List all tasks
            if ("list".equals(word)) {
                ui.showTaskList(tasks);
                continue;
            }

            // Split input into words
            String[] command = word.split("\\s+");

            // Mark / unmark commands
            if ("mark".equals(command[0]) ||
                    "unmark".equals(command[0])) {

                // Check that a task number was provided
                if (command.length < 2) {
                    ui.showMessage("Please specify a task number.");
                    continue;
                }

                try {
                    int num = Integer.parseInt(command[1]);

                    // Check that task number is valid
                    if (num < 1 || num > tasks.size()) {
                        ui.showMessage("Invalid task number.");
                        continue;
                    }

                    // Change task status
                    Task task = tasks.get(num - 1);
                    task.changeStatus(command[0]);
                    storage.save(tasks, ui);

                    ui.showMarkResult(task, "mark".equals(command[0]));

                } catch (NumberFormatException e) {
                    ui.showMessage("Please enter a valid task number.");
                }

                continue;
            }

            // Delete command
            if ("delete".equals(command[0])) {

                // Check that a task number was provided
                if (command.length < 2) {
                    ui.showMessage("Please specify a task number.");
                    continue;
                }

                try {
                    int num = Integer.parseInt(command[1]);

                    // Check that task number is valid
                    if (num < 1 || num > tasks.size()) {
                        ui.showMessage("Invalid task number.");
                        continue;
                    }

                    Task removed = tasks.remove(num - 1);
                    storage.save(tasks, ui);

                    ui.showDeleteResult(removed, tasks.size());

                } catch (NumberFormatException e) {
                    ui.showMessage("Please enter a valid task number.");
                }

                continue;
            }

            // Find command
            if ("find".equals(command[0])) {

                String keyword = word.length() > 4 ? word.substring(5).trim() : "";

                if (keyword.isEmpty()) {
                    ui.showMessage("Please specify a keyword to search for.");
                    continue;
                }

                ui.showMatchingTasks(findTasks(tasks, keyword));

                continue;
            }

            // Add a new task
            try {
                tasks.add(createTask(word));
                storage.save(tasks, ui);

                ui.showTaskAdded(word);
            } catch (FridayException e) {
                ui.showMessage(e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Returns the tasks whose description contains {@code keyword} as a
     * case-sensitive substring, in their original list order.
     */
    private static ArrayList<Task> findTasks(ArrayList<Task> tasks, String keyword) {
        ArrayList<Task> matches = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                matches.add(task);
            }
        }

        return matches;
    }

    /**
     * Parses a raw "todo", "deadline" or "event" command into the matching
     * task, e.g. {@code deadline return book /by 2019-12-02}.
     *
     * @param input the full command line typed by the user
     * @return the newly created task
     * @throws FridayException if the command word is unknown, a required
     *                         part is missing or empty, a deadline date is
     *                         not in yyyy-MM-dd format, or the text contains '|'
     */
    public static Task createTask(String input) throws FridayException {

        if (input.equals("todo") || input.startsWith("todo ")) {

            String description = input.length() > 4 ? input.substring(5).trim() : "";

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of a todo cannot be empty.");
            }

            validateNoPipeCharacter(description);

            return new Todo(description);

        } else if (input.equals("deadline") || input.startsWith("deadline ")) {

            String details = input.length() > 8 ? input.substring(9).trim() : "";

            if (!details.contains(" /by ")) {
                throw new FridayException(
                        "OOPS!!! A deadline needs a description and a '/by' date, "
                                + "e.g. deadline return book /by 2019-12-02");
            }

            String[] parts = details.split(" /by ", 2);
            String description = parts[0].trim();
            String byText = parts[1].trim();

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of a deadline cannot be empty.");
            }

            if (byText.isEmpty()) {
                throw new FridayException("OOPS!!! Please state a date after '/by'.");
            }

            validateNoPipeCharacter(description);

            LocalDate by;
            try {
                by = LocalDate.parse(byText);
            } catch (DateTimeParseException e) {
                throw new FridayException(
                        "OOPS!!! Please give the date after '/by' in yyyy-MM-dd format, "
                                + "e.g. deadline return book /by 2019-12-02");
            }

            return new Deadline(description, by);

        } else if (input.equals("event") || input.startsWith("event ")) {

            String details = input.length() > 5 ? input.substring(6).trim() : "";

            if (!details.contains(" /from ")) {
                throw new FridayException(
                        "OOPS!!! An event needs a description, a '/from' and a '/to' timing, "
                                + "e.g. event meeting /from Mon 2pm /to Mon 4pm");
            }

            String[] parts = details.split(" /from ", 2);
            String description = parts[0].trim();

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of an event cannot be empty.");
            }

            if (!parts[1].contains(" /to ")) {
                throw new FridayException("OOPS!!! Please state both a '/from' and a '/to' timing.");
            }

            String[] times = parts[1].split(" /to ", 2);
            String from = times[0].trim();
            String to = times[1].trim();

            if (from.isEmpty() || to.isEmpty()) {
                throw new FridayException("OOPS!!! Please state both a '/from' and a '/to' timing.");
            }

            validateNoPipeCharacter(description);
            validateNoPipeCharacter(from);
            validateNoPipeCharacter(to);

            return new Event(description, from, to);
        }

        throw new FridayException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Rejects task text containing '|', since that character is the field
     * delimiter used by {@link Task#toFileFormat} and {@link Storage} when
     * reading the file back - allowing it would silently corrupt the save
     * file on the next reload.
     */
    private static void validateNoPipeCharacter(String value) throws FridayException {
        if (value.contains("|")) {
            throw new FridayException("OOPS!!! Task details cannot contain the '|' character.");
        }
    }

}
