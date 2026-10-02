package foodielover.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListConstructionTest {

    @Test
    public void constructor_withList_copiesTheTasks() {
        List<Task> original = new ArrayList<>();
        original.add(new Todo("first"));
        TaskList tasks = new TaskList(original);
        original.add(new Todo("added later to the original"));
        assertEquals(1, tasks.size());
        assertEquals("first", tasks.get(0).getDescription());
    }

    @Test
    public void getAll_returnsAnUnmodifiableView() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        List<Task> view = tasks.getAll();
        assertEquals(1, view.size());
        assertThrows(UnsupportedOperationException.class, () -> view.add(new Todo("sneaky")));
    }
}
