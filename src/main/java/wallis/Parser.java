package wallis;

public class Parser {

    /**
     * Parses the user's input command and executes the corresponding operations.
     *
     * @param userInput The raw string input entered by the user.
     * @param tasks The TaskList to modify.
     * @param ui The Ui instance for displaying output.
     * @param storage The Storage instance for saving data.
     * @throws WallisException If the user command is invalid or improperly formatted.
     */

    public static void parseAndExecute(String userInput, TaskList tasks, Ui ui, Storage storage) throws WallisException {
        if (userInput.equals("list")) {
            printList(tasks, ui);
        } else if (userInput.startsWith("mark")) {
            markTask(userInput, tasks, ui, storage, true);
        } else if (userInput.startsWith("unmark")) {
            markTask(userInput, tasks, ui, storage, false);
        } else if (userInput.startsWith("delete")) {
            deleteTask(userInput, tasks, ui, storage);
        } else if (userInput.startsWith("todo")) {
            addTodo(userInput, tasks, ui, storage);
        } else if (userInput.startsWith("deadline")) {
            addDeadline(userInput, tasks, ui, storage);
        } else if (userInput.startsWith("event")) {
            addEvent(userInput, tasks, ui, storage);
        } else if (userInput.startsWith("find")) {
            findTask(userInput, tasks, ui);
        } else {
            throw new WallisException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static void printList(TaskList tasks, Ui ui) {
        ui.showLine();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.getSize(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.getTask(i).toString());
        }
        ui.showLine();
    }

    private static void findTask(String input, TaskList tasks, Ui ui) throws WallisException {
        if (input.trim().equals("find")) {
            throw new WallisException("OOPS!!! The search keyword cannot be empty.");
        }
        
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new WallisException("OOPS!!! The search keyword cannot be empty.");
        }
        
        ui.showLine();
        System.out.println(" Here are the matching tasks in your list:");
        int count = 1;
        for (int i = 0; i < tasks.getSize(); i++) {
            Task task = tasks.getTask(i);
            if (task.toString().contains(keyword)) {
                System.out.println(" " + count + "." + task.toString());
                count++;
            }
        }
        if (count == 1) {
            System.out.println(" No matching tasks found.");
        }
        ui.showLine();
    }

    private static void markTask(String input, TaskList tasks, Ui ui, Storage storage, boolean isDone) throws WallisException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new WallisException("OOPS!!! Please provide a task number.");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.getSize()) {
                throw new WallisException("OOPS!!! That task number does not exist.");
            }
            if (isDone) {
                tasks.getTask(index).markAsDone();
                ui.showLine();
                System.out.println(" Nice! I've marked this task as done:");
            } else {
                tasks.getTask(index).unmarkAsDone();
                ui.showLine();
                System.out.println(" OK, I've marked this task as not done yet:");
            }
            System.out.println("   " + tasks.getTask(index).toString());
            ui.showLine();
            storage.save(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new WallisException("OOPS!!! The task number must be a valid integer.");
        }
    }

    private static void deleteTask(String input, TaskList tasks, Ui ui, Storage storage) throws WallisException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new WallisException("OOPS!!! Please provide a task number to delete.");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0 || index >= tasks.getSize()) {
                throw new WallisException("OOPS!!! That task number does not exist.");
            }
            Task removedTask = tasks.removeTask(index);
            ui.showLine();
            System.out.println(" Noted. I've removed this task:");
            System.out.println("   " + removedTask.toString());
            System.out.println(" Now you have " + tasks.getSize() + " tasks in the list.");
            ui.showLine();
            storage.save(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new WallisException("OOPS!!! The task number must be a valid integer.");
        }
    }

    private static void addTodo(String input, TaskList tasks, Ui ui, Storage storage) throws WallisException {
        if (input.trim().equals("todo")) {
            throw new WallisException("OOPS!!! The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        tasks.addTask(new Todo(description));
        confirmAddition(tasks, ui, storage);
    }

    private static void addDeadline(String input, TaskList tasks, Ui ui, Storage storage) throws WallisException {
        if (input.trim().equals("deadline")) {
            throw new WallisException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (!input.contains(" /by ")) {
            throw new WallisException("OOPS!!! A deadline must contain a '/by' date.");
        }
        String content = input.substring(9).trim();
        String[] parts = content.split(" /by ");
        tasks.addTask(new Deadline(parts[0], parts[1]));
        confirmAddition(tasks, ui, storage);
    }

    private static void addEvent(String input, TaskList tasks, Ui ui, Storage storage) throws WallisException {
        if (input.trim().equals("event")) {
            throw new WallisException("OOPS!!! The description of an event cannot be empty.");
        }
        if (!input.contains(" /from ") || !input.contains(" /to ")) {
            throw new WallisException("OOPS!!! An event must contain '/from' and '/to' times.");
        }
        String content = input.substring(6).trim();
        String[] parts = content.split(" /from ");
        String[] times = parts[1].split(" /to ");
        tasks.addTask(new Event(parts[0], times[0], times[1]));
        confirmAddition(tasks, ui, storage);
    }

    private static void confirmAddition(TaskList tasks, Ui ui, Storage storage) throws WallisException {
        ui.showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.getTask(tasks.getSize() - 1).toString());
        System.out.println(" Now you have " + tasks.getSize() + " tasks in the list.");
        ui.showLine();
        storage.save(tasks.getTasks());
    }
}