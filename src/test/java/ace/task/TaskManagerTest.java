package ace.task;

import java.util.List;

import ace.TestSupport;

/** Regression tests for task management and case-insensitive searches. */
public final class TaskManagerTest {
    private TaskManagerTest() {
    }

    public static void run() throws Exception {
        TaskManager tasks = new TaskManager();
        TestSupport.equal(0, tasks.getTasksCount(), "new task list is empty");
        TestSupport.rejects(() -> tasks.addTask(null), "reject null task");
        TestSupport.rejects(() -> tasks.addTask(new Todo("  ")), "reject blank description");

        tasks.addTask(new Todo("Read Book"));
        tasks.addTask(new Todo("write essay"));
        tasks.addTask(new Todo("return BOOK"));
        TestSupport.equal(3, tasks.getTasksCount(), "three tasks added");
        TestSupport.equal("Read Book", tasks.getTask(0).getLabel(), "retrieve first task");
        TestSupport.equal(List.of(0, 2), tasks.findTaskIndices("book"),
                "search preserves original indices");
        TestSupport.equal(List.of(0, 2), tasks.findTaskIndices("BOOK"),
                "search ignores case");
        TestSupport.equal(List.of(), tasks.findTaskIndices("missing"), "search with no matches");

        tasks.markAsDone(0);
        TestSupport.check(tasks.getTask(0).isDone(), "mark as completed");
        tasks.markAsUndone(0);
        TestSupport.check(!tasks.getTask(0).isDone(), "mark as incomplete");
        TestSupport.equal("write essay", tasks.deleteTask(1).getLabel(), "delete returns removed task");
        TestSupport.equal(2, tasks.getTasksCount(), "delete decreases task count");
        TestSupport.equal(List.of(0, 1), tasks.findTaskIndices("book"), "indices update after deletion");

        TestSupport.rejects(() -> tasks.getTask(-1), "negative task index");
        TestSupport.rejects(() -> tasks.getTask(2), "out-of-range task index");
        TestSupport.rejects(() -> tasks.deleteTask(5), "delete out-of-range index");
        TestSupport.rejects(() -> tasks.markAsDone(5), "mark out-of-range index");
        TestSupport.rejects(() -> new TaskManager(List.of(new Todo(""))),
                "saved tasks must have descriptions");
        System.out.println("PASS TaskManagerTest");
    }
}
