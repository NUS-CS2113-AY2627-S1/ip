# Level-6: Delete and A-Collections

Use `delete <task number>` to remove a task, for example `delete 3`.
King prints the removed task and the remaining count. Subsequent tasks are
renumbered, so use `list` to check their new numbers.

Missing, nonnumeric, oversized, and out-of-range task numbers produce a
recoverable error. A rejected deletion leaves the list unchanged.

Tasks are stored in an `ArrayList<Task>` through the `List<Task>` interface.
The list grows dynamically, replacing the previous 100-task array limit.
The same index validation is used by `mark`, `unmark`, and `delete`.

Run `king.KingTest` with Java 25 using the compilation instructions in
[the test guide](level-5.md#run-the-regression-tests). The suite also covers
deletion of first, middle, last, and only tasks, renumbering, invalid deletion,
and growth beyond 100 tasks.
