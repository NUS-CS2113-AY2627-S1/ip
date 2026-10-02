package foodielover.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import foodielover.ConsoleCapture;
import foodielover.task.Task;
import foodielover.task.TaskList;
import foodielover.task.Todo;

public class UiTest {

    @Test
    public void readCommand_andHasCommand_readLinesInOrder() {
        try (ConsoleCapture console = new ConsoleCapture("first\nsecond\n")) {
            Ui ui = new Ui();
            assertTrue(ui.hasCommand());
            assertEquals("first", ui.readCommand());
            assertTrue(ui.hasCommand());
            assertEquals("second", ui.readCommand());
            assertFalse(ui.hasCommand());
        }
    }

    @Test
    public void showLine_printsDividerOfUnderscores() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showLine();
            assertTrue(console.output().trim().matches("_+"));
        }
    }

    @Test
    public void showWelcome_printsGreeting() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showWelcome();
            String output = console.output();
            assertTrue(output.contains("Hello! I'm Foodielover."));
            assertTrue(output.contains("What can I do for you?"));
        }
    }

    @Test
    public void showGoodbye_printsFarewell() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showGoodbye();
            assertTrue(console.output().contains("Bye. Hope to see you again soon!"));
        }
    }

    @Test
    public void showError_printsMessageAsIs() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showError("Something went wrong.");
            assertEquals("Something went wrong.", console.output().trim());
        }
    }

    @Test
    public void showCorruptedLineWarning_includesTheBadLine() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showCorruptedLineWarning("GARBAGE LINE");
            String output = console.output();
            assertTrue(output.contains("Skipping corrupted task entry"));
            assertTrue(output.contains("GARBAGE LINE"));
        }
    }

    @Test
    public void showSaveError_includesReason() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showSaveError("disk full");
            assertTrue(console.output().contains("An error occurred while saving tasks: disk full"));
        }
    }

    @Test
    public void showTaskList_withList_numbersEveryTask() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            List<Task> tasks = List.of(new Todo("read book"), new Todo("write essay"));
            new Ui().showTaskList(tasks);
            String output = console.output();
            assertTrue(output.contains("Here are the tasks in your list:"));
            assertTrue(output.contains("1.[T][ ] read book"));
            assertTrue(output.contains("2.[T][ ] write essay"));
        }
    }

    @Test
    public void showTaskList_withTaskList_numbersEveryTask() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            TaskList tasks = new TaskList();
            tasks.add(new Todo("read book"));
            new Ui().showTaskList(tasks);
            assertTrue(console.output().contains("1.[T][ ] read book"));
        }
    }

    @Test
    public void showTasksOnDate_noMatches_printsNoTasksMessage() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showTasksOnDate(List.of(), LocalDate.of(2026, 10, 15));
            assertTrue(console.output().contains("No tasks found occurring on Oct 15 2026."));
        }
    }

    @Test
    public void showTasksOnDate_withMatches_printsHeaderAndTasks() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showTasksOnDate(List.of(new Todo("read book")), LocalDate.of(2026, 10, 15));
            String output = console.output();
            assertTrue(output.contains("Here are the tasks occurring on Oct 15 2026:"));
            assertTrue(output.contains("1.[T][ ] read book"));
        }
    }

    @Test
    public void showMatchingTasks_noMatches_printsNoMatchMessage() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showMatchingTasks(List.of());
            assertTrue(console.output().contains("No matching tasks found in your list."));
        }
    }

    @Test
    public void showMatchingTasks_withMatches_printsHeaderAndTasks() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            new Ui().showMatchingTasks(List.of(new Todo("read book")));
            String output = console.output();
            assertTrue(output.contains("Here are the matching tasks in your list:"));
            assertTrue(output.contains("1.[T][ ] read book"));
        }
    }

    @Test
    public void showTaskAdded_usesSingularAndPluralCorrectly() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            Ui ui = new Ui();
            ui.showTaskAdded(new Todo("read book"), 1);
            ui.showTaskAdded(new Todo("write essay"), 2);
            String output = console.output();
            assertTrue(output.contains("Got it. I've added this task:"));
            assertTrue(output.contains("  [T][ ] read book"));
            assertTrue(output.contains("Now you have 1 task in the list."));
            assertTrue(output.contains("Now you have 2 tasks in the list."));
        }
    }

    @Test
    public void showTaskRemoved_usesSingularAndPluralCorrectly() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            Ui ui = new Ui();
            ui.showTaskRemoved(new Todo("read book"), 1);
            ui.showTaskRemoved(new Todo("write essay"), 0);
            String output = console.output();
            assertTrue(output.contains("Noted. I've removed this task:"));
            assertTrue(output.contains("Now you have 1 task in the list."));
            assertTrue(output.contains("Now you have 0 tasks in the list."));
        }
    }

    @Test
    public void showTaskMarked_andUnmarked_printConfirmations() {
        try (ConsoleCapture console = new ConsoleCapture("")) {
            Ui ui = new Ui();
            Todo todo = new Todo("read book");
            todo.markAsDone();
            ui.showTaskMarked(todo);
            todo.markAsUndone();
            ui.showTaskUnmarked(todo);
            String output = console.output();
            assertTrue(output.contains("Nice! I've marked this task as done:"));
            assertTrue(output.contains("  [T][X] read book"));
            assertTrue(output.contains("OK, I've marked this task as not done yet:"));
            assertTrue(output.contains("  [T][ ] read book"));
        }
    }
}
