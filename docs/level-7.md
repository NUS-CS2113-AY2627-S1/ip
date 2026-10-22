# Level-7: Save

King loads tasks from `data/king.txt` at startup and saves after every successful
add, mark, unmark, or delete command. The path is relative to the working directory;
start King from the project root. Java's `Path.of("data", "king.txt")` handles
the operating system's path separators. Missing files load as an empty list,
and missing folders are created on the first save. Local data is ignored by Git.

## File format

The UTF-8 file contains one task per line:

```text
T|0|<Base64 description>
D|1|<Base64 description>|<Base64 deadline>
E|0|<Base64 description>|<Base64 start>|<Base64 end>
```

`0` means incomplete and `1` means complete. Text fields use Base64 so pipes,
tabs, line breaks, and Unicode cannot be confused with record separators.
An empty file represents an empty list.

After integration with Level-6, the restored tasks use a dynamic collection;
saves may contain more than 100 tasks. Deleting the last task saves an empty
file so it does not reappear on the next startup.

## Failure handling

If a record is corrupt, King reports its line number and stops startup without
changing the file. Repair the file or move it aside before restarting. Other
read failures also stop startup to prevent overwriting unread data.

Saves write a complete temporary file in the same folder, then replace the
destination. An atomic move is used where supported, with a replacement move
as a fallback. A failed save prints a warning: changes remain in memory and
the next successful task-changing command attempts to save the whole list
again. Fix the storage problem before exiting to avoid losing unsaved changes.

## Tests

Compile all production and test sources with Java 25 as shown in
[the test guide](level-5.md#run-the-regression-tests), then run:

```powershell
java -cp target/level-5-tests king.KingTest
java -cp target/level-5-tests king.KingPersistenceTest
```

Tests use temporary save files, so they do not read or overwrite personal tasks.
