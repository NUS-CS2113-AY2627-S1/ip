package jude;

import java.nio.file.Paths;

import jude.exception.JudeException;
import jude.task.Deadline;
import jude.task.Event;
import jude.task.Task;
import jude.task.Todo;

/**
 * The main class for the Jude task management application.
 */
public class Jude {

    private static final String CMD_BYE = "bye";
    private static final String CMD_LIST = "list";
    private static final String CMD_UNMARK = "unmark";
    private static final String CMD_MARK = "mark";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_TODO = "todo";
    private static final String CMD_DEADLINE = "deadline";
    private static final String CMD_EVENT = "event";
    private static final String CMD_FIND = "find";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Jude application with the specified file path for storage.
     *
     * @param filePath The path to the file where tasks will be saved and loaded from.
     */
    public Jude(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Starts the application and handles the main event loop for processing user commands.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String line = ui.readCommand();
            String action = Parser.getAction(line);

            try {
                if (action.equals(CMD_BYE)) {
                    isExit = true;
                    ui.showMessage("Bye. Hope to see you again soon!");
                } else if (action.equals(CMD_LIST)) {
                    if (tasks.isEmpty()) {
                        ui.showMessage("Here are the tasks in your list:\n    <None>");
                    } else {
                        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
                        for (int i = 0; i < tasks.size(); i++) {
                            sb.append("    ").append(i + 1).append(". ").append(tasks.get(i).toString());
                            if (i < tasks.size() - 1) {
                                sb.append("\n");
                            }
                        }
                        ui.showMessage(sb.toString());
                    }
                } else if (action.equals(CMD_UNMARK)) {
                    int taskNo = Parser.getTaskNumber(line);
                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }
                    Task selectedTask = tasks.get(taskNo - 1);
                    selectedTask.unMark();
                    storage.save(tasks);
                    ui.showMessage("OK, I've marked this task as not done yet:\n    " + selectedTask.toString());
                } else if (action.equals(CMD_MARK)) {
                    int taskNo = Parser.getTaskNumber(line);
                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }
                    Task selectedTask = tasks.get(taskNo - 1);
                    selectedTask.markAsDone();
                    storage.save(tasks);
                    ui.showMessage("Nice! I've marked this task as done:\n    " + selectedTask.toString());
                } else if (action.equals(CMD_DELETE)) {
                    int taskNo = Parser.getTaskNumber(line);
                    if (taskNo <= 0 || taskNo > tasks.size()) {
                        throw new JudeException("OOPS!!! Task number " + taskNo + " does not exist.");
                    }
                    Task selectedTask = tasks.delete(taskNo - 1);
                    storage.save(tasks);
                    ui.showMessage("Noted. I've removed this task:\n    " + selectedTask.toString()
                            + "\nNow you have " + tasks.size() + " tasks in the list.");
                } else if (action.equals(CMD_TODO) || action.equals(CMD_DEADLINE) || action.equals(CMD_EVENT)) {
                    Parser.checkEmptyDescription(line, action);
                    Task newTask = null;

                    if (action.equals(CMD_TODO)) {
                        newTask = new Todo(Parser.parseTodoDescription(line));
                    } else if (action.equals(CMD_DEADLINE)) {
                        String[] details = Parser.parseDeadlineDetails(line);
                        newTask = new Deadline(details[0], details[1]);
                    } else if (action.equals(CMD_EVENT)) {
                        String[] details = Parser.parseEventDetails(line);
                        newTask = new Event(details[0], details[1], details[2]);
                    }

                    if (newTask != null) {
                        tasks.add(newTask);
                        storage.save(tasks);
                        ui.showMessage("Got it. I've added this task:\n    " + newTask.toString()
                                + "\nNow you have " + tasks.size() + " tasks in the list.");
                    }
                } else if (action.equals(CMD_FIND)) {
                    String keyword = Parser.parseFindKeyword(line);
                    StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
                    boolean hasMatch = false;

                    for (int i = 0; i < tasks.size(); i++) {
                        Task currentTask = tasks.get(i);
                        if (currentTask.toString().contains(keyword)) {
                            hasMatch = true;
                            sb.append("    ").append(i + 1).append(". ").append(currentTask.toString()).append("\n");
                        }
                    }

                    if (!hasMatch) {
                        ui.showMessage("No matching tasks found.");
                    } else {
                        sb.setLength(sb.length() - 1);
                        ui.showMessage(sb.toString());
                    }
                } else {
                    throw new JudeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (JudeException e) {
                ui.showError(e.getMessage());
            } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
                ui.showError("OOPS!!! Your command is incomplete or formatted incorrectly.");
            } catch (NumberFormatException e) {
                ui.showError("OOPS!!! Please provide a valid task number.");
            }
        }
        ui.close();
    }

    /**
     * The main entry point for the Jude application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        String filePath = Paths.get("data", "jude.txt").toString();
        new Jude(filePath).run();
    }
}