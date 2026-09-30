package xxlilchatxx;

import java.util.ArrayList;

/**
 * Entry point for the XxLilChatxX chatbot application.
 * Handles user commands to add, list, mark, and unmark tasks.
 */
public class XxLilChatxX {

    /**
     * Runs the chatbot, reading commands from standard input until "bye" is entered.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ArrayList<Task> loaded = new ArrayList<>();
        Storage.load(loaded);
        TaskList tasks = new TaskList(loaded);

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            Parser.CommandType type = Parser.parseCommandType(input);

            if (type == Parser.CommandType.BYE) {
                break;
            }

            try {
                switch (type) {
                    case LIST:
                        ui.showLine();
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + "." + tasks.get(i));
                        }
                        ui.showLine();
                        break;
                    case MARK: {
                        int index = Parser.parseIndex(input, 5);
                        if (!tasks.isValidIndex(index)) {
                            throw new XxLilChatxXException("OOPS!!! That task number doesn't exist.");
                        }
                        tasks.get(index).markAsDone();
                        Storage.save(tasks.getTasks());
                        ui.showLine();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks.get(index));
                        ui.showLine();
                        break;
                    }
                    case UNMARK: {
                        int index = Parser.parseIndex(input, 7);
                        if (!tasks.isValidIndex(index)) {
                            throw new XxLilChatxXException("OOPS!!! That task number doesn't exist.");
                        }
                        tasks.get(index).markAsNotDone();
                        Storage.save(tasks.getTasks());
                        ui.showLine();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks.get(index));
                        ui.showLine();
                        break;
                    }
                    case DELETE: {
                        int index = Parser.parseIndex(input, 7);
                        if (!tasks.isValidIndex(index)) {
                            throw new XxLilChatxXException("OOPS!!! That task number doesn't exist.");
                        }
                        Task removed = tasks.delete(index);
                        Storage.save(tasks.getTasks());
                        ui.showLine();
                        System.out.println("Noted. I've removed this task:");
                        System.out.println("  " + removed);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                        ui.showLine();
                        break;
                    }
                    case TODO: {
                        String desc = Parser.parseTodoDescription(input);
                        if (desc.isEmpty()) {
                            throw new XxLilChatxXException("OOPS!!! The description of a todo cannot be empty.");
                        }
                        Task t = new Todo(desc);
                        tasks.add(t);
                        Storage.save(tasks.getTasks());
                        printAdded(ui, t, tasks.size());
                        break;
                    }
                    case DEADLINE: {
                        String[] parts = Parser.parseDeadlineParts(input);
                        if (parts[0].isEmpty() || parts[1].isEmpty()) {
                            throw new XxLilChatxXException(
                                    "OOPS!!! A deadline needs a description and a /by date, e.g. deadline return book /by Sunday");
                        }
                        Task t = new Deadline(parts[0], parts[1]);
                        tasks.add(t);
                        Storage.save(tasks.getTasks());
                        printAdded(ui, t, tasks.size());
                        break;
                    }
                    case EVENT: {
                        String[] parts = Parser.parseEventParts(input);
                        if (parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
                            throw new XxLilChatxXException(
                                    "OOPS!!! An event needs a description, /from time, and /to time.");
                        }
                        Task t = new Event(parts[0], parts[1], parts[2]);
                        tasks.add(t);
                        Storage.save(tasks.getTasks());
                        printAdded(ui, t, tasks.size());
                        break;
                    }
                    case FIND: {
                        String keyword = Parser.parseFindKeyword(input);
                        if (keyword.isEmpty()) {
                            throw new XxLilChatxXException("OOPS!!! Please specify a keyword to search for, e.g. find book");
                        }
                        ArrayList<Task> matches = tasks.find(keyword);
                        ui.showLine();
                        if (matches.isEmpty()) {
                            System.out.println("No matching tasks found.");
                        } else {
                            System.out.println("Here are the matching tasks in your list:");
                            for (int i = 0; i < matches.size(); i++) {
                                System.out.println((i + 1) + "." + matches.get(i));
                            }
                        }
                        ui.showLine();
                        break;
                    }
                    default:
                        throw new XxLilChatxXException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (XxLilChatxXException e) {
                ui.showLine();
                ui.showError(e.getMessage());
                ui.showLine();
            } catch (NumberFormatException e) {
                ui.showLine();
                ui.showError("OOPS!!! Please provide a valid task number.");
                ui.showLine();
            }
        }

        ui.showGoodbye();
    }

    /**
     * Prints the confirmation message after a task has been added.
     *
     * @param task  The task that was added.
     * @param count The total number of tasks after adding.
     */
    private static void printAdded(Ui ui, Task task, int count) {
        ui.showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + count + " tasks in the list.");
        ui.showLine();
    }
}
