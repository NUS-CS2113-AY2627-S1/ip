package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addAndRemove_tasks_updatesSizeAndList() {
        assertTrue(taskList.isEmpty());
        assertEquals(0, taskList.size());

        Todo todo = new Todo("read book");
        taskList.add(todo);
        assertFalse(taskList.isEmpty());
        assertEquals(1, taskList.size());
        assertEquals(todo, taskList.get(0));

        Task removed = taskList.remove(0);
        assertEquals(todo, removed);
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void findTasks_keyword_returnsMatchingTasks() {
        taskList.add(new Todo("read book"));
        taskList.add(new Deadline("return book", "2026-10-15"));
        taskList.add(new Todo("cook dinner"));

        List<Task> results = taskList.findTasks("book");
        assertEquals(2, results.size());

        List<Task> upperResults = taskList.findTasks("BOOK");
        assertEquals(2, upperResults.size());

        List<Task> noResults = taskList.findTasks("sports");
        assertTrue(noResults.isEmpty());
    }

    @Test
    public void getTasksOnDate_targetDate_returnsMatchingTasks() {
        LocalDate date = LocalDate.of(2026, 10, 15);
        taskList.add(new Deadline("return book", "2026-10-15"));
        taskList.add(new Event("workshop", "2026-10-14", "2026-10-16"));
        taskList.add(new Todo("read book"));

        List<Task> matches = taskList.getTasksOnDate(date);
        assertEquals(2, matches.size());
    }
}
