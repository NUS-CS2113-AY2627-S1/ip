# Clammy v0.1

Clammy is a command-line task manager. This release supports todos, deadlines,
events, completion status, deletion, and automatic saving/loading.

## Download and run

1. Install Java 25. Check the version with `java -version`.
2. Download the attached `clammy-all.jar` asset.
3. Copy it into an empty folder and open a terminal in that folder.
4. Run:

   ```bash
   java -jar "clammy-all.jar"
   ```

You do not need the source code or Gradle to run this JAR. If you rename the file,
use that filename in the command, keeping the double quotes.

Try these commands:

```text
todo read book
deadline return book /by Sunday
event meeting /from Monday 2pm /to 4pm
list
mark 1
unmark 1
delete 2
bye
```

Clammy creates `data/clammy.txt` in the folder when you first change a task.
Keep that folder and run the JAR from it again to restore your tasks. If saved
data is malformed, Clammy reports the problem and leaves the file unchanged.
A failed save reports that the latest changes remain in memory only.
