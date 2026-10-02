package Ui;

import java.io.IOException;

import Exceptions.InvalidCommandException;
import Task.TasksList;

/** Handles user commands and saves the current task list after successful changes. */
public class Ui {

    private final TasksList tasks;
    private final Parser parser = new Parser();
    private final Storage storage = new Storage();

    private static final String GOODBYE = "Bye! See you soon";
    private static final String LINE_BREAK = "─".repeat(60);

    /** Uses the same task list that startup restores and later commands update. */
    public Ui(TasksList tasks) {
        this.tasks = tasks;
    }

    /** Processes input and saves a snapshot after a successful task change. */
    public boolean processCommand(String userInput) {
        boolean tasksChanged = false;
        try {
            System.out.println(LINE_BREAK);
            String command = parser.parseCommand(userInput);

            switch (command) {
            case "bye":
                System.out.println(GOODBYE);
                return true;

            case "help":
                System.out.println(HelpText.COMMAND_LIST);
                break;

            case "list":

                if (tasks.isEmpty()) {
                    System.out.println("Add tasks first");
                    break;
                }

                tasks.List();
                break;

            case "mark", "unmark", "delete":
                int target = parser.parseTaskNumber(userInput);
                tasks.targetingCommand(command, target);
                tasksChanged = true;
                break;

            case "find":
                var matches = tasks.find(parser.parseKeyword(userInput));
                if (matches.isEmpty()) {
                    System.out.println("No matching tasks found.");
                    break;
                }
                System.out.println("Here are the matching tasks in your list:");
                for (int i = 0; i < matches.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    matches.get(i).printResponse();
                }
                break;

            case "todo", "deadline", "event":
                tasks.add(parser.parseTask(userInput));
                tasksChanged = true;
                System.out.println("Task added: ");
                tasks.getLast().printResponse();
                System.out.printf("You have %d tasks added to list\n", tasks.size());
                break;

            }

            if (tasksChanged) {
                storage.saveTasks(tasks);
            }

        } catch (InvalidCommandException e) {
            System.out.println(e.getMessage() + HelpText.COMMAND_LIST);
        } catch (IOException e) {
            System.out.println("Could not save tasks. Your changes are only in memory.");
        }

        System.out.println(LINE_BREAK);
        return false;
    }
}
