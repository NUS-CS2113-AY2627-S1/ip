package Bastion;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Starts the Bastion command-line task manager and coordinates its collaborators.
 */
public class Bastion {
    private static final int MAX_TASKS = 100;

    /**
     * Starts the command-line application and processes commands until the user exits.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if task data cannot be read or saved
     */
    public static void main(String[] args) throws IOException {
        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.loadTasks());
        Ui ui = new Ui();

        ui.showWelcome();

        while (ui.hasNextCommand()) {
                String input = ui.readCommand();

                if (input.equals("bye")) {
                    ui.showGoodbye();
                    break;
                }

                try{
                    if (input.equals("list")) {
                        ui.showTaskList(tasks);
                    }
                    else if (input.equals("find") || input.startsWith("find ")) {
                        String keyword = input.equals("find") ? "" : input.substring(5).strip();
                        if (keyword.isEmpty()) {
                            throw new BastionException(
                                "Beep Beep Boop!!! Please provide a keyword after 'find'.");
                        }
                        ui.showMatchingTasks(tasks.find(keyword));
                    }
                    else if (input.startsWith("mark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsDone();
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskStatusChanged(tasks.get(taskNumber), true);
                            } else {
                                ui.showError(" Beep Beep Boop!!! Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            ui.showError(" Beep Beep Boop!!! Please provide a valid task number after 'mark'.");
                        }
                    } 
                    
                    else if (input.startsWith("unmark ")) {
                        try {
                            int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                tasks.get(taskNumber).markAsNotDone();
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskStatusChanged(tasks.get(taskNumber), false);
                            } else {
                                ui.showError("Beep Beep Boop!!! Invalid task number.");
                            }
                        } catch (NumberFormatException e) {
                            ui.showError("Beep Beep Boop!!! Please provide a valid task number after 'unmark'.");
                        }
                    } 
                    
                    else if (input.equals("todo") || input.startsWith("todo ")) {
                        String description = input.equals("todo") ? "" : input.substring(5).strip();
                        if (description.isEmpty()) {
                            throw new BastionException(
                                "Beep Beep Boop!!! The description of a todo cannot be empty.");
                        } else if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                        } else {
                            tasks.add(new Todo(description));
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    }
                    
                    else if (input.equals("deadline") || input.startsWith("deadline ")) {
                        if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                            continue;
                        }
                        Task task = createDeadline(input);
                        if (task == null) {
                            ui.showError("Beep Beep Boop!!! The description or deadline date cannot be empty. Format: deadline [desc] /by [date]");
                        } else {
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 
                    
                    else if (input.equals("event") || input.startsWith("event ")) {
                        if (tasks.size() == MAX_TASKS) {
                            ui.showError("Beep Beep Boop!!! The task list is full.");
                            continue;
                        }
                        Task task = createEvent(input);
                        if (task == null) {
                            ui.showError("Beep Beep Boop!!! The description or event timing cannot be empty. Format: event [desc] /from [date] /to [date]");
                        } else {
                            tasks.add(task);
                            storage.saveTasks(tasks.getTasks());
                            ui.showTaskAdded(tasks.get(tasks.size() - 1), tasks.size());
                        }
                    } 

                    else if (input.startsWith("delete ")) {
                        try {
                            String numberString = input.substring(7).strip(); 
                            int taskNumber = Integer.parseInt(numberString) - 1;
                            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                                Task removedTask = tasks.remove(taskNumber);
                                storage.saveTasks(tasks.getTasks());
                                ui.showTaskRemoved(removedTask, tasks.size());
                            } else {
                                ui.showError("Beep Beep Boop!!! Invalid task number.");
                            }

                        } catch (NumberFormatException e) {
                            ui.showError("Beep Beep Boop!!! Please provide a valid task number after 'delete'.");
                        }
                    }

                    else {
                        throw new BastionException(
                            "Beep Beep Boop!!! I'm sorry, but I don't know what that means :-(");
                    }
                        
                } catch (BastionException e) {
                    ui.showError(e.getMessage());
                }
            }
    }

    /**
     * Extracts the description and due date from a deadline command.
     *
     * @param input complete command entered by the user
     * @return a deadline, or {@code null} when required parts are missing
     * @throws BastionException if the due date has an unsupported format
     */
    private static Deadline createDeadline(String input) throws BastionException {
        if (input.equals("deadline")) return null;
        int byIndex = input.indexOf(" /by ");
        if (byIndex <= "deadline ".length() || byIndex + " /by ".length() >= input.length()) {
            return null;
        }
        String description = input.substring("deadline ".length(), byIndex).strip();
        String by = input.substring(byIndex + " /by ".length()).strip();
        if (description.isEmpty() || by.isEmpty()) {
            return null;
        }

        return new Deadline(description, parseDateTime(by));
    }

    /**
     * Extracts the description, start, and end date-times from an event command.
     *
     * @param input complete command entered by the user
     * @return an event, or {@code null} when required parts are missing
     * @throws BastionException if a date or time has an unsupported format
     */
    private static Event createEvent(String input) throws BastionException {
        if (input.equals("event")) return null;
        int fromIndex = input.indexOf(" /from ");
        if (fromIndex <= "event ".length()) return null;
        int toIndex = input.indexOf(" /to ", fromIndex + " /from ".length());
        if (toIndex < 0 || toIndex + " /to ".length() >= input.length()) {
            return null;
        }
        String description = input.substring("event ".length(), fromIndex).strip();
        String from = input.substring(fromIndex + " /from ".length(), toIndex).strip();
        String to = input.substring(toIndex + " /to ".length()).strip();
        return description.isEmpty() || from.isEmpty() || to.isEmpty()
                ? null : new Event(description, parseDateTime(from), parseDateTime(to));
    }

    /**
     * Parses a supported date or date-time into a structured value.
     *
     * @param input date or date-time text from a command
     * @return parsed date and time; a date without a time starts at midnight
     * @throws BastionException if the input does not match a supported format
     */
    private static LocalDateTime parseDateTime(String input) throws BastionException {
        try {
            return LocalDate.parse(input).atStartOfDay();
        } catch (DateTimeParseException e) {
            // Try the date-time formats below.
        }

        DateTimeFormatter isoDateTime = DateTimeFormatter.ofPattern("uuuu-M-d HHmm")
                .withResolverStyle(ResolverStyle.STRICT);
        DateTimeFormatter dayMonthDateTime = DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDateTime.parse(input, isoDateTime);
        } catch (DateTimeParseException e) {
            try {
                return LocalDateTime.parse(input, dayMonthDateTime);
            } catch (DateTimeParseException ignored) {
                throw new BastionException(
                    "Beep Beep Boop!!! Use yyyy-mm-dd, yyyy-mm-dd HHmm, or d/m/yyyy HHmm for a date.");
            }
        }
    }

}
