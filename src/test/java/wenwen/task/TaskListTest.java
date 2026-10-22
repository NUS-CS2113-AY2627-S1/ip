package wenwen.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import wenwen.exception.WenwenException;

class TaskListTest {
    @Test
    void addAndDelete_validTaskNumbers_updatesList() throws WenwenException {
        TaskList tasks = new TaskList(List.of(new Todo("first")));

        tasks.add(new Todo("second"));
        Task removedTask = tasks.delete(1);

        assertEquals("first", removedTask.getDescription());
        assertEquals(1, tasks.size());
        assertEquals("second", tasks.getByNumber(1).getDescription());
    }

    @Test
    void getByNumber_numberOutsideList_throwsWenwenException() {
        TaskList tasks = new TaskList();

        assertThrows(WenwenException.class, () -> tasks.getByNumber(1));
    }

    @Test
    void find_keywordWithDifferentCase_returnsMatchingTasks() {
        TaskList tasks = new TaskList(List.of(
                new Todo("Read book"),
                new Todo("write essay"),
                new Todo("return BOOK")));

        TaskList matches = tasks.find("book");

        assertEquals(2, matches.size());
        assertEquals("Read book", matches.get(0).getDescription());
        assertEquals("return BOOK", matches.get(1).getDescription());
    }
}
