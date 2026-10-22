package doug.task;

import java.util.ArrayList;
import java.util.EnumSet;

import doug.errors.DougException;
import doug.ui.Line;

public class TaskManager {
    private ArrayList<Task> taskList = new ArrayList<>();

    private void printTaskSyntax(Task.Type taskType) {
        System.out.printf("\t%s <description>\n", taskType.toString().toLowerCase());
    }

    private void printHelp() {
        System.out.println("\tHere are the list of commands");

        System.out.println("\n\t---- List Tasks ----");
        System.out.println("\tlist");

        System.out.println("\n\t---- Adding Tasks ----");
        EnumSet.allOf(Task.Type.class).forEach(Type -> printTaskSyntax(Type));

        System.out.println("\n\t---- Marking Tasks ----");
        System.out.println("\tmark <index>");
        System.out.println("\tunmark <index>");

        System.out.println("\n\t---- Deleting Tasks ----");
        System.out.println("\tdelete <index>");

        System.out.println("\n\t---- Termination ----");
        System.out.println("\tbye");

    }

    private void printList() {
        for (int i = 0; i < taskList.size(); ++i) {
            System.out.printf("\t%d.%s\n", i + 1, taskList.get(i));
        }
    }

    private void getList() {
        if (taskList.size() <= 0)
            System.out.println("\tYou haven't added anything yet. Start adding.");
        else {
            System.out.println("\tHere's what you have to do:");
            printList();
        }
    }

    private void addToList(String input, Task.Type taskType) {
        String[] words = input.split(" ", 2);
        try {
            String description = words[1];
            Task task = new Task(description, taskType);
            taskList.add(task);
            System.out.printf("\tAlright your %s is in the list:\n\t%d.%s\n", taskType.toString(), taskList.size(),
                    task);

            saveList();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException(String.format("Give a description for the %s please.",
                    taskType.toString()));
        }
    }

    private void markDone(boolean isDone, String input) {
        String[] words = input.split(" ", 2);
        try {
            int index = Integer.parseInt(words[1]) - 1;
            taskList.get(index).setDone(isDone);
            if (isDone)
                System.out.println("\tOk, this is done.");
            else
                System.out.println("\tOh, wait i guess this isn't done yet.");
            System.out.printf("\t%d.%s\n", index + 1, taskList.get(index));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException("I need the index of the task.");
        } catch (NumberFormatException e) {
            throw new DougException(String.format("%s Is not a number.Put a number please.", words[1]));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new DougException("What? That doesn't exist yet!");
        }
    }

    private void deleteFromList(String input) {
        String[] words = input.split(" ", 2);
        try {
            int index = Integer.parseInt(words[1]) - 1;
            Task task = taskList.get(index);
            System.out.printf("\tOk that's removed:\n\t%d.%s\n", index + 1, task);
            taskList.remove(index);
            if (taskList.size() == 0)
                System.out.println("\tSweet thats all tasks gone!");
            else
                System.out.printf("\tYou have %d remaining things to do.\n", taskList.size());
            saveList();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DougException("What are you trying to delete? I need a number here.");
        } catch (NumberFormatException e) {
            throw new DougException(String.format("%s Is not a number. Put a number please.", words[1]));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new DougException("Delete something thats already in!");
        }
    }

    private void saveList() {
        TaskSaver.createSaveFile(taskList);
    }

    public void initTaskList(ArrayList<Task> savedTaskList) {
        taskList = savedTaskList;
    }

    public void reply(String input) {
        String[] words = input.split(" ", 2);
        switch (words[0].toLowerCase()) {
            case "help" -> printHelp();
            case "list" -> getList();
            case "todo" -> addToList(input, Task.Type.TODO);
            case "deadline" -> addToList(input, Task.Type.DEADLINE);
            case "event" -> addToList(input, Task.Type.EVENT);
            case "mark" -> markDone(true, input);
            case "unmark" -> markDone(false, input);
            case "delete" -> deleteFromList(input);
            default -> throw new DougException("Hey don't try to be funny! Get some help with 'help'!");
        }
        Line.printLine();
    }
}
